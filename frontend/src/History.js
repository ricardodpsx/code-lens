/* eslint-disable react/jsx-no-undef */
import React, { Component } from 'react';
import HistoryMetrics from "./HistoryMetrics"
import TextField from '@material-ui/core/TextField'


import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';

class History extends Component {

  constructor(props) {
      super(props)
      this.state = {query: '', results: []}
  }

  handleQuerySearch(query) {
      this.setState({query})
      this.updateGraph(query)
  }

  updateGraph(query) {
      const that = this;
      fetch('http://localhost:8080/?query=file%20' + encodeURIComponent(query))
         .then(function(response) {
           return response.json();
         })
         .then(function(data) {
           that.setState({
             codeTree: data.codeTree,
             results: data.results,
             analytics: {params: data.analyticsParams}
           })
      })
    }

  handleHistoryParamSelect(param) {
    let that = this;
    fetch(`http://localhost:8080/history/${param}?query=file%20${encodeURIComponent(this.state.query)}`)
       .then(function(response) {
         return response.json();
       })
       .then(function(data) {
         that.setState( prev => (
            {...prev, analytics:{ ...prev.analytics, selectedParam: param}, history:{...prev.history, rows: data}}
         ))
       })
  }

  componentDidMount() {
    this.updateGraph(this.state.query)
  }

  render() {
        let commits = this.state.history != null ? this.state.history.rows: false;
        return (
            <Grid container spacing={3}>
               <Grid item xs={12}>
                    <h1>History</h1>
                    <TextField  value={this.state.query}
                                               onChange={e=> this.handleQuerySearch(e.target.value)}
                                               label="search"
                                               margin="normal"
                                               variant="outlined"
                                   />
                    <select onChange={e => this.handleHistoryParamSelect(e.target.value)}>
                      <option>--Select--</option>
                      {this.state.analytics != null && this.state.analytics.params.map( p => <option value={p} key={p} >{p}</option>)}
                    </select>
                    {this.state.analytics && this.state.analytics.selectedParam && commits ?
                        <HistoryMetrics
                             analytics={this.state.analytics}
                             history={this.state.history}
                        />
                     : ''}
               </Grid>
            </Grid>
        );
  }

}

export default History;