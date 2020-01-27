import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import Explore from '@material-ui/icons/Explore';
import {useHistory} from "react-router-dom";
import Toolbar from "@material-ui/core/Toolbar/Toolbar";
import IconButton from "@material-ui/core/IconButton/IconButton";
import Typography from "@material-ui/core/Typography/Typography";
import AppBar from "@material-ui/core/AppBar/AppBar";


const useStyles = makeStyles({
  root: {
    width: 500,
  },
  appBar: {
    marginBottom: 20
  }
});

export default function MainMenu() {
  let history = useHistory();
  const classes = useStyles();
  const [value, setValue] = React.useState(0);

  return (
     <AppBar className={classes.appBar} position="static">
       <Toolbar>
         <IconButton edge="start" className={classes.menuButton} color="inherit" aria-label="menu">
           <Explore/>
         </IconButton>
         <Typography variant="h6" className={classes.title}>
           Code Explorer
         </Typography>
       </Toolbar>
     </AppBar>
  );
}