import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import BottomNavigation from '@material-ui/core/BottomNavigation';
import BottomNavigationAction from '@material-ui/core/BottomNavigationAction';
import Assessment from '@material-ui/icons/Assessment';
import Explore from '@material-ui/icons/Explore';
import {useHistory} from "react-router-dom";


const useStyles = makeStyles({
  root: {
    width: 500,
  },
});

export default function MainMenu() {
  let history = useHistory();
  const classes = useStyles();
  const [value, setValue] = React.useState(0);

  return (
     <BottomNavigation
        value={value}
        onChange={(event, newValue) => {
          history.push(newValue);
          setValue(newValue);
        }}
        showLabels
        className={classes.root}
     >
       <BottomNavigationAction label="Explore" value="/" icon={<Explore/>}/>
       <BottomNavigationAction label="Evolution" value="/history" icon={<Assessment/>}/>

     </BottomNavigation>
  );
}