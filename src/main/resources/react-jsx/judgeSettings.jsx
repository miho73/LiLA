const CaptionJudgeOption = ({style}) => {
    return (
        <div className="notset-judge" style={style}>
            <i className="caption">채점 방법을 선택해주세요</i>
        </div>
    );
}
const EquationJudgeOption = ({code, style}) => {
    return (
        <div className="equation-judge" style={style}>
            <span id={"jud-mat-"+code} className="math-edit"></span>
        </div>
    );
}

const JudgeElement = ({code, noticeAllocationUpdate}) => {
    const [judgeType, setJudgeType] = React.useState(-1);
    var allocated = 0;

    const updateElementAllocated = (e) => {
        noticeAllocationUpdate(e.target.value);
    }
    const updateJudgeType = (e) => {
        setJudgeType(e.target.value);
    }

    var judgeAnswer;
    if(judgeType == -1) {
        judgeAnswer = (
            <div className="jud-answer">
                <input id={"jud-typ-"+code} readOnly hidden value={judgeType}/>
                <CaptionJudgeOption code={code} style={{display: 'block'}}/>
                <p style={{display: 'none'}}></p>
                <EquationJudgeOption code={code} style={{display: 'none'}}/>
            </div>
        );
    }
    else if(judgeType == 0) {
        judgeAnswer = (
            <div className="jud-answer">
                <input id={"jud-typ-"+code} readOnly hidden value={judgeType}/>
                <CaptionJudgeOption code={code} style={{display: 'none'}}/>
                <p style={{display: 'block'}}>문제를 풀고 사용자가 직접 채점을 합니다.</p>
                <EquationJudgeOption code={code} style={{display: 'none'}}/>
            </div>
        );
    }
    else if(judgeType == 1) {
        judgeAnswer = (
            <div className="jud-answer">
                <input id={"jud-typ-"+code} readOnly hidden value={judgeType}/>
                <CaptionJudgeOption code={code} style={{display: 'none'}}/>
                <p style={{display: 'none'}}>문제를 풀고 사용자가 직접 채점을 합니다.</p>
                <EquationJudgeOption code={code} style={{display: 'block'}}/>
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
            {judgeAnswer}
            <div className="horizontal-align">
                <input id={"jud-weig-"+code} type="number" placeholder="배점 비율 %" title="배점 비율 %" onChange={updateElementAllocated}/>
                <span id={"jud-weex-"+code} className="judge-expect-score"></span>
            </div>
        </div>
    )
};

var answerMathQuill = {}

const JudgeSettings = () => {
    const [judgeCode, setJudgeCode] = React.useState({});
    const [allocated, setAllocated] = React.useState(0);

    const noticeAllocationUpdate = (delta) => {
        setAllocated(allocated + delta);
    }

    const addJudge = () => {
        const code = Object.keys(judgeCode).length;

        setJudgeCode({...judgeCode, [code]: <JudgeElement key={code} code={code} noticeAllocationUpdate={noticeAllocationUpdate}/>});

        setTimeout(() => {
            const answerMQ = MQ.MathField(gei(`jud-mat-${code}`), {});
            answerMathQuill = {...answerMathQuill, [code]: answerMQ};
        }, 100);
    }

    const judgeElements = [];
    Object.values(judgeCode).forEach(val => {
        judgeElements.push(val);
    });

    var allocationFeedback;
    if(allocated == 100) {
        allocationFeedback = <p id="alloc">배점이 모두 할당되었습니다!</p>
    }
    else if(allocated > 100) {
        allocationFeedback = <p id="alloc">배점이 {allocated-100}% 만큼 초과하여 할당되었습니다.</p>
    }
    else if(allocated < 100) {
        allocationFeedback = <p id="alloc">배점을 {100-allocated}% 만큼 더 할당해야 합니다.</p>
    }
    return (
        <div className="vertical-align" style={{width: "100%"}}>
            <input id="judge-count" hidden readOnly value={Object.keys(judgeCode).length}/>
            {judgeElements}
            <button className="button" onClick={addJudge}>채점 추가</button>
            {allocationFeedback}
        </div>
    );
}

const root = ReactDOM.createRoot(document.getElementById('judge-settings'));
root.render(<JudgeSettings/>);
