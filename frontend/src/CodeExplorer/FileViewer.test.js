import React from 'react';
import {slice} from './treeUtils'


it('Can slice the ast text', () => {

  let g = {
    '0': {"children": ["1", "2", "4"], "data": {"startOffset": 0, "endOffset": 15}},
    '1': {"children": ["3", "5"], "data": {"startOffset": 7, "endOffset": 14}},
    '2': {"children": [], "data": {"startOffset": 1, "endOffset": 6}},
    '3': {"children": [], "data": {"startOffset": 8, "endOffset": 13}},
    '5': {"children": [], "data": {"startOffset": 6, "endOffset": 7}},
    '4': {children: [], data: {startOffset: 0, endOffset: 1}}
  }

  expect(slice("{hello (world)}", g, '0'))
     .toEqual({
       vid: '0',
       children: [{vid: '4', children: ["{"]},
         {vid: '2', children: ["hello"]},
         {vid: '5', children: [" "]},
         {vid: '1', children: ["(", {vid: '3', children: ["world"]}, ")"]},
         "}"]
     })

});


it('Can slice the ast text (Regression)', () => {

  let g = {
    "text":
       "fun functionWith2Lines() {\n    println(\"a\")\n    println(\"b\")\n }\n\n\n\n",
    "ast": {
      "16231": {
        "data": {
          "endOffset": 67,
          "fileName": "functions.kt",
          "functions": 1,
          "textLines": 8,
          "classes": 0,
          "bindings": 0,
          "language": "kotlin",
          "type": "file",
          "startOffset": 0,
          "name": "functions",
          "astType": "file",
          "lang": "kotlin",
          "lines": 3
        }, "parent": "15470", "children": ["16232", "16233", "16234", "16249"]
      },
      "16232": {
        "data": {
          "endOffset": 0,
          "startOffset": 0,
          "name": "<root>",
          "astType": "packageDirective",
          "type": "packageDirective",
          "firstLine": ""
        }, "parent": "16231", "children": []
      },
      "16233": {
        "data": {
          "endOffset": 0,
          "startOffset": 0,
          "astType": "importList",
          "type": "importList",
          "firstLine": ""
        }, "parent": "16231", "children": []
      },
      "16234": {
        "data": {
          "endOffset": 63,
          "startOffset": 0,
          "depth": 0,
          "textLines": 4,
          "name": "functionWith2Lines",
          "astType": "fun",
          "type": "fun",
          "lines": 2,
          "params": 0,
          "firstLine": "fun functionWith2Lines() {"
        }, "parent": "16231", "children": ["16235", "16236"]
      },
      "16235": {
        "data": {
          "endOffset": 24,
          "startOffset": 22,
          "astType": "valueParameterList",
          "type": "params",
          "firstLine": "()"
        }, "parent": "16234", "children": []
      },
      "16236": {
        "data": {"endOffset": 63, "startOffset": 25, "astType": "block", "type": "block", "firstLine": "{"},
        "parent": "16234",
        "children": ["16237", "16243"]
      },
      "16237": {
        "data": {
          "args": 1,
          "endOffset": 43,
          "startOffset": 31,
          "name": "println(\"a\")",
          "astType": "callExpression",
          "type": "call",
          "firstLine": "println(\"a\")"
        }, "parent": "16236", "children": ["16238", "16239"]
      },
      "16238": {
        "data": {
          "endOffset": 38,
          "startOffset": 31,
          "name": "println",
          "astType": "referenceExpression",
          "type": "referenceExpression",
          "firstLine": "println"
        }, "parent": "16237", "children": []
      },
      "16239": {
        "data": {
          "endOffset": 43,
          "startOffset": 38,
          "astType": "valueArgumentList",
          "type": "args",
          "firstLine": "(\"a\")"
        }, "parent": "16237", "children": ["16240"]
      },
      "16240": {
        "data": {
          "endOffset": 42,
          "startOffset": 39,
          "astType": "valueArgument",
          "type": "arg",
          "firstLine": "\"a\""
        }, "parent": "16239", "children": ["16241"]
      },
      "16241": {
        "data": {
          "endOffset": 42,
          "startOffset": 39,
          "name": "\"a\"",
          "astType": "stringTemplate",
          "type": "string",
          "firstLine": "\"a\""
        }, "parent": "16240", "children": ["16242"]
      },
      "16242": {
        "data": {
          "endOffset": 41,
          "startOffset": 40,
          "astType": "literalStringTemplateEntry",
          "type": "literalStringTemplateEntry",
          "firstLine": "a"
        }, "parent": "16241", "children": []
      },
      "16243": {
        "data": {
          "args": 1,
          "endOffset": 60,
          "startOffset": 48,
          "name": "println(\"b\")",
          "astType": "callExpression",
          "type": "call",
          "firstLine": "println(\"b\")"
        }, "parent": "16236", "children": ["16244", "16245"]
      },
      "16244": {
        "data": {
          "endOffset": 55,
          "startOffset": 48,
          "name": "println",
          "astType": "referenceExpression",
          "type": "referenceExpression",
          "firstLine": "println"
        }, "parent": "16243", "children": []
      },
      "16245": {
        "data": {
          "endOffset": 60,
          "startOffset": 55,
          "astType": "valueArgumentList",
          "type": "args",
          "firstLine": "(\"b\")"
        }, "parent": "16243", "children": ["16246"]
      },
      "16246": {
        "data": {
          "endOffset": 59,
          "startOffset": 56,
          "astType": "valueArgument",
          "type": "arg",
          "firstLine": "\"b\""
        }, "parent": "16245", "children": ["16247"]
      },
      "16247": {
        "data": {
          "endOffset": 59,
          "startOffset": 56,
          "name": "\"b\"",
          "astType": "stringTemplate",
          "type": "string",
          "firstLine": "\"b\""
        }, "parent": "16246", "children": ["16248"]
      },
      "16248": {
        "data": {
          "endOffset": 58,
          "startOffset": 57,
          "astType": "literalStringTemplateEntry",
          "type": "literalStringTemplateEntry",
          "firstLine": "b"
        }, "parent": "16247", "children": []
      },
      "16249": {
        "data": {
          "endOffset": 67,
          "startOffset": 63,
          "astType": "whiteSpace",
          "type": "whiteSpace",
          "firstLine": ""
        }, "parent": "16231", "children": []
      },
      "rootVid": "16231"
    }
  }

  expect(JSON.stringify(slice(g.text, g.ast, '16231')))
     .toEqual("{\"vid\":\"16231\",\"children\":[{\"vid\":\"16234\",\"children\":[\"fun functionWith2Lines\",{\"vid\":\"16235\",\"children\":[\"()\"]},\" \",{\"vid\":\"16236\",\"children\":[\"{\\n    \",{\"vid\":\"16237\",\"children\":[{\"vid\":\"16238\",\"children\":[\"println\"]},{\"vid\":\"16239\",\"children\":[\"(\",{\"vid\":\"16240\",\"children\":[{\"vid\":\"16241\",\"children\":[\"\\\"\",{\"vid\":\"16242\",\"children\":[\"a\"]},\"\\\"\"]}]},\")\"]}]},\"\\n    \",{\"vid\":\"16243\",\"children\":[{\"vid\":\"16244\",\"children\":[\"println\"]},{\"vid\":\"16245\",\"children\":[\"(\",{\"vid\":\"16246\",\"children\":[{\"vid\":\"16247\",\"children\":[\"\\\"\",{\"vid\":\"16248\",\"children\":[\"b\"]},\"\\\"\"]}]},\")\"]}]},\"\\n }\"]}]},{\"vid\":\"16249\",\"children\":[\"\\n\\n\\n\\n\"]}]}")

});

