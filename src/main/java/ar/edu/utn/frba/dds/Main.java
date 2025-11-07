package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.cron.TareasCronometradas;
import ar.edu.utn.frba.dds.server.Server;

import java.util.Arrays;

public class Main {
  public static void main(String[] args) {
    if (args != null && args.length > 0 && "cron".equalsIgnoreCase(args[0])) {
      String[] cronArgs = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[0];
      TareasCronometradas.main(cronArgs);
      return;
    }
    new Server().start();
  }
}

