document.addEventListener("DOMContentLoaded", () => {
    const imageLinks = Array.from(document.querySelectorAll(".img-grid-list a"))
    let currentIndex = 0

    function showImage(index) {
        const img = imageLinks[index].getAttribute("data-src")
        document.getElementById("lightbox-img").src = img
        document.getElementById("lightbox").style.display = "block"
    }

    imageLinks.forEach((el, index) => {
        el.addEventListener("click", (event) => {
            event.preventDefault() // oprește navigarea
            currentIndex = index
            showImage(currentIndex)
        })
    })

    window.closeLightbox = (event) => {
        if (event.target.id === "lightbox" || event.target.classList.contains("lightbox-close")) {
            document.getElementById("lightbox").style.display = "none"
        }
    }

    window.changeImage = (step) => {
        currentIndex = (currentIndex + step + imageLinks.length) % imageLinks.length
        showImage(currentIndex)
    }
})
