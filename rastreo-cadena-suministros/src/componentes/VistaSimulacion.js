import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './VistaSimulacion.css';

const VistaSimulacion = () => {
    const navigate = useNavigate();
    const [estado, setEstado] = useState({});
    const [alertaAbastecimiento, setAlertaAbastecimiento] = useState('');
    const [alertaProduccion, setAlertaProduccion] = useState('');
    const [alertaAlmacenamiento, setAlertaAlmacenamiento] = useState('');
    const [alertaAbastecimiento2, setAlertaAbastecimiento2] = useState(''); // Alerta para Abastecimiento 2
    const [dificultad, setDificultad] = useState('principiante'); // Dificultad

    useEffect(() => {
        // Obtener la dificultad desde localStorage
        const dificultadAlmacenada = localStorage.getItem('dificultad') || 'principiante';
        setDificultad(dificultadAlmacenada);

        const fetchEstado = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/simulacion/estado');
                setEstado(response.data);
                setAlertaAbastecimiento(response.data.alertaAbastecimiento || '');
                setAlertaProduccion(response.data.alertaProduccion || '');
                setAlertaAlmacenamiento(response.data.alertaAlmacenamiento || '');
                if (dificultadAlmacenada === 'avanzado') {
                    setAlertaAbastecimiento2(response.data.alertaAbastecimiento2 || '');
                }
            } catch (error) {
                console.error('Error fetching estado de la simulación', error);
            }
        };

        const interval = setInterval(fetchEstado, 1000);

        return () => clearInterval(interval);
    }, []);

    const detenerSimulacion = async () => {
        try {
            await axios.post('http://localhost:8080/api/simulacion/detener');
            alert('Simulación detenida');
            navigate('/');
        } catch (error) {
            console.error('Error deteniendo la simulación', error);
            alert('Error deteniendo la simulación');
        }
    };

    const generarReporte = async () => {
        try {
            const response = await axios({
                url: 'http://localhost:8080/api/simulacion/reporte',
                method: 'GET',
                responseType: 'blob',
            });

            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'reporte_simulacion.txt');
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        } catch (error) {
            console.error('Error generando el reporte', error);
            alert('Error generando el reporte');
        }
    };

    return (
        <div className="simulacion-container">
            <h1 className="titulo">Simulación en Ejecución</h1>
            <div className="etapa-container">
                <div className="etapa">
                    <h2>Abastecimiento ({estado.unidadesAbastecimientoAba || 0})</h2>
                    <div className="contenido-etapa">
                        {Array.from({ length: estado.unidadesAbastecimientoAba || 0 }).map((_, idx) => (
                            <div key={idx} className="unidad-abastecimiento"></div>
                        ))}
                    </div>
                    {alertaAbastecimiento && <div className="alerta">{alertaAbastecimiento}</div>}
                </div>

                {/* Condicional: Mostrar Abastecimiento 2 si la dificultad es avanzado */}
                {dificultad === 'avanzado' && (
                    <>
                        <div className="etapa">
                            <h2>Abastecimiento 2 ({estado.unidadesAbastecimiento2 || 0})</h2>
                            <div className="contenido-etapa">
                                {Array.from({ length: estado.unidadesAbastecimiento2 || 0 }).map((_, idx) => (
                                    <div key={idx} className="unidad-abastecimiento"></div>
                                ))}
                            </div>
                            {alertaAbastecimiento2 && <div className="alerta">{alertaAbastecimiento2}</div>}
                        </div>

                        {/* Transición Abastecimiento 2 -> Producción */}
                        <div className="transicion">
                            <h3>Transición (Abastecimiento 2 - Producción)</h3>
                            <div className="contenido-transicion">
                                {Array.from({ length: estado.unidadesEnTransicion2 || 0 }).map((_, idx) => (
                                    <div key={idx} className="unidad-abastecimiento"></div>
                                ))}
                            </div>
                        </div>
                    </>
                )}

                {/* Transición Abastecimiento -> Producción */}
                <div className="transicion">
                    <h3>Transición (Abastecimiento - Producción)</h3>
                    <div className="contenido-transicion">
                        {Array.from({ length: estado.unidadesEnTransicion || 0 }).map((_, idx) => (
                            <div key={idx} className="unidad-abastecimiento"></div>
                        ))}
                    </div>
                </div>

                <div className="etapa">
                    <h2>Producción (UA: {estado.unidadesAbastecimientoProduccion || 0}, P: {estado.unidadesProductosProd || 0})</h2>
                    <div className="contenido-etapa">
                        {Array.from({ length: estado.unidadesAbastecimientoProduccion || 0 }).map((_, idx) => (
                            <div key={idx} className="unidad-abastecimiento-produccion"></div>
                        ))}
                        {Array.from({ length: estado.unidadesProductosProd || 0 }).map((_, idx) => (
                            <div key={idx} className="unidad-produccion"></div>
                        ))}
                    </div>
                    {alertaProduccion && <div className="alerta">{alertaProduccion}</div>}
                </div>

                {/* Transición Producción -> Almacenamiento */}
                <div className="transicion">
                    <h3>Transición (Producción - Almacenamiento)</h3>
                    <div className="contenido-transicion">
                        {Array.from({ length: estado.productosEnTransicion || 0 }).map((_, idx) => (
                            <div key={idx} className="unidad-produccion"></div>
                        ))}
                    </div>
                </div>

                <div className="etapa">
                    <h2>Almacenamiento ({estado.unidadesProductosAlma || 0})</h2>
                    <div className="contenido-etapa">
                        {Array.from({ length: estado.unidadesProductosAlma || 0 }).map((_, idx) => (
                            <div key={idx} className="unidad-produccion"></div>
                        ))}
                    </div>
                    {alertaAlmacenamiento && <div className="alerta">{alertaAlmacenamiento}</div>}
                </div>
            </div>

            <div className="button-container">
                <button className="stop-button" onClick={detenerSimulacion}>
                    Detener Simulación
                </button>
                <button className="report-button" onClick={generarReporte}>
                    Generar Reporte
                </button>
            </div>
        </div>
    );
};

export default VistaSimulacion;
