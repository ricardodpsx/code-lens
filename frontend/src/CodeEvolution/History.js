/* eslint-disable react/jsx-no-undef */
import React, {useEffect, useState} from 'react';
import {formatDate, Title} from "../common";
import {loadHistory} from "../CodeLensApi";
import BarChart from "../BarChart";


function History({selectedMetric, query}) {
  let [history, setHistory] = useState([])

  useEffect(() => {
    loadHistory(selectedMetric, query, setHistory)
  }, [selectedMetric, query])


  return (<div>
    <Title title={`Evolution of Metric "${selectedMetric}"`}/>
    <BarChart
       xField="commit"
       yField="value"
       data={history.map(it => ({commit: formatDate(it.commit.commitTime), value: it.statistics.mean}))}/>
  </div>);


}

export default History;