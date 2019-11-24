import Typography from "@material-ui/core/Typography/Typography";
import React from "react";

export function Title({title}) {
  return <Typography variant="h6" component="h3">{title}</Typography>;
}