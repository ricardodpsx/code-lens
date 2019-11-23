import React from "react";
import Chart from 'react-google-charts';


export default function Metrics({selectedParam = "", params = [], rows = [], onParamChange}) {

  return <div>
    <select name="metricNames" onChange={e => onParamChange(e.target.value)}>
      <option>--Select--</option>
      {params.map( p => <option value={p} key={p} >{p}</option>)}
    </select>

    {selectedParam &&
    <Chart
       width={'500px'}
       height={'300px'}
       chartType="Bar"
       loader={<div>Loading Chart</div>}
       columns={[selectedParam, "Frequency"]}
       rows={rows}
       // For tests
       rootProps={{'data-testid': '2'}}
    />}
  </div>
}