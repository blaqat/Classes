import React from 'react';
import { Modal, ModalHeader, ModalFooter, Button } from 'reactstrap';
import Food from './Food';
// import logo from './logo.svg';
import './Nutrikit.css';

class DeleteFood extends React.Component {
    constructor(props){
        super(props)
        this.delete = this.delete.bind(this);
    }

    delete = (food) => {
        this.props.delete(food);
    }

    render(){
        let food = Food.getFood(this.props.foodName)
        return <Modal isOpen={this.props.active}>

        <ModalHeader toggle={this.props.exit}>
            Are you sure you want to permnamently delete {food?food.name:"Nothing"} from the entire system?
        </ModalHeader>

        <ModalFooter>
            <Button color="primary" onClick={()=>this.delete(food)}>Yes</Button>
            <Button color="dangerous" outline onClick={this.props.exit}>No</Button>
        </ModalFooter>
        
        </Modal>
    }

}

export default DeleteFood