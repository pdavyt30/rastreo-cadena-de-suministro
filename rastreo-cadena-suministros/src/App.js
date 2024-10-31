import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, useLocation } from 'react-router-dom';
import BarraNavegacion from './componentes/BarraNavegacion';
import VistaInicio from './componentes/vistas/VistaInicio';
import VistaCadena from './componentes/vistas/VistaCadena';
import FormularioEtapa from './componentes/FormularioEtapa';
import VistaSimulacion from './componentes/vistas/VistaSimulacion';
import ModalInformacion from './componentes/modales/ModalInformacion';
import ModalTutorial from './componentes/modales/ModalTutorial'; 
import axios from 'axios';

const App = () => {
    const [dificultad, setDificultad] = useState('principiante');
    const [enEjecucion, setEnEjecucion] = useState(false);
    const [modalActivo, setModalActivo] = useState('');

    const abrirModal = (tipo) => setModalActivo(tipo);
    const cerrarModal = () => setModalActivo('');

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
<Router basename="/">
    <Rutas 
        setDificultad={setDificultad} 
        dificultad={dificultad} 
        enEjecucion={enEjecucion}
        abrirModal={abrirModal} 
    />
            <ModalInformacion isOpen={modalActivo === 'informacion'} onClose={cerrarModal} />
            <ModalTutorial isOpen={modalActivo === 'tutorial'} onClose={cerrarModal} />
</Router>

    );
};

const Rutas = ({ setDificultad, dificultad, enEjecucion, abrirModal }) => {
    const location = useLocation();
    const mostrarBarraNavegacion = location.pathname !== '/';

    return (
        <>
            {mostrarBarraNavegacion && <BarraNavegacion setDificultad={setDificultad} abrirModal={abrirModal} />}
            <Routes>
                <Route path="/" element={<VistaInicio abrirModal={abrirModal} />} />
                <Route path="/cadena" element={<VistaCadena dificultad={dificultad} enEjecucion={enEjecucion} />} />
                <Route path="/configurar-etapa" element={<FormularioEtapa />} />
                <Route path="/simulacion" element={<VistaSimulacion />} />
                <Route path="*" element={<Navigate to="/" />} />
            </Routes>
        </>
    );
};

export default App;
