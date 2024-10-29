import React, { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import './BarraNavegacion.css';

const BarraNavegacion = ({ setDificultad, abrirModal }) => {
    const location = useLocation();
    const [dificultadSeleccionada, setDificultadSeleccionada] = useState(
        localStorage.getItem('dificultad') || 'principiante'
    );

    useEffect(() => {
        setDificultad(dificultadSeleccionada); // Sincroniza con el estado principal
    }, [dificultadSeleccionada, setDificultad]);

    const handleSelectChange = (e) => {
        const selectedDifficulty = e.target.value;
        setDificultadSeleccionada(selectedDifficulty); // Sincroniza con el estado local
        localStorage.setItem('dificultad', selectedDifficulty); // Guarda en localStorage
        setDificultad(selectedDifficulty); // Propaga el cambio
    };

    return (
        <nav className="navbar">
            <ul className="navbar-list">
                {location.pathname === '/configurar-etapa' && (
                    <li className="navbar-item">
                        <Link to="/cadena" className="navbar-link">Volver a Cadena</Link>
                    </li>
                )}
                <li className="navbar-item">
                    <Link to="/" className="navbar-link">Inicio</Link>
                </li>
                {location.pathname !== '/' && (
                    <>
                        <li className="navbar-item">
                            <button
                                className="navbar-button"
                                onClick={() => abrirModal('informacion')}
                            >
                                Información
                            </button>
                        </li>
                        <li className="navbar-item">
                            <button
                                className="navbar-button"
                                onClick={() => abrirModal('tutorial')}
                            >
                                Tutorial
                            </button>
                        </li>
                    </>
                )}
                <li className="navbar-item">
                    <select
                        className="navbar-select"
                        value={dificultadSeleccionada}
                        onChange={handleSelectChange}
                    >
                        <option value="principiante">Principiante</option>
                        <option value="avanzado">Avanzado</option>
                    </select>
                </li>
            </ul>
        </nav>
    );
};

export default BarraNavegacion;
