package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.cron.TareasCronometradas;
import ar.edu.utn.frba.dds.server.Server;

import java.util.Arrays;

public class Main {
  public static void main(String[] args) {
    try {
      if (args != null && args.length > 0 && "cron".equalsIgnoreCase(args[0])) {
        String[] cronArgs = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[0];
        TareasCronometradas.main(cronArgs);
        return;
      }

      System.out.println("Main: iniciando Server...");
      new Server().start();
      System.out.println("Main: Server.start() retornó correctamente. Bloqueando hilo principal para depuración.");

      synchronized (Main.class) {
        Main.class.wait();
      }
    } catch (Throwable t) {
      System.err.println("Main: Excepción no capturada:");
      t.printStackTrace();
      try {
        Thread.sleep(5 * 60 * 1000L); // 5 min
      } catch (InterruptedException ignored) {}
    }
  }
}
