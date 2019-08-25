import React, { Component } from 'react';
import _ from 'lodash'

class FitnessFunction extends Component {
  constructor(props) {
    super(props)
    this.state = {nodes: []}
  }

  addNode() {
    this.setState((state, props) => {
      return {nodes: _.uniqBy(state.nodes.concat(props.selectedNode), 'vid')};
    });
  }

  save() {

  }

  render() {
    return (
      <div>
        <div>Follow node changes </div>
        <input type="text" placeholder="description" /> <br/>
          <input type="button" value="Add" onClick={()=>this.addNode(this.props.selectedNode)}  />
          <ul>
            {this.state.nodes.map(n=><li>{n.data.path}</li>)}
          </ul>
          <input type="button" value="save" onClick={this.save.bind(this)} />
        <div>
        </div>
      </div>
    )
  }
}

export default FitnessFunction