import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './VistaSimulacion.css';

const VistaSimulacion = () => {
    const navigate = useNavigate();
    const [estado, setEstado] = useState({});
    const [alerta, setAlerta] = useState('');

    useEffect(() => {
        const fetchEstado = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/simulacion/estado');
                setEstado(response.data);
                setAlerta(response.data.alerta || '');
            } catch (error) {
                console.error('Error fetching estado de la simulación', error);
            }
        };

        const interval = setInterval(fetchEstado, 1000);

        return () => clearInterval(interval);
    }, []);

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
            <h1 className="titulo">Simulación en Ejecución</h1>
            <div className="etapa-container">
                <div className="linea"></div>
                <div className="etapa">
                    <h2>Abastecimiento</h2>
                    <div className="contenido-etapa">
                        {Array.from({ length: estado.unidadesAbastecimiento || 0 }).map((_, idx) => (
                            <div key={idx} className="unidad-abastecimiento"></div>
                        ))}
                    </div>
                    {alerta && <div className="alerta">{alerta}</div>}
                </div>
                <div className="etapa">
                    <h2>Producción</h2>
                    {/* Aquí se añadirá la lógica de la etapa de producción */}
                </div>
                <div className="etapa">
                    <h2>Almacenamiento</h2>
                    {/* Aquí se añadirá la lógica de la etapa de almacenamiento */}
                </div>
            </div>
            <button className="stop-button" onClick={detenerSimulacion}>
                Detener Simulación
            </button>
        </div>
    );
};

export default VistaSimulacion;
