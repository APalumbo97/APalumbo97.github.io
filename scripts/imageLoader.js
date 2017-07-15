/**
 * Author: Anthony Palumbo
 * Date: 7-14-2017
 * Description: scripts to load images at the correct time.
 */

function loadBackground() {
    setTimeout(function() {
        var path = window.location.pathname;
        var page = path.split("/").pop();
        if (page != "index.html") {
            document.body.style.backgroundImage = 'url("../images/wallpaperHigh.jpg")';
        } else {
            document.body.style.backgroundImage = 'url("images/wallpaperHigh.jpg")';
        }
    }, 25);
}

loadBackground();
