function openhamburger() {
    document.getElementById('hamburger-menu')?.classList.add('hamburger-open')
}
function closehamburger() {
    document.getElementById('hamburger-menu')?.classList.remove('hamburger-open')
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

var noti_code = 0;

/**
 * Prompt notification to user
 * @param {string} title 
 * @param {string} content 
 * @param {number} level 0: info / 1: warning / 2: error
 */
function notification(title, content, level) {
    const noti = document.createElement("div");
    noti.id = `n${noti_code}`
    noti.setAttribute('nc', noti_code);
    noti.classList.add("notification");
    if(level == 1) {
        noti.style.backgroundColor = 'var(--warning)';
    }
    else if(level == 2) {
        noti.style.backgroundColor = 'var(--error)';
    }
    noti.addEventListener('click', () => {
        noti.style.opacity = 0;
        setTimeout(() => {
            noti.remove();
        }, 100);
    });
    const titlee = document.createElement("p");
    titlee.setAttribute('nc', noti_code);
    titlee.classList.add("notification-title");
    titlee.innerText = title;
    const contente = document.createElement("p");
    contente.setAttribute('nc', noti_code);
    contente.classList.add("notification-content");
    contente.innerText = content;
    noti.appendChild(titlee);
    noti.appendChild(contente);
    gei('notification-root').appendChild(noti);
    setTimeout((noti_code) => {
        noti.style.opacity = 0;
        setTimeout(() => {
            noti.remove();
        }, 100);
    }, 5000);
    noti_code++;
}

function axiosError(code) {
    switch(code.code) {
        case 'ERR_NETWORK':
            return '인터넷 연결을 확인해주세요.';
            break;
        default:
            return '문제가 발생하였습니다.';
            break;
    }
}