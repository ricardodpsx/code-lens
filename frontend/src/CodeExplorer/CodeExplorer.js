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
import Tabs from "@material-ui/core/Tabs/Tabs";
import Tab from "@material-ui/core/Tab/Tab";
import Typography from "@material-ui/core/Typography/Typography";
import Box from "@material-ui/core/Box/Box";
import History from "../CodeEvolution/History";
import MetricNameSelect from "./MetricNameSelect";


function TabPanel(props) {
  const {children, value, index, ...other} = props;

  return (
     <Typography
        component="div"
        role="tabpanel"
        hidden={value !== index}
        id={`simple-tabpanel-${index}`}
        aria-labelledby={`simple-tab-${index}`}
        {...other}
     >
       <Box p={3}>{value == index ? children : null}</Box>
     </Typography>
  );
}


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

  const [activeTab, setActiveTab] = useState(0);


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
              onFileSelect={file => {
                selectNode(file.vid)
                selectFileNode(file);
              }}
           />
         </Paper>
       </Grid>

       <Grid item xs={6}>
         <Paper>

           <Tabs value={activeTab} onChange={(event, newValue) => setActiveTab(newValue)}>
             <Tab label="Node Info"/>
             <Tab label="Frequency"/>
             <Tab label="Frequency in Time"/>
             <Tab label="Evolution of Param"/>
           </Tabs>
           <TabPanel value={activeTab} index={0}>
             <CodeEntityData
                onNodeSelected={selectNode}
                className="CodeEntityData"
                vid={selectedNode}
                ast={ast}
                text={text}
             />
           </TabPanel>
           <TabPanel value={activeTab} index={1}>
             <MetricNameSelect
                params={metricNames}
                value={selectedMetric}
                onParamChange={selectMetric}/>
             <Metrics
                query={query}
                rows={metricData}
                selectedMetric={selectedMetric}
             />
           </TabPanel>
           <TabPanel value={activeTab} index={2}>
             <MetricNameSelect
                params={metricNames}
                value={selectedMetric}
                onParamChange={selectMetric}/>
             <History
                query={query}
                selectedMetric={selectedMetric}
             />
           </TabPanel>

         </Paper>
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

     </Grid>
  );
}