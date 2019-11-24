import React from "react";
import Chart from 'react-google-charts';
import History from "../CodeEvolution/History";
import {Title} from "../common";


export default function Metrics({selectedMetric = "", query, params = [], rows = [], onParamChange}) {

  return <div>

    <select name="metricNames" onChange={e => onParamChange(e.target.value)}>
      <option>--Select--</option>
      {params.map( p => <option value={p} key={p} >{p}</option>)}
    </select>

    {selectedMetric &&
    <div>
      <Title title="Frequency Metrics"/>
    <Chart
       width={'500px'}
       height={'300px'}
       chartType="Bar"
       loader={<div>Loading Chart</div>}
       columns={[selectedMetric, "Frequency"]}
       rows={rows}
       // For tests
       rootProps={{'data-testid': '2'}}
    /></div>}


    {selectedMetric &&
    <History
       query={query}
       selectedMetric={selectedMetric}
       rows={rows}
    />}
  </div>
}