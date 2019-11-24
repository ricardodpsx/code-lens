import React, {useState} from "react";
import Chart from 'react-google-charts';
import Checkbox from '@material-ui/core/Checkbox';
import FormControlLabel from '@material-ui/core/FormControlLabel';


export default function HistoryMetrics({history = {}}) {

  const [state, setState] = useState({
    mean: {status: true, title: "Mean"},
    median: {status: false, title: "Median"},
    std: {status: false, title: "Std. Deviation"},
    min: {status: false, title: "Min"},
    max: {status: false, title: "Max"},
  });

  const handleChange = name => event => {
    setState({ ...state, [name]: {...state[name], status: event.target.checked} });
  };

  const activeMetricsTitles = () => {
    let titles = [];
    Object.values(state).map( statData => {
        if(statData.status)titles.push(statData.title);
    })
    return titles;
  }

  const activeMetricsRows = (commitId) => {
    let row = [];
    Object.entries(state).forEach(([statKey, statData]) => {
      if (statData.status) row.push(history[commitId][statKey]);
    });
    return row;
  }

  return <div>
  {Object.entries(state).map( ([statKey, statData]) =>
    <FormControlLabel
            key={statKey}
            control={
              <Checkbox
                checked={state[statKey].status}
                onChange={handleChange(statKey)}
                value={statKey}
                color="primary"
              />
            }
            label={statData.title}
        />
  )}
    <Chart
       width={'800px'}
       height={'300px'}
       chartType="ColumnChart"
       loader={<div>Loading Chart</div>}
       columns={["Commits", activeMetricsTitles(), {role: 'style'}].flat()}
       rows={Object.keys(history).map(commitId => [commitId, activeMetricsRows(commitId), commitId === 'Avg' ? 'color: #C62828' : ''].flat())}
       rootProps={{ 'data-testid': '2' }}
  />
  </div>
}