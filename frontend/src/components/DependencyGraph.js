import {connect} from "react-redux";
import Graph from "react-graph-vis";

import React from "react";
import {adj, isOfType, vertice} from "../lib/treeUtils";
import {selectTreeNode, setQuery} from "../appModel";
// make sure parent container have a defined height when using
// responsive component, otherwise height will be 0 and
// no chart will be rendered.
// website examples showcase many properties,
// you'll often use just a few of them.

function DependencyGraph({results, codeTree}) {

  let data = {"nodes": [], "edges": []}

  results.forEach(v => {
    //if(v.type !== "file") return
    data.nodes.push({id: v.vid, label: v.data.name, color: isOfType(v, 'codeFile') ? "#e04141" : "#41e0c9"})
    adj(codeTree, v.vid, "imports").forEach(to => {
      data.edges.push({from: v.vid, to: to, color: 'black'})
    })
    adj(codeTree, v.vid, "children").forEach(to => {
      data.edges.push({from: v.vid, to: to, color: 'black'})
    })
  })
  const options = {
    layout: {
      hierarchical: false
    },
    edges: {
      color: "#000000"
    }
  };

  const events = {
    select: function (event) {
      var {nodes, edges} = event;
      console.log("Selected nodes:");
      console.log(nodes);
      if (nodes.length && isOfType(vertice(codeTree, nodes[0]), "dir")) {
        let vid = vertice(codeTree, nodes[0]).vid
        setQuery(`file | collapsing('${vid}')`)
      }

      if (nodes.length) selectTreeNode(nodes[0])
      console.log("Selected edges:");
      console.log(edges);
    }
  };

  return <div style={{height: 600}}>
    <Graph graph={data} options={options} events={events} style={{height: "640px"}}/>
  </div>;
}

let mapStateToProps = ({query: {results, codeTree}}) => {
  return {
    results,
    codeTree
  }
}

export default connect(mapStateToProps)(DependencyGraph)