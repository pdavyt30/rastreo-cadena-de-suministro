import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import BarraNavegacion from './componentes/BarraNavegacion';
import VistaCadena from './componentes/VistaCadena';
import FormularioEtapa from './componentes/FormularioEtapa';
import VistaSimulacion from './componentes/VistaSimulacion';
import axios from 'axios';

const App = () => {
    const [dificultad, setDificultad] = useState('principiante');
    const [enEjecucion, setEnEjecucion] = useState(false);

    useEffect(() => {
        const verificarEstadoSimulacion = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/simulacion/estado');
                setEnEjecucion(response.data.enEjecucion);
            } catch (error) {
                console.error('Error verificando el estado de la simulación', error);
            }
        };
        verificarEstadoSimulacion();
    }, []);

    return (
        <Router>
            <BarraNavegacion setDificultad={setDificultad} />
            <Routes>
                <Route path="/" element={<VistaCadena dificultad={dificultad} enEjecucion={enEjecucion} />} />
                <Route path="/configurar-etapa" element={<FormularioEtapa />} />
                <Route path="/simulacion" element={<VistaSimulacion />} />
                <Route path="*" element={<Navigate to="/" />} />
            </Routes>
        </Router>
    );
};

export default App;
