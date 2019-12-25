import React from "react";

export default function MetricNameSelect({params = [], onParamChange, value}) {
  return <select name="metricNames"
                 value={value}
                 onChange={e => onParamChange(e.target.value)}>
    <option>--Select--</option>
    {params.map(p => <option value={p} key={p}>{p}</option>)}
  </select>
}