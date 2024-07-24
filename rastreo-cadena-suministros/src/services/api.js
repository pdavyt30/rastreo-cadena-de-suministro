import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
});

export const guardarEtapa = (tipo, etapa) => {
    return api.post(`/${tipo}/guardar`, etapa);
};

export const listarEtapas = (tipo) => {
    return api.get(`/${tipo}/listar`);
};

export const iniciarSimulacion = (estado) => {
    return api.post('/simulacion/iniciar', estado);
};

export const obtenerEstadoSimulacion = () => {
    return api.get('/simulacion/estado');
};
