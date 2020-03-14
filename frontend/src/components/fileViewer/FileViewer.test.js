import React from 'react';
import {slice} from '../../lib/treeUtils'


it('Can slice the ast text', () => {

  let g = {
    edges: {
      "6": [{name: 'children', to: '2'}, {name: 'children', to: '5'}]
    },
    vertices: {
      '0': {vid: '0', "start": 0, "end": 15},
      '6': {vid: '6'}, //Should take start and end from children
      '1': {vid: '1', "start": 7, "end": 14},
      '2': {vid: '2', "start": 1, "end": 6},
      '3': {vid: '3', "start": 8, "end": 13},
      '5': {vid: '5', "start": 6, "end": 7},
      '4': {vid: '4', start: 0, end: 1}
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
