import React from 'react';
// import logo from './logo.svg';
import 'bootstrap/dist/css/bootstrap.min.css'; 
import './Nutrikit.css';
import {Progress, Input, Button, Card, CardFooter} from 'reactstrap';

const order = ["calories", "saturatedFat", "transFat", "proteins", "carbs", "totalFat"]

class CalorieProgress extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      goals: [],
      editing: '',
      newVal: '',
      showing: false
    }
    this.onGoalChange = this.onGoalChange.bind(this);
    this.displayGoals = this.displayGoals.bind(this);
    this.cancel = this.cancel.bind(this);
    this.saveButtion = this.saveButtion.bind(this);
    this.saveGoal = this.saveGoal.bind(this);
    this.updateVal = this.updateVal.bind(this);
    this.updateShow = this.updateShow.bind(this);

  }

  fetchData = () => {
    //In package.json add "proxy": "http://localhost:5000" 
    //This will allow redirect to REST api in Flask w/o CORS errors
    console.log("HELLO HELP PLEASE")
     fetch('/goals')
     .then(
         response => response.json() 
         )//The promise response is returned, then we extract the json data
     .then(jsonOutput => //jsonOutput now has result of the data extraction
              {
                  this.updateData(jsonOutput)
              }
          )
  }

  updateShow = (e) => {
    this.setState({showing: !this.state.showing})
  }

  updateVal = (e) => {
    this.setState({newVal: e.target.value})
  }

  updateData = (apiResponse) => {
    this.setState({goals: apiResponse})
  }

  componentDidMount(){
    this.fetchData();
  }

  onGoalChange(goal){
      this.setState({editing: goal[1]})
  }

  saveButtion(goal){
    return <div><Button color='primary' onClick={()=>{this.saveGoal(goal)}}>Save</Button> <Button onClick={this.cancel}>Cancel</Button></div>
  }

  saveGoal(goal){
    fetch('/goals/' + goal[0], {method: 'PUT', headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({new_goal: this.state.newVal})
    }).then(()=> this.cancel())
   
  }

  cancel(){
    this.setState({editing:'', newVal:'', goals: []})
    this.fetchData();
  }

  displayGoals(){
    console.log(this.state.goals)
    if(this.state.goals.length <= 0)
      return;

    let elements = []
    for(let x = 0; x < order.length; x++){
      let lookingFor = order[x]
      for(let y= 0; y < this.state.goals.length; y++){
        let goal = this.state.goals[y]
        if(goal[1]===lookingFor){
          let status = this.props.status[goal[1]]
          let percent = status/goal[2]*100
          elements.push( <Card>
          <CardFooter>
            {goal[1]}: {status} /
            <Button outline color="" margin-left="-30px" >
            <Input defaultValue={goal[2]} onChange={(e)=>{this.updateVal(e); this.onGoalChange(goal)}}/></Button>
            {this.state.editing===goal[1]?this.saveButtion(goal):""}
          </CardFooter>
          <Progress color="primary" value={percent}>{percent>=100?"YOU DID IT!":""}</Progress>
          </Card>)
          break;
        }
      }
    }
    this.state.goals.forEach((goal) => {
      
    })
    return elements
  }

  render() {
    let showing = this.state.showing
    return <div className="CalorieProgress">
        <h3>User Goals: <Button color="info" onClick={this.updateShow}>{!showing?"Show Goals":"Hide Goals"}</Button></h3>
        
        {showing?this.displayGoals():""}
    </div>
  }
}

export default CalorieProgress;