import Input from '@material-ui/core/Input';
import React, {useEffect, useState} from "react";
import SearchIcon from '@material-ui/icons/Search';
import IconButton from '@material-ui/core/IconButton';

export default function QuerySearchField({query, setQuery}) {
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
