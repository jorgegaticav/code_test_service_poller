import React, {useEffect, useState} from "react";
import axios from "axios";
import {Link, useNavigate, useParams} from "react-router-dom";

const EditService = () => {
    let history = useNavigate();
    const { id } = useParams();
    const [service, setService] = useState({
        name: "",
        url: "",
    });

    const [isValid, setValid] = useState(null)

    const { name, url, } = service;
    const onInputChange = e => {
        setService({ ...service, [e.target.name]: e.target.value });
    };

    const urlPatternValidation = URL => {
        const regex = new RegExp('(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?');
        return regex.test(URL);
    };

    useEffect(() => {
        loadService().then(result => setService(result.data));
    }, []);

    const onSubmit = async e => {
        e.preventDefault();

        if(urlPatternValidation(service.url)){
            await axios.put(`http://localhost:8080/services/${id}`, service);
            history('/');
        }
        else {
            setValid(false);
        }
    };

    const loadService = async () => {
        return await axios.get(`http://localhost:8080/services/${id}`);
    };
    return (
        <div className="container  pt-5">

            <div className="w-75 mx-auto shadow p-5">
                <Link className="btn btn-secondary" to="/">
                    back to Home
                </Link>
                <h2 className="text-center mb-4">Edit A Service</h2>
                <form onSubmit={e => onSubmit(e)}>
                    <div className="form-group mb-2">
                        <input
                            type="text"
                            className="form-control form-control-lg"
                            placeholder="Enter Your Name"
                            name="name"
                            value={name}
                            onChange={e => onInputChange(e)}
                            required
                        />
                    </div>
                    <div className="form-group  mb-2">
                        <input
                            type="text"
                            className={isValid != null ? isValid ? "form-control form-control-lg is-valid" : "form-control form-control-lg is-invalid" : "form-control form-control-lg"} //"form-control form-control-lg"
                            placeholder="Enter the url"
                            name="url"
                            value={url}
                            onChange={e => onInputChange(e)}
                            required
                        />
                    </div>
                    <button className="btn btn-warning btn-block">Update Service</button>
                </form>
            </div>
        </div>
    );
};

export default EditService;