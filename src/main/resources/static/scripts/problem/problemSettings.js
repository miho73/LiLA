function preview() {
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
        console.log(error);
    });
    setTimeout(() => {
        window.scroll({
            top: document.body.scrollHeight,
            behavior: 'smooth'
        });
    }, 100);
}

var name, difficulty, branch, content, solution, tags;

function create() {
    if(validate()) {
        axios.post('/problems/create', {
            problem_name: this.name,
            difficulty: this.difficulty,
            branch: this.branch,
            content: this.content,
            solution: this.solution,
            tags: this.tag
        })
        .then(resp => {

        })
        .catch(error => {

        });
    }
}

function update() {

}

function validate() {
    this.name = getValue('name');
    this.difficulty = gei('difficulty').selectedIndex;
    this.branch = gei('branch').selectedIndex;
    this.content = vContent.getValue();
    this.solution = vSolution.getValue();
    this.tag = (gei('o1').checked << 0);

    var wasError = false;

    wasError = checkSingle(inRange(this.name.length, 50, 1), 'name')        || wasError;
    wasError = checkSingle(inRange(this.difficulty, 11, 1), 'difficulty')   || wasError;
    wasError = checkSingle(inRange(this.branch, 8, 1), 'branch')            || wasError;

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
