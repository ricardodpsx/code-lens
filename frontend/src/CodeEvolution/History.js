/* eslint-disable react/jsx-no-undef */
import React, {useEffect, useState} from 'react';
import HistoryMetrics from "./HistoryMetrics"
import {Title} from "../common";


function loadHistory(selectedMetric, query, onLoad, maxCommits = 15) {
  let that = this;
  fetch(`http://localhost:8080/history/${selectedMetric}?query=` + encodeURIComponent(query)
     + '&maxCommits=' + encodeURIComponent(maxCommits)
  )
     .then(function (response) {
       return response.json();
     })
     .then(function (data) {
       onLoad(data)
     })
}

function History({selectedMetric, query}) {
  let [history, setHistory] = useState([])

  if (!selectedMetric) return null

  useEffect(() => {
    loadHistory(selectedMetric, query, setHistory)
  }, [selectedMetric, query])

  return (<div>
    <Title title={`Evolution of Metric ${selectedMetric}`}/>
    <HistoryMetrics history={history}/>
  </div>);


}

export default History;