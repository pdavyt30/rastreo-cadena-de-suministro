import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';
import './FormularioEtapa.css';

const FormularioEtapa = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const { tipo, etapa } = location.state || {};

    const initialData = {
        nombre: '',
        tipo: tipo,
        tiempoProduccionAbastecimiento: '',
        tiempoProduccionAbastecimiento2: '',
        capacidadMaximaAbastecimiento: '',
        capacidadMaximaAbastecimiento2: '',
        periodoExpedicionAbastecimiento: '',
        periodoExpedicionAbastecimiento2: '',
        tiempoFabricacionProducto: '',
        capacidadMaximaAbastecimientoProduccion: '', 
        capacidadMaximaProductosProduccion: '', 
        capacidadMaximaProductosAlmacenamiento: '', 
        periodoExpedicionProduccion: '', 
        unidadesPorProducto: '',
        periodoCompras: ''
    };

    const [etapaData, setEtapaData] = useState(etapa || initialData);

    // Función para validar valores negativos y fraccionarios
    const validarValor = (valor) => {
        return valor > 0 && Number.isInteger(Number(valor)); // Debe ser un número positivo y entero
    };

    const handleChange = (e) => {
        const { name, value } = e.target;

        if (validarValor(value)) {
            setEtapaData({ ...etapaData, [name]: value });
        } else {
            alert('Por favor ingrese un número entero positivo.');
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Validar que el tiempo de expedición no sea menor al tiempo de producción
        if (tipo === 'abastecimiento' && Number(etapaData.periodoExpedicionAbastecimiento) < Number(etapaData.tiempoProduccionAbastecimiento)) {
            alert('El periodo de expedición no puede ser menor que el tiempo de producción en Abastecimiento.');
            return;
        }

        if (tipo === 'abastecimiento2' && Number(etapaData.periodoExpedicionAbastecimiento2) < Number(etapaData.tiempoProduccionAbastecimiento2)) {
            alert('El periodo de expedición no puede ser menor que el tiempo de producción en Abastecimiento 2.');
            return;
        }

        if (tipo === 'produccion' && Number(etapaData.periodoExpedicionProduccion) < Number(etapaData.tiempoFabricacionProducto)) {
            alert('El periodo de expedición no puede ser menor que el tiempo de producción en Producción.');
            return;
        }

        try {
            const response = await axios.post(`http://localhost:8080/api/${tipo}/guardar`, etapaData);
            alert(`Etapa guardada: ${JSON.stringify(response.data)}`);
            navigate('/');
        } catch (error) {
            console.error('Error guardando la etapa', error);
            alert('Error guardando la etapa');
        }
    };

    const renderFields = () => {
        switch (tipo) {
            case 'abastecimiento':
            case 'abastecimiento2': // Usamos el mismo formulario para abastecimiento y abastecimiento2
                return (
                    <>
                        <label>
                            Tiempo de Producción (minutos):
                            <input
                                type="number"
                                name={tipo === 'abastecimiento' ? 'tiempoProduccionAbastecimiento' : 'tiempoProduccionAbastecimiento2'}
                                value={tipo === 'abastecimiento' ? etapaData.tiempoProduccionAbastecimiento : etapaData.tiempoProduccionAbastecimiento2}
                                onChange={handleChange}
                                placeholder="Ej: 5 minutos"
                                required
                            />
                        </label>
                        <label>
                            Capacidad Máxima de Abastecimiento:
                            <div className="input-container">
                                <input
                                    type="number"
                                    name={tipo === 'abastecimiento' ? 'capacidadMaximaAbastecimiento' : 'capacidadMaximaAbastecimiento2'}
                                    value={tipo === 'abastecimiento' ? etapaData.capacidadMaximaAbastecimiento : etapaData.capacidadMaximaAbastecimiento2}
                                    onChange={handleChange}
                                    required
                                />
                                <span className="info-icon">i
                                    <span className="tooltip">Capacidad máxima que puede almacenar la etapa de abastecimiento.</span>
                                </span>
                            </div>
                        </label>
                        <label>
                            Periodo entre Tandas de Expedición (minutos):
                            <input
                                type="number"
                                name={tipo === 'abastecimiento' ? 'periodoExpedicionAbastecimiento' : 'periodoExpedicionAbastecimiento2'}
                                value={tipo === 'abastecimiento' ? etapaData.periodoExpedicionAbastecimiento : etapaData.periodoExpedicionAbastecimiento2}
                                onChange={handleChange}
                                placeholder="Ej: 10 minutos"
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
                            <div className="input-container">
                                <input
                                    type="number"
                                    name="unidadesPorProducto"
                                    value={etapaData.unidadesPorProducto || ''}
                                    onChange={handleChange}
                                    required
                                />
                                <span className="info-icon">i
                                    <span className="tooltip">Número de unidades de abastecimiento necesarias para fabricar un producto.</span>
                                </span>
                            </div>
                        </label>
                        <label>
                            Tiempo de Fabricación de Producto (minutos):
                            <input
                                type="number"
                                name="tiempoFabricacionProducto"
                                value={etapaData.tiempoFabricacionProducto || ''}
                                onChange={handleChange}
                                placeholder="Ej: 5 minutos"
                                required
                            />
                        </label>
                        <label>
                            Capacidad Máxima de Abastecimiento en Producción:
                            <div className="input-container">
                                <input
                                    type="number"
                                    name="capacidadMaximaAbastecimientoProduccion"
                                    value={etapaData.capacidadMaximaAbastecimientoProduccion || ''}
                                    onChange={handleChange}
                                    required
                                />
                                <span className="info-icon">i
                                    <span className="tooltip">Cantidad máxima de unidades de abastecimiento que puede contener Producción.</span>
                                </span>
                            </div>
                        </label>
                        <label>
                            Capacidad Máxima de Productos en Producción:
                            <div className="input-container">
                                <input
                                    type="number"
                                    name="capacidadMaximaProductosProduccion"
                                    value={etapaData.capacidadMaximaProductosProduccion || ''}
                                    onChange={handleChange}
                                    required
                                />
                                <span className="info-icon">i
                                    <span className="tooltip">Número máximo de productos que Producción puede almacenar.</span>
                                </span>
                            </div>
                        </label>
                        <label>
                            Periodo entre Expediciones en Producción (minutos):
                            <input
                                type="number"
                                name="periodoExpedicionProduccion"
                                value={etapaData.periodoExpedicionProduccion || ''}
                                onChange={handleChange}
                                placeholder="Ej: 10 minutos"
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
                            <div className="input-container">
                                <input
                                    type="number"
                                    name="capacidadMaximaProductosAlmacenamiento"
                                    value={etapaData.capacidadMaximaProductosAlmacenamiento || ''}
                                    onChange={handleChange}
                                    required
                                />
                                <span className="info-icon">i
                                    <span className="tooltip">Cantidad máxima de productos que puede contener Almacenamiento.</span>
                                </span>
                            </div>
                        </label>
                        <label>
                            Periodo de Compras (minutos):
                            <input
                                type="number"
                                name="periodoCompras"
                                value={etapaData.periodoCompras || ''}
                                onChange={handleChange}
                                placeholder="Ej: 10 minutos"
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
        <form onSubmit={handleSubmit}>
            <h2>Configurar {tipo.charAt(0).toUpperCase() + tipo.slice(1)}</h2>
            {renderFields()}
            <button type="submit">Guardar Etapa</button>
        </form>
    );
};

export default FormularioEtapa;
