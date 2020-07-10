import {connect} from "react-redux";

import React, {useEffect, useState} from "react";
import {adj, allVertices, isOfType, vertice} from "../../lib/treeUtils";
import Grid from "@material-ui/core/Grid/Grid";
import {makeStyles} from "@material-ui/core";
import Paper from "@material-ui/core/Paper/Paper";
import DependenciesQuerySearch from "./DependenciesQuerySearch";
import {selectTreeNode, setDependenciesQuery} from "../../appModel";
import {take, truncate} from "lodash"
import {parent} from "../../lib/treeUtils";

let vis = require("vis-network/dist/vis-network")
require("vis-network/dist/vis-network.css")

const useStyles = makeStyles(theme => ({
  control: {
    padding: theme.spacing(2),
  },
}));


function DependencyGraph({query, results, codeTree}) {

  useEffect(()=>{
    if(query === "") setDependenciesQuery("file")
  }, [query])

  const classes = useStyles();

  return <Grid container className={classes.control}>
    <Grid container item spacing={3}>
      <Grid item xs={6}>
        <DependenciesQuerySearch />
      </Grid>
    </Grid>
    <Grid item xs={10}>
      <Paper>
          <GraphContainer results={results} codeTree={codeTree} />
      </Paper>
    </Grid>

  </Grid>

}

const options = {
  // nodes: {
  //   shape: "box"
  // },
  physics: false
  ,
  layout: {
    hierarchical: {
      direction: "UD"
    }
  },
  edges: {
    color: "#000000"
  }
};
var network = null



function GraphContainer({results, codeTree}) {

  if(!codeTree) return null

  let [collapsedVids, setCollapsed] = useState([])

  let str = collapsedVids.map(it => `'${it}'`).join(',')
  useEffect(()=>{
    setDependenciesQuery(`file | collapsing(${str})`)
  }, [collapsedVids])


  let data = {"nodes": [], "edges": []}
  let resultTree = allVertices(codeTree)
  let N = 500
  let maxSize = 0
  take(resultTree, N).forEach( v =>
     maxSize = Math.max(v.data.lines || 0, maxSize)
  )
  console.info(maxSize)
  take(resultTree, N).forEach(v => {
    //if(v.type !== "file") return
    let label = truncate(`[${v.data.type}] ${v.data.name}`, {'length': 24})
      data.nodes.push({id: v.vid, label,
        group: "A",
        level: v.data.level,
        // widthConstraint: { minimum: 120  + 5*120*( (v.data.lines || 0) / maxSize) },
        // heightConstraint: { minimum: 20  + 5*20*( (v.data.lines || 0) / maxSize) },
        color: isOfType(v, 'codeFile') ? "#e04141" : "#41e0c9"}
        )


    adj(codeTree, v.vid, "imports").forEach(to => {
        data.edges.push({from: v.vid, to: to, color: 'red', arrows: "to" })
    })
    adj(codeTree, v.vid, "children").forEach(to => {
      data.edges.push({from: v.vid, to: to, color: 'black'})
    })
  })

  var container = null

  function renderGraph(container) {
    if (container) {
        network = new vis.Network(container, data, options)

        network.on("selectNode", function (event) {
          var node = event.nodes[0];
          // console.log("Selected nodes:");
          // console.log(node);
          if (isOfType(vertice(codeTree, node), "dir")) {
            setCollapsed(collapsedVids.concat(node))
          }
        });

        console.info("Rendering network")

      // console.info(graphDiv.innerHTML)
      // console.info(edges)
      // graphDiv.style.height = 600
      // console.info(graphDiv)
      //  //graphDiv
      //  container.appendChild(graphDiv)
    }
  }

  return <div >
    {resultTree.length > N ?  <div>Showing only {N} results of {resultTree.length}</div> : null}
    <div style={{height: 600}} ref={el => renderGraph(el)}></div>
  </div>
}

let mapStateToProps = ({dependencies: {query, results, codeTree}}) => {
  return {
    query,
    results,
    codeTree
  }
}

export default connect(mapStateToProps)(DependencyGraph)