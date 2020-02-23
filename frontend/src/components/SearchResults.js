import {CodeEntityInfo} from "./CodeData";
import React from "react";
import {connect} from "react-redux";
import {selectTreeNode} from "../appModel";

function SearchResults({codeTree, results}) {
  if (!results || results.length === 0) return "No results"

  return results.map(r => {
    return <div key={r.vid}>
      <a href=''
         onClick={(e) => {
           selectTreeNode(r.vid)
           e.preventDefault();
         }}>
        {r.data.parentFile || r.data.path}

      </a> <br/>
      <CodeEntityInfo vertice={r}/>
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