"use strict";

function _slicedToArray(arr, i) { return _arrayWithHoles(arr) || _iterableToArrayLimit(arr, i) || _unsupportedIterableToArray(arr, i) || _nonIterableRest(); }

function _nonIterableRest() { throw new TypeError("Invalid attempt to destructure non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method."); }

function _unsupportedIterableToArray(o, minLen) { if (!o) return; if (typeof o === "string") return _arrayLikeToArray(o, minLen); var n = Object.prototype.toString.call(o).slice(8, -1); if (n === "Object" && o.constructor) n = o.constructor.name; if (n === "Map" || n === "Set") return Array.from(o); if (n === "Arguments" || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)) return _arrayLikeToArray(o, minLen); }

function _arrayLikeToArray(arr, len) { if (len == null || len > arr.length) len = arr.length; for (var i = 0, arr2 = new Array(len); i < len; i++) { arr2[i] = arr[i]; } return arr2; }

function _iterableToArrayLimit(arr, i) { var _i = arr == null ? null : typeof Symbol !== "undefined" && arr[Symbol.iterator] || arr["@@iterator"]; if (_i == null) return; var _arr = []; var _n = true; var _d = false; var _s, _e; try { for (_i = _i.call(arr); !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"] != null) _i["return"](); } finally { if (_d) throw _e; } } return _arr; }

function _arrayWithHoles(arr) { if (Array.isArray(arr)) return arr; }

var JudgeElement = function JudgeElement(_ref) {
  var code = _ref.code;

  var _React$useState = React.useState(-1),
      _React$useState2 = _slicedToArray(_React$useState, 2),
      judgeType = _React$useState2[0],
      setJudgeType = _React$useState2[1];

  var updateJudgeType = function updateJudgeType(e) {
    setJudgeType(e.target.value);
  };

  var judgeAnswer;

  if (judgeType == -1) {
    judgeAnswer = /*#__PURE__*/React.createElement("div", {
      className: "notset-judge"
    }, /*#__PURE__*/React.createElement("i", {
      className: "caption"
    }, "\uCC44\uC810 \uBC29\uBC95\uC744 \uC120\uD0DD\uD574\uC8FC\uC138\uC694"));
  } else if (judgeType == 0) {
    judgeAnswer = /*#__PURE__*/React.createElement("div", {
      className: "self-judge"
    }, /*#__PURE__*/React.createElement("div", {
      id: "jud-self-" + code
    }));
  } else if (judgeType == 1) {
    judgeAnswer = /*#__PURE__*/React.createElement("div", {
      className: "equation-judge"
    }, /*#__PURE__*/React.createElement("span", {
      className: "math-edit"
    }));
  }

  return /*#__PURE__*/React.createElement("div", {
    className: "glow vertical-align",
    style: {
      width: "100%"
    }
  }, /*#__PURE__*/React.createElement("div", {
    className: "space-between",
    style: {
      width: "100%"
    }
  }, /*#__PURE__*/React.createElement("input", {
    id: "jud-name-" + code,
    type: "text",
    placeholder: "\uCC44\uC810 \uC774\uB984",
    title: "\uCC44\uC810 \uC774\uB984"
  }), /*#__PURE__*/React.createElement("button", {
    className: "button"
  }, "\uC0AD\uC81C")), /*#__PURE__*/React.createElement("select", {
    id: "jud-type-" + code,
    title: "\uCC44\uC810 \uBC29\uBC95",
    defaultValue: -1,
    onChange: updateJudgeType
  }, /*#__PURE__*/React.createElement("option", {
    disabled: true,
    value: -1
  }, "\uCC44\uC810 \uBC29\uBC95"), /*#__PURE__*/React.createElement("option", {
    value: 0
  }, "\uC9C1\uC811 \uCC44\uC810"), /*#__PURE__*/React.createElement("option", {
    value: 1
  }, "\uC218\uC2DD")), /*#__PURE__*/React.createElement("div", {
    className: "jud-answer"
  }, judgeAnswer), /*#__PURE__*/React.createElement("div", {
    className: "horizontal-align"
  }, /*#__PURE__*/React.createElement("input", {
    id: "jud-weig-" + code,
    type: "number",
    placeholder: "\uBC30\uC810 \uBE44\uC728",
    title: "\uBC30\uC810 \uBE44\uC728"
  }), /*#__PURE__*/React.createElement("span", {
    id: "jud-weex-" + code,
    className: "judge-expect-score"
  })));
};

var JudgeSettings = function JudgeSettings() {
  var _React$useState3 = React.useState(0),
      _React$useState4 = _slicedToArray(_React$useState3, 2),
      judgeCode = _React$useState4[0],
      setJudgeCode = _React$useState4[1];

  var addJudge = function addJudge() {
    setJudgeCode(judgeCode + 1);
  };

  var judgeElements = [];

  for (var i = 1; i <= judgeCode; i++) {
    judgeElements.push( /*#__PURE__*/React.createElement(JudgeElement, {
      key: i,
      code: i
    }));
  }

  return /*#__PURE__*/React.createElement("div", {
    className: "vertical-align",
    style: {
      width: "100%"
    }
  }, judgeElements, /*#__PURE__*/React.createElement("button", {
    className: "button",
    onClick: addJudge
  }, "\uCC44\uC810 \uCD94\uAC00"));
};

var root = ReactDOM.createRoot(document.getElementById('judge-settings'));
root.render( /*#__PURE__*/React.createElement(JudgeSettings, null));