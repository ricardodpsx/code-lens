import React from "react";
import {parents, vertice} from "./treeUtils"

export default function CodeEntityData({
                                         vid, codeTree, onNodeSelected = () => {
  }
                                       }) {


  if (!codeTree || !vid) return null

  let data = vertice(vid, codeTree)
  console.info(data, vid, codeTree)
  if (!data) return <div>??</div>

  let path = parents(codeTree[vid].parent, codeTree).map(pVid => [pVid, vertice(pVid, codeTree)])

  return <div className="codeEntityInfo">
    {path.map(([pVid, v]) =>
       <i onClick={() => onNodeSelected(pVid)}>{v.type} &#9658; </i>)}
    {<i>{data.type}</i>}
    <br/>
    {Object.entries(data)
       .filter(([k, v]) => k != "code")
       .map(([k, v]) => <span key={k}><strong>{k}: </strong>{JSON.stringify(v)} | </span>)}
  </div>
}