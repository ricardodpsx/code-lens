/* eslint-disable react/jsx-no-undef */
import React, {useEffect, useState} from 'react';
import {formatDate, Title} from "../common";
import {loadFrequencyHistory} from "../CodeLensApi";
import BarChart from "../BarChart";


function EvolutionOfFrequency({query}) {

  let [frequencies, setFrequencies] = useState([])

  useEffect(() => {
    loadFrequencyHistory(query, setFrequencies)
  }, [query])

  if (!frequencies) return null

  return (<div>
       <Title title={`Frequency of ${query}`}/>
       <BarChart
          xField="commit"
          yField="frequency"
          data={frequencies.map(it => ({commit: formatDate(it.commit.commitTime), frequency: it.frequency}))}
          // For tests
          rootProps={{'data-testid': '2'}}/>
     </div>
  );


}

export default EvolutionOfFrequency;