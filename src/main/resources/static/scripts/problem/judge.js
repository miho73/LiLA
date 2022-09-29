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
    // pre - validate text input
    var validation = false, idx = 0;
    for(const answerElement of answerElements) {
        if(answerElement != 0) {
            const len = answerElement.latex().length;
            if(len > 100) {
                validation = true;
                gei(`je-${idx}`).classList.add('form-error');
            }
            else {
                gei(`je-${idx}`).classList.remove('form-error');
            }
        }
        idx++;
    }
    if(validation) {
        notification('정답을 확인해주세요', '정답이 너무 깁니다.', 1);
        return;
    }

    // all valid. proceed answer collection
    if(isFirst) {
        gei('answer').remove();
        openAnswer();
        isFirst = false;
    }

    // check all answers;
    var idx = 0, pass = true;
    for(const answerElement of answerElements) {
        // user judge select
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
    // validate
    for(const answer of answers) {
        if(answer.m != 0 && answer.a.length > 100) {
            notification('제출하지 못했습니다', '정답을 검증하지 못했습니다.', 2);
            return;
        } 
    }

    gei('judge-result').style.display = 'block';
    axios.post('/problems/judge/submit', {
        'problem-code': PROBLEM_CODE,
        'answer': JSON.stringify(answers)
    }).then(response => {
        gei('answer-input').classList.add('after-submit');

        if(response.status != 200) throw new Error();
        afterSubmit(response.data.result);
    }).catch(error => {
        notification('제출하지 못했습니다.', '제출 중 문제가 발생했습니다.', 2);
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
    console.log(res);

    var html = '<table class="report-table"><thead><tr><td>채점</td><td>정답</td><td>내 정답</td><td>점수</td></tr></thead><tbody>';
    var idx = 0;
    for(var jud of res.judge) {
        if(jud.method == 0) {
            const ac = jud.correct;
            html += `<tr><td>${gei(`jl-`+idx).innerText}</td><td></td><td>${ac ? '정답' : '오답'}</td><td class="${ac ? 'ac' : 'wa'}">${jud['your-score']} / ${jud['quota'] * res['full-score'] / 100}</td></tr>`;
        }
        else if(jud.method == 1) {
            const ac = jud.correct;
            html += `<tr><td>${gei(`jl-`+idx).innerText}</td><td>$${jud.answer}$</td><td>$${jud.yours}$</td><td class="${ac ? 'ac' : 'wa'}">${jud['your-score']} / ${jud['quota'] * res['full-score'] / 100}</td></tr>`;
        }
    }
    html += `</tbody></table><p class="judge-summary">${res['your-score']} / ${res['full-score']}</p>`;
    gei('judge-result').innerHTML = html;

    MathJax.Hub.Typeset();
}