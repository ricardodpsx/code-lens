import {fileAncestor} from "../lib/treeUtils";
import {CodeEntityInfo} from "./CodeData";
import React from "react";
import {connect} from "react-redux";
import {selectTreeNode} from "../appModel";

function SearchResults({codeTree, results}) {
  if (!results || results.length == 0) return "No results"

  return results.map(r => {

    if (!codeTree[r]) return null;

    let f = fileAncestor(codeTree, r)

    return <div key={r}>
      <a href=''
         onClick={(e) => {
           selectTreeNode(r)
           e.preventDefault();
         }}>
        {codeTree[f] && codeTree[f].data.path}

      </a> <br/>
      <CodeEntityInfo vid={r} codeTree={codeTree}/>
      <hr/>
    </div>
  })
}

let mapStateToProps = ({query: {results, codeTree}}) => {
  return {
    results,
    codeTree
  }
}

export default connect(mapStateToProps)(SearchResults)