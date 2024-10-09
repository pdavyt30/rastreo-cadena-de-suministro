import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./VistaCadena.css";

const VistaCadena = ({ dificultad }) => {
  const navigate = useNavigate();
  const [etapas, setEtapas] = useState({
    abastecimiento: null,
    produccion: null,
    almacenamiento: null,
    abastecimiento2: null // Agregamos abastecimiento2
  });
  const [enEjecucion, setEnEjecucion] = useState(false);

  useEffect(() => {
    const fetchEtapas = async () => {
      try {
        const [abastecimiento, produccion, almacenamiento, estadoSimulacion] = await Promise.all([
          axios.get("http://localhost:8080/api/abastecimiento/listar"),
          axios.get("http://localhost:8080/api/produccion/listar"),
          axios.get("http://localhost:8080/api/almacenamiento/listar"),
          axios.get("http://localhost:8080/api/simulacion/estado"),
        ]);
        setEtapas({
          abastecimiento: abastecimiento.data[0] || null,
          produccion: produccion.data[0] || null,
          almacenamiento: almacenamiento.data[0] || null,
          abastecimiento2: dificultad === "avanzado" ? abastecimiento.data[1] || null : null // Obtenemos la segunda etapa si la dificultad es avanzado
        });
        setEnEjecucion(estadoSimulacion.data.enEjecucion);
      } catch (error) {
        console.error("Error fetching etapas", error);
      }
    };
    fetchEtapas();
  }, [dificultad]); // El hook se activa cuando la dificultad cambia

  const handleButtonClick = (tipo) => {
    if (etapas[tipo]) {
      navigate("/configurar-etapa", { state: { tipo, etapa: etapas[tipo] } });
    } else {
      navigate("/configurar-etapa", { state: { tipo } });
    }
  };

  const iniciarSimulacion = async () => {
    const dificultad = localStorage.getItem("dificultad");
    const simulacionData = { ...etapas, dificultad };

    try {
      const response = await axios.post(
        "http://localhost:8080/api/simulacion/iniciar",
        simulacionData
      );
      alert("Simulación iniciada");
      setEnEjecucion(true);
      navigate("/simulacion");
    } catch (error) {
      console.error("Error iniciando la simulación", error);
      alert("Error iniciando la simulación");
    }
  };

  const detenerSimulacion = async () => {
    try {
      await axios.post("http://localhost:8080/api/simulacion/detener");
      alert("Simulación detenida");
      setEnEjecucion(false);
      navigate("/");
    } catch (error) {
      console.error("Error deteniendo la simulación", error);
      alert("Error deteniendo la simulación");
    }
  };

  const verSimulacion = () => {
    navigate("/simulacion");
  };

  const borrarDatos = async () => {
    try {
      await axios.post("http://localhost:8080/api/simulacion/borrar-datos");
      alert("Datos borrados con éxito");
      setEtapas({
        abastecimiento: null,
        produccion: null,
        almacenamiento: null,
        abastecimiento2: null
      });
    } catch (error) {
      console.error("Error borrando los datos", error);
      alert("Error borrando los datos");
    }
  };

  return (
    <div className="container">
      <h1>Cadena de Suministro</h1>
      <div className="button-container">
        <button
          className="stage-button"
          onClick={() => handleButtonClick("abastecimiento")}
          disabled={enEjecucion}
        >
          {etapas.abastecimiento ? "Editar Abastecimiento" : "Configurar Abastecimiento"}
        </button>

        {/* Mostrar el botón de Abastecimiento 2 si la dificultad es avanzado */}
        {dificultad === "avanzado" && (
          <button
            className="stage-button"
            onClick={() => handleButtonClick("abastecimiento2")}
            disabled={enEjecucion}
          >
            {etapas.abastecimiento2 ? "Editar Abastecimiento 2" : "Configurar Abastecimiento 2"}
          </button>
        )}

        <button
          className="stage-button"
          onClick={() => handleButtonClick("produccion")}
          disabled={enEjecucion}
        >
          {etapas.produccion ? "Editar Producción" : "Configurar Producción"}
        </button>
        <button
          className="stage-button"
          onClick={() => handleButtonClick("almacenamiento")}
          disabled={enEjecucion}
        >
          {etapas.almacenamiento ? "Editar Almacenamiento" : "Configurar Almacenamiento"}
        </button>
      </div>

      <div className="start-stop-container">
        {enEjecucion ? (
          <>
            <button className="start-button" onClick={verSimulacion}>
              Ver Simulación
            </button>
            <button className="stop-button" onClick={detenerSimulacion}>
              Detener Simulación
            </button>
          </>
        ) : (
          <>
            <button className="start-button" onClick={iniciarSimulacion}>
              Iniciar Simulación
            </button>
            <button className="delete-button" onClick={borrarDatos}>
              Borrar Datos
            </button>
          </>
        )}
      </div>
    </div>
  );
};

export default VistaCadena;
