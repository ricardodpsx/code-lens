
import React, { Component } from 'react';
import './App.css';


/* eslint no-console:0, react/no-danger: 0 */
import 'rc-tree/assets/index.css';
import Tree, { TreeNode } from 'rc-tree';
import cssAnimation from 'css-animation';

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
  if(g[v].data.type == "file") return

  return g[v].children.map(c =>
    <TreeNode title={g[c].data.fileName} key={c} >
      {g[c].children.length && expand(g, c)}
    </TreeNode>
  )
}

class DirectoryTree extends Component {

  constructor(props = {onFileSelect:()=>{}}) {
    super(props)
    this.state = {code: ''}
  }

  handleSelect = (keys)=> {
    if(keys.length !== 1) return true;
    this.props.onFileSelect({vid: keys[0], name: this.props.graph[keys[0]].name, data: this.props.graph[keys[0]].data})
    return true
  }

  render() {
    if(this.props.results.length == 0)
      return <div>...loading</div>

    return (
      <div>
          <style dangerouslySetInnerHTML={{ __html: STYLE }}/>
            <Tree
              onSelect={this.handleSelect}
              defaultExpandAll={false}
              defaultExpandedKeys={[this.props.graph.rootVid]}
              //openAnimation={animation}
            >
              {expand(this.props.graph, this.props.graph.rootVid)}
            </Tree>
      </div>
    )
  }
}



export default DirectoryTree;
