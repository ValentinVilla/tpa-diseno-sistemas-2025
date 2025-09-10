package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.repositorios.RepositorioEstadisticas;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

  public static void main(String[] args) {

    RepositorioEstadisticas repo = RepositorioEstadisticas.getInstancia();

    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    scheduler.scheduleAtFixedRate(() -> {
      try {
        System.out.println("Refrescando vistas materializadas...");
        repo.refrescarTodas();
        System.out.println("Refresco completado.");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }, 0, 1, TimeUnit.MINUTES);

    System.out.println("Scheduler iniciado. Presiona Ctrl+C para detener.");

  }
}

