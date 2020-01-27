import TextField from "@material-ui/core/TextField/TextField";
import React from "react";
import {connect} from "react-redux";
import {setQuery} from "../appModel";

function QuerySearch({query, ...rest}) {
  return <TextField
     id="search"
     value={query}
     {...rest}
     onChange={e =>
        setQuery(e.target.value)
     }
  />
}

let mapStateToProps = ({query: {text}}) => ({query: text})
export default connect(mapStateToProps)(QuerySearch)