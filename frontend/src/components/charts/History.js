/* eslint-disable react/jsx-no-undef */
import React from 'react';
import {formatDate} from "../../lib/stringUtils";
import BarChart from "./BarChart";
import {connect} from "react-redux";
import MetricNameSelect from "./MetricNameSelect";
import {selectEvolutionParam} from "../../appModel";
import {Title} from "../layout/Title";


function History({selectedParam, history}) {

  return (<div>
    <Title title={`Evolution of Metric "${selectedParam}"`}/>
    <MetricNameSelect selectedMetric={selectedParam} onSelectMetric={selectEvolutionParam}/>
    <BarChart
       xField="commit"
       yField="value"
       data={history.map(it => ({commit: formatDate(it.commit.commitTime), value: it.statistics.mean}))}/>
  </div>);


}

export default connect(({evolution: {selectedParam, history}}) => ({selectedParam, history}))(History);