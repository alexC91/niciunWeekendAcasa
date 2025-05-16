document.addEventListener("DOMContentLoaded", function () {
    const citiesByCountry = {
        "US": ["New York", "Los Angeles", "Chicago"],
        "RO": ["Bucuresti", "Cluj-Napoca", "Iasi"],
        "FR": ["Paris", "Lyon", "Marseille"],
        "DE": ["Berlin", "Munich", "Hamburg"],
        "JP": ["Tokyo", "Osaka", "Kyoto"]
    };

    const countrySelect = document.getElementById('countrySelect');
    const citySelect = document.getElementById('citySelect');
    const cropModal = document.getElementById('cropModal');
    const cropImage = document.getElementById('cropImage');
    const profilePhotoInput = document.getElementById('profilePhotoInput');
    const preview = document.getElementById('photoPreview');
    const mainImage = document.getElementById('mainProfileImage');
    const cropConfirm = document.getElementById('cropConfirm');
    const cropCancel = document.getElementById('cropCancel');

    let cropper;

    cropModal.style.display = "none";

    if (countrySelect && citySelect) {
        countrySelect.addEventListener('change', function () {
            const selectedCountry = this.value;
            const cities = citiesByCountry[selectedCountry] || [];
            citySelect.innerHTML = '<option value="">Select City</option>';
            cities.forEach(city => {
                const option = document.createElement('option');
                option.value = city;
                option.textContent = city;
                citySelect.appendChild(option);
            });
        });
    }

    profilePhotoInput.addEventListener('change', function () {
        const file = this.files[0];
        if (!file) return;

        const reader = new FileReader();
        reader.onload = function (e) {
            cropImage.src = e.target.result;
            cropModal.style.display = "flex";

            if (cropper) {
                cropper.destroy();
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
                dragMode: 'none',
                ready() {
                    const containerData = cropper.getContainerData();
                    cropper.setCropBoxData({
                        width: 114,
                        height: 114,
                        left: (containerData.width - 114) / 2,
                        top: (containerData.height - 114) / 2
                    });
                }
            });
        };
        reader.readAsDataURL(file);
    });

    cropConfirm.addEventListener('click', function () {
        if (cropper) {
            const canvas = cropper.getCroppedCanvas({ width: 114, height: 114 });
            const dataUrl = canvas.toDataURL();
            preview.src = dataUrl;
            mainImage.src = dataUrl;
            cropModal.style.display = "none";
            cropper.destroy();
            cropper = null;
        }
    });

    cropCancel.addEventListener('click', function () {
        cropModal.style.display = "none";
        if (cropper) {
            cropper.destroy();
            cropper = null;
        }
    });
});