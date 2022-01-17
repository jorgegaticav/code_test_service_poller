import React, {Component} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import {MDBCol, MDBRow, MDBTable, MDBTableBody, MDBTableHead} from "mdb-react-ui-kit";

// import {w3cwebsocket as W3CWebSocket} from 'websocket';

class Home extends Component {

    _isMounted = false;
    /* WebSocket Client*/
    // client = new W3CWebSocket("ws://localhost:8000");

    state = {
        services: [],
        delay: 8000,
        pollingCount: 0
    }

    loadServices() {
        return axios.get("http://localhost:8080/services");
    }


    refresh(){
        if (this._isMounted) {
            console.log("polling... " + this.state.pollingCount);
            this.loadServices().then(res => {
                this.setState({
                    services: res.data,
                    pollingCount: this.state.pollingCount + 1
                });
            })
        }
    }

    constructor(props, context) {
        super(props, context);
        this.loadServices = this.loadServices.bind(this);
        this.refresh = this.refresh.bind(this);
    }

    componentDidMount() {
        this._isMounted = true;

        console.log("First polling... " + this.state.pollingCount);
        this.loadServices().then(res => {
            this.setState({
                services: res.data,
                pollingCount: this.state.pollingCount + 1
            });
        })

        /* Polling Service */
        this.interval = setInterval(this.refresh, this.state.delay)

        /// WebSockets
        // this.client.onopen = () => {
        //     console.log('WebSocket Client Connected');
        // };
        //
        // this.client.onmessage = async (message) => {
        //
        //     console.log(message);
        //     this.loadServices().then(res => {
        //         this.setState({
        //             services: res.data,
        //         });
        //     })
        //
        // };
    }

    componentWillUnmount() {
        this._isMounted = false;
    }

    async deleteService(id) {
        await axios.delete(`http://localhost:8080/services/${id}`);
        this.loadServices().then(result => this.state.services = result.data.reverse());
    };


    closeClient(){
        this._isMounted = false;
        /* Close WebSocket client when switching page */
        // this.client.close();
    }

    render() {
        return (
            <>
                <MDBRow>
                    <MDBCol>

                    </MDBCol>
                </MDBRow>
                <div className="container pt-4">
                    <Link className="btn btn-secondary"
                          to="/services/add"
                          onClick={this.closeClient}
                    >Add Service</Link>
                </div>

                <div className="container">
                    <div className="py-4">
                        {/*<h1>Home Page</h1>*/}
                        <MDBTable striped>
                            <MDBTableHead>
                                <tr>
                                    {/*<th scope="col">#</th>*/}
                                    <th scope="col">Name</th>
                                    <th scope="col">URL</th>
                                    <th scope="col">Status</th>
                                    <th scope="col">Creation Date</th>
                                    <th scope="col">Last Update</th>
                                    <th colSpan={3}>Action</th>
                                </tr>
                            </MDBTableHead>
                            <MDBTableBody>
                                {this.state.services.map((service) => (
                                    <tr key={service.id}>
                                        {/*<th scope="row">{index + 1}</th>*/}
                                        <td>{service.name}</td>
                                        <td ><a href={service.url} target="_blank" rel="noopener">{service.url}</a></td>
                                        <td>{service.status}</td>
                                        <td>{service.createdDate}</td>
                                        <td>{service.lastUpdated}</td>
                                        <td>
                                            <Link className="btn btn-primary mr-2" to={`/services/${service.id}`}>
                                                View
                                            </Link>
                                        </td>
                                        <td>
                                            <Link
                                                className="btn btn-light mr-2"
                                                to={`/services/edit/${service.id}`}
                                                onClick={this.closeClient}
                                            >
                                                Edit
                                            </Link>
                                        </td>
                                        <td>
                                            <Link
                                                className="btn btn-danger"
                                                onClick={() => this.deleteService(service.id)}
                                                to={"/"}
                                            >
                                                Delete
                                            </Link>
                                        </td>
                                    </tr>
                                ))}
                            </MDBTableBody>
                        </MDBTable>
                    </div>
                </div>
                {/*{ this.state.isBusy ? <div className="d-flex justify-content-center">*/}
                {/*    <div className="spinner-border" role="status">*/}
                {/*        <span className="visually-hidden">Loading...</span>*/}
                {/*    </div>*/}
                {/*</div>: null}*/}
            </>

        );
    }

}

export default Home;