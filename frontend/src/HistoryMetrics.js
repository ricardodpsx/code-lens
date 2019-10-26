import React, {useState} from "react";
import Chart from 'react-google-charts';


export default function HistoryMetrics({analytics:{ selectedParam = "", params = []} = {}, history:{ rows = [] } = {}}) {
console.log(Object.keys(rows).map(commitId => [commitId, rows[commitId].mean, 'color: #76A7FA']))
  return <div>
    <Chart
     width={'500px'}
     height={'300px'}
     chartType="ColumnChart"
     loader={<div>Loading Chart</div>}
     columns={["Commits", "Mean", {role: 'style'}]}
     rows={Object.keys(rows).map(commitId => [commitId, rows[commitId].mean, commitId == 'Avg' ? 'color: #C62828':''])}
     // For tests
     rootProps={{ 'data-testid': '2' }}
  />
  </div>
}