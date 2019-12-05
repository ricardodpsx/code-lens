import React from "react";
import BarChart from "../BarChart";

export default function Metrics({selectedMetric = "", query, rows = []}) {
  if (!selectedMetric) return null

  console.info(rows)
// make sure parent container have a defined height when using
// responsive component, otherwise height will be 0 and
// no chart will be rendered.
// website examples showcase many properties,
// you'll often use just a few of them.


  return <BarChart
     xField="paramValue"
     yField="frequency"
     xLabel="Param Value"
     yLabel="Frequency"
     data={rows}
  />
}