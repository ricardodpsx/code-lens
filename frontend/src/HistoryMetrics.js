import React, {useState} from "react";
import Chart from 'react-google-charts';
import Checkbox from '@material-ui/core/Checkbox';
import FormControlLabel from '@material-ui/core/FormControlLabel';


export default function HistoryMetrics({analytics:{ selectedParam = "", params = []} = {}, history:{ rows = [] } = {}}) {

  const [state, setState] = useState({
    mean: true,
    median: false,
    std: false,
    min: false,
    max: false,
  });

  const handleChange = name => event => {
    setState({ ...state, [name]: event.target.checked });
  };

  const activeMetricsTitles = () => {
    let titles = []
    if(state.mean)titles.push("Mean");
    if(state.median)titles.push("Median");
    if(state.std)titles.push("Std. Deviation");
    if(state.min)titles.push("Min");
    if(state.max)titles.push("Max");
    return titles;
  }

  const activeMetricsRows = (commitId) => {
    let row = []
    if(state.mean)row.push(rows[commitId].mean);
    if(state.median)row.push(rows[commitId].median);
    if(state.std)row.push(rows[commitId].std);
    if(state.min)row.push(rows[commitId].min);
    if(state.max)row.push(rows[commitId].max);
    return row;
  }

  return <div>
    <FormControlLabel
        control={
          <Checkbox
            checked={state.mean}
            onChange={handleChange('mean')}
            value="mean"
            color="primary"
          />
        }
        label="Mean"
    />
    <FormControlLabel
        control={
          <Checkbox
            checked={state.median}
            onChange={handleChange('median')}
            value="median"
            color="primary"
          />
        }
        label="Median"
    />
    <FormControlLabel
        control={
          <Checkbox
            checked={state.std}
            onChange={handleChange('std')}
            value="std"
            color="primary"
          />
        }
        label="Std. Deviation"
    />
    <FormControlLabel
        control={
          <Checkbox
            checked={state.min}
            onChange={handleChange('min')}
            value="min"
            color="primary"
          />
        }
        label="Min"
    />
    <FormControlLabel
        control={
          <Checkbox
            checked={state.max}
            onChange={handleChange('max')}
            value="max"
            color="primary"
          />
        }
        label="Max"
    />
    <Chart
     width={'800px'}
     height={'300px'}
     chartType="ColumnChart"
     loader={<div>Loading Chart</div>}
     columns={["Commits", activeMetricsTitles(), {role: 'style'}].flat()}
     rows={Object.keys(rows).map(commitId => [commitId, activeMetricsRows(commitId), commitId == 'Avg' ? 'color: #C62828':''].flat())}
     // For tests
     rootProps={{ 'data-testid': '2' }}
  />
  </div>
}