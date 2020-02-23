import {ResponsiveBar} from "@nivo/bar";
import React from "react";

export default ({
                  data, xField, yField, xLabel = xField, yLabel = yField, onDataSelected = () => {
  }
                }) => (

   <div style={{height: 400}}>
     <ResponsiveBar
        data={data}
        keys={yField}
        indexBy={xField}
        margin={{top: 50, right: 130, bottom: 50, left: 60}}
        padding={0.3}
        axisTop={null}
        axisRight={null}
        axisBottom={{
          tickSize: 5,
          tickPadding: 5,
        tickRotation: 30,
        legend: xLabel,
        legendPosition: 'end',
        legendOffset: 5
        }}
        onClick={({data}) => onDataSelected(data)}
        axisLeft={{
          tickSize: 5,
          tickPadding: 5,
          tickRotation: 0,
          legend: yLabel,
          legendPosition: 'middle',
          legendOffset: -40
        }}
        legends={[
          {
            dataFrom: 'keys',
            anchor: 'bottom-right',
            direction: 'column',
            justify: false,
            translateX: 120,
            translateY: 0,
            itemsSpacing: 2,
            itemWidth: 100,
            itemHeight: 20,
            itemDirection: 'left-to-right',
            itemOpacity: 0.85,
            symbolSize: 20,
            effects: [
              {
                on: 'hover',
                style: {
                  itemOpacity: 1
                }
              }
            ]
          }
        ]}
        labelSkipWidth={12}
        labelSkipHeight={12}
        labelTextColor={{from: 'color', modifiers: [['darker', 1.6]]}}
        animate={true}
        motionStiffness={90}
        motionDamping={15}
     /></div>
)