import Grid from "@material-ui/core/Grid/Grid";
import Paper from "@material-ui/core/Paper/Paper";
import TextField from "@material-ui/core/TextField/TextField";
import DirectoryTree from "./DirectoryTree";
import FileViewer from "./fileViewer/FileViewer";
import CodeEntityData from "./CodeData";
import Metrics from "./Metrics";
import React, {useEffect, useState} from "react";
import {loadFile, loadGraph, loadMetrics} from "../CodeLensApi";
import SmellList from "./SmellList";


export function CodeExplorer() {

  let [query, setQuery] = useState("")
  let [graph, setGraph] = useState({codeTree: null, results: [], metricNames: []})
  let {codeTree, results, metricNames} = graph

  let [selectedFile, selectFileNode] = useState(null)
  let [fileNode, setFileNode] = useState({ast: null, text: ""})
  let {text, ast} = fileNode

  let [selectedNode, selectNode] = useState(null)

  let [selectedMetric, selectMetric] = useState(null)
  let [metricData, setMetricData] = useState([])

  useEffect(() => {
    loadGraph(query, setGraph)
  }, [query])

  useEffect(() => {
    if (selectedFile) loadFile(selectedFile, setFileNode)
  }, [selectedFile])

  useEffect(() => {
    if (selectedMetric) loadMetrics(selectedMetric, query, setMetricData)
  }, [selectedMetric, query])

  return (
     <Grid container spacing={3}>
       <Grid item xs={2}>
         <Paper>
           <TextField
              id="search"
              value={query}
              onChange={e => setQuery(e.target.value)}
              label="search"
              margin="normal"
              variant="outlined"
           />

           <SmellList
              onSmellSelection={smell => setQuery(smell.query)}
           />

           <DirectoryTree
              query={query}
              results={results}
              graph={codeTree}
              onFileSelect={selectFileNode}
           />
         </Paper>
       </Grid>

       <Grid item xs={6}>
         <Paper>
           <FileViewer
              text={text}
              ast={ast}
              results={results}
              selectedNode={selectedNode}
              onNodeSelected={selectNode}
           />
         </Paper>
       </Grid>

       <Grid item xs={4}>
         <CodeEntityData
            onNodeSelected={selectNode}
            className="CodeEntityData"
            vid={selectedNode}
            ast={ast}
            text={text}
         />

         <Metrics
            query={query}
            onParamChange={selectMetric}
            params={metricNames}
            rows={metricData}
            selectedMetric={selectedMetric}
         />
       </Grid>
     </Grid>
  );
}