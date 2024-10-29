import React from 'react';
import { useNavigate } from 'react-router-dom';
import "../vistas/VistaInicio.css";

const VistaInicio = ({ abrirModal }) => {
    const navigate = useNavigate();

    const handleIniciarSimulador = () => {
        navigate('/cadena');
    };

    return (
        <div className="vista-inicio">
            <div className="boton-columna">
                <button className="boton-grande" onClick={() => abrirModal('informacion')}>
                    Información
                </button>
            </div>
            <div className="boton-columna">
                <button className="boton-grande" onClick={() => abrirModal('tutorial')}>
                    Tutorial
                </button>
            </div>
            <div className="boton-columna">
                <button className="boton-grande" onClick={handleIniciarSimulador}>
                    Iniciar Simulador
                </button>
            </div>
        </div>
    );
};

export default VistaInicio;
