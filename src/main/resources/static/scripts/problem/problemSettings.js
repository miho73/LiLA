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

var name, difficulty, branch, content, solution, tags, state;

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
            answer: ''
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

function update() {
    if(validate()) {
        gei('update').disabled = true;
        axios.post('/problems/update', {
            problem_name: this.name,
            difficulty: this.difficulty,
            branch: this.branch,
            content: this.content,
            solution: this.solution,
            tags: this.tag,
            state: this.state,
            answer: ''
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
    this.difficulty = gei('difficulty').selectedIndex;
    this.branch = gei('branch').selectedIndex;
    this.state = gei('status').selectedIndex;
    this.content = vContent.getValue();
    this.solution = vSolution.getValue();
    this.tag = (gei('o1').checked << 0);
    
    var wasError = false;
    
    wasError = checkSingle(inRange(this.name.length, 50, 1), 'name')        || wasError;
    wasError = checkSingle(inRange(this.difficulty, 11, 1), 'difficulty')   || wasError;
    wasError = checkSingle(inRange(this.branch, 8, 1), 'branch')            || wasError;
    wasError = checkSingle(inRange(this.state, 4, 1), 'status')            || wasError;
    
    if(wasError) {
        window.scroll({
            top: 0,
            behavior: 'smooth'
        });
    }
    else {
        this.name.replace('<', '&#60;')
        .replace('>', '&#62;');
    }

    return !wasError;
}
