/* eslint-disable react/jsx-no-undef */
import React, { Component } from 'react';
import './App.css';
import DirectoryTree from "./DirectoryTree";
import FileViewer from "./FileViewer";
import FitnessFunction from "./FitnessFunction";
import Metrics from "./Metrics"


import { makeStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField'
import CodeEntityData from "./CodeData";

class App extends Component {

  constructor(props) {
    super(props)
    this.state = {selectedNode: '', query: '', results: []}
  }

  handleQuerySearch(query) {
    this.setState({query})

    this.updateGraph(query)
  }

  updateGraph(query) {
    const that = this;
    fetch('http://localhost:8080/?cssQuery=file%20' + encodeURIComponent(query))
       .then(function(response) {
         return response.json();
       })
       .then(function(data) {
         that.setState({codeTree: data.resulTree, results: data.results})
       })
  }

  handleFileSelect(selectedFile) {
    this.setState({selectedNode: selectedFile})

    let that = this;
    fetch(`http://localhost:8080/node/${selectedFile.vid}`)
       .then(function(response) {
         return response.json();
       })
       .then(function(data) {
         that.setState({ast: data.ast, text: data.text})
       })

  }

  handleCodeEntitySelect(vid) {
    this.setState({selectedCodeEntity: vid})
  }

  componentDidMount() {
    this.updateGraph(this.state.query)
  }

  onCodeEntitySelected(vid) {
    this.setState({selectedCodeEntity: vid})
  }

  render() {
    let {selectedCodeEntity} = this.state;

    return (
       <Grid container spacing={3}>

         <Grid item xs={2}>
           <Paper >
             <TextField  value={this.state.query}
                         onChange={e=> this.handleQuerySearch(e.target.value)}
                         label="search"
                         margin="normal"
                         variant="outlined"
             />
             <DirectoryTree
                results={this.state.results}
                graph={this.state.codeTree}

                onFileSelect={this.handleFileSelect.bind(this)}
             />
             <CodeEntityData
                onCodeEntitySelected={this.onCodeEntitySelected.bind(this)}
                className="CodeEntityData" vid={selectedCodeEntity}
                ast={this.state.ast}
                text={this.state.text}
             />

           </Paper>
         </Grid>


         <Grid item xs={6}>
           <Paper >
             <FileViewer
                text={this.state.text}
                ast={this.state.ast}
                results={this.state.results}  
                selectedNode={this.state.selectedNode}
                onCodeEntitySelected={this.onCodeEntitySelected.bind(this)}
             />
           </Paper>
         </Grid>

         <Grid item xs={4}>
           <Metrics codeTree={this.state.codeTree} results={this.state.results}/>
         </Grid>

      </Grid>
    )
  }
}



export default App;
