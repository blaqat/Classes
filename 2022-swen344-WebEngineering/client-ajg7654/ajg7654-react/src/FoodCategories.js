import React from 'react';
import {Dropdown, DropdownItem, DropdownToggle, DropdownMenu, Card, CardBody, CardTitle} from 'reactstrap';


class FoodCategories extends React.Component {
    constructor(props){
        super(props);
        this.toggle = this.toggle.bind(this);
        this.state = {
          dropdownOpen: false
        };
    }
    toggle() {
        this.setState(prevState => ({
          dropdownOpen: !prevState.dropdownOpen
        }));
      }
    render() {
        return <Card className="FoodCategories">
            <CardBody>
                <CardTitle> Categories </CardTitle>
                <div className="d-flex justify-content-center p-5">
                <Dropdown isOpen={this.state.dropdownOpen} toggle={this.toggle}>
                    <DropdownToggle caret>
                        {this.props.active?this.props.active:"Categories"}
                    </DropdownToggle>

                    <DropdownMenu>
                        {this.props.categories.map(category => 
                            <DropdownItem onClick={()=>this.props.handleChange(category)}>{category}</DropdownItem> 
                        )}
                    </DropdownMenu>

                </Dropdown>
                </div>
            </CardBody>
        </Card>
    }

}

export default FoodCategories