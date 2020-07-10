import {setDependenciesQuery} from "../../appModel";
import React from "react";
import QuerySearchField from "../QuerySearchField";
import {connect} from "react-redux";

function DependenciesQuerySearch({query}) {
  return <QuerySearchField query={query} setQuery={setDependenciesQuery}/>
}

let mapStateToProps = ({dependencies: {query}}) => ({query})
export default connect(mapStateToProps)(DependenciesQuerySearch)