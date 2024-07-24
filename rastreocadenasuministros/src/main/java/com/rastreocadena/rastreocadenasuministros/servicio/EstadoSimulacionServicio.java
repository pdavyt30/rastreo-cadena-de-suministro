package com.rastreocadena.rastreocadenasuministros.servicio;

import com.rastreocadena.rastreocadenasuministros.modelo.EstadoSimulacion;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class EstadoSimulacionServicio {

    private EstadoSimulacion estadoSimulacion;
    private Timer timer;

    public EstadoSimulacion iniciarSimulacion(Map<String, Object> etapas) {
        estadoSimulacion = new EstadoSimulacion(true);
        estadoSimulacion.setAbastecimientoCondiciones((Map<String, Object>) etapas.get("abastecimiento"));
        estadoSimulacion.setProduccionCondiciones((Map<String, Object>) etapas.get("produccion"));
        estadoSimulacion.setAlmacenamientoCondiciones((Map<String, Object>) etapas.get("almacenamiento"));

        long tiempoProduccion = (Integer) estadoSimulacion.getAbastecimientoCondiciones().get("tiempoProduccion") * 10L;
        long periodoExpedicion = (Integer) estadoSimulacion.getAbastecimientoCondiciones().get("periodoExpedicion") * 10L;

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (estadoSimulacion.getUnidadesAbastecimiento() < (Integer) estadoSimulacion.getAbastecimientoCondiciones().get("capacidad")) {
                    estadoSimulacion.incrementarUnidadesAbastecimiento();
                    estadoSimulacion.setAlerta(null);
                } else {
                    estadoSimulacion.setAlerta("Capacidad máxima alcanzada hasta próxima expedición");
                }
            }
        }, 0, tiempoProduccion * 1000);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                estadoSimulacion.expedirUnidadesAbastecimiento();
                estadoSimulacion.setAlerta(null);
            }
        }, 0, periodoExpedicion * 1000);

        return estadoSimulacion;
    }

    public EstadoSimulacion detenerSimulacion() {
        if (timer != null) {
            timer.cancel();
        }
        estadoSimulacion.setEnEjecucion(false);
        return estadoSimulacion;
    }

    public EstadoSimulacion obtenerEstadoSimulacion() {
        return estadoSimulacion;
    }
}
