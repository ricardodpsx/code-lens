import React from "react";
import BarChart from "./BarChart";
import {connect} from "react-redux";
import {filterResults, selectMetric} from "../../appModel";
import MetricNameSelect from "./MetricNameSelect";

function Metrics({data, selectedMetric}) {
  return <div>
    <MetricNameSelect selectedMetric={selectedMetric} onSelectMetric={selectMetric}/>
    <BarChart

       xField="paramValue"
       yField={["frequency"]}
       xLabel="Param Value"
       yLabel="Frequency"
       data={data}
       onDataSelected={data => {
         filterResults(data.nodes)
       }}
    /></div>
}

export default connect(({metrics: {data, selectedMetric}}) => ({data, selectedMetric}))(Metrics)