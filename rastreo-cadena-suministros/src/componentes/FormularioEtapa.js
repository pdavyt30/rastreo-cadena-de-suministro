import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';
import './FormularioEtapa.css';

const FormularioEtapa = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const { tipo, tipoAbastecimiento } = location.state || {}; 

    const initialData = {
        tiempoProduccionAbastecimiento: '',
        capacidadMaximaAbastecimiento: '',
        periodoExpedicionAbastecimiento: '',
        tiempoFabricacionProducto: '',
        unidadesPorProducto: '',
        capacidadMaximaAbastecimientoProduccion: '',
        capacidadMaximaProductosProduccion: '',
        periodoExpedicionProduccion: '',
        capacidadMaximaProductosAlmacenamiento: '',
        periodoCompras: '',
        tipoAbastecimiento, 
    };

    const [etapaData, setEtapaData] = useState(initialData);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const url =
                    tipo === 'abastecimiento'
                        ? `http://localhost:8080/api/abastecimiento/listar?tipoAbastecimiento=${tipoAbastecimiento}`
                        : `http://localhost:8080/api/${tipo}/listar`;

                const response = await axios.get(url);
                if (response.data.length > 0) {
                    setEtapaData(response.data[0]);
                }
            } catch (error) {
                console.error('Error obteniendo los datos de la etapa:', error);
            }
        };

        fetchData();
    }, [tipo, tipoAbastecimiento]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setEtapaData({ ...etapaData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const url =
                tipo === 'abastecimiento'
                    ? `http://localhost:8080/api/abastecimiento/guardar?tipoAbastecimiento=${tipoAbastecimiento}`
                    : `http://localhost:8080/api/${tipo}/guardar`;

            await axios.post(url, etapaData);
            alert('Etapa guardada con éxito.');
            navigate('/cadena');
        } catch (error) {
            console.error('Error guardando la etapa:', error);
            alert('Error guardando la etapa.');
        }
    };

    const renderFields = () => {
        switch (tipo) {
            case 'abastecimiento':
                return (
                    <>
                        <label>
                            Tiempo de Producción (minutos):
                            <input
                                type="number"
                                name="tiempoProduccionAbastecimiento"
                                value={etapaData.tiempoProduccionAbastecimiento || ''}
                                onChange={handleChange}
                                required
                            />
                        </label>
                        <label>
                            Capacidad Máxima de Abastecimiento:
                            <input
                                type="number"
                                name="capacidadMaximaAbastecimiento"
                                value={etapaData.capacidadMaximaAbastecimiento || ''}
                                onChange={handleChange}
                                required
                            />
                        </label>
                        <label>
                            Periodo entre Tandas de Expedición (minutos):
                            <input
                                type="number"
                                name="periodoExpedicionAbastecimiento"
                                value={etapaData.periodoExpedicionAbastecimiento || ''}
                                onChange={handleChange}
                                required
                            />
                        </label>
                    </>
                );
            case 'produccion':
                return (
                    <>
                        <label>
                            Unidades por Producto:
                            <input
                                type="number"
                                name="unidadesPorProducto"
                                value={etapaData.unidadesPorProducto || ''}
                                onChange={handleChange}
                                required
                            />
                        </label>
                        <label>
                            Tiempo de Fabricación de Producto (minutos):
                            <input
                                type="number"
                                name="tiempoFabricacionProducto"
                                value={etapaData.tiempoFabricacionProducto || ''}
                                onChange={handleChange}
                                required
                            />
                        </label>
                        <label>
                            Capacidad Máxima de Abastecimiento en Producción:
                            <input
                                type="number"
                                name="capacidadMaximaAbastecimientoProduccion"
                                value={etapaData.capacidadMaximaAbastecimientoProduccion || ''}
                                onChange={handleChange}
                                required
                            />
                        </label>
                        <label>
                            Capacidad Máxima de Productos en Producción:
                            <input
                                type="number"
                                name="capacidadMaximaProductosProduccion"
                                value={etapaData.capacidadMaximaProductosProduccion || ''}
                                onChange={handleChange}
                                required
                            />
                        </label>
                        <label>
                            Periodo entre Expediciones en Producción (minutos):
                            <input
                                type="number"
                                name="periodoExpedicionProduccion"
                                value={etapaData.periodoExpedicionProduccion || ''}
                                onChange={handleChange}
                                required
                            />
                        </label>
                    </>
                );
            case 'almacenamiento':
                return (
                    <>
                        <label>
                            Capacidad Máxima de Productos en Almacenamiento:
                            <input
                                type="number"
                                name="capacidadMaximaProductosAlmacenamiento"
                                value={etapaData.capacidadMaximaProductosAlmacenamiento || ''}
                                onChange={handleChange}
                                required
                            />
                        </label>
                        <label>
                            Periodo de Compras (minutos):
                            <input
                                type="number"
                                name="periodoCompras"
                                value={etapaData.periodoCompras || ''}
                                onChange={handleChange}
                                required
                            />
                        </label>
                    </>
                );
            default:
                return null;
        }
    };

    return (
        <div className="container">
            <form onSubmit={handleSubmit}>
            <h2>
                {tipo === 'abastecimiento' && tipoAbastecimiento === 2
                    ? 'Editar Abastecimiento 2'
                    : tipo === 'abastecimiento'
                    ? 'Editar Abastecimiento'
                    : tipo === 'produccion'
                    ? 'Editar Producción'
                    : 'Editar Almacenamiento'}
            </h2>
                {renderFields()}
                <button type="submit">Guardar Etapa</button>
            </form>
        </div>
    );
};

export default FormularioEtapa;
