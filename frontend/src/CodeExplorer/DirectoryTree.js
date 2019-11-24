import React, {useState} from 'react';
import '../App.css';
/* eslint no-console:0, react/no-danger: 0 */
import 'rc-tree/assets/index.css';
import Tree, {TreeNode} from 'rc-tree';
import cssAnimation from 'css-animation';
import Paper from "@material-ui/core/Paper/Paper";
import {useStyles} from "../baseStyles";
import Link from "@material-ui/core/Link/Link";
import {Title} from "../common";

const STYLE = `
.collapse {
  overflow: hidden;
  display: block;
}

.collapse-active {
  transition: height 0.3s ease-out;
}
`;

function animate(node, show, done) {
  let height = node.offsetHeight;
  return cssAnimation(node, 'collapse', {
    start() {
      if (!show) {
        node.style.height = `${node.offsetHeight}px`;
      } else {
        height = node.offsetHeight;
        node.style.height = 0;
      }
    },
    active() {
      node.style.height = `${show ? height : 0}px`;
    },
    end() {
      node.style.height = '';
      done();
    },
  });
}

const animation = {
  enter(node, done) {
    return animate(node, true, done);
  },
  leave(node, done) {
    return animate(node, false, done);
  },
};


function expand(g, v) {
  if (g[v].data.type === "file") return

  return g[v].children.map(c =>
    <TreeNode title={g[c].data.fileName} key={c} >
      {g[c].children.length && expand(g, c)}
    </TreeNode>
  )
}


function TreeCollapsed({handleSelect, graph}) {
  return <Tree
     onSelect={handleSelect}
     defaultExpandAll={false}
     defaultExpandedKeys={[graph.rootVid]}
     //openAnimation={animation}
  >{expand(graph, graph.rootVid)}
  </Tree>
}

function TreeExpanded({handleSelect, graph}) {
  return <Tree
     onSelect={handleSelect}
     defaultExpandAll={true}
     defaultExpandedKeys={[graph.rootVid]}
     //openAnimation={animation}
  >{expand(graph, graph.rootVid)}
  </Tree>
}


function DirectoryTree({results, graph, onFileSelect}) {

  const classes = useStyles();

  const handleSelect = (keys) => {
    if(keys.length !== 1) return true;
    onFileSelect({vid: keys[0], name: graph[keys[0]].name, data: graph[keys[0]].data})
    return true
  }

  let [expanded, doExpand] = useState(false)

  return (
     <div className={classes.root}>
       <Paper className={classes.paper}>

         <Title title="Directory"/>

         <style dangerouslySetInnerHTML={{__html: STYLE}}/>
         <Link href="#" color="primary" variant="body2" onClick={e => {
           e.preventDefault();
           doExpand(true)
         }}>expand</Link>&nbsp;
         | &nbsp;<Link href="#" color="primary" variant="body2" onClick={e => {
         e.preventDefault();
         doExpand(false)
       }}>collapse</Link>

         {//This dumb code was necesary because the library doesn't support just expanding the tree
           !!results.length &&
           (expanded ?
              <TreeExpanded graph={graph} handleSelect={handleSelect}/>
              : <TreeCollapsed graph={graph} handleSelect={handleSelect}/>)
         }

       </Paper>
     </div>
  )

}



export default DirectoryTree;
