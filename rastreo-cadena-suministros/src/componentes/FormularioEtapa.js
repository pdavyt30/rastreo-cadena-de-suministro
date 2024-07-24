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
        tiempoProduccion: '',
        capacidad: '',
        periodoExpedicion: '',
        unidadesPorProducto: '',
        periodoCompras: ''
    };

    const [etapaData, setEtapaData] = useState(etapa || initialData);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setEtapaData({ ...etapaData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const url = `http://localhost:8080/api/${tipo}/guardar`;
        try {
            const response = await axios.post(url, etapaData);
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
                return (
                    <>
                        <label>
                            Tiempo de Producción:
                            <input type="number" name="tiempoProduccion" value={etapaData.tiempoProduccion || ''} onChange={handleChange} required />
                        </label>
                        <label>
                            Capacidad Máxima:
                            <input type="number" name="capacidad" value={etapaData.capacidad || ''} onChange={handleChange} required />
                        </label>
                        <label>
                            Periodo entre Tandas de Expedición:
                            <input type="number" name="periodoExpedicion" value={etapaData.periodoExpedicion || ''} onChange={handleChange} required />
                        </label>
                    </>
                );
            case 'produccion':
                return (
                    <>
                        <label>
                            Unidades por Producto:
                            <input type="number" name="unidadesPorProducto" value={etapaData.unidadesPorProducto || ''} onChange={handleChange} required />
                        </label>
                        <label>
                            Capacidad Máxima:
                            <input type="number" name="capacidad" value={etapaData.capacidad || ''} onChange={handleChange} required />
                        </label>
                        <label>
                            Periodo entre Expediciones:
                            <input type="number" name="periodoExpedicion" value={etapaData.periodoExpedicion || ''} onChange={handleChange} required />
                        </label>
                    </>
                );
            case 'almacenamiento':
                return (
                    <>
                        <label>
                            Capacidad Máxima:
                            <input type="number" name="capacidad" value={etapaData.capacidad || ''} onChange={handleChange} required />
                        </label>
                        <label>
                            Periodo de Compras:
                            <input type="number" name="periodoCompras" value={etapaData.periodoCompras || ''} onChange={handleChange} required />
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
            <label>
                Nombre:
                <input type="text" name="nombre" value={etapaData.nombre} onChange={handleChange} required />
            </label>
            {renderFields()}
            <button type="submit">Guardar Etapa</button>
        </form>
    );
};

export default FormularioEtapa;
