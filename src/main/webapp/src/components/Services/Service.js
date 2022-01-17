import React, {useEffect, useState} from "react";
import {Link, useParams} from "react-router-dom";
import axios from "axios";

const Service = () => {
    const [user, setService] = useState({
        name: "",
        url: "",
        status: "",
        createdDate: "",
        lastUpdated: ""
    });
    const { id } = useParams();
    useEffect(() => {
        loadService().then(res => setService(res.data));
    }, []);
    const loadService = async () => {
        return await axios.get(`http://localhost:8080/services/${id}`);
    };
    return (
        <div className="container pt-4">
            <Link className="btn btn-secondary" to="/">
                back to Home
            </Link>
            <h1 className="display-4 pt-4">User Id: {id}</h1>
            <hr />
            <ul className="list-group w-50">
                <li className="list-group-item">name: {user.name}</li>
                <li className="list-group-item">url: {user.url}</li>
                <li className="list-group-item">status: {user.status}</li>
                <li className="list-group-item">created date: {user.createdDate}</li>
                <li className="list-group-item">last updated: {user.lastUpdated}</li>
            </ul>
        </div>
    );
};

export default Service;