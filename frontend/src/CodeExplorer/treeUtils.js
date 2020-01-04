//Todo: Refactor
import _ from "lodash"

//Todo: WIP

export function ancestors(graph, v) {
  if (!graph[v] || graph[v].parent == null) return []
  return [graph[v].parent].concat(ancestors(graph, graph[v].parent))
}

export function fileAncestor(graph, v) {
  if (graph[v].data.type == "file") return v
  let a = ancestors(graph, v)
  return a.find(f => graph[f].data.type === "file")
}

export function slice(text, graph, v = graph.rootVid) {

  //Index starts
  let c = 0
  let starts = []
  let ends = []
  let items = []
  Object.keys(graph).forEach(cVid =>{
    if(graph[cVid].data) {
      graph[cVid].data.vid = cVid
      if (graph[cVid].data.start < graph[cVid].data.end) {
        starts.push(graph[cVid].data)
        //ends.push(graph[cVid].data)
      }
    }
  })

  starts.sort((a, b) => {
    let x = a.start - b.start
    return x == 0 ? b.end - a.end : x
  })
  //ends.sort((a, b) => a.endOffset - b.endOffset)


//Work in progress
  return sliceRec(text)

  function sliceRec(text) {
    let out = ""
    let stack = [{children: []}]

    while(c < text.length) {

      if (ends.length > 0 && c == _.last(ends).end) {
        stack.pop()
        ends.pop()
      } else if (starts[0] && c == starts[0].start) {
        let n = starts.shift()
        ends.push(n)
        let e = {vid: n.vid, children: []}
        _.last(stack).children.push(e)
        stack.push(e)
      } else {
        let children = _.last(stack).children
        if (typeof _.last(children) == "string")
          children[children.length - 1] = children[children.length - 1] + text.charAt(c++)
        else
          children.push(text.charAt(c++))
      }
    }

    return stack[0].children[0]
  }
}


export function slice2(text, graph, v = graph.rootVid) {

  let last = 0
  let res = []
  let {start: parentStartOffset} = graph[v].data


  graph[v].children.sort((a, b) => graph[a].data.start - graph[b].data.start)
     .forEach(c => {
       let {start, end} = graph[c].data

       if (start < parentStartOffset) {
         return;
       }

       let startOffsetRel = start - parentStartOffset
       let endOffsetRel = end - parentStartOffset

       if (text.slice(last, startOffsetRel)) {
         //console.info(`(parent) ${graph[v].data.type} Adding text `, (text.slice(last, startOffsetRel)), graph[c].data)
         res = res.concat([text.slice(last, startOffsetRel)])
       }

       res.push(slice2(text.slice(startOffsetRel, endOffsetRel), graph, c))

       last = endOffsetRel
     })

  if (text.slice(last).length > 0) {
    //console.info("Adding text ", (text.slice(last)), graph[v].data)
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
  return ast[vid] && ast[vid].data
}