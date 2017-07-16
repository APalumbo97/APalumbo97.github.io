/**
 * Author: Anthony Palumbo
 * Date: 7-16-2017
 * Description: script to load all images after the pages has finished loading.
 */

/**
 * Switches all the images src to their data-src, if it exists.
 */
function loadImages() {
    window.addEventListener("load", function() {
        var allImages = document.getElementsByTagName("img");
        for (var i = 0; i < allImages.length; i++) {
            if (allImages[i].getAttribute("data-src")) {
                allImages[i].setAttribute("src", allImages[i].getAttribute("data-src"));
            }
        }
    })
}

loadImages();
