import React from 'react';
import { useNavigate } from 'react-router-dom';
import "../vistas/VistaInicio.css";
import imagenLogoUDE from '../../imagenes/imagenLogoUDE.png';

const VistaInicio = ({ abrirModal }) => {
    const navigate = useNavigate();

    const handleIniciarSimulador = () => {
        navigate('/cadena');
    };

    return (
        <div className="contenedor-principal">
            <header className="header-inicio">
                <img src={imagenLogoUDE} alt="Logo UDE" className="logo-imagen" />
                <h1 className="titulo">Rastreo de Cadena de Suministro</h1>
            </header>
            <div className="vista-inicio">
                <div className="boton-columna">
                    <button className="boton-grande" onClick={() => abrirModal('informacion')}>
                        Información
                    </button>
                </div>            
                <div className="boton-columna">
                    <button className="boton-grande" onClick={handleIniciarSimulador}>
                        Iniciar Simulador
                    </button>
                </div>
                <div className="boton-columna">
                    <button className="boton-grande" onClick={() => abrirModal('tutorial')}>
                        Tutorial
                    </button>
                </div>
            </div>
        </div>
    );
};

export default VistaInicio;
