import React, {useEffect, useState} from 'react';
import Typography from '@material-ui/core/Typography';
import {loadSmells} from "../CodeLensApi";
import Paper from "@material-ui/core/Paper/Paper";
import List from "@material-ui/core/List/List";
import ListItem from "@material-ui/core/ListItem/ListItem";
import {useStyles} from "../baseStyles";
import Divider from "@material-ui/core/Divider/Divider";


export default function SmellList({onSmellSelection}) {
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
                          onClick={e => onSmellSelection(smell)}
                >
                  {smell.title} </ListItem> <Divider/>
              </div>
           )}
         </List>
       </Paper>
     </div>
  );
}