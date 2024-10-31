import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../vistas/VistaCadena.css";

import imagenAbastecimiento from "../../imagenes/imagenAbastecimiento.png";
import imagenProduccion from "../../imagenes/imagenProduccion.png";
import imagenAlmacenamiento from "../../imagenes/imagenAlmacenamiento.png";

const VistaCadena = ({ dificultad }) => {
  const navigate = useNavigate();

  const [etapas, setEtapas] = useState({
    abastecimiento: {},
    abastecimiento2: {},
    produccion: {},
    almacenamiento: {},
  });

  const [enEjecucion, setEnEjecucion] = useState(false);

  useEffect(() => {
    const fetchEtapas = async () => {
      try {
        const [
          abastecimiento,
          abastecimiento2,
          produccion,
          almacenamiento,
          estadoSimulacion,
        ] = await Promise.all([
          axios.get("http://localhost:8080/api/abastecimiento/listar?tipoAbastecimiento=1"),
          axios.get("http://localhost:8080/api/abastecimiento/listar?tipoAbastecimiento=2"),
          axios.get("http://localhost:8080/api/produccion/listar"),
          axios.get("http://localhost:8080/api/almacenamiento/listar"),
          axios.get("http://localhost:8080/api/simulacion/estado"),
        ]);
        setEtapas({
          abastecimiento: abastecimiento.data[0] || {},
          abastecimiento2: abastecimiento2.data[0] || {},
          produccion: produccion.data[0] || {},
          almacenamiento: almacenamiento.data[0] || {},
        });
        setEnEjecucion(estadoSimulacion.data.enEjecucion);
      } catch (error) {
        console.error("Error fetching etapas", error);
      }
    };
    fetchEtapas();
  }, []);

  const handleButtonClick = (tipoAbastecimiento = 1) => {
    const etapaSeleccionada = etapas[`abastecimiento${tipoAbastecimiento}`];
    navigate("/configurar-etapa", {
      state: { tipo: "abastecimiento", tipoAbastecimiento, etapa: etapaSeleccionada },
    });
  };

  const iniciarSimulacion = async () => {
    const simulacionData = { ...etapas, dificultad: localStorage.getItem("dificultad") };
    try {
      await axios.post("http://localhost:8080/api/simulacion/iniciar", simulacionData);
      alert("Simulación iniciada");
      setEnEjecucion(true);
      navigate("/simulacion");
    } catch (error) {
      console.error("Error iniciando la simulación", error);
      alert("Error iniciando la simulación");
    }
  };

  const borrarDatos = async () => {
    try {
      await axios.post("http://localhost:8080/api/simulacion/borrar-datos");
      alert("Datos borrados con éxito");
      setEtapas({
        abastecimiento: {},
        abastecimiento2: {},
        produccion: {},
        almacenamiento: {},
      });
      setEnEjecucion(false);
    } catch (error) {
      console.error("Error borrando los datos", error);
      alert("Error borrando los datos");
    }
  };

  const getTituloEtapa = (etapa, nombre) => {
    return etapa && Object.keys(etapa).length > 0 ? `Editar ${nombre}` : `Configurar ${nombre}`;
  };

  return (
    <div className="cadena-container">
      <h1 className="cadena-titulo">CADENA DE SUMINISTRO</h1>
      <div className="cadena-etapas-container">
        <div className="cadena-etapa" onClick={() => handleButtonClick(1)}>
          <img src={imagenAbastecimiento} alt="Abastecimiento" />
          <div className="cadena-etapa-titulo">
            {getTituloEtapa(etapas.abastecimiento, "Abastecimiento")}
          </div>
        </div>

        {dificultad === "avanzado" && (
          <div className="cadena-etapa" onClick={() => handleButtonClick(2)}>
            <img src={imagenAbastecimiento} alt="Abastecimiento 2" />
            <div className="cadena-etapa-titulo">
              {getTituloEtapa(etapas.abastecimiento2, "Abastecimiento 2")}
            </div>
          </div>
        )}

        <div className="cadena-etapa" onClick={() => navigate("/configurar-etapa", { state: { tipo: "produccion", etapa: etapas.produccion } })}>
          <img src={imagenProduccion} alt="Producción" />
          <div className="cadena-etapa-titulo">
            {getTituloEtapa(etapas.produccion, "Producción")}
          </div>
        </div>

        <div className="cadena-etapa" onClick={() => navigate("/configurar-etapa", { state: { tipo: "almacenamiento", etapa: etapas.almacenamiento } })}>
          <img src={imagenAlmacenamiento} alt="Almacenamiento" />
          <div className="cadena-etapa-titulo">
            {getTituloEtapa(etapas.almacenamiento, "Almacenamiento")}
          </div>
        </div>
      </div>

      <div className="cadena-start-stop-container">
        {enEjecucion ? (
          <>
            <button className="cadena-start-button" onClick={() => navigate("/simulacion")}>
              Ver Simulación
            </button>
            <button className="cadena-delete-button" onClick={borrarDatos}>
              Borrar Datos
            </button>
          </>
        ) : (
          <>
            <button className="cadena-start-button" onClick={iniciarSimulacion}>
              Iniciar Simulación
            </button>
            <button className="cadena-delete-button" onClick={borrarDatos}>
              Borrar Datos
            </button>
          </>
        )}
      </div>
    </div>
  );
};

export default VistaCadena;
