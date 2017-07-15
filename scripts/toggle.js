/**
 * Author: Anthony Palumbo
 * Date: 11-16-16
 * Description: toggle function that animates the drop down menu for mobile displays.
 */

/**
 * Toggles the menu bar being displayed.
 */
function toggle() {
    if (document.getElementById("tog").className.match(/(?:^|\s)showing(?!\S)/)) {
        document.getElementById("tog").className = "";
    } else {
        document.getElementById("tog").className = "showing";
    }
}
