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
}

function create() {

}

function update() {

}