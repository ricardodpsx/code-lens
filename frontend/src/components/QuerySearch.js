import Input from '@material-ui/core/Input';
import React, {useEffect, useState} from "react";
import {connect} from "react-redux";
import {setQuery} from "../appModel";
import SearchIcon from '@material-ui/icons/Search';
import IconButton from '@material-ui/core/IconButton';

function QuerySearch({query}) {
  let [text, setText] = useState(query)
  useEffect(() => {
    if (query !== text) setText(query)
  }, [query]);

  return <form onSubmit={e => {
    setQuery(text)
    e.preventDefault()
  }}>
    <Input id="search"
           size="small"
           style={{width: "60%"}}
           value={text}
           label="search" margin="normal" variant="outlined"
           onChange={e => setText(e.target.value)}/>
    <IconButton type="submit" aria-label="search">
      <SearchIcon/>
    </IconButton>
  </form>
}

let mapStateToProps = ({query: {text}}) => ({query: text})
export default connect(mapStateToProps)(QuerySearch)