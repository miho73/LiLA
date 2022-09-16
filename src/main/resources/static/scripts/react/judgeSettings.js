"use strict";

function ownKeys(object, enumerableOnly) { var keys = Object.keys(object); if (Object.getOwnPropertySymbols) { var symbols = Object.getOwnPropertySymbols(object); enumerableOnly && (symbols = symbols.filter(function (sym) { return Object.getOwnPropertyDescriptor(object, sym).enumerable; })), keys.push.apply(keys, symbols); } return keys; }

function _objectSpread(target) { for (var i = 1; i < arguments.length; i++) { var source = null != arguments[i] ? arguments[i] : {}; i % 2 ? ownKeys(Object(source), !0).forEach(function (key) { _defineProperty(target, key, source[key]); }) : Object.getOwnPropertyDescriptors ? Object.defineProperties(target, Object.getOwnPropertyDescriptors(source)) : ownKeys(Object(source)).forEach(function (key) { Object.defineProperty(target, key, Object.getOwnPropertyDescriptor(source, key)); }); } return target; }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

function _slicedToArray(arr, i) { return _arrayWithHoles(arr) || _iterableToArrayLimit(arr, i) || _unsupportedIterableToArray(arr, i) || _nonIterableRest(); }

function _nonIterableRest() { throw new TypeError("Invalid attempt to destructure non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method."); }

function _unsupportedIterableToArray(o, minLen) { if (!o) return; if (typeof o === "string") return _arrayLikeToArray(o, minLen); var n = Object.prototype.toString.call(o).slice(8, -1); if (n === "Object" && o.constructor) n = o.constructor.name; if (n === "Map" || n === "Set") return Array.from(o); if (n === "Arguments" || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)) return _arrayLikeToArray(o, minLen); }

function _arrayLikeToArray(arr, len) { if (len == null || len > arr.length) len = arr.length; for (var i = 0, arr2 = new Array(len); i < len; i++) { arr2[i] = arr[i]; } return arr2; }

function _iterableToArrayLimit(arr, i) { var _i = arr == null ? null : typeof Symbol !== "undefined" && arr[Symbol.iterator] || arr["@@iterator"]; if (_i == null) return; var _arr = []; var _n = true; var _d = false; var _s, _e; try { for (_i = _i.call(arr); !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"] != null) _i["return"](); } finally { if (_d) throw _e; } } return _arr; }

function _arrayWithHoles(arr) { if (Array.isArray(arr)) return arr; }

var CaptionJudgeOption = function CaptionJudgeOption(_ref) {
  var style = _ref.style;
  return /*#__PURE__*/React.createElement("div", {
    className: "notset-judge",
    style: style
  }, /*#__PURE__*/React.createElement("i", {
    className: "caption"
  }, "\uCC44\uC810 \uBC29\uBC95\uC744 \uC120\uD0DD\uD574\uC8FC\uC138\uC694"));
};

var EquationJudgeOption = function EquationJudgeOption(_ref2) {
  var code = _ref2.code,
      style = _ref2.style;
  return /*#__PURE__*/React.createElement("div", {
    className: "equation-judge",
    style: style
  }, /*#__PURE__*/React.createElement("span", {
    id: "jud-mat-" + code,
    className: "math-edit"
  }));
};

var JudgeElement = function JudgeElement(_ref3) {
  var code = _ref3.code,
      noticeAllocationUpdate = _ref3.noticeAllocationUpdate;

  var _React$useState = React.useState(-1),
      _React$useState2 = _slicedToArray(_React$useState, 2),
      judgeType = _React$useState2[0],
      setJudgeType = _React$useState2[1];

  var allocated = 0;

  var updateElementAllocated = function updateElementAllocated(e) {
    noticeAllocationUpdate(e.target.value);
  };

  var updateJudgeType = function updateJudgeType(e) {
    setJudgeType(e.target.value);
  };

  var judgeAnswer;

  if (judgeType == -1) {
    judgeAnswer = /*#__PURE__*/React.createElement("div", {
      className: "jud-answer"
    }, /*#__PURE__*/React.createElement("input", {
      id: "jud-typ-" + code,
      readOnly: true,
      hidden: true,
      value: judgeType
    }), /*#__PURE__*/React.createElement(CaptionJudgeOption, {
      code: code,
      style: {
        display: 'block'
      }
    }), /*#__PURE__*/React.createElement("p", {
      style: {
        display: 'none'
      }
    }), /*#__PURE__*/React.createElement(EquationJudgeOption, {
      code: code,
      style: {
        display: 'none'
      }
    }));
  } else if (judgeType == 0) {
    judgeAnswer = /*#__PURE__*/React.createElement("div", {
      className: "jud-answer"
    }, /*#__PURE__*/React.createElement("input", {
      id: "jud-typ-" + code,
      readOnly: true,
      hidden: true,
      value: judgeType
    }), /*#__PURE__*/React.createElement(CaptionJudgeOption, {
      code: code,
      style: {
        display: 'none'
      }
    }), /*#__PURE__*/React.createElement("p", {
      style: {
        display: 'block'
      }
    }, "\uBB38\uC81C\uB97C \uD480\uACE0 \uC0AC\uC6A9\uC790\uAC00 \uC9C1\uC811 \uCC44\uC810\uC744 \uD569\uB2C8\uB2E4."), /*#__PURE__*/React.createElement(EquationJudgeOption, {
      code: code,
      style: {
        display: 'none'
      }
    }));
  } else if (judgeType == 1) {
    judgeAnswer = /*#__PURE__*/React.createElement("div", {
      className: "jud-answer"
    }, /*#__PURE__*/React.createElement("input", {
      id: "jud-typ-" + code,
      readOnly: true,
      hidden: true,
      value: judgeType
    }), /*#__PURE__*/React.createElement(CaptionJudgeOption, {
      code: code,
      style: {
        display: 'none'
      }
    }), /*#__PURE__*/React.createElement("p", {
      style: {
        display: 'none'
      }
    }, "\uBB38\uC81C\uB97C \uD480\uACE0 \uC0AC\uC6A9\uC790\uAC00 \uC9C1\uC811 \uCC44\uC810\uC744 \uD569\uB2C8\uB2E4."), /*#__PURE__*/React.createElement(EquationJudgeOption, {
      code: code,
      style: {
        display: 'block'
      }
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
  }), "// TODO: implement remove feature", /*#__PURE__*/React.createElement("button", {
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
  }, "\uC218\uC2DD")), judgeAnswer, /*#__PURE__*/React.createElement("div", {
    className: "horizontal-align"
  }, /*#__PURE__*/React.createElement("input", {
    id: "jud-weig-" + code,
    type: "number",
    placeholder: "\uBC30\uC810 \uBE44\uC728 %",
    title: "\uBC30\uC810 \uBE44\uC728 %",
    onChange: updateElementAllocated
  }), /*#__PURE__*/React.createElement("span", {
    id: "jud-weex-" + code,
    className: "judge-expect-score"
  })));
};

var answerMathQuill = {};

var JudgeSettings = function JudgeSettings() {
  var _React$useState3 = React.useState({}),
      _React$useState4 = _slicedToArray(_React$useState3, 2),
      judgeCode = _React$useState4[0],
      setJudgeCode = _React$useState4[1];

  var _React$useState5 = React.useState(0),
      _React$useState6 = _slicedToArray(_React$useState5, 2),
      allocated = _React$useState6[0],
      setAllocated = _React$useState6[1];

  var noticeAllocationUpdate = function noticeAllocationUpdate(delta) {
    setAllocated(allocated + delta);
  };

  var addJudge = function addJudge() {
    var code = Object.keys(judgeCode).length;
    setJudgeCode(_objectSpread(_objectSpread({}, judgeCode), {}, _defineProperty({}, code, /*#__PURE__*/React.createElement(JudgeElement, {
      key: code,
      code: code,
      noticeAllocationUpdate: noticeAllocationUpdate
    }))));
    setTimeout(function () {
      var answerMQ = MQ.MathField(gei("jud-mat-".concat(code)), {});
      answerMathQuill = _objectSpread(_objectSpread({}, answerMathQuill), {}, _defineProperty({}, code, answerMQ));
    }, 100);
  };

  var judgeElements = [];
  Object.values(judgeCode).forEach(function (val) {
    judgeElements.push(val);
  });
  var allocationFeedback;

  if (allocated == 100) {
    allocationFeedback = /*#__PURE__*/React.createElement("p", {
      id: "alloc"
    }, "\uBC30\uC810\uC774 \uBAA8\uB450 \uD560\uB2F9\uB418\uC5C8\uC2B5\uB2C8\uB2E4!");
  } else if (allocated > 100) {
    allocationFeedback = /*#__PURE__*/React.createElement("p", {
      id: "alloc"
    }, "\uBC30\uC810\uC774 ", allocated - 100, "% \uB9CC\uD07C \uCD08\uACFC\uD558\uC5EC \uD560\uB2F9\uB418\uC5C8\uC2B5\uB2C8\uB2E4.");
  } else if (allocated < 100) {
    allocationFeedback = /*#__PURE__*/React.createElement("p", {
      id: "alloc"
    }, "\uBC30\uC810\uC744 ", 100 - allocated, "% \uB9CC\uD07C \uB354 \uD560\uB2F9\uD574\uC57C \uD569\uB2C8\uB2E4.");
  }

  return /*#__PURE__*/React.createElement("div", {
    className: "vertical-align",
    style: {
      width: "100%"
    }
  }, /*#__PURE__*/React.createElement("input", {
    id: "judge-count",
    hidden: true,
    readOnly: true,
    value: Object.keys(judgeCode).length
  }), judgeElements, /*#__PURE__*/React.createElement("button", {
    className: "button",
    onClick: addJudge
  }, "\uCC44\uC810 \uCD94\uAC00"), allocationFeedback);
};

var root = ReactDOM.createRoot(document.getElementById('judge-settings'));
root.render( /*#__PURE__*/React.createElement(JudgeSettings, null));