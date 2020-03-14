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
  if (!vertice(graph, v)) return {}
  return vertice(graph, v).data || {}
}

export function root(graph) {
  return graph.vertices[graph.rootDirVid]
}

export function allVertices({vertices = {}}) {
  return Object.keys(vertices).map(k => vertices[k])
}

export function edgesOf(tree, vid) {
  return tree.edges[vid] || []
}

export function adj(tree, vid, name) {
  if (!vid) return []
  return edgesOf(tree, vid).filter(it => it.name === name).map(it => it.to)
}

export function primaryType(v) {
  return v.type.split(" ")[0].trim()
}

export function isOfType(v, type) {
  if (!v || !v.type) return false
  return v.type.indexOf(type) !== -1
}

export function children(g, vid) {
  return adj(g, vid, "children")
}

export function parent(g, v) {
  let res = adj(g, v, "parent")
  return (res && res[0]) || null
}

export function fileAncestor(graph, vid) {
  if (!vertice(graph, vid)) return null
  if (isOfType(vertice(graph, vid), "codeFile")) return vid
  let a = ancestors(graph, vid)
  return a.find(f => isOfType(vertice(graph, f), "codeFile"))
}

export function slice(text, graph) {

  //Index starts
  let c = 0
  let starts = []
  let ends = []


  allVertices(graph).forEach(v => {
    if (v.start === undefined) {
      let c = children(graph, v.vid).map(it => vertice(graph, it))
      c.sort((a, b) => a.start - b.start)
      v.start = c[0].start
      v.end = last(c).end
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


export function slice2(text, graph, v = graph.rootDirVid) {
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