import React from 'react';
import './Modal.css';
import imagen1 from '../../imagenes/imagen 1.png'; 
import imagen2 from '../../imagenes/imagen2.png';

const ModalTutorial = ({ isOpen, onClose }) => {
  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content modal-tutorial" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h1>Tutorial</h1>
          <button className="close-button" onClick={onClose}>&times;</button>
        </div>
        <div className="modal-body">
          <ul>
            <li>Haz clic en <strong>"Iniciar Simulador"</strong> para comenzar.</li>
            <li>
              Configura las etapas de <strong>Abastecimiento</strong>, <strong>Producción</strong> y <strong>Almacenamiento</strong>. 
              (Las condiciones que elijas para cada etapa definirán los tiempos y estados de la cadena de suministro).
            </li>
            <li>Luego de completar todos los formularios, inicia la simulación.</li>
            <li>Pausa y edita las condiciones de la Cadena de Suministro para mejorar su rendimiento.</li>
          </ul>

          <div className="tutorial-images">
            <img 
              src={imagen1} 
              alt="Formulario de Abastecimiento"
              className="tutorial-image"
            />
            <img 
              src={imagen2} 
              alt="Vista de Cadena de Suministro"
              className="tutorial-image"
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default ModalTutorial;
