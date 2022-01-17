import './App.css';

import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Home from "./pages/Home";
import AddService from "./components/Services/AddService";
import EditService from "./components/Services/EditService";
import Navbar from "./components/Navbar";
import NotFound from "./pages/NotFound";
import Service from "./components/Services/Service";

import 'mdb-react-ui-kit/dist/css/mdb.min.css'
import React from "react";

function App() {

    return (
        <Router>
            <div className="App">
                <Navbar/>
                <Routes>
                    <Route exact path="/" element={<Home />}/>
                    <Route exact path="/services/add" element={<AddService/>}/>
                    <Route exact path="/services/edit/:id" element={<EditService/>}/>
                    <Route exact path="/services/:id" element={<Service/>}/>
                    <Route element={<NotFound/>}/>
                </Routes>
            </div>

        </Router>
    );
}

export default App;
