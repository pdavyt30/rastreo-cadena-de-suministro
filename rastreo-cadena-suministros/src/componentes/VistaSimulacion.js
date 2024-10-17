import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './VistaSimulacion.css';

const VistaSimulacion = () => {
    const navigate = useNavigate();
    const [estado, setEstado] = useState({});
    const [alertas, setAlertas] = useState({
        alertaAbastecimiento: '',
        alertaAbastecimiento2: '',
        alertaProduccion: '',
        alertaAlmacenamiento: ''
    });
    const [dificultad, setDificultad] = useState('principiante');

    useEffect(() => {
        const dificultadAlmacenada = localStorage.getItem('dificultad') || 'principiante';
        setDificultad(dificultadAlmacenada);

        const fetchEstado = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/simulacion/estado');
                setEstado(response.data);
                setAlertas({
                    alertaAbastecimiento: response.data.alertaAbastecimiento || '',
                    alertaAbastecimiento2: response.data.alertaAbastecimiento2 || '',
                    alertaProduccion: response.data.alertaProduccion || '',
                    alertaAlmacenamiento: response.data.alertaAlmacenamiento || ''
                });
            } catch (error) {
                console.error('Error obteniendo el estado de la simulación', error);
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
        <div className="contenedor-principal">
            <h1 className="titulo">Simulación en Ejecución</h1>

            <div className="simulacion-container">
                <div className="contenedor-abastecimientos">
                    <div className="fila">
                        <div className="etapa">
                            <h2>Abastecimiento ({estado.unidadesAbastecimientoAba || 0})</h2>
                            <div className="contenido-etapa">
                                {Array.from({ length: estado.unidadesAbastecimientoAba || 0 }).map((_, idx) => (
                                    <div key={idx} className="unidad-abastecimiento"></div>
                                ))}
                            </div>
                            {alertas.alertaAbastecimiento && (
                                <div className="alerta">{alertas.alertaAbastecimiento}</div>
                            )}
                        </div>
                        <div className="transicion">
                            <h3>Transición</h3>
                            <div className="contenido-transicion">
                                {Array.from({ length: estado.unidadesEnTransicion || 0 }).map((_, idx) => (
                                    <div key={idx} className="unidad-abastecimiento"></div>
                                ))}
                            </div>
                        </div>
                    </div>

                    {dificultad === 'avanzado' && (
                        <div className="fila">
                            <div className="etapa">
                                <h2>Abastecimiento 2 ({estado.unidadesAbastecimientoAba2 || 0})</h2>
                                <div className="contenido-etapa">
                                    {Array.from({ length: estado.unidadesAbastecimientoAba2 || 0 }).map((_, idx) => (
                                        <div key={idx} className="unidad-abastecimiento"></div>
                                    ))}
                                </div>
                                {alertas.alertaAbastecimiento2 && (
                                    <div className="alerta">{alertas.alertaAbastecimiento2}</div>
                                )}
                            </div>
                            <div className="transicion">
                                <h3>Transición</h3>
                                <div className="contenido-transicion">
                                    {Array.from({ length: estado.unidadesEnTransicion2 || 0 }).map((_, idx) => (
                                        <div key={idx} className="unidad-abastecimiento"></div>
                                    ))}
                                </div>
                            </div>
                        </div>
                    )}
                </div>

                <div className="contenedor-produccion-almacenamiento">
                    <div className="etapa">
                        <h2>Producción (UA: {estado.unidadesAbastecimientoProduccion || 0}, P: {estado.unidadesProductosProd || 0})</h2>
                        <div className="contenido-etapa">
                            {Array.from({ length: estado.unidadesAbastecimientoProduccion || 0 }).map((_, idx) => (
                                <div key={idx} className="unidad-abastecimiento"></div>
                            ))}
                            {Array.from({ length: estado.unidadesProductosProd || 0 }).map((_, idx) => (
                                <div key={idx} className="unidad-produccion"></div>
                            ))}
                        </div>
                        {alertas.alertaProduccion && <div className="alerta">{alertas.alertaProduccion}</div>}
                    </div>

                    <div className="transicion vertical">
                        <h3>Transición</h3>
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
                        {alertas.alertaAlmacenamiento && <div className="alerta">{alertas.alertaAlmacenamiento}</div>}
                    </div>
                </div>
            </div>

            <div className="button-container">
                <button className="stop-button" onClick={detenerSimulacion}>Detener Simulación</button>
                <button className="report-button" onClick={generarReporte}>Generar Reporte</button>
            </div>
        </div>
    );
};

export default VistaSimulacion;
