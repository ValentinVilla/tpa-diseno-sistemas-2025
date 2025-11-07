package ar.edu.utn.frba.dds.helpers;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public final class EntityManagerFactoryProvider {

  private static volatile EntityManagerFactory emf;
  private static final String PU_NAME = "simple-persistence-unit";

  private EntityManagerFactoryProvider() {}

  public static EntityManagerFactory getEntityManagerFactory() {
    if (emf == null) {
      synchronized (EntityManagerFactoryProvider.class) {
        if (emf == null) {
          emf = createEntityManagerFactoryWithEnv();
          Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
              close();
            } catch (Exception ignored) {}
          }));
        }
      }
    }
    return emf;
  }

  private static EntityManagerFactory createEntityManagerFactoryWithEnv() {
    Map<String, String> props = new HashMap<>();

    String jdbc = getenvOrProp("JDBC_DATABASE_URL");
    String user = getenvOrProp("DB_USER");
    String pass = getenvOrProp("DB_PASS");

    if (isBlank(jdbc)) {
      jdbc = getenvOrProp("javax.persistence.jdbc.url");
    }
    if (isBlank(user)) {
      user = getenvOrProp("javax.persistence.jdbc.user");
    }
    if (isBlank(pass)) {
      pass = getenvOrProp("javax.persistence.jdbc.password");
    }

    if (!isBlank(jdbc)) {
      props.put("javax.persistence.jdbc.url", jdbc);
      props.put("hibernate.connection.url", jdbc);
    }
    if (!isBlank(user)) {
      props.put("javax.persistence.jdbc.user", user);
      props.put("hibernate.connection.username", user);
    }
    if (!isBlank(pass)) {
      props.put("javax.persistence.jdbc.password", pass);
      props.put("hibernate.connection.password", pass);
    }

    // Opcional: pool size desde env
    String pool = getenvOrProp("DB_POOL_MAX");
    if (!isBlank(pool)) {
      props.put("hibernate.connection.pool_size", pool);
    }

    return Persistence.createEntityManagerFactory(PU_NAME, props);
  }

  private static String getenvOrProp(String name) {
    String v = System.getenv(name);
    if (v != null && !v.trim().isEmpty()) return v.trim();
    v = System.getProperty(name);
    return v;
  }

  private static boolean isBlank(String s) {
    return s == null || s.trim().isEmpty();
  }

  public static synchronized void close() {
    if (emf != null && emf.isOpen()) {
      emf.close();
      emf = null;
    }
  }
}
