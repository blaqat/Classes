import React from 'react';
import './Nutrikit.css';
import {Card, Button, CardBody, CardTitle} from 'reactstrap';
import CreateFood from './CreateFood';
import DeleteFood from './DeleteFood';


class SelectItemButton extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            create: false,
            delete: false
        }

        this.startCreate = this.startCreate.bind(this);
        this.endCreate = this.endCreate.bind(this);
        this.create = this.create.bind(this);
        this.delete = this.delete.bind(this);
        this.endDelete = this.endDelete.bind(this);
        this.startDelete = this.startDelete.bind(this);
    }

    startCreate = () => {
        this.setState({create: true})
    }

    endCreate = () => {
        this.setState({create: false})
    }

    startDelete = () => {
        this.setState({delete: true})
    }

    endDelete = () => {
        this.setState({delete: false})
    }

    create = (food, category) => {
        this.endCreate();
        this.props.create(food, category);
    }

    delete = (food) => {
        this.endDelete();
        this.props.delete(food);
    }

    render(){
        return <Card className="SelectItemButton">
        <CardBody>
            <CardTitle> Edit Item </CardTitle>
            
            <div className="d-flex justify-content-center p-5">
                <Button className="ItemButton" color="primary" onClick={()=>this.props.handleChange(true)}>
                    Add to List
                </Button>

                <Button className="ItemButton" color="warning" onClick={()=>this.props.handleChange(false)}>
                    Remove From List
                </Button>
                </div>

                <div className="d-flex justify-content-center">
                <Button className="ItemButton" color="success" onClick={this.startCreate}>
                    Create Item
                </Button>

                <Button className="ItemButton" color="danger" onClick={this.startDelete}>
                    Delete Item
                </Button>
                </div>
        </CardBody>
        <CreateFood create={this.create} exit={this.endCreate} active={this.state.create}/>
        <DeleteFood foodName={this.props.foodName} delete={this.delete} exit={this.endDelete} active={this.state.delete}/>
    </Card>
    }

}

export default SelectItemButton