function openHambuger() {
    document.getElementById('hambuger-menu')?.classList.add('hambuger-open')
}
function closeHambuger() {
    document.getElementById('hambuger-menu')?.classList.remove('hambuger-open')
}

function getValue(id: string) {
    var element = document.getElementById(id) as HTMLInputElement | null;
    if(element == null) return null;
    else return element.value;
}

function gei(id: string) {
    return document.getElementById(id);
}