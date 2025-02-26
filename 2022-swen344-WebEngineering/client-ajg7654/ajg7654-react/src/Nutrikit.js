import React from 'react';
// import logo from './logo.svg';
import 'bootstrap/dist/css/bootstrap.min.css'; 
import './Nutrikit.css';
import FoodCategories from './FoodCategories';
import MenuItems from './MenuItems';
import SelectItemButton from './SelectItemButton';
import SelectedItems from './SelectedItems';
import GroceryList from './GroceryList';
import Food from './Food';
import FoodDataLoader from './FoodDatabase';
import {Container, Row, Col} from 'reactstrap';
import CalorieProgress from './CalorieProgress';


const userGroceryList = new GroceryList();
const restAPI = FoodDataLoader.loadData("sql", userGroceryList);

class Nutrikit extends React.Component {
  constructor(props){
    super(props);

    this.state = {
      selectedCategory: '',
      selectedFoodName: '',
      userSelectedFoodName: '',
      recentlySelected: '',
      onAddMode: true,
      database: []
    }
    this.userGroceryList = userGroceryList;
    this.changeFoodCategory = this.changeFoodCategory.bind(this)
    this.changeFoodName = this.changeFoodName.bind(this)
    this.changeSelectedFoodName = this.changeSelectedFoodName.bind(this);
    this.changeSelectedItem = this.changeSelectedItem.bind(this);
    this.setAddMode = this.setAddMode.bind(this);
    this.fetchData = this.fetchData.bind(this);
    this.edit = this.edit.bind(this);
    this.create = this.create.bind(this);
    this.reload = this.reload.bind(this);
    this.get = this.get.bind(this);
  }

  componentDidMount(){
    restAPI.get(this.fetchData)
  }

  delete = (a) => {
    restAPI.delete(a)
    this.get()
  }

  edit = (a) => {
    restAPI.edit(a)
    this.get();
  }

  create = (a, b) => {
    restAPI.create(a, b)
    this.get();
  }

  reload = () => {
    restAPI.reload()
    this.get();
  }

  get = () => {
    restAPI.get(this.fetchData)
  }

  fetchData = (data)=>{this.setState({database: data})}

  changeFoodCategory(e) {
    this.setState({selectedCategory: e})
  }

  setAddMode(value) { 
    this.setState({onAddMode: value})
  }

  changeFoodName(e) {
    this.setState({selectedFoodName: e})
    this.setState({recentlySelected: e})
  }

  changeSelectedFoodName(e) {
    this.setState({userSelectedFoodName: e})
    this.setState({recentlySelected: e})
  }

  changeSelectedItem(e) {
    if( e ) {
      if ( this.state.selectedFoodName in Food.foodDict[this.state.selectedCategory])
        this.userGroceryList.addItem(this.state.selectedCategory, this.state.selectedFoodName);
    } else {
        this.userGroceryList.remItem(this.state.userSelectedFoodName);
    }
    this.setAddMode(this.state.onAddMode)
  }

  render() {
    return <div className="App">
      <h1> NutriKit Food Planner </h1>
      <h3> NutriKit allows you to sleect your groceries, and track your nutritional progress (good or bad) </h3>
      <br/>
      <Container fluid="md" className="bg-light border">

        <CalorieProgress status={this.userGroceryList.getTotalProps()}/>

        <Row xs="1" sm="2" md="4" lg="4" xl="4" xxl="4">
          <Col>
            <FoodCategories categories={Object.keys(Food.foodDict)} handleChange={this.changeFoodCategory} active={this.state.selectedCategory}/>
          </Col>

          <Col>
            <MenuItems category={this.state.selectedCategory} handleChange={this.changeFoodName} onFocus={()=>this.setAddMode(true)} currentlySelected={this.state.selectedFoodName}/>
          </Col>

          <Col>
            <SelectItemButton foodName={this.state.recentlySelected} delete={this.delete} create={this.create} handleChange={this.changeSelectedItem} onAddMode={this.state.onAddMode}/>
          </Col>
          
          <Col>
            <SelectedItems save={this.edit} reload={this.reload} groceryList={this.userGroceryList} handleChange={this.changeSelectedFoodName} onFocus={()=>this.setAddMode(false)} currentlySelected={this.state.userSelectedFoodName}/>
          </Col>
        </Row>

      </Container>
    </div>
  }
}

export default Nutrikit;