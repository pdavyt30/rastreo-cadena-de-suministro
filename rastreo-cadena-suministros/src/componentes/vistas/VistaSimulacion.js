import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import "../vistas/VistaSimulacion.css";

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
            navigate('/cadena');
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
        <div className="simulacion-contenedor-principal">
            <h1 className={`simulacion-titulo-${dificultad}`}>Simulación en Ejecución</h1>

            <div className="simulacion-container">
                <div className="simulacion-contenedor-abastecimientos">
                    <div className="simulacion-fila">
                        <section className={`abastecimiento-etapa-${dificultad}`}>
                            <h2 className='simulacion-h2'>Abastecimiento ({estado.unidadesAbastecimientoAba || 0})</h2>
                            <div className="simulacion-contenido-etapa">
                                {Array.from({ length: estado.unidadesAbastecimientoAba || 0 }).map((_, idx) => (
                                    <div key={idx} className="simulacion-unidad-abastecimiento"></div>
                                ))}
                            </div>
                            {alertas.alertaAbastecimiento && (
                                <div className="simulacion-alerta">{alertas.alertaAbastecimiento}</div>
                            )}
                        </section>
                        <div className="simulacion-transicion">
                            <div className="simulacion-contenido-transicion">
                                {Array.from({ length: estado.unidadesEnTransicion || 0 }).map((_, idx) => (
                                    <div key={idx} className="simulacion-unidad-abastecimiento"></div>
                                ))}
                            </div>
                            <h3>----------------------------</h3>
                        </div>
                    </div>

                    {dificultad === 'avanzado' && (
                        <div className="simulacion-fila">
                            <section className={`abastecimiento-etapa-${dificultad}`}>
                                <h2 className='simulacion-h2'>Abastecimiento 2 ({estado.unidadesAbastecimientoAba2 || 0})</h2>
                                <div className="simulacion-contenido-etapa">
                                    {Array.from({ length: estado.unidadesAbastecimientoAba2 || 0 }).map((_, idx) => (
                                        <div key={idx} className="simulacion-unidad-abastecimiento"></div>
                                    ))}
                                </div>
                                {alertas.alertaAbastecimiento2 && (
                                    <div className="simulacion-alerta">{alertas.alertaAbastecimiento2}</div>
                                )}
                            </section>
                            <div className="simulacion-transicion">
                                <div className="simulacion-contenido-transicion">
                                    {Array.from({ length: estado.unidadesEnTransicion2 || 0 }).map((_, idx) => (
                                        <div key={idx} className="simulacion-unidad-abastecimiento"></div>
                                    ))}
                                </div>
                                <h3>----------------------------</h3>
                            </div>
                        </div>
                    )}
                </div>

                <div className="simulacion-contenedor-produccion-almacenamiento">
                    <article className="produccion-etapa">
                        <h2 className='simulacion-h2'>Producción (UA: {estado.unidadesAbastecimientoProduccion || 0}, P: {estado.unidadesProductosProd || 0})</h2>
                        <div className="simulacion-contenido-etapa">
                            {Array.from({ length: estado.unidadesAbastecimientoProduccion || 0 }).map((_, idx) => (
                                <div key={idx} className="simulacion-unidad-abastecimiento"></div>
                            ))}
                        </div>
                        <div className="simulacion-contenido-etapa">
                            {Array.from({ length: estado.unidadesProductosProd || 0 }).map((_, idx) => (
                                <div key={idx} className="simulacion-unidad-produccion"></div>
                            ))}
                        </div>
                        {alertas.alertaProduccion && <div className="simulacion-alerta">{alertas.alertaProduccion}</div>}
                    </article>

                    <div className="simulacion-transicion-vertical">
                        <div className="simulacion-contenido-transicion">
                            {Array.from({ length: estado.productosEnTransicion || 0 }).map((_, idx) => (
                                <div key={idx} className="simulacion-unidad-produccion"></div>
                            ))}
                        </div>
                        <h3>---------------------------------</h3>
                    </div>

                    <article className="almacenamiento-etapa">
                        <h2 className='simulacion-h2'>Almacenamiento ({estado.unidadesProductosAlma || 0})</h2>
                        <div className="simulacion-contenido-etapa">
                            {Array.from({ length: estado.unidadesProductosAlma || 0 }).map((_, idx) => (
                                <div key={idx} className="simulacion-unidad-produccion"></div>
                            ))}
                        </div>
                        {alertas.alertaAlmacenamiento && <div className="simulacion-alerta">{alertas.alertaAlmacenamiento}</div>}
                    </article>
                </div>
            </div>

            <div className={`simulacion-button-container-${dificultad}`}>
                <button className="simulacion-stop-button" onClick={detenerSimulacion}>Detener Simulación</button>
                <button className="simulacion-report-button" onClick={generarReporte}>Generar Reporte</button>
            </div>
        </div>
    );
};

export default VistaSimulacion;
