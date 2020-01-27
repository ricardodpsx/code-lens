import React from "react";
import "./FileViewer.css"
import CodeEntity from "./CodeEntity";
import {slice} from "../../lib/treeUtils";
import {connect} from "react-redux";
import {selectNodeInFile} from "../../appModel";


function TextParts({text, nodeData}) {
  let parts = text.split(" ")
  return parts.map((s, i) => {
    let space = (i < parts.length - 1) ? " " : ""

    if (nodeData.name && s.trim().toLowerCase() === nodeData.name.toLowerCase()) {
      return <span key={i} className={"name"}>{s + space}</span>
    } else if (s.replace(/\s+/gi, "").length > 0)
      return <span key={i} className={"text-node"}>{s + space}</span>
    else return s + space
  })
}

function CodeText({slicedText, ast, results, onNodeSelected}) {
  let nodeData = ast[slicedText.vid].data
  let nodeSelected = nodeData.type !== "file" && results.indexOf(slicedText.vid) !== -1
  return <CodeEntity vid={slicedText.vid}
                     key={slicedText.vid}
                     selected={nodeSelected}
                     data={nodeData}
                     onNodeSelected={onNodeSelected}>
    {slicedText.children.map((t, i) => (typeof t === 'string') ?
       <TextParts key={i} text={t} nodeData={nodeData}/> :
       <CodeText key={i} slicedText={t} ast={ast} results={results} onNodeSelected={onNodeSelected}/>
    )}
  </CodeEntity>
}


function FileViewer({ast, text, results}) {
  if (!ast) return null
  let slicedText = slice(text, ast, ast.rootVid, results);
  let file = ast[ast.rootVid].data


  return (<div style={{height: 400, overflow: 'auto'}}>
      <pre className={`code file-${file.lang}`}>
        <CodeText
           ast={ast}
           results={results}
           slicedText={slicedText}
           onNodeSelected={selectNodeInFile}/>
      </pre>
    </div>)
}


export default connect(
   ({selectedFile, query: {results}}) =>
      ({...selectedFile, results})
)(FileViewer);