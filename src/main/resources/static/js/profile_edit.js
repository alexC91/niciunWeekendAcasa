document.addEventListener("DOMContentLoaded", () => {
    const citiesByCountry = {
        US: ["New York", "Los Angeles", "Chicago"],
        RO: ["Bucuresti", "Cluj-Napoca", "Iasi"],
        FR: ["Paris", "Lyon", "Marseille"],
        DE: ["Berlin", "Munich", "Hamburg"],
        JP: ["Tokyo", "Osaka", "Kyoto"],
    }

    const countrySelect = document.getElementById("countrySelect")
    const citySelect = document.getElementById("citySelect")
    const cropModal = document.getElementById("cropModal")
    const cropImage = document.getElementById("cropImage")
    const profilePhotoInput = document.getElementById("profilePhotoInput")
    const preview = document.getElementById("photoPreview")
    const mainImage = document.getElementById("mainProfileImage")
    const cropConfirm = document.getElementById("cropConfirm")
    const cropCancel = document.getElementById("cropCancel")
    const profilePhotoBase64 = document.getElementById("profilePhotoBase64")
    const compressionStatus = document.getElementById("compressionStatus")

    // Maximum file size in bytes (2MB)
    const MAX_FILE_SIZE = 2 * 1024 * 1024

    let cropper
    const Cropper = window.Cropper // Declare the Cropper variable

    cropModal.style.display = "none"

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

    if (profilePhotoInput) {
        profilePhotoInput.addEventListener("change", function () {
            const file = this.files[0]
            if (!file) return

            const reader = new FileReader()
            reader.onload = (e) => {
                cropImage.src = e.target.result
                cropModal.style.display = "flex"

                if (cropper) {
                    cropper.destroy()
                }

                cropper = new Cropper(cropImage, {
                    aspectRatio: 1,
                    viewMode: 1,
                    autoCrop: true,
                    autoCropArea: 1,
                    cropBoxResizable: false,
                    cropBoxMovable: true,
                    zoomable: false,
                    scalable: false,
                    dragMode: "none",
                    ready() {
                        const containerData = cropper.getContainerData()
                        cropper.setCropBoxData({
                            width: 114,
                            height: 114,
                            left: (containerData.width - 114) / 2,
                            top: (containerData.height - 114) / 2,
                        })
                    },
                })
            }
            reader.readAsDataURL(file)
        })
    }

    // Function to compress image with progressive quality reduction
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

    if (cropConfirm) {
        cropConfirm.addEventListener("click", async () => {
            if (cropper) {
                const canvas = cropper.getCroppedCanvas({
                    width: 114,
                    height: 114,
                    imageSmoothingQuality: "high",
                })

                try {
                    // Compress the image with progressive quality reduction
                    const dataUrl = await compressImage(canvas)

                    preview.src = dataUrl
                    if (mainImage) {
                        mainImage.src = dataUrl
                    }
                    if (profilePhotoBase64) {
                        profilePhotoBase64.value = dataUrl
                    }
                } catch (error) {
                    console.error("Error compressing image:", error)
                    alert("There was an error processing your image. Please try a smaller image.")
                }

                cropModal.style.display = "none"
                cropper.destroy()
                cropper = null
            }
        })
    }

    if (cropCancel) {
        cropCancel.addEventListener("click", () => {
            cropModal.style.display = "none"
            if (cropper) {
                cropper.destroy()
                cropper = null
            }
        })
    }
})
