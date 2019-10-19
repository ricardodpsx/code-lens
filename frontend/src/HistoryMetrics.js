import FileViewer from "./FileViewer";
import React, {useState} from "react";
import Chart from 'react-google-charts';


export default function HistoryMetrics({analytics:{ selectedParam = "", params = []} = {}, history:{ rows = [] } = {}}) {

  return <div>
    <Chart
     width={'500px'}
     height={'300px'}
     chartType="Bar"
     loader={<div>Loading Chart</div>}
     columns={[selectedParam, "Frequency"]}
     rows={rows}
     // For tests
     rootProps={{ 'data-testid': '2' }}
  /></div>
}