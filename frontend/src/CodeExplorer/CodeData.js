import React from "react";
import {parents, vertice} from "./treeUtils"

export default function CodeEntityData({
                                         vid, ast, onNodeSelected = () => {
  }
                                       }) {

  if(!ast || !vid ) return null

  let data = vertice(vid, ast)

  if (!data) return null

  let path = parents(ast[vid].parent, ast).map(pVid => [pVid, vertice(pVid, ast)])

  return <div className="codeEntityInfo">
    {path.map(([pVid, v]) =>
       <i onClick={() => onNodeSelected(pVid)}>{v.type} &#9658; </i>)}
    {<i>{data.type}</i>}
    <br/>
    {Object.entries(data)
       .filter(([k, v]) => k != "code")
       .map(([k, v]) => <span key={k}><strong>{k}: </strong>{v} | </span>)}
  </div>
}