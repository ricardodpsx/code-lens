import {last} from "lodash"

export function ancestors(graph, v) {
  if (!vertice(graph, v) || parent(graph, v) == null) return []
  return [parent(graph, v)].concat(ancestors(graph, parent(graph, v)))
}

export function vertice(graph = {}, v) {
  if (!graph || !v) return null
  return graph.vertices[v]
}

export function vdata(graph = {}, v) {
  if (!graph || !v) return {}
  return graph.vertices[v].data || {}
}

export function root(graph) {
  return graph.vertices[graph.rootVid]
}

export function allVertices({vertices = {}}) {
  return Object.keys(vertices).map(k => vertices[k])
}

export function adj(g, v) {
  return children(g, v).map(vid => vertice(g, vid))
}

export function children(g, v) {
  return vertice(g, v).relations.filter(it => it.name === "children").map(it => it.to)
}

export function parent(g, v) {
  return vertice(g, v).relations.filter(it => it.name === "parent").map(it => it.to)[0]
}

export function fileAncestor(graph, vid) {
  if (!graph || !vid) return null
  if (vertice(graph, vid).type === "file") return vid
  let a = ancestors(graph, vid)
  return a.find(f => vertice(graph, f).type === "file")
}

export function slice(text, graph) {

  //Index starts
  let c = 0
  let starts = []
  let ends = []


  allVertices(graph).forEach(v => {
    if (v.start === undefined) {
      let children = adj(graph, v.vid)
      children.sort((a, b) => a.start - b.start)
      v.start = children[0].start
      v.end = last(children).end
    }

    if (v.start <= v.end)
      starts.push(v)
  })

  starts.sort((a, b) => {
    let x = a.start - b.start
    return x === 0 ? b.end - a.end : x
  })

  return sliceRec(text)

  function sliceRec(text) {
    let stack = [{children: []}]

    while(c < text.length) {

      if (ends.length > 0 && c === last(ends).end) {
        stack.pop()
        ends.pop()
      } else if (starts[0] && c === starts[0].start) {
        let n = starts.shift()
        ends.push(n)
        let e = {vid: n.vid, children: []}
        last(stack).children.push(e)
        stack.push(e)
      } else {
        let children = last(stack).children
        if (typeof last(children) == "string")
          children[children.length - 1] = children[children.length - 1] + text.charAt(c++)
        else
          children.push(text.charAt(c++))
      }
    }

    return stack[0].children[0]
  }
}


export function slice2(text, graph, v = graph.rootVid) {
  console.info("Slicing 2")
  let last = 0
  let res = []
  let {start: parentStartOffset} = graph.vertices[v]


  children(graph, v).sort((a, b) => graph.vertices[a].start - graph[b].start)
     .forEach(c => {
       let {start, end} = vertice(graph, c).data

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
  if (!vertice(ast, parentVid)) return []

  return parents(parent(ast, parentVid), ast).concat([parentVid])
}