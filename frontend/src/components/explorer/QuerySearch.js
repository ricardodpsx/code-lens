import {setQuery} from "../../appModel";
import React from "react";
import QuerySearchField from "../QuerySearchField";
import {connect} from "react-redux";

function QuerySearch({query}) {
  return <QuerySearchField query={query} setQuery={setQuery}/>
}

let mapStateToProps = ({query: {text}}) => ({query: text})

export default connect(mapStateToProps)(QuerySearch)