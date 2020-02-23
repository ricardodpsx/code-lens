import React from "react";
import {parent, parents, vdata, vertice} from "../lib/treeUtils"
import {connect} from "react-redux";
import {selectNodeInFile} from "../appModel";
import {groupBy, take} from "lodash"

export function CodeEntityInfo({vertice}) {
  let data = vertice.data || {}

  return <div className="codeEntityInfo">
    {<i>{data.type}</i>}
    <br/>
    {Object.entries(data)
       .filter(([k, v]) => k != "code")
       .map(([k, v]) => <span key={k}><strong>{k}: </strong>{JSON.stringify(v)} | </span>)}

    {
      Object.entries(groupBy(Object.values(vertice.relations), 'name'))

         .map(([k, v]) =>
            <div><strong>{k}</strong>
              <ul>{take(v, 10).map(t => <li>{t.to}</li>)}</ul>
            </div>
         )}

  </div>
}

function CodeEntityData({selectedNode, ast}) {
  if (!ast) return null

  let data = vdata(ast, selectedNode)
  if (!data) {
    console.error(`Couldn't load Code node data of ${selectedNode} from:`, ast)
    return null
  }

  let path = parents(parent(ast, selectedNode), ast).map(pVid => [pVid, vertice(ast, pVid)])

  return <div className="codeEntityInfo">
    {path.map(([pVid, v]) =>
       <i key={pVid} onClick={() => selectNodeInFile(pVid)}>{v.type} &#9658; </i>)}
    {<i>{data.type}</i>}
    <br/>
    {Object.entries(data)
       .filter(([k, v]) => k !== "code")
       .map(([k, v]) => <span key={k}><strong>{k}: </strong>{JSON.stringify(v)} | </span>)}
  </div>
}

export default connect(
   ({selectedFile: {selectedNode, ast}}) => ({selectedNode: selectedNode, ast}))
(CodeEntityData)