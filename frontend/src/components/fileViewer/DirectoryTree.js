import React, {useEffect, useState} from 'react';
import '../App.css';
import {allVertices, ancestors, children, vdata, vertice} from '../../lib/treeUtils'
/* eslint no-console:0, react/no-danger: 0 */
import 'rc-tree/assets/index.css';
import Tree, {TreeNode} from 'rc-tree';
import Paper from "@material-ui/core/Paper/Paper";
import {useStyles} from "../layout/baseStyles";
import Link from "@material-ui/core/Link/Link";
import {Title} from "../layout/Title";
import {connect} from "react-redux";
import {selectFile} from "../../appModel";

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
  if (!g || !v) return
  if (vertice(g, v) && vertice(g, v).type === "file") return

  return children(g, v).map(c => {
       return <TreeNode title={vdata(g, c).fileName} key={c}>
         {children(g, c).length && expand(g, c)}
       </TreeNode>
     }
  )
}

function DirectoryTree({codeTree, selectedFile}) {
  if (!codeTree) return null

  const classes = useStyles();

  let selectedFileAncestors = selectedFile ? ancestors(codeTree, selectedFile) : []
  const [expandedKeys, setExpandedKeys] = useState([codeTree.rootVid])

  let allKeys = allVertices(codeTree).filter(v => v.type === "dir").map(v => v.vid)

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
    selectFile(keys[0])
    return true
  }

  return (
     <div className={classes.root}>
       <Paper className={classes.paper}>

         <Title title="Directory"/>

         <style dangerouslySetInnerHTML={{__html: STYLE}}/>
         <Link href="#" color="primary" variant="body2" onClick={e => {
           e.preventDefault();
           setExpandedKeys(allKeys)
         }}>expand</Link>&nbsp;
         | &nbsp;<Link href="#" color="primary" variant="body2" onClick={e => {
         e.preventDefault();
         setExpandedKeys([])
       }}>collapse</Link>
         <Tree
            onExpand={onExpand}
            onSelect={handleSelect}
            selectedKeys={[selectedFile]}
            expandedKeys={expandedKeys}>
           {expand(codeTree, codeTree.rootVid)}
         </Tree>
       </Paper>
     </div>
  )
}

let mapStateToProps = ({selectedFile: {fileVid}, query: {results, codeTree} = {}}) =>
   ({selectedFile: fileVid, results, codeTree})

export default connect(mapStateToProps)(DirectoryTree);
