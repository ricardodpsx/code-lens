import React, {useState} from "react";
import Chart from 'react-google-charts';


export default function HistoryMetrics({analytics:{ selectedParam = "", params = []} = {}, history:{ rows = [] } = {}}) {
console.log(Object.keys(rows).map(commitId => rows[commitId].mean))
  return <div>
    <Chart
     width={'500px'}
     height={'300px'}
     chartType="Bar"
     loader={<div>Loading Chart</div>}
     columns={["Commits", "Mean of " + selectedParam]}
     rows={Object.keys(rows).map(commitId => [commitId, rows[commitId].mean])}
     // For tests
     rootProps={{ 'data-testid': '2' }}
  /></div>
}