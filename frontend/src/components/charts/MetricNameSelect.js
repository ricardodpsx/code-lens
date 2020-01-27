import React from "react";
import {connect} from "react-redux";

function MetricNameSelect({metricNames = [], selectedMetric, onSelectMetric}) {
  return <select name="metricNames"
                 value={selectedMetric}
                 onChange={e => onSelectMetric(e.target.value)}>
    <option>--Select--</option>
    {metricNames.map(p =>
       <option value={p} key={p}>{p}</option>
    )}
  </select>
}

let mapStateToProps = ({query: {metricNames}}) => ({metricNames})

export default connect(mapStateToProps)(MetricNameSelect)