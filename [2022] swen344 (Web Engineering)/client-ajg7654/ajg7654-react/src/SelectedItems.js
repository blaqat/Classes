import React from 'react';
import { Card, CardTitle, CardBody, ListGroup, ListGroupItem, Button } from 'reactstrap';
// import logo from './logo.svg';
import './Nutrikit.css';
import ViewFood from './ViewFood';

class SelectedItems extends React.Component {
    constructor(props){
        super(props);
        this.onViewFood = this.onViewFood.bind(this);
        this.onExitViewFood = this.onExitViewFood.bind(this);
        this.state = {
            viewFood: false,
            activeFood: null
        }
        this.save = this.save.bind(this);
    }

    listFoods(foods){
        if(foods){
          foods = Object.values(foods)
          return foods.map(food => 
            <ListGroupItem color={this.props.currentlySelected===food.name?"danger":null} active={this.props.currentlySelected===food.name} action tag="button" onClick={()=>{this.props.handleChange(food.name); this.props.onFocus()}}>
              {food.name}
              <p></p>
              <Button outline={this.props.currentlySelected!==food.name} onClick={()=>this.onViewFood(food)}>view</Button>
            </ListGroupItem> 
          )
        }
        else 
          return <option>Select a category</option>
    }

    onViewFood(food){
        this.setState({viewFood: true, activeFood: food.name})
    }

    onExitViewFood(){
        this.setState({viewFood: false, activeFood: null})
        this.props.reload(false);
    }

    save(food){
        this.props.save(food);
        this.setState({viewFood: false, activeFood: null})
    }

    render(){   
        let groceryList = this.props.groceryList;
        let activeFood = this.state.activeFood;

        return <div>
        <Card className="SelectedItems">
            <CardBody>
                <CardTitle> Selected Items </CardTitle>
                <div className="d-flex justify-content-center p-1">
                <ListGroup>
                    {this.listFoods(groceryList.foods)}
                </ListGroup>
                </div>    
            </CardBody>
        </Card>

        <ViewFood food={groceryList.getFood(activeFood)} exit={this.onExitViewFood} active={this.state.viewFood} save={this.save}/>
        </div>
    }

}

export default SelectedItems