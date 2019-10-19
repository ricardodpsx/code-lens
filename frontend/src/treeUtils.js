//Todo: Refactor
import _ from "lodash"

//Todo: WIP
export function slice(text, graph, v = graph.rootVid) {

  //Index starts
  let c = 0
  let starts = {}
  let ends = {}
  Object.keys(graph).forEach(cVid =>{
    if(graph[cVid].data) {

      let s = starts[graph[cVid].data.startOffset] || []
      let e = ends[graph[cVid].data.endOffset] || []

      s.push(cVid)
      e.push(cVid)

      starts[graph[cVid].data.startOffset] = s
      ends[graph[cVid].data.endOffset] = e
    }

  })



//Todo: SORT!!!
  return sliceRec(text)

  function sliceRec(text) {
    let out = ""
    let last = []
    let stack = []
    while(c < text.length) {

      if(ends[c] && ends[c].length > 0) {
        let e = stack.pop()
        if(stack.length > 0)
        _.last(stack).children.push(e)

        ends[c].shift()
      } else
      if(starts[c] && starts[c].length > 0) {
        stack.push({vid: starts[c].shift(), children: []})
      } else {
        let children = _.last(stack).children
        if(typeof _.last(children) == "string")
          children[children.length - 1] = children[children.length - 1] + text.charAt(c++)
          else
          _.last(stack).children.push(text.charAt(c++))
      }
    }

    return stack.pop()
  }
}


export function slice2(text, graph, v = graph.rootVid) {

  let last = 0
  let res = []
  let {startOffset: parentStartOffset} = graph[v].data


  graph[v].children.sort((a, b) => graph[a].data.startOffset - graph[b].data.startOffset)
     .forEach(c => {
       let {startOffset, endOffset} = graph[c].data

       if(startOffset < parentStartOffset ) {
         //Todo: Create more general (less assusmption) algorithm to wrap text
        // console.info("Ignoring node " + graph[c].data.type)
         return;
       }

       let startOffsetRel = startOffset - parentStartOffset
       let endOffsetRel = endOffset - parentStartOffset

       if (text.slice(last, startOffsetRel)) {
         console.info(`(parent) ${graph[v].data.type} Adding text `, (text.slice(last, startOffsetRel)), graph[c].data)
         res = res.concat([text.slice(last, startOffsetRel)])
       }
       //console.info(`Slicing ${c} ${startOffset} ${endOffset} `, text.slice(startOffsetRel, endOffsetRel))

       res.push(slice2(text.slice(startOffsetRel, endOffsetRel), graph, c))

       last = endOffsetRel
     })

  if (text.slice(last).length > 0) {
    console.info("Adding text ", (text.slice(last)), graph[v].data)
    res.push(text.slice(last))
  }

  return {vid: v, children: res}
}

export function parents(parentVid, ast) {
  if (!parentVid) return []
  if (!ast[parentVid]) return []

  return parents(ast[parentVid].parent, ast).concat([parentVid])
}

export function vertice(vid, ast) {
  return ast[vid].data
}