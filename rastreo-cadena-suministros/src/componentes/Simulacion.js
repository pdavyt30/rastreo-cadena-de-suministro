import React from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './Simulacion.css';

const Simulacion = () => {
    const navigate = useNavigate();

    const detenerSimulacion = async () => {
        try {
            const response = await axios.post('http://localhost:8080/api/simulacion/detener');
            alert('Simulación detenida');
            navigate('/');
        } catch (error) {
            console.error('Error deteniendo la simulación', error);
            alert('Error deteniendo la simulación');
        }
    };

    return (
        <div className="simulacion-container">
            <h1>Simulación en Ejecución</h1>
            {/* Aquí se puede agregar la visualización de la simulación */}
            <button className="stop-button" onClick={detenerSimulacion}>
                Detener Simulación
            </button>
        </div>
    );
};

export default Simulacion;
