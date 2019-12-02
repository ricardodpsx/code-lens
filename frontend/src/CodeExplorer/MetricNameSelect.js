import React from "react";

export default function MetricNameSelect({params = [], onParamChange, value}) {
  return <select name="metricNames" onChange={e => onParamChange(e.target.value)}>
    <option>--Select--</option>
    {params.map(p => <option
       selected={p == value}
       value={p} key={p}>{p}</option>)}
  </select>
}