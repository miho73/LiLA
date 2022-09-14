function preview() {
    gei('previw').disabled = true;
    axios.all([
        axios.post('/problems/lilac/compile', {
            lilac: vContent.getValue()
        }),
        axios.post('/problems/lilac/compile', {
            lilac: vSolution.getValue()
        })
    ])
    .then(axios.spread((vResp, sResp) => {
        document.getElementById('preview').innerHTML = vResp.data.result + sResp.data.result;
    })).catch((error) => {
        notification('컴파일에 실패했습니다.', axiosError(error), 2);
    }).finally(() => {
        gei('previw').disabled = false;
    });

    setTimeout(() => {
        window.scroll({
            top: document.body.scrollHeight,
            behavior: 'smooth'
        });
    }, 100);
}

var name, difficulty, branch, content, solution, tags, state, judge;

function create() {
    if(validate()) {
        gei('commit').disabled = true;
        axios.post('/problems/create', {
            problem_name: this.name,
            difficulty: this.difficulty,
            branch: this.branch,
            content: this.content,
            solution: this.solution,
            tags: this.tag,
            state: this.state,
            answer: JSON.stringify(judge)
        })
        .then(resp => {
            var pn = resp.data.result;
            window.location.href = `/problems/solve/${pn}`
        })
        .catch(error => {
            notification('문제를 등록하지 못했습니다.', axiosError(error), 2);
        })
        .finally(() => {
            gei('commit').disabled = false;
        });
    }
}

function update(problem_code) {
    if(validate()) {
        gei('update').disabled = true;
        axios.put(`/problems/update/${problem_code}`, {
            problem_name: this.name,
            difficulty: this.difficulty,
            branch: this.branch,
            content: this.content,
            solution: this.solution,
            tags: this.tag,
            state: this.state,
            answer: JSON.stringify(judge)
        })
        .then(resp => {
            var pn = resp.data.result;
            window.location.href = `/problems/solve/${pn}`
        })
        .catch(error => {
            notification('문제를 수정하지 못했습니다.', axiosError(error), 2);
        }).finally(() => {
            gei('update').disabled = false;
        });
    }
}

function validate() {
    this.name = getValue('name');
    this.difficulty = gei('difficulty').selectedIndex-1;
    this.branch = gei('branch').selectedIndex-1;
    this.state = gei('status').selectedIndex-1;
    this.content = vContent.getValue();
    this.solution = vSolution.getValue();
    this.tag = (gei('o1').checked << 0);
    this.judge = [];

    if(state == 1) {
        this.tag ^= 0b00100000000000000000000000000000;
    }
    else if(state == 2) {
        this.tag ^= 0b01000000000000000000000000000000;
    }
    else if(state == 3) {
        this.tag ^= 0b10000000000000000000000000000000;
    }
    
    var wasError = false;
    
    wasError = checkSingle(inRange(this.name.length, 50, 1), 'name')        || wasError;
    wasError = checkSingle(inRange(this.difficulty, 10, 0), 'difficulty')   || wasError;
    wasError = checkSingle(inRange(this.branch, 7, 0), 'branch')            || wasError;
    wasError = checkSingle(inRange(this.state, 3, 0), 'status')             || wasError;

    const judgeCount = gei('judge-count').value;
    var quotaSum = 0;
    // per element validation
    for(var i=0; i<judgeCount; i++) {
        const judge = {
            n: gei(`jud-name-${i}`).value,
            m: gei(`jud-type-${i}`).selectedIndex-1,
            q: Number.parseFloat(gei(`jud-weig-${i}`).value)
        };
        wasError = checkSingle(inRange(judge.n.length, 50, 1), `jud-name-${i}`)     || wasError;
        wasError = checkSingle(inRange(judge.m, 1, 0), `jud-type-${i}`)             || wasError;
        wasError = checkSingle(Number.isInteger(judge.q), `jud-weig-${i}`)          || wasError;
        judge['a'] = judge.m == 0 ? '' : answerMathQuill[i].latex();
        quotaSum += judge.q;

        this.judge.push(judge);
    }
    wasError = checkSingle(quotaSum == 100, 'alloc') || wasError;
    
    if(wasError) {
        window.scroll({
            top: 0,
            behavior: 'smooth'
        });
    }
    else {
        this.name.replaceAll('<', '&#60;')
                 .replaceAll('>', '&#62;');
    }

    return !wasError;
}
