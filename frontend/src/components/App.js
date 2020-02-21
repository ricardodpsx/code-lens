/* eslint-disable react/jsx-no-undef */
import React, {Component} from 'react';
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import './App.css';
import MainMenu from "./layout/MainMenu";
import CodeExplorer from "./layout/Main";
import {Provider} from 'react-redux'
import {store} from "../appModel";

class App extends Component {
  render() {
    return (
       <Provider store={store}>
         <Router>
           <MainMenu/>
           <Switch>
             <Route path="/hello">
               {/*<FileViewer />*/}
             </Route>

             <Route path="/">

               <CodeExplorer/>

             </Route>

           </Switch>
         </Router>
       </Provider>);
  }
}

export default App;