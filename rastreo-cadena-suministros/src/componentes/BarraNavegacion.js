import React from 'react';
import { Link } from 'react-router-dom';
import './BarraNavegacion.css';

const BarraNavegacion = ({ setDificultad }) => {
    const handleSelectChange = (e) => {
        setDificultad(e.target.value);
    };

    return (
        <nav className="navbar">
            <ul className="navbar-list">
                <li className="navbar-item"><Link to="/" className="navbar-link">Inicio</Link></li>
                <li className="navbar-item"><button className="navbar-button" onClick={() => alert('Información sobre la cadena de suministro')}>Información</button></li>
                <li className="navbar-item"><button className="navbar-button" onClick={() => alert('Tutorial de uso del simulador')}>Tutorial</button></li>
                <li className="navbar-item">
                    <select className="navbar-select" onChange={handleSelectChange}>
                        <option value="principiante">Principiante</option>
                        <option value="avanzado">Avanzado</option>
                    </select>
                </li>
            </ul>
        </nav>
    );
};

export default BarraNavegacion;
