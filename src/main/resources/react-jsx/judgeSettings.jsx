const JudgeElement = ({code}) => {
    const [judgeType, setJudgeType] = React.useState(-1);

    const updateJudgeType = (e) => {
        setJudgeType(e.target.value);
    }

    var judgeAnswer;
    if(judgeType == -1) {
        judgeAnswer = (
            <div className="notset-judge">
                <i className="caption">채점 방법을 선택해주세요</i>
            </div>
        );
    }
    else if(judgeType == 0) {
        judgeAnswer = (
            <div className="self-judge">
                <div id={"jud-self-"+code}></div>
            </div>
        );
    }
    else if(judgeType == 1) {
        judgeAnswer = (
            <div className="equation-judge">
                <span className="math-edit"></span>
            </div>
        );
    }

    return (
        <div className="glow vertical-align" style={{width: "100%"}}>
            <div className="space-between" style={{width: "100%"}}>
                <input id={"jud-name-"+code} type="text" placeholder="채점 이름" title="채점 이름"/>
                <button className="button">삭제</button>
            </div>
            <select id={"jud-type-"+code} title="채점 방법" defaultValue={-1} onChange={updateJudgeType}>
                <option disabled value={-1}>채점 방법</option>
                <option value={0}>직접 채점</option>
                <option value={1}>수식</option>
            </select>
            <div className="jud-answer">
                {judgeAnswer}
            </div>
            <div className="horizontal-align">
                <input id={"jud-weig-"+code} type="number" placeholder="배점 비율" title="배점 비율"/>
                <span id={"jud-weex-"+code} className="judge-expect-score"></span>
            </div>
        </div>
    )
};

const JudgeSettings = () => {
    const [judgeCode, setJudgeCode] = React.useState(0);

    const addJudge = () => {
        setJudgeCode(judgeCode+1);
    }

    const judgeElements = [];
    for(var i=1; i<=judgeCode; i++) {
        judgeElements.push(<JudgeElement key={i} code={i}/>);
    }

    return (
        <div className="vertical-align" style={{width: "100%"}}>
            {judgeElements}
            <button className="button" onClick={addJudge}>채점 추가</button>
        </div>
    )
}

const root = ReactDOM.createRoot(document.getElementById('judge-settings'));
root.render(<JudgeSettings/>);
