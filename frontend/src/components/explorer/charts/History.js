/* eslint-disable react/jsx-no-undef */
import React from 'react';
import {formatDate} from "../../../lib/stringUtils";
import BarChart from "./BarChart";
import {connect} from "react-redux";
import MetricNameSelect from "./MetricNameSelect";
import {selectEvolutionParam} from "../../../appModel";
import {Title} from "../../layout/Title";


function History({selectedParam, history}) {
  return (<div>
    <Title title={`Evolution of Metric "${selectedParam}"`}/>
    <MetricNameSelect selectedMetric={selectedParam} onSelectMetric={selectEvolutionParam}/>
    <BarChart
       xField="date"
       yField={["25%", "50%", "75%", "90%"]}
       yLabel={""}
       data={history.map(it => ({
         date: formatDate(it.commit.commitTime),
         "25%": it.statistics.quartiles[0],
         "50%": it.statistics.quartiles[1],
         "75%": it.statistics.quartiles[2],
         "90%": it.statistics.quartiles[3]
       }))}/>
  </div>);
}

export default connect(({evolution: {selectedParam, history}}) => ({selectedParam, history}))(History);