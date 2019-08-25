import React, {Component} from "react";
//import SyntaxHighlighter from "react-syntax-highlighter/dist/esm/default-highlight";
import "./FileViewer.css"
import CodeEntity from "./CodeEntity";
import {slice} from "./treeUtils";


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
      {slicedText.children.map(t => (typeof t == 'string') ? t : this.text(t))}
    </CodeEntity>
  }


  render() {
    if(!this.props.ast) return null
    let slicedText = slice(this.props.text, this.props.ast, this.props.ast.rootVid, this.props.results);

    return (<div>
      <pre className="code">
        {this.text(slicedText)}
      </pre>
    </div>)
  }
}

export default FileViewer;