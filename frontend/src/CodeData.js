import React from "react";
import {vertice, parents} from "./treeUtils"
export default function CodeEntityData({vid, ast, onCodeEntitySelected = () => {}}) {

  if(!ast || !vid ) return null

  let data = vertice(vid, ast)

  return <div className="codeEntityInfo">
    {parents(ast[vid].parent, ast).map(pVid => [pVid, vertice(pVid, ast)]).map(([pVid, v]) =>
       <span onClick={() => onCodeEntitySelected(pVid)}> {v.type} &#9658; </span>)}
    {data.type}
    {Object.entries(data).map(([k, v]) => <div><strong>{k}: </strong>{v}</div>)}
  </div>
}