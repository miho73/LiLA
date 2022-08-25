function openHambuger() {
    document.getElementById('hambuger-menu')?.classList.add('hambuger-open')
}
function closeHambuger() {
    document.getElementById('hambuger-menu')?.classList.remove('hambuger-open')
}

function getValue(id) {
    var element = document.getElementById(id);
    if(element == null) return null;
    else return element.value;
}

function gei(id) {
    return document.getElementById(id);
}