import React, {useEffect, useState} from 'react';
import Typography from '@material-ui/core/Typography';
import {loadSmells} from "../../api";
import Paper from "@material-ui/core/Paper/Paper";
import List from "@material-ui/core/List/List";
import ListItem from "@material-ui/core/ListItem/ListItem";
import {useStyles} from "../layout/baseStyles";
import Divider from "@material-ui/core/Divider/Divider";
import ErrorIcon from '@material-ui/icons/Error';
import CheckCircleIcon from '@material-ui/icons/CheckCircle';
import {green, red} from '@material-ui/core/colors';
import {setQuery} from "../../appModel";


export default function SmellList() {
  const classes = useStyles();

  let [smellList, setSmellList] = useState([])
  useEffect(() => {
    loadSmells(setSmellList)
  }, [smellList.length])

  return (
     <div className={classes.root}>
       <Paper className={classes.paper}>
         <Typography variant="h6" component="h3">Explore Code Smells</Typography>
         <List>
           {Object.values(smellList).map(smell =>
              <div key={smell.title}>
                <ListItem className={classes.listItem} button
                          onClick={e => setQuery(smell.query)}>
                  {smell.title} {renderSmellSemaphore(smell.stinky)}</ListItem> <Divider/>
              </div>
           )}
         </List>
       </Paper>
     </div>
  );
}

function renderSmellSemaphore(smellStinks){
    return <span style={{marginLeft: 5}}>
        {smellStinks?
        <ErrorIcon style={{ color: red[400] }}/>:
        <CheckCircleIcon style={{ color: green[400] }}/>}
    </span>;
}