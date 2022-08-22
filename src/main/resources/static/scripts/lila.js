"use strict";
function openHambuger() {
    var _a;
    (_a = document.getElementById('hambuger-menu')) === null || _a === void 0 ? void 0 : _a.classList.add('hambuger-open');
}
function closeHambuger() {
    var _a;
    (_a = document.getElementById('hambuger-menu')) === null || _a === void 0 ? void 0 : _a.classList.remove('hambuger-open');
}
function getValue(id) {
    var element = document.getElementById(id);
    if (element == null)
        return null;
    else
        return element.value;
}
function gei(id) {
    return document.getElementById(id);
}
