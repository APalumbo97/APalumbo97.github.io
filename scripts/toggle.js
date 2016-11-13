function toggle() {
    if(document.getElementById("tog").className.match(/(?:^|\s)showing(?!\S)/)) {
        document.getElementById("tog").className = "";
    }
    else {
        document.getElementById("tog").className = "showing";
    }
}
