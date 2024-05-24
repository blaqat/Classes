import React from 'react';
import { Table, Input, Modal, ModalBody, ModalHeader, ModalFooter, Button } from 'reactstrap';
import Food from './Food';
// import logo from './logo.svg';
import './Nutrikit.css';
import FoodCategories from './FoodCategories';
var foodProps = {
    name: '',
    calories: '',
    totalFat: '',
    saturatedFat: '',
    transFat: '',
    proteins: '',
    carbs: ''
}
class CreateFood extends React.Component {
    constructor(props){
        super(props)
        this.state = {
            active: false,
            category: '',
        }
        this.changeCategory = this.changeCategory.bind(this);
        this.changeProperty = this.changeProperty.bind(this);
        this.createFood = this.createFood.bind(this);
    }

    changeProperty = (key, value) => {
        foodProps[key] = value;
    }

    changeCategory = (n) => {
        this.setState({category: n})
    }

    createFood = () => {
        let props = foodProps
        let category = this.state.category
        let newFood = new Food(props.name, category, props.calories, props.totalFat, props.saturatedFat, props.transFat, props.proteins, props.carbs)
        console.log(newFood, category);
        this.props.create(newFood, category)
        foodProps = {
            name: '',
            calories: '',
            totalFat: '',
            saturatedFat: '',
            transFat: '',
            proteins: '',
            carbs: ''
        }
    }

    createTable(){
        let props = foodProps;
        let components = []
        for(let [key, value] of Object.entries(props)){
            let comp = <tr>
                <th>{key}:</th>
                <td><Input defaultValue={value} onChange={(e)=>{this.changeProperty(key, e.target.value)}}></Input></td>
            </tr>
            components.push(comp);
        }
        return components;
    }

    render(){
        return <Modal isOpen={this.props.active}>

        <ModalHeader toggle={this.props.exit}>
            {"Creating New Food"}
        </ModalHeader>

        <ModalBody>
        <FoodCategories categories={Object.keys(Food.foodDict)} handleChange={this.changeCategory} active={this.state.category}/>
            <Table>
                <thead>
                    <tr>
                        <th>Property</th>
                        <th>Value</th>
                    </tr>
                </thead>
                <tbody>
                    {this.createTable()}
                </tbody>  
            </Table>
        </ModalBody>

        <ModalFooter>
            <Button color="primary" onClick={this.createFood}>Create</Button>
            <Button onClick={this.props.exit}>Cancel</Button>
        </ModalFooter>
        
        </Modal>
    }

}

export default CreateFood