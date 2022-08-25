function setLilac(inputValue) {
    return {
        value: inputValue,
        language: 'lilac',
        theme: "lilac-theme",
        fontFamily: "D2CODING",
        fontSize: 15,
        DefaultEndOfLine: 0,
        lineNumbers: 'on',
        glyphMargin: false,
        vertical: 'auto',
        horizontal: 'auto',
        verticalScrollbarSize: 10,
        horizontalScrollbarSize: 10,
        scrollBeyondLastLine: false,
        automaticLayout: true,
        minimap: {
            enabled: false
        },
        lineHeight: 19
    }
}

const ML = monaco.languages;

ML.register({id:'lilac'});

ML.setMonarchTokensProvider('lilac', {
    defaultToken: 'text',
    tokenizer: {
        root: [
            [/!\s.*$/, 'quote'],
            [/^==\s+(.*?)\s+==$/, 'section'],
            [/[']{4}(.*?)[']{4}/, 'bold-n-italic'],
            [/[']{3}(.*?)[']{3}/, 'italic'],
            [/[']{2}(.*?)[']{2}/, 'bold'],
            [/[_]{2}(.*?)[_]{2}/, 'underline'],
            [/[-]{2}(.*?)[-]{2}/, 'strike'],
            [/[\^]{2}(.*?)[\^]{2}/, 'super'],
            [/[,]{2}(.*?)[,]{2}/, 'sub'],
            [/\#def\s.*?=.*/, 'define'],
            [/^\\\d\s/, 'control'],
            [/\[.*?\(.*?\)\]/, 'function'],
            [/-{3}/, 'control'],
            [/\[lf\]/, 'control'],

            // equations
            [/\$([^\$]|\\.)*$/, 'invalid' ], // non-teminated equation
            [/\$/, { token: 'equation', bracket: '@open', next: '@equation' } ],
        ],

        equation: [
            [/[^\$]+/, 'equation'],
            [/\$/, { token: 'equation', bracket: '@close', next: '@pop' } ]
        ]
    }
});

ML.registerCompletionItemProvider('lilac', {
    provideCompletionItems: () => {
        const suggestions = [
            {
                label: 'separator',
                kind: ML.CompletionItemKind.keywords,
                insertText: '---'
            },
            {
                label: 'define',
                kind: ML.CompletionItemKind.keywords,
                insertText: '#def ${1:attribute}=${2:value}',
                insertTextRules: ML.CompletionItemInsertTextRule.InsertAsSnippet
            },
            {
                label: 'section',
                kind: ML.CompletionItemKind.keywords,
                insertText: '== ${1:title} ==',
                insertTextRules: ML.CompletionItemInsertTextRule.InsertAsSnippet
            },
            {
                label: 'equation',
                kind: ML.CompletionItemKind.keywords,
                insertText: '$${1:latex}$',
                insertTextRules: ML.CompletionItemInsertTextRule.InsertAsSnippet
            },
            {
                label: 'bold',
                kind: ML.CompletionItemKind.keywords,
                insertText: '\'\'${1:text}\'\'',
                insertTextRules: ML.CompletionItemInsertTextRule.InsertAsSnippet
            },
            {
                label: 'italic',
                kind: ML.CompletionItemKind.keywords,
                insertText: '\'\'\'${1:text}\'\'\'',
                insertTextRules: ML.CompletionItemInsertTextRule.InsertAsSnippet
            },
            {
                label: 'bolditalic',
                kind: ML.CompletionItemKind.keywords,
                insertText: '\'\'\'\'${1:text}\'\'\'\'',
                insertTextRules: ML.CompletionItemInsertTextRule.InsertAsSnippet
            },
            {
                label: 'underline',
                kind: ML.CompletionItemKind.keywords,
                insertText: '__${1:text}__',
                insertTextRules: ML.CompletionItemInsertTextRule.InsertAsSnippet
            },
            {
                label: 'strike',
                kind: ML.CompletionItemKind.keywords,
                insertText: '--${1:text}--',
                insertTextRules: ML.CompletionItemInsertTextRule.InsertAsSnippet
            },
            {
                label: 'super',
                kind: ML.CompletionItemKind.keywords,
                insertText: '^^${1:text}^^',
                insertTextRules: ML.CompletionItemInsertTextRule.InsertAsSnippet
            },
            {
                label: 'sub',
                kind: ML.CompletionItemKind.keywords,
                insertText: ',,${1:text},,',
                insertTextRules: ML.CompletionItemInsertTextRule.InsertAsSnippet
            },
            {
                label: 'size',
                kind: ML.CompletionItemKind.keywords,
                insertText: '{{{${1:size} ${2:text}}}}',
                insertTextRules: ML.CompletionItemInsertTextRule.InsertAsSnippet
            },
            {
                label: 'foreground',
                kind: ML.CompletionItemKind.keywords,
                insertText: '{{{#${1:color} ${2:text}}}}',
                insertTextRules: ML.CompletionItemInsertTextRule.InsertAsSnippet
            },
            {
                label: 'background',
                kind: ML.CompletionItemKind.keywords,
                insertText: '[[[#${1:color} ${2:text}]]]',
                insertTextRules: ML.CompletionItemInsertTextRule.InsertAsSnippet
            },
            {
                label: 'url',
                kind: ML.CompletionItemKind.keywords,
                insertText: '[[${1:url}]]',
                insertTextRules: ML.CompletionItemInsertTextRule.InsertAsSnippet
            },
            {
                label: 'hyperlink',
                kind: ML.CompletionItemKind.keywords,
                insertText: '[[${1:url}|${2:text}]]',
                insertTextRules: ML.CompletionItemInsertTextRule.InsertAsSnippet
            },
            {
                label: 'imagelocal',
                kind: ML.CompletionItemKind.keywords,
                insertText: '[{img:${1:resource code}}]',
                insertTextRules: ML.CompletionItemInsertTextRule.InsertAsSnippet
            },
            {
                label: 'image',
                kind: ML.CompletionItemKind.keywords,
                insertText: '[{${1:source}}]',
                insertTextRules: ML.CompletionItemInsertTextRule.InsertAsSnippet
            },
            {
                label: 'quote',
                kind: ML.CompletionItemKind.keywords,
                insertText: '! ${1:quote}',
                insertTextRules: ML.CompletionItemInsertTextRule.InsertAsSnippet
            },
            {
                label: 'line',
                kind: ML.CompletionItemKind.keywords,
                insertText: '[lf]',
            },
            {
                label: 'ytp',
                kind: ML.CompletionItemKind.keywords,
                insertText: '[ytp(${1:video code})]',
                insertTextRules: ML.CompletionItemInsertTextRule.InsertAsSnippet
            },
            {
                label: 'embed',
                kind: ML.CompletionItemKind.keywords,
                insertText: '[embed(${1:url})]',
                insertTextRules: ML.CompletionItemInsertTextRule.InsertAsSnippet
            },
        ];
        return {suggestions: suggestions}
    }
});

monaco.editor.defineTheme('lilac-theme', {
    base: 'vs-light',
    inherit: true,
    rules: [
        {token: 'section', foreground: '#A5D3EB'},
        {token: 'bold-n-italic', fontStyle: 'bold italic'},
        {token: 'italic', fontStyle: 'italic'},
        {token: 'bold', fontStyle: 'bold'},
        {token: 'underline', fontStyle: 'underline'},
        {token: 'quote', foreground: '#DCDCDC', fontStyle: 'italic'},
        {token: 'equation', foreground: '#F76C61'},
        {token: 'define', foreground: '#1C8FFF'},
        {token: 'function', foreground: '#1889FC'},
        {token: 'control', foreground: '#ABABAB', fontStyle: 'italic'},
        {token: 'text', foreground: '#F0F0F0'},
        {token: 'invalid', fontStyle: 'underline'}
    ],
    colors: {
		'editor.foreground': '#F0F0F0'
	}
});