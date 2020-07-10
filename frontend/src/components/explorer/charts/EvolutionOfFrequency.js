/* eslint-disable react/jsx-no-undef */
import React, {useEffect} from 'react';
import {formatDate} from "../../../lib/stringUtils";
import BarChart from "./BarChart";
import {loadFrequencyEvolution} from "../../../appModel";
import {connect} from "react-redux";


function EvolutionOfFrequency({frequency, queryText}) {
  useEffect(()=>{
    loadFrequencyEvolution()
  }, [queryText])

  return (<div>
    <BarChart
       xField="commit"
       yField={["frequency"]}
       data={frequency.map(it => ({commit: formatDate(it.commit.commitTime), frequency: it.frequency}))}
       // For tests
       rootProps={{'data-testid': '2'}}/>
  </div>);

}

export default connect(({query: {text}, evolution: {frequency}}) => ({queryText: text, frequency}))(EvolutionOfFrequency);