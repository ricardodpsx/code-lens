import React, {Component} from "react";
import SyntaxHighlighter from "react-syntax-highlighter/dist/esm/default-highlight";
import {docco} from "react-syntax-highlighter/dist/styles/hljs";


class FileViewer extends Component {

  constructor(props) {
    super(props)
    this.state = {code: ''}
  }

  componentDidUpdate(prevProps) {
    if(this.props.selectedNode === prevProps.selectedNode) return;

    let that = this;
    fetch(`http://localhost:8080/node/${this.props.selectedNode.vid}`)
      .then(function(response) {
        return response.text();
      })
      .then(function(code) {
        that.setState({code: code})
      })
  }

  render() {
    return (<SyntaxHighlighter language='javascript' style={docco}>
      {this.state.code}
    </SyntaxHighlighter>)
  }
}

export default FileViewer;