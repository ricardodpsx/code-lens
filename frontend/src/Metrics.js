import FileViewer from "./FileViewer";
import React, {useState} from "react";
import Chart from 'react-google-charts';


export default function Metrics({codeTree, results}) {
  const [param, setParam] = useState('lines');

  let data = {}
  let params = new Set()
  results.forEach(vid => {
    let node = codeTree[vid]

    Object.keys(node.data).forEach(p => params.add(p))

    if(node.data.hasOwnProperty(param)) {
      if(!data[node.data[param]])
        data[node.data[param]] = 0

      data[node.data[param]]++
    }

  })

  let rows = Object.entries(data).map(
     ([k, v]) => [k, v]
  )

  return <div>
    <select onChange={e => setParam(e.target.value)}>
      {Array.from(params).map( p => <option value={p} key={p} >{p}</option>)}
    </select>

    <Chart
     width={'500px'}
     height={'300px'}
     chartType="Bar"
     loader={<div>Loading Chart</div>}
     columns={['Size', 'Functions']}
     rows={rows}
     options={{
       // Material design options
       chart: {
         title: 'Function size',
       },
     }}
     // For tests
     rootProps={{ 'data-testid': '2' }}
  /></div>
}