import React from 'react';
import { ListGroupItem } from 'reactstrap';
// import logo from './logo.svg';
import './Nutrikit.css';
import {Card, CardBody, CardTitle, ListGroup} from 'reactstrap';
import Food from './Food';

class MenuItems extends React.Component {
    constructor(props){
      super(props);
      this.state = {
        edited: Food.foodDict
      }
      this.check = this.check.bind(this);
    }

    check = () => {
      this.setState({edited: Food.foodDict});
    }

    listFoods(foods){
      if(foods){
        foods = Object.values(foods)
        return foods.map(food => 
          <ListGroupItem active={this.props.currentlySelected===food.name} action tag="button" onClick={()=>{this.props.handleChange(food.name); this.check(); }}>
            {food.name}
          </ListGroupItem> 
        )
      }
      else 
        return <option>Select a category</option>
    }

    render() {
      return <Card className="MenuItems">
      <CardBody>
          <CardTitle> Menu Items </CardTitle>
          <div className="d-flex justify-content-center p-1">
          <ListGroup>
            {this.listFoods(this.state.edited[this.props.category])}
          </ListGroup>
          </div>
      </CardBody>
  </Card>
    }

}

export default MenuItems