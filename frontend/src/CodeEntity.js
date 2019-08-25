import React, {useState} from "react";

export default function CodeEntity({
                             vid, data, children, selected, onCodeEntitySelected = () => {
  }
                           }) {
  const [info, seeInfo] = useState(false);

  return <span
     onClick={(e) => {
       onCodeEntitySelected(vid, data)
       e.stopPropagation()
     }}
     style={selected ? {fontWeight: 'bold'} : {}}
     className={`${data.type} code-entity node-${vid}`}>
    {children}
    </span>
}