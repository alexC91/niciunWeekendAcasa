document.addEventListener("DOMContentLoaded", () => {
    const citiesByCountry = {
        US: ["New York", "Los Angeles", "Chicago"],
        RO: ["Bucuresti", "Cluj-Napoca", "Iasi"],
        FR: ["Paris", "Lyon", "Marseille"],
        DE: ["Berlin", "Munich", "Hamburg"],
        JP: ["Tokyo", "Osaka", "Kyoto"],
    }

    // Helper functions
    const qs = (s) => document.querySelector(s)
    const id = (s) => document.getElementById(s)

    // Elements
    const countrySelect = id("countrySelect")
    const citySelect = id("citySelect")
    const cropModal = id("cropModal")
    const cropTitle = id("cropTitle")
    const cropImage = id("cropImage")
    const profilePhotoInput = id("profilePhotoInput")
    const backgroundPhotoInput = id("backgroundPhotoInput")
    const preview = id("photoPreview")
    const coverPreview = id("coverPreview")
    const mainImage = id("mainProfileImage")
    const coverDiv = qs(".profile-header-cover")
    const cropConfirm = id("cropConfirm")
    const cropCancel = id("cropCancel")
    const profilePhotoBase64 = id("profilePhotoBase64")
    const coverPhotoBase64 = id("coverPhotoBase64")
    const compressionStatus = id("compressionStatus")
    const updateBtn = id("updateBtn")

    // Maximum file size in bytes (2MB)
    const MAX_FILE_SIZE = 2 * 1024 * 1024

    // Cropper variables
    let cropper = null
    let cropContext = null // 'profile' or 'background'
    const Cropper = window.Cropper

    cropModal.style.display = "none"

    // City selection based on country
    if (countrySelect && citySelect) {
        countrySelect.addEventListener("change", function () {
            const selectedCountry = this.value
            const cities = citiesByCountry[selectedCountry] || []
            citySelect.innerHTML = '<option value="">Select City</option>'
            cities.forEach((city) => {
                const option = document.createElement("option")
                option.value = city
                option.textContent = city
                citySelect.appendChild(option)
            })
        })

        // Initialize cities based on current country selection
        if (countrySelect.value) {
            const event = new Event("change")
            countrySelect.dispatchEvent(event)

            // Set the current city if it exists
            if (citySelect.dataset.currentCity) {
                const currentCity = citySelect.dataset.currentCity
                setTimeout(() => {
                    Array.from(citySelect.options).forEach((option) => {
                        if (option.value === currentCity) {
                            option.selected = true
                        }
                    })
                }, 100)
            }
        }
    }

    // Profile photo selection
    if (profilePhotoInput) {
        profilePhotoInput.addEventListener("change", function () {
            const file = this.files[0]
            if (!file) return

            cropContext = "profile"
            startCropping(file)
        })
    }

    // Cover photo selection
    if (backgroundPhotoInput) {
        backgroundPhotoInput.addEventListener("change", function () {
            const file = this.files[0]
            if (!file) return

            cropContext = "background"
            startCropping(file)
        })
    }

    // Start cropping process
    function startCropping(file) {
        const reader = new FileReader()
        reader.onload = (e) => {
            cropImage.src = e.target.result
            cropModal.style.display = "flex"

            if (cropTitle) {
                cropTitle.textContent = cropContext === "background" ? "Crop your cover photo" : "Crop your profile photo"
            }

            if (cropper) {
                cropper.destroy()
            }

            const aspect = cropContext === "background" ? 6 / 1 : 1
            const boxW = cropContext === "background" ? 300 : 114
            const boxH = cropContext === "background" ? 50 : 114

            cropper = new Cropper(cropImage, {
                aspectRatio: aspect,
                viewMode: 1,
                autoCrop: true,
                autoCropArea: 1,
                cropBoxResizable: false,
                cropBoxMovable: true,
                zoomable: false,
                scalable: false,
                dragMode: "move",
                ready() {
                    const containerData = cropper.getContainerData()
                    cropper.setCropBoxData({
                        width: boxW,
                        height: boxH,
                        left: (containerData.width - boxW) / 2,
                        top: (containerData.height - boxH) / 2,
                    })
                },
            })
        }
        reader.readAsDataURL(file)
    }

    // Image compression function
    async function compressImage(canvas, initialQuality = 0.9, minQuality = 0.1, maxSizeBytes = MAX_FILE_SIZE) {
        let quality = initialQuality
        let dataUrl
        let binary

        if (compressionStatus) {
            compressionStatus.textContent = "Compressing image..."
            compressionStatus.style.display = "block"
        }

        // Try progressively lower quality until file size is acceptable or we hit minimum quality
        while (quality >= minQuality) {
            dataUrl = canvas.toDataURL("image/jpeg", quality)

            // Calculate approximate size (base64 is ~33% larger than binary)
            const base64 = dataUrl.split(",")[1]
            binary = atob(base64)
            const size = binary.length

            if (compressionStatus) {
                compressionStatus.textContent = `Compressing: ${Math.round(quality * 100)}% quality, size: ${(size / 1024).toFixed(1)}KB`
            }

            if (size <= maxSizeBytes) {
                if (compressionStatus) {
                    compressionStatus.textContent = `Compression complete: ${Math.round(quality * 100)}% quality, size: ${(size / 1024).toFixed(1)}KB`
                    setTimeout(() => {
                        compressionStatus.style.display = "none"
                    }, 3000)
                }
                return dataUrl
            }

            // Reduce quality for next attempt
            quality -= 0.1
        }

        // If we get here, even the lowest quality is too big
        // Try reducing dimensions as a last resort
        const MAX_DIMENSION = 800
        const originalWidth = canvas.width
        const originalHeight = canvas.height

        if (originalWidth > MAX_DIMENSION || originalHeight > MAX_DIMENSION) {
            if (compressionStatus) {
                compressionStatus.textContent = "Image still too large, reducing dimensions..."
            }

            const tempCanvas = document.createElement("canvas")
            const tempCtx = tempCanvas.getContext("2d")

            // Calculate new dimensions while maintaining aspect ratio
            let newWidth, newHeight
            if (originalWidth > originalHeight) {
                newWidth = MAX_DIMENSION
                newHeight = Math.floor(originalHeight * (MAX_DIMENSION / originalWidth))
            } else {
                newHeight = MAX_DIMENSION
                newWidth = Math.floor(originalWidth * (MAX_DIMENSION / originalHeight))
            }

            tempCanvas.width = newWidth
            tempCanvas.height = newHeight
            tempCtx.drawImage(canvas, 0, 0, newWidth, newHeight)

            // Try with lowest quality on smaller dimensions
            dataUrl = tempCanvas.toDataURL("image/jpeg", minQuality)

            if (compressionStatus) {
                const base64 = dataUrl.split(",")[1]
                binary = atob(base64)
                const size = binary.length
                compressionStatus.textContent = `Final compression: ${Math.round(minQuality * 100)}% quality, ${newWidth}x${newHeight}px, size: ${(size / 1024).toFixed(1)}KB`
                setTimeout(() => {
                    compressionStatus.style.display = "none"
                }, 3000)
            }

            return dataUrl
        }

        // If all else fails, return the lowest quality version
        if (compressionStatus) {
            compressionStatus.textContent = "Warning: Image is still large, upload may fail"
            setTimeout(() => {
                compressionStatus.style.display = "none"
            }, 5000)
        }

        return dataUrl
    }

    // Confirm crop action
    if (cropConfirm) {
        cropConfirm.addEventListener("click", async () => {
            if (!cropper) return

            try {
                if (cropContext === "profile") {
                    const canvas = cropper.getCroppedCanvas({
                        width: 114,
                        height: 114,
                        imageSmoothingQuality: "high",
                    })

                    // Compress the image with progressive quality reduction
                    const dataUrl = await compressImage(canvas)

                    // Ensure we have a valid image data URL
                    if (!dataUrl || !dataUrl.startsWith("data:image")) {
                        throw new Error("Invalid image data generated")
                    }

                    // Update the preview and form value
                    preview.src = dataUrl
                    if (mainImage) {
                        mainImage.src = dataUrl
                    }
                    if (profilePhotoBase64) {
                        profilePhotoBase64.value = dataUrl
                        console.log("Profile image data set to form field, length: " + dataUrl.length)
                    }
                } else if (cropContext === "background") {
                    const canvas = cropper.getCroppedCanvas({
                        width: 600,
                        height: 100,
                        imageSmoothingQuality: "high",
                    })

                    // Compress the image with progressive quality reduction
                    const dataUrl = await compressImage(canvas, 0.8, 0.1, 3 * 1024 * 1024)

                    // Ensure we have a valid image data URL
                    if (!dataUrl || !dataUrl.startsWith("data:image")) {
                        throw new Error("Invalid image data generated")
                    }

                    // Update the preview and form value
                    coverPreview.src = dataUrl
                    coverDiv.style.backgroundImage = `url('${dataUrl}')`
                    if (coverPhotoBase64) {
                        coverPhotoBase64.value = dataUrl
                        console.log("Cover image data set to form field, length: " + dataUrl.length)
                    }
                }
            } catch (error) {
                console.error("Error processing image:", error)
                alert("There was an error processing your image. Please try a smaller image.")
            }

            closeCropper()
        })
    }

    // Cancel crop action
    if (cropCancel) {
        cropCancel.addEventListener("click", closeCropper)
    }

    // Close cropper function
    function closeCropper() {
        cropModal.style.display = "none"
        if (cropper) {
            cropper.destroy()
            cropper = null
        }
        cropContext = null
    }
})
