window.addEventListener('load', load);

const answerElements = [];

function load() {
    var answers = document.getElementsByClassName('answer-field');
    var idx = 0;

    for(var answer of answers) {
        var judgeType = answer.getAttribute('jtype');
        switch(judgeType) {
            case '0':
                answerElements.push(0);
                break;
            case '1':
                const answerMQ = MQ.MathField(gei(`je-${idx}`), {});
                answerElements.push(answerMQ);
                break;
            default:
                console.error(judgeType+' is not a judge type');
        }
        idx++;
    }
}

const answers = []

function openAnswer() {
    gei('answer-area').style.display = 'block';
    setTimeout(()=>{
        gei('answer-area').classList.add('open-answer');
    }, 10);
}

var isFirst = true;
function preSubmit() {
    if(isFirst) {
        gei('answer').remove();
        openAnswer();
        isFirst = false;
    }

    // check all answers;
    var idx = 0, pass = true;
    for(const answerElement of answerElements) {
        if(answerElement == 0) {
            if(answers[idx] == undefined) {
                pass = false;

                gei(`js-${idx}-ac`).disabled = false;
                gei(`js-${idx}-wa`).disabled = false;
                answers.push({
                    m: 0,
                    a: undefined
                });
            }
            else if(answers[idx].a == undefined) pass = false;
        }
        // MathQuill input
        else {
            if(answers[idx] == undefined) {
                const latex = answerElement.latex();
                answers.push({
                    m: 1,       //type
                    a: latex    //answer
                });
                gei(`je-${idx}`).innerText = `$${latex}$`;
                gei(`je-${idx}`).classList = ['mathjax'];
            }
        }
        idx++;
    }

    if(pass) {
        submit();
    }
    else {
        MathJax.Hub.Typeset();
    }
}

function submit() {
    axios.post('/problems/judge/submit', {
        'problem-code': PROBLEM_CODE,
        'answer': JSON.stringify(answers)
    }).then(response => {
        gei('answer-input').classList.add('after-submit');

        afterSubmit(response);
    }).catch(error => {
        console.error(error);
    });
}

function ac(e) {
    gei(`js-${e}-wa`).style.opacity = 0;
    gei(`js-${e}-ac`).disabled = true;
    gei(`js-${e}-wa`).disabled = true;
    answers[e].a = true;
}
function wa(e) {
    gei(`js-${e}-ac`).style.opacity = 0;
    gei(`js-${e}-ac`).disabled = true;
    gei(`js-${e}-wa`).disabled = true;
    answers[e].a = false;
}

function afterSubmit(res) {

}