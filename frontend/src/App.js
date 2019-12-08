/* eslint-disable react/jsx-no-undef */
import React, {Component} from 'react';
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import './App.css';
import History from "./CodeEvolution/History"
import MainMenu from "./MainMenu";
import {CodeExplorer} from "./CodeExplorer/CodeExplorer";


class App extends Component {

  render() {
    return (
           <Router>
             <MainMenu/>
               <Switch>
                 <Route path="/history">
                   <History />
                 </Route>
                 <Route path="/">
                   <CodeExplorer/>
                 </Route>
               </Switch>
           </Router>
         );
  }



}

export default App;
