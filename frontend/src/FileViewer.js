import React, {Component} from "react";
//import SyntaxHighlighter from "react-syntax-highlighter/dist/esm/default-highlight";
import "./FileViewer.css"
import CodeEntity from "./CodeEntity";
import {slice} from "./treeUtils";


function textParts(text, nodeData) {
  let parts = text.split(" ")
  return parts.map((s, i) => {
    let space = (i < parts.length - 1) ? " " : ""

    if (nodeData.name && s.trim().toLowerCase() == nodeData.name.toLowerCase()) {
      return <span className={"name"}>{s + space}</span>
    } else if (s.replace(/\s+/gi, "").length > 0)
      return <span className={"text-node"}>{s + space}</span>
    else return s + space
  })
}

class FileViewer extends Component {

  constructor(props) {
    super(props)
  }

  onCodeEntitySelected(vid) {
    this.props.onCodeEntitySelected(vid)
  }

  text(slicedText) {
    let nodeData = this.props.ast[slicedText.vid].data
    let nodeSelected = nodeData.type != "file" && this.props.results.indexOf(slicedText.vid) != -1
    return <CodeEntity vid={slicedText.vid}
                       selected={nodeSelected}
                       data={nodeData}
                       onCodeEntitySelected={this.onCodeEntitySelected.bind(this)}>
      {slicedText.children.map((t, i) => (typeof t == 'string') ? textParts(t, nodeData) : this.text(t))}
    </CodeEntity>
  }

  render() {
    if(!this.props.ast) return null
    let slicedText = slice(this.props.text, this.props.ast, this.props.ast.rootVid, this.props.results);
    let file = this.props.ast[this.props.ast.rootVid].data

    return (<div>
      <pre className={`code file-${file.lang}`}>
        {this.text(slicedText)}
      </pre>
    </div>)
  }
}

export default FileViewer;