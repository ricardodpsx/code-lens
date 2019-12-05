/* eslint-disable react/jsx-no-undef */
import React, {Component} from 'react';
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import './App.css';


import MainMenu from "./MainMenu";
import {CodeExplorer} from "./CodeExplorer/CodeExplorer";


class App extends Component {

  render() {
    return (
           <Router>
             <MainMenu/>
               <Switch>
                 <Route path="/">
                   <CodeExplorer/>
                 </Route>
               </Switch>
           </Router>
         );
  }



}

export default App;
