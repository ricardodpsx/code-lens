import React from "react";
import BarChart from "../BarChart";

export default function Metrics({
                                  selectedMetric = "", query, rows = [], onDataSelected = () => {
  }
                                }) {
  if (!selectedMetric) return null

  return <BarChart
     xField="paramValue"
     yField="frequency"
     xLabel="Param Value"
     yLabel="Frequency"
     data={rows}
     onDataSelected={onDataSelected}
  />
}