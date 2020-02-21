import React from 'react';
import {slice} from '../../lib/treeUtils'

// it('Can slice the ast text', () => {
//
//   let response = {
//     "text": "\n\nvar res = 1  // Function is called, return value will end up in x\nlet x = 2", "ast": {
//       "..-code_examples-js-fixtures-bindings.js": {
//         "data": {
//           "fileName": "bindings.js",
//           "extension": "js",
//           "code": "\n\nvar res = 1  // Function is called, return value will end up in x\nlet x = 2",
//           "functions": 0,
//           "textLines": 4,
//           "classes": 0,
//           "bindings": 2,
//           "start": 0,
//           "percentOfChanges": "0.033 0.033 0.033 0.033 0.033 0.033",
//           "type": "file",
//           "firstLine": "var res = 1  // Function is called, return value will end up in x",
//           "vid": "..-code_examples-js-fixtures-bindings.js",
//           "path": "js/fixtures/bindings.js",
//           "name": "bindings",
//           "commits": 1,
//           "end": 77,
//           "lang": "js",
//           "lines": 2
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-0"}, {
//           "name": "commit",
//           "to": "e3b714c0279370eb6bb683b020fcc2a673973890"
//         }, {"name": "parent", "to": "../code-examples/js/fixtures"}],
//         "type": "file",
//         "vid": "..-code_examples-js-fixtures-bindings.js",
//         "end": 77,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-0": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-0",
//           "type": "body"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-1"}, {
//           "name": "children",
//           "to": "..-code_examples-js-fixtures-bindings.js-2"
//         }, {"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js"}],
//         "type": "body",
//         "vid": "..-code_examples-js-fixtures-bindings.js-0",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-1": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-1",
//           "kind": "var",
//           "start": "2",
//           "index": 0,
//           "end": "13",
//           "type": "VariableDeclaration binding",
//           "firstLine": "var res = 1"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-3"}, {
//           "name": "children",
//           "to": "..-code_examples-js-fixtures-bindings.js-4"
//         }, {"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-5"}, {
//           "name": "parent",
//           "to": "..-code_examples-js-fixtures-bindings.js-0"
//         }],
//         "type": "VariableDeclaration binding",
//         "vid": "..-code_examples-js-fixtures-bindings.js-1",
//         "end": 13,
//         "start": 2
//       },
//       "..-code_examples-js-fixtures-bindings.js-10": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-10",
//           "line": "3",
//           "column": "11",
//           "type": "end"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-3"}],
//         "type": "end",
//         "vid": "..-code_examples-js-fixtures-bindings.js-10",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-11": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-11",
//           "start": "6",
//           "name": "res",
//           "index": 0,
//           "end": "13",
//           "type": "VariableDeclarator",
//           "firstLine": "res = 1"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-17"}, {
//           "name": "children",
//           "to": "..-code_examples-js-fixtures-bindings.js-18"
//         }, {"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-19"}, {
//           "name": "parent",
//           "to": "..-code_examples-js-fixtures-bindings.js-4"
//         }],
//         "type": "VariableDeclarator",
//         "vid": "..-code_examples-js-fixtures-bindings.js-11",
//         "end": 13,
//         "start": 6
//       },
//       "..-code_examples-js-fixtures-bindings.js-12": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-12",
//           "start": "15",
//           "index": 0,
//           "end": "67",
//           "type": "CommentLine comment",
//           "value": " Function is called, return value will end up in x",
//           "firstLine": "// Function is called, return value will end up in x"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-20"}, {
//           "name": "parent",
//           "to": "..-code_examples-js-fixtures-bindings.js-5"
//         }],
//         "type": "CommentLine comment",
//         "vid": "..-code_examples-js-fixtures-bindings.js-12",
//         "end": 67,
//         "start": 15
//       },
//       "..-code_examples-js-fixtures-bindings.js-13": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-13",
//           "line": "4",
//           "column": "0",
//           "type": "start"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-6"}],
//         "type": "start",
//         "vid": "..-code_examples-js-fixtures-bindings.js-13",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-14": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-14",
//           "line": "4",
//           "column": "9",
//           "type": "end"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-6"}],
//         "type": "end",
//         "vid": "..-code_examples-js-fixtures-bindings.js-14",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-15": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-15",
//           "start": "72",
//           "name": "x",
//           "index": 0,
//           "end": "77",
//           "type": "VariableDeclarator",
//           "firstLine": "x = 2"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-21"}, {
//           "name": "children",
//           "to": "..-code_examples-js-fixtures-bindings.js-22"
//         }, {"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-23"}, {
//           "name": "parent",
//           "to": "..-code_examples-js-fixtures-bindings.js-7"
//         }],
//         "type": "VariableDeclarator",
//         "vid": "..-code_examples-js-fixtures-bindings.js-15",
//         "end": 77,
//         "start": 72
//       },
//       "..-code_examples-js-fixtures-bindings.js-16": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-16",
//           "start": "15",
//           "index": 0,
//           "end": "67",
//           "type": "CommentLine comment",
//           "value": " Function is called, return value will end up in x",
//           "firstLine": "// Function is called, return value will end up in x"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-24"}, {
//           "name": "parent",
//           "to": "..-code_examples-js-fixtures-bindings.js-8"
//         }],
//         "type": "CommentLine comment",
//         "vid": "..-code_examples-js-fixtures-bindings.js-16",
//         "end": 67,
//         "start": 15
//       },
//       "..-code_examples-js-fixtures-bindings.js-17": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-17",
//           "type": "loc"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-25"}, {
//           "name": "children",
//           "to": "..-code_examples-js-fixtures-bindings.js-26"
//         }, {"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-11"}],
//         "type": "loc",
//         "vid": "..-code_examples-js-fixtures-bindings.js-17",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-18": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-18",
//           "start": "6",
//           "name": "res",
//           "end": "9",
//           "type": "Identifier id",
//           "firstLine": "res"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-27"}, {
//           "name": "parent",
//           "to": "..-code_examples-js-fixtures-bindings.js-11"
//         }],
//         "type": "Identifier id",
//         "vid": "..-code_examples-js-fixtures-bindings.js-18",
//         "end": 9,
//         "start": 6
//       },
//       "..-code_examples-js-fixtures-bindings.js-19": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-19",
//           "start": "12",
//           "end": "13",
//           "type": "NumericLiteral init number",
//           "value": "1",
//           "firstLine": "1"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-28"}, {
//           "name": "children",
//           "to": "..-code_examples-js-fixtures-bindings.js-29"
//         }, {"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-11"}],
//         "type": "NumericLiteral init number",
//         "vid": "..-code_examples-js-fixtures-bindings.js-19",
//         "end": 13,
//         "start": 12
//       },
//       "..-code_examples-js-fixtures-bindings.js-2": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-2",
//           "kind": "let",
//           "start": "68",
//           "index": 1,
//           "end": "77",
//           "type": "VariableDeclaration binding",
//           "firstLine": "let x = 2"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-6"}, {
//           "name": "children",
//           "to": "..-code_examples-js-fixtures-bindings.js-7"
//         }, {"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-8"}, {
//           "name": "parent",
//           "to": "..-code_examples-js-fixtures-bindings.js-0"
//         }],
//         "type": "VariableDeclaration binding",
//         "vid": "..-code_examples-js-fixtures-bindings.js-2",
//         "end": 77,
//         "start": 68
//       },
//       "..-code_examples-js-fixtures-bindings.js-20": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-20",
//           "type": "loc"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-30"}, {
//           "name": "children",
//           "to": "..-code_examples-js-fixtures-bindings.js-31"
//         }, {"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-12"}],
//         "type": "loc",
//         "vid": "..-code_examples-js-fixtures-bindings.js-20",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-21": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-21",
//           "type": "loc"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-32"}, {
//           "name": "children",
//           "to": "..-code_examples-js-fixtures-bindings.js-33"
//         }, {"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-15"}],
//         "type": "loc",
//         "vid": "..-code_examples-js-fixtures-bindings.js-21",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-22": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-22",
//           "start": "72",
//           "name": "x",
//           "end": "73",
//           "type": "Identifier id",
//           "firstLine": "x"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-34"}, {
//           "name": "parent",
//           "to": "..-code_examples-js-fixtures-bindings.js-15"
//         }],
//         "type": "Identifier id",
//         "vid": "..-code_examples-js-fixtures-bindings.js-22",
//         "end": 73,
//         "start": 72
//       },
//       "..-code_examples-js-fixtures-bindings.js-23": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-23",
//           "start": "76",
//           "end": "77",
//           "type": "NumericLiteral init number",
//           "value": "2",
//           "firstLine": "2"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-35"}, {
//           "name": "children",
//           "to": "..-code_examples-js-fixtures-bindings.js-36"
//         }, {"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-15"}],
//         "type": "NumericLiteral init number",
//         "vid": "..-code_examples-js-fixtures-bindings.js-23",
//         "end": 77,
//         "start": 76
//       },
//       "..-code_examples-js-fixtures-bindings.js-24": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-24",
//           "type": "loc"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-37"}, {
//           "name": "children",
//           "to": "..-code_examples-js-fixtures-bindings.js-38"
//         }, {"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-16"}],
//         "type": "loc",
//         "vid": "..-code_examples-js-fixtures-bindings.js-24",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-25": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-25",
//           "line": "3",
//           "column": "4",
//           "type": "start"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-17"}],
//         "type": "start",
//         "vid": "..-code_examples-js-fixtures-bindings.js-25",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-26": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-26",
//           "line": "3",
//           "column": "11",
//           "type": "end"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-17"}],
//         "type": "end",
//         "vid": "..-code_examples-js-fixtures-bindings.js-26",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-27": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-27",
//           "identifierName": "res",
//           "name": "res",
//           "type": "loc"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-39"}, {
//           "name": "children",
//           "to": "..-code_examples-js-fixtures-bindings.js-40"
//         }, {"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-18"}],
//         "type": "loc",
//         "vid": "..-code_examples-js-fixtures-bindings.js-27",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-28": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-28",
//           "type": "loc"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-41"}, {
//           "name": "children",
//           "to": "..-code_examples-js-fixtures-bindings.js-42"
//         }, {"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-19"}],
//         "type": "loc",
//         "vid": "..-code_examples-js-fixtures-bindings.js-28",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-29": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-29",
//           "rawValue": "1",
//           "raw": "1",
//           "type": "extra"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-19"}],
//         "type": "extra",
//         "vid": "..-code_examples-js-fixtures-bindings.js-29",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-3": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-3",
//           "type": "loc"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-10"}, {
//           "name": "children",
//           "to": "..-code_examples-js-fixtures-bindings.js-9"
//         }, {"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-1"}],
//         "type": "loc",
//         "vid": "..-code_examples-js-fixtures-bindings.js-3",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-30": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-30",
//           "line": "3",
//           "column": "13",
//           "type": "start"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-20"}],
//         "type": "start",
//         "vid": "..-code_examples-js-fixtures-bindings.js-30",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-31": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-31",
//           "line": "3",
//           "column": "65",
//           "type": "end"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-20"}],
//         "type": "end",
//         "vid": "..-code_examples-js-fixtures-bindings.js-31",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-32": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-32",
//           "line": "4",
//           "column": "4",
//           "type": "start"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-21"}],
//         "type": "start",
//         "vid": "..-code_examples-js-fixtures-bindings.js-32",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-33": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-33",
//           "line": "4",
//           "column": "9",
//           "type": "end"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-21"}],
//         "type": "end",
//         "vid": "..-code_examples-js-fixtures-bindings.js-33",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-34": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-34",
//           "identifierName": "x",
//           "name": "x",
//           "type": "loc"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-43"}, {
//           "name": "children",
//           "to": "..-code_examples-js-fixtures-bindings.js-44"
//         }, {"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-22"}],
//         "type": "loc",
//         "vid": "..-code_examples-js-fixtures-bindings.js-34",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-35": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-35",
//           "type": "loc"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-45"}, {
//           "name": "children",
//           "to": "..-code_examples-js-fixtures-bindings.js-46"
//         }, {"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-23"}],
//         "type": "loc",
//         "vid": "..-code_examples-js-fixtures-bindings.js-35",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-36": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-36",
//           "rawValue": "2",
//           "raw": "2",
//           "type": "extra"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-23"}],
//         "type": "extra",
//         "vid": "..-code_examples-js-fixtures-bindings.js-36",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-37": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-37",
//           "line": "3",
//           "column": "13",
//           "type": "start"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-24"}],
//         "type": "start",
//         "vid": "..-code_examples-js-fixtures-bindings.js-37",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-38": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-38",
//           "line": "3",
//           "column": "65",
//           "type": "end"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-24"}],
//         "type": "end",
//         "vid": "..-code_examples-js-fixtures-bindings.js-38",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-39": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-39",
//           "line": "3",
//           "column": "4",
//           "type": "start"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-27"}],
//         "type": "start",
//         "vid": "..-code_examples-js-fixtures-bindings.js-39",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-4": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-4",
//           "type": "declarations"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-11"}, {
//           "name": "parent",
//           "to": "..-code_examples-js-fixtures-bindings.js-1"
//         }],
//         "type": "declarations",
//         "vid": "..-code_examples-js-fixtures-bindings.js-4",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-40": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-40",
//           "line": "3",
//           "column": "7",
//           "type": "end"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-27"}],
//         "type": "end",
//         "vid": "..-code_examples-js-fixtures-bindings.js-40",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-41": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-41",
//           "line": "3",
//           "column": "10",
//           "type": "start"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-28"}],
//         "type": "start",
//         "vid": "..-code_examples-js-fixtures-bindings.js-41",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-42": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-42",
//           "line": "3",
//           "column": "11",
//           "type": "end"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-28"}],
//         "type": "end",
//         "vid": "..-code_examples-js-fixtures-bindings.js-42",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-43": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-43",
//           "line": "4",
//           "column": "4",
//           "type": "start"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-34"}],
//         "type": "start",
//         "vid": "..-code_examples-js-fixtures-bindings.js-43",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-44": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-44",
//           "line": "4",
//           "column": "5",
//           "type": "end"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-34"}],
//         "type": "end",
//         "vid": "..-code_examples-js-fixtures-bindings.js-44",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-45": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-45",
//           "line": "4",
//           "column": "8",
//           "type": "start"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-35"}],
//         "type": "start",
//         "vid": "..-code_examples-js-fixtures-bindings.js-45",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-46": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-46",
//           "line": "4",
//           "column": "9",
//           "type": "end"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-35"}],
//         "type": "end",
//         "vid": "..-code_examples-js-fixtures-bindings.js-46",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-5": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-5",
//           "type": "trailingComments"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-12"}, {
//           "name": "parent",
//           "to": "..-code_examples-js-fixtures-bindings.js-1"
//         }],
//         "type": "trailingComments",
//         "vid": "..-code_examples-js-fixtures-bindings.js-5",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-6": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-6",
//           "type": "loc"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-13"}, {
//           "name": "children",
//           "to": "..-code_examples-js-fixtures-bindings.js-14"
//         }, {"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-2"}],
//         "type": "loc",
//         "vid": "..-code_examples-js-fixtures-bindings.js-6",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-7": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-7",
//           "type": "declarations"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-15"}, {
//           "name": "parent",
//           "to": "..-code_examples-js-fixtures-bindings.js-2"
//         }],
//         "type": "declarations",
//         "vid": "..-code_examples-js-fixtures-bindings.js-7",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-8": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-8",
//           "type": "leadingComments"
//         },
//         "relations": [{"name": "children", "to": "..-code_examples-js-fixtures-bindings.js-16"}, {
//           "name": "parent",
//           "to": "..-code_examples-js-fixtures-bindings.js-2"
//         }],
//         "type": "leadingComments",
//         "vid": "..-code_examples-js-fixtures-bindings.js-8",
//         "end": 0,
//         "start": 0
//       },
//       "..-code_examples-js-fixtures-bindings.js-9": {
//         "data": {
//           "vid": "..-code_examples-js-fixtures-bindings.js-9",
//           "line": "3",
//           "column": "0",
//           "type": "start"
//         },
//         "relations": [{"name": "parent", "to": "..-code_examples-js-fixtures-bindings.js-3"}],
//         "type": "start",
//         "vid": "..-code_examples-js-fixtures-bindings.js-9",
//         "end": 0,
//         "start": 0
//       },
//       "rootVid": "..-code_examples-js-fixtures-bindings.js"
//     }
//   }
//
//   //response.ast['..-code_examples-js-fixtures-bindings.js-0'].end = 77
//   expect(slice2(response.text, response.ast))
//      .toEqual({
//        vid: '0',
//        children: [{vid: '4', children: ["{"]},
//          {vid: '2', children: ["hello"]},
//          {vid: '5', children: [" "]},
//          {vid: '1', children: ["(", {vid: '3', children: ["world"]}, ")"]},
//          "}"]
//      })
//
// });

it('Can slice the ast text', () => {

  let g = {
    vertices: {
      '0': {vid: '0', "start": 0, "end": 15, relations: []},
      '6': {vid: '6', relations: [{name: 'children', to: '2'}, {name: 'children', to: '5'}]}, //Should take start and end from children
      '1': {vid: '1', "start": 7, "end": 14, relations: []},
      '2': {vid: '2', "start": 1, "end": 6, relations: []},
      '3': {vid: '3', "start": 8, "end": 13, relations: []},
      '5': {vid: '5', "start": 6, "end": 7, relations: []},
      '4': {vid: '4', start: 0, end: 1, relations: []}
    }
  }

  expect(slice("{hello (world)}", g, '0'))
     .toEqual({
       vid: '0',
       children: [{vid: '4', children: ["{"]},
         {
           vid: '6', children: [
             {vid: '2', children: ["hello"]},
             {vid: '5', children: [" "]}
           ]
         },
         {vid: '1', children: ["(", {vid: '3', children: ["world"]}, ")"]},
         "}"]
     })

});
