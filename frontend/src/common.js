import Typography from "@material-ui/core/Typography/Typography";
import React from "react";

export function Title({title}) {
  return <Typography variant="h6" component="h3">{title}</Typography>;
}

export function formatDate(commitTime) {
  let monthNames = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']


  let date = new Date(commitTime * 1000)
  var day = date.getDate();
  var monthIndex = date.getMonth();
  var hour = date.getHours();
  var min = date.getMinutes();

  return `${hour}:${min} ${day}/${monthNames[monthIndex]}`;
}