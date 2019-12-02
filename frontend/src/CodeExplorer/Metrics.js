import React from "react";
import Chart from 'react-google-charts';


export default function Metrics({selectedMetric = "", query, rows = []}) {
  if (!selectedMetric) return null

  return <div>
    <Chart
       width={'500px'}
       height={'300px'}
       chartType="Bar"
       loader={<div>Loading Chart</div>}
       columns={[selectedMetric, "Frequency"]}
       rows={rows}
       // For tests
       rootProps={{'data-testid': '2'}}
    /></div>

}