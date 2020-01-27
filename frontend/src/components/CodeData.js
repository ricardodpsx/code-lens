import React from "react";
import {parents, vertice} from "../lib/treeUtils"
import {connect} from "react-redux";
import {selectNodeInFile} from "../appModel";

export function CodeEntityInfo({vid, codeTree}) {
  if (!codeTree || !vid) return null
  let data = vertice(vid, codeTree)

  return <div className="codeEntityInfo">
    {<i>{data.type}</i>}
    <br/>
    {Object.entries(data)
       .filter(([k, v]) => k != "code")
       .map(([k, v]) => <span key={k}><strong>{k}: </strong>{JSON.stringify(v)} | </span>)}
  </div>
}

function CodeEntityData({selectedNode, ast}) {
  if (!ast || !selectedNode) return null

  let data = vertice(selectedNode, ast)
  if (!data) {
    console.error(`Couldn't load Code node data of ${selectedNode} from:`, ast)
    return null
  }

  let path = parents(ast[selectedNode].parent, ast).map(pVid => [pVid, vertice(pVid, ast)])

  return <div className="codeEntityInfo">
    {path.map(([pVid, v]) =>
       <i key={pVid} onClick={() => selectNodeInFile(pVid)}>{v.type} &#9658; </i>)}
    {<i>{data.type}</i>}
    <br/>
    {Object.entries(data)
       .filter(([k, v]) => k != "code")
       .map(([k, v]) => <span key={k}><strong>{k}: </strong>{JSON.stringify(v)} | </span>)}
  </div>
}

export default connect(
   ({selectedFile: {selectedNode, ast}}) => ({selectedNode, ast}))
(CodeEntityData)