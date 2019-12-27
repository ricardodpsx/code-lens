import React, {useEffect, useState} from 'react';
import '../App.css';
/* eslint no-console:0, react/no-danger: 0 */
import 'rc-tree/assets/index.css';
import Tree, {TreeNode} from 'rc-tree';
import Paper from "@material-ui/core/Paper/Paper";
import {useStyles} from "../baseStyles";
import Link from "@material-ui/core/Link/Link";
import {Title} from "../common";
import {ancestors} from "./treeUtils";

const STYLE = `
.collapse {
  overflow: hidden;
  display: block;
}

.collapse-active {
  transition: height 0.3s ease-out;
}
`;

function expand(g, v) {
  if (!g[v] || g[v].data.type === "file") return

  return g[v].children.map(c =>
    <TreeNode title={g[c].data.fileName} key={c} >
      {g[c].children.length && expand(g, c)}
    </TreeNode>
  )
}


function DirectoryTree({results, graph, onFileSelect, selectedFile}) {

  if (!graph || results.length === 0) return null

  const classes = useStyles();

  let selectedFileAncestors = selectedFile ? ancestors(graph, selectedFile) : []
  const [expandedKeys, setExpandedKeys] = useState([graph.rootVid])

  useEffect(() => {
    setExpandedKeys(expandedKeys.concat(selectedFileAncestors))
  }, [selectedFile])

  const onExpand = (keys, {expanded}) => {
    let s = new Set(keys)

    if (expanded) keys.forEach(k => s.add(k))
    else s.delete(keys)

    setExpandedKeys(Array.from(s))
  }

  const handleSelect = (keys) => {
    if(keys.length !== 1) return true;

    setExpandedKeys(expandedKeys.concat(selectedFileAncestors))
    onFileSelect({vid: keys[0], name: graph[keys[0]].name, data: graph[keys[0]].data})
    return true
  }

  let [allExpanded, doExpandAll] = useState(false)

  return (
     <div className={classes.root}>
       <Paper className={classes.paper}>

         <Title title="Directory"/>

         <style dangerouslySetInnerHTML={{__html: STYLE}}/>
         <Link href="#" color="primary" variant="body2" onClick={e => {
           e.preventDefault();
           doExpandAll(true)
         }}>expand</Link>&nbsp;
         | &nbsp;<Link href="#" color="primary" variant="body2" onClick={e => {
         e.preventDefault();
         doExpandAll(false)
       }}>collapse</Link>

         <Tree
            onExpand={onExpand}
            onSelect={handleSelect}
            selectedKeys={[selectedFile]}
            expandedKeys={expandedKeys}
            //openAnimation={animation}
         >{expand(graph, graph.rootVid)}
         </Tree>

       </Paper>
     </div>
  )

}



export default DirectoryTree;
