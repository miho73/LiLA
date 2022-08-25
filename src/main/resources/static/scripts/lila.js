function openHambuger() {
    document.getElementById('hambuger-menu')?.classList.add('hambuger-open')
}
function closeHambuger() {
    document.getElementById('hambuger-menu')?.classList.remove('hambuger-open')
}

/**
 * Get value from input element
 * @param {string} id 
 * @returns value of input element
 */
function getValue(id) {
    var element = document.getElementById(id);
    if(element == null) return null;
    else return element.value;
}

/**
 * Get HTML element has passed id
 * @param {string} id 
 * @returns HTML element
 */
function gei(id) {
    return document.getElementById(id);
}

/**
 * Check each condition to validate form
 * @param {boolean} condition true when element is valid
 * @param {string} id id of element to apply warning
 * @returns true when element is invalid
 */
function checkSingle(condition, id) {
    if(!condition) gei(id).classList.add('form-error');
    else gei(id).classList.remove('form-error');
    return !condition
}

/**
 * Check if value is in bound [upper, lower]
 * @param {number} value value to check if it is in bound
 * @param {number} upper upper bound
 * @param {number} lower lower bound
 * @returns true when value is in bound
 */
function inRange(value, upper, lower) {
    return value <= upper && value >= lower;
}