//Todo: Refactor
export function slice(text, graph, v = graph.rootVid) {
  let last = 0
  let res = []
  let {startOffset: parentStartOffset} = graph[v].data

  graph[v].children.sort((a, b) => graph[a].data.startOffset - graph[b].data.startOffset)
     .forEach(c => {
       let {startOffset, endOffset} = graph[c].data

       let startOffsetRel = startOffset - parentStartOffset
       let endOffsetRel = endOffset - parentStartOffset

       if (text.slice(last, startOffsetRel)) {
         res = res.concat([text.slice(last, startOffsetRel)])
       }
       res.push(slice(text.slice(startOffsetRel, endOffsetRel), graph, c))

       last = endOffsetRel
     })

  if (text.slice(last).length > 0)
    res.push(text.slice(last))

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