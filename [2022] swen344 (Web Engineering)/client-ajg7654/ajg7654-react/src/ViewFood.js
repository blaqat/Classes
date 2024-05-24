import React from 'react';
import { Table, Input, Modal, ModalBody, ModalHeader, ModalFooter, Button } from 'reactstrap';
// import logo from './logo.svg';
import './Nutrikit.css';

class ViewFood extends React.Component {
    changeProperty(food, property, value){
        food[property] = value;
    }

    createTable(food){
        if(!food)
            return;
        let props = food.getProps()
        let ratings = food.propsToDVRating()
        let components = []
        for(let [key, value] of Object.entries(props)){
            let rating = ratings[key]
            let comp = <tr>
                <th>{key}:</th>
                <td><Input invalid={rating!=="success"} defaultValue={value} onChange={(e)=>{this.changeProperty(food, key, e.target.value)}}></Input></td>
            </tr>
            components.push(comp);
        }
        return components;
    }

    render(){
        let food = this.props.food
        

        return <Modal isOpen={this.props.active}>

        <ModalHeader toggle={this.props.exit}>
            {food?food.name:""}
        </ModalHeader>

        <ModalBody>
            <Table>
                <thead>
                    <tr>
                        <th>Property</th>
                        <th>Amount</th>
                    </tr>
                </thead>
                <tbody>
                    {this.createTable(food)}
                </tbody>  
            </Table>
        </ModalBody>

        <ModalFooter>
            <Button color="primary" onClick={()=>this.props.save(food)}>Save</Button>
            <Button onClick={this.props.exit}>Cancel</Button>
        </ModalFooter>
        
        </Modal>
    }

}

export default ViewFood