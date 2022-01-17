import React, {useState} from "react";
import axios from 'axios'
import {Link, useNavigate} from "react-router-dom";

const AddService = () => {
    let history = useNavigate();
    const [service, setService] = useState({
        name: "",
        url: ""
    });

    const [isValid, setValid] = useState(null);

    const { name, url } = service;
    // const isTrueVal = false;
    const onInputChange = e => {
        setService({ ...service, [e.target.name]: e.target.value });
    };

    const urlPatternValidation = URL => {
        const regex = new RegExp('(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?');
        return regex.test(URL);
    };

    const onSubmit = async e => {
        e.preventDefault();
        if(urlPatternValidation(service.url)){
            await axios.post("http://localhost:8080/services", service);
            history('/');
        }
        else {
            setValid(false);
        }


    };


    return (
        <div className="container pt-5">

            <div className="w-75 mx-auto shadow p-5">
                <Link className="btn btn-secondary" to="/">
                    back to Home
                </Link>
                <h2 className="text-center mb-4">Add A Service</h2>
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
                    <div className="form-group mb-2">
                        <input
                            type="url"
                            className={isValid != null ? isValid ? "form-control form-control-lg is-valid" : "form-control form-control-lg is-invalid" : "form-control form-control-lg"} //"form-control form-control-lg"
                            placeholder="Enter the url"
                            name="url"
                            value={url}
                            onChange={e => onInputChange(e)}
                            required
                        />
                    </div>
                    <button className="btn btn-primary btn-block">Add Service</button>
                </form>
            </div>
        </div>
    );
};

export default AddService;