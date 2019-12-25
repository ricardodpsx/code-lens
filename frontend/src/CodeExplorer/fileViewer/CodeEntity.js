import React from "react";


export default function CodeEntity({
                                     vid, data, children, selected, onNodeSelected = () => {
  }
                                   }) {
  return <span
     id={`code-entity-${vid}`}
     onClick={(e) => {
       onNodeSelected(vid)
       e.stopPropagation()
     }}
     style={selected ? {fontWeight: 'bold'} : {}}
     className={`${data.type} code-entity node-${vid} code-entity-${selected ? "active" : "inactive"}`}>
    {children}
    </span>
}