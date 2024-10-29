import React from 'react';
import './Modal.css';

const ModalInformacion = ({ isOpen, onClose }) => {
  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content modal-informacion" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h1>Marco Teórico: Cadena de Suministro</h1>
          <button className="close-button" onClick={onClose}>&times;</button>
        </div>
        <div className="modal-body">
          <h2>¿Qué es una Cadena de Suministro?</h2>
          <p>
            Una cadena de suministro es un sistema interconectado que abarca todas las etapas
            involucradas en la producción y distribución de un producto o servicio, desde la adquisición
            de materias primas hasta la entrega al cliente final. Su objetivo es gestionar el flujo eficiente
            de bienes, servicios, información y recursos entre las distintas partes involucradas, asegurando
            la satisfacción del cliente y optimizando costos.
          </p>
          <p>
            Las principales funciones de la cadena de suministro incluyen planificación, abastecimiento,
            producción, almacenamiento, distribución y seguimiento de inventario.
          </p>

          <h3>Etapas Clave de la Cadena de Suministro</h3>

          <h3>Abastecimiento</h3>
          <p>
            La etapa de abastecimiento consiste en la adquisición de las materias primas, insumos o productos
            necesarios para iniciar el proceso productivo. El objetivo es garantizar la disponibilidad de los
            recursos adecuados en el momento y lugar requeridos para mantener la continuidad del flujo de trabajo.
          </p>
          <ul>
            <li><strong>Tiempo de Producción:</strong> Período que tarda en generarse una unidad de insumo.</li>
            <li><strong>Capacidad Máxima:</strong> Cantidad máxima de unidades que se puede almacenar antes de ser expedidas.</li>
            <li><strong>Periodo de Expedición:</strong> Tiempo entre cada tanda de unidades enviadas a la etapa de producción.</li>
          </ul>

          <h3>Producción</h3>
          <p>
            La etapa de producción se centra en la transformación de los insumos provenientes del abastecimiento
            en productos terminados o semielaborados. Este proceso incluye actividades como ensamblaje, procesamiento
            y control de calidad, asegurando que los productos finales cumplan con los estándares definidos.
          </p>
          <ul>
            <li><strong>Unidades por Producto:</strong> Cantidad de insumos necesaria para fabricar un producto.</li>
            <li><strong>Capacidad Total de Producción:</strong> Límite de productos que se pueden mantener en esta etapa.</li>
            <li><strong>Periodo de Expedición:</strong> Intervalo de tiempo entre el envío de productos al almacenamiento.</li>
          </ul>

          <h3>Almacenamiento</h3>
          <p>
            En la etapa de almacenamiento, los productos terminados son guardados temporalmente hasta que sean enviados
            al cliente o a la siguiente etapa de la cadena de suministro. Es fundamental para gestionar el inventario
            y evitar cuellos de botella en el proceso.
          </p>
          <ul>
            <li><strong>Capacidad Total:</strong> Máxima cantidad de productos que se puede almacenar.</li>
            <li><strong>Periodo de Compras:</strong> Tiempo entre una compra realizada por un cliente y la siguiente.</li>
          </ul>
          <p>
            Esta etapa también permite responder con agilidad a la demanda del mercado y optimizar los niveles de inventario.
          </p>
        </div>
      </div>
    </div>
  );
};

export default ModalInformacion;
