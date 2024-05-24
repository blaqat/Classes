import React, { Component } from 'react';
import './courses_display.css';
import {Table, Button, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader, Container, Row, Col} from 'reactstrap';

class CoursesDisplay extends Component{
    constructor(props){
        super(props);
        this.state={
            showModal: false,
            course_data: {},
            page_data: {},
        }
    }

    updateData = (apiResponse) => {
        this.setState({page_data: apiResponse})
    }

    fetchData = () => {
        //In package.json add "proxy": "http://localhost:5000" 
        //This will allow redirect to REST api in Flask w/o CORS errors
        console.log("HELLO HELP PLEASE")
         fetch('/courses')
         .then(
             response => response.json() 
             )//The promise response is returned, then we extract the json data
         .then (jsonOutput => //jsonOutput now has result of the data extraction
                  {
                      this.updateData(jsonOutput)
                    }
              )
    }


    componentDidMount(){
        this.fetchData();
    }
    
    // Removes a course from the DB
    delete = (course) => {
        fetch('/courses/' + course['id'], {method: 'DELETE'})
        this.fetchData()
        window.location.reload(false);
    }

    // Edits a course in the DB
    edit = (course) => {
        fetch('/courses/' + course['id'], {method: 'PUT', headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({course_number: course['num'], course_title: course['title'], course_details: course['details']})
        })
        this.setState({showModal:false, course_data:[]})
    }

    // Closes the Modal
    close = ()=>{this.fetchData(); this.setState({showModal:false, course_data:[]})}


    render(){
        {/*If the data has not yet been loaded from the server, return empty page */}
        if ( this.state.page_data == null )
        return (<div>No data</div>)
        else
        {

        // Writes the courses in DB as a table
        let course_data = Object.values(this.state.page_data).map((e) => (
            <tr> 
            <th><Button onClick={() => (this.setState({showModal: true, course_data:e}))}>Edit</Button></th>
            <th><Button onClick={() => this.delete(e)}>Delete</Button></th>
            <th>{e['id']}</th>
            <th>{e['department']}</th>
            <th>{e['num']}</th>
            <th>{e['title']}</th>
            <th>{e['details']}</th>
            </tr>
        ));
        let active_course = this.state.course_data

        return (
            <Table bordered striped>

            <thead className="Header"> <tr>
                <th></th>
                <th></th>
                <th scope="row">ID</th>
                <th>Dept</th>
                <th>Class</th>
                <th>Title</th>
                <th>Details</th>
            </tr></thead>

            <tbody>
            {course_data}
            </tbody>

            <Modal isOpen={this.state.showModal}>

                    <ModalHeader toggle={this.close}>
                        {active_course['department']}<br/>{active_course['college']}
                    </ModalHeader>

                    <ModalBody>
                        <Container>
                        <Label for="course">Course</Label>
                        <Input id="course" type='text' defaultValue={active_course['num']} onChange={(e) => (active_course['num'] = e.target.value)}></Input>
                        <Label for="title">Title</Label>
                        <Input id="title" type='text' defaultValue={active_course['title']} onChange={(e) => (active_course['title'] = e.target.value)}></Input>
                        <Label for="det">Details</Label>
                        <Input id="det" type='text' defaultValue={active_course['details']} onChange={(e) => (active_course['details'] = e.target.value)}></Input>
                        </Container>
                    </ModalBody>

                    <ModalFooter>
                        <Button onClick={()=>this.edit(active_course)}>Save</Button>
                        <Button onClick={this.close}>Cancel</Button>
                    </ModalFooter>

                </Modal>
           </Table>
           
        )
        }
    }
}

export default CoursesDisplay;