function setCookie(name, value, days) {
    const date = new Date();
    date.setTime(date.getTime() + (days*24*60*60*1000));
    const expires = "expires=" + date.toUTCString();
    document.cookie = name + "=" + value + ";" + expires + ";path=/";
}

function getCookie(name) {
    const decodedCookie = decodeURIComponent(document.cookie);
    const cookies = decodedCookie.split(';');
    name = name + "=";
    for (let i = 0; i < cookies.length; i++) {
        let c = cookies[i].trim();
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function acceptCookies() {
    setCookie("accepted_cookies", "yes", 365);
    document.getElementById("cookie-banner").style.display = "none";
}

window.onload = function () {
    if (!getCookie("accepted_cookies")) {
        document.getElementById("cookie-banner").style.display = "block";
    }
};
