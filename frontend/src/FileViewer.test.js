import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import {slice} from './treeUtils'


it('Can slice the ast text', () => {

  let g = {
      '0': {"children": ["1", "2"], "data": {"startOffset": 0, "endOffset": 15}},
      '1': {"children": ["3"], "data": {"startOffset": 7, "endOffset": 14}},
      '2': {"children": [], "data": {"startOffset": 1, "endOffset": 6}},
      '3': {"children": [], "data": {"startOffset": 8, "endOffset": 13}}
      }

    expect(slice("{hello (world)}", g))
       .toEqual({vid: '0',
         children: ["{",
           {vid: '2', children: ["hello"]},
            " ",
           {vid: '1', children: ["(", {vid: '3', children: ["world"]}, ")"]},
            "}"]})

});
