import React from "react";
import {Link} from "react-router-dom";

const Navbar = () => {
    return (
        // <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
        <nav className="navbar navbar-expand-lg navbar-light">
            <div className="container">
                <Link className="navbar-brand" to="/">
                    Kry Service Poller
                </Link>

                {/*<MDBInput label="Username" group type="email" validate error="wrong" success="right"/>*/}
                {/*<MDBInput label="Password" group type="password" validate containerClass="mb-0"/>*/}

                <button
                    className="navbar-toggler"
                    type="button"
                    data-toggle="collapse"
                    data-target="#navbarSupportedContent"
                    aria-controls="navbarSupportedContent"
                    aria-expanded="false"
                    aria-label="Toggle navigation"
                >
                    <span className="navbar-toggler-icon" />
                </button>

                {/*<Link className="btn btn-primary" to="/services/add">Sign In</Link>*/}
                {/*<Link className="btn btn-danger" to="/services/add">Log Out</Link>*/}

            </div>
        </nav>
    );
};

export default Navbar;