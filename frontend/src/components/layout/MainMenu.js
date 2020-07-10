import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import Explore from '@material-ui/icons/Explore';
import {useHistory} from "react-router-dom";
import Toolbar from "@material-ui/core/Toolbar/Toolbar";
import IconButton from "@material-ui/core/IconButton/IconButton";
import Typography from "@material-ui/core/Typography/Typography";
import AppBar from "@material-ui/core/AppBar/AppBar";
import BottomNavigation from "@material-ui/core/BottomNavigation";
import BottomNavigationAction from "@material-ui/core/BottomNavigationAction";
import SearchIcon from '@material-ui/icons/Search';
import BubbleChartIcon from '@material-ui/icons/BubbleChart';

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
     <AppBar  position="static">
       <Toolbar>
         <IconButton edge="start" className={classes.menuButton} color="inherit" aria-label="menu">
           <Explore/>
         </IconButton>
         <Typography variant="h6" className={classes.title}>
           Code Explorer
         </Typography>

         <BottomNavigation
            value={value}
            onChange={(event, newValue) => {
              setValue(newValue);
            }}
            showLabels
            className={classes.root}
         >
           <BottomNavigationAction
              onClick={() => history.push("/")}
              label="Code Search" icon={<SearchIcon />} />
           <BottomNavigationAction
              onClick={() => history.push("/dependencies")}
              label="Dependencies" icon={<BubbleChartIcon />} />

         </BottomNavigation>

       </Toolbar>
     </AppBar>
  );
}