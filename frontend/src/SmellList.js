import React, {Component} from "react";

class SmellList extends Component {

    constructor(props) {
        super(props)
        this.state = {smellList: {}}
    }

    loadSmellList(){
        const that = this;
        fetch('http://localhost:8080/smells')
           .then(function(response) {
             return response.json();
           })
           .then(function(data) {
             that.setState({
               smellList: data,
             })
           })
    }

    handleLoadSmell(event, smellKey){
        event.preventDefault()
        fetch(`http://localhost:8080/smell/${smellKey}`)
           .then(function(response) {
             return response.json();
           })
           .then(function(data) {
             console.log(data)
           })
    }

    componentDidMount() {
        this.loadSmellList();
     }

    render() {
        const smellList = this.state.smellList;
        return <div><ul>
            {smellList && Object.keys(smellList).map( smellKey =>
                <li key={smellKey}><a href="#" onClick={e => this.handleLoadSmell(e, smellKey)}>{smellList[smellKey].title}</a></li>
            )}
            </ul>
        </div>
    }
}

export default SmellList