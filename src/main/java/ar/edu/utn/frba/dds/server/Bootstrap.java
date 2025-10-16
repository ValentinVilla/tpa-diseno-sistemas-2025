package ar.edu.utn.frba.dds.server;

/*
public class Bootstrap {

  public static void init() {
    List<Contribuyente> usuarios = crearUsuarios();
    usuarios.forEach(usuario -> RepositorioUsuarios.getInstancia().guardar(usuario));

    List<Hecho> hechos = crearHechos();
    hechos.forEach(hecho -> {
      try {
        DAOHechos.getInstancia().guardar(hecho);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    // Ejemplo de obtener un usuario específico
    Contribuyente gonzalo = usuarios.get(0);
  }

  private static List<Contribuyente> crearUsuarios() {
    return List.of(
        new Contribuyente("gonzalo", "garcia", "2324455678", "gonzalo@gmail.com", 20, "123456")
    );
  }

  private static List<Hecho> crearHechos() {
    return Arrays.asList(
        new HechoBuilder()
            .titulo("Corte de luz en el centro")
            .descripcion("Zona céntrica sin suministro eléctrico desde las 18hs.")
            .categoria("Energía")
            .latitud(-34.6037)
            .longitud(-58.3816)
            .fechaAcontecimiento(LocalDateTime.now().minusHours(3))
            .fechaCarga(LocalDateTime.now())
            .origen(Origen.CONTRIBUYENTE)
            .build(),

        new HechoBuilder()
            .titulo("Accidente en Av. Corrientes")
            .descripcion("Choque leve entre dos vehículos, sin heridos.")
            .categoria("Tránsito")
            .latitud(-34.6041)
            .longitud(-58.3820)
            .fechaAcontecimiento(LocalDateTime.now().minusDays(1))
            .fechaCarga(LocalDateTime.now().minusDays(1).plusHours(1))
            .origen(Origen.CONTRIBUYENTE)
            .build(),

        new HechoBuilder()
            .titulo("Manifestación en Plaza de Mayo")
            .descripcion("Concentración pacífica por reclamos laborales.")
            .categoria("Social")
            .latitud(-34.6083)
            .longitud(-58.3700)
            .fechaAcontecimiento(LocalDateTime.now().minusHours(5))
            .fechaCarga(LocalDateTime.now().minusHours(4))
            .origen(Origen.DATASET)
            .build(),

        new HechoBuilder()
            .titulo("Inundación en Villa Urquiza")
            .descripcion("Calles anegadas tras tormenta fuerte.")
            .categoria("Clima")
            .latitud(-34.5739)
            .longitud(-58.4898)
            .fechaAcontecimiento(LocalDateTime.now().minusDays(2))
            .fechaCarga(LocalDateTime.now().minusDays(2).plusHours(2))
            .origen(Origen.CONTRIBUYENTE)
            .build(),

        new HechoBuilder()
            .titulo("Incendio en depósito industrial")
            .descripcion("Bomberos controlan incendio en galpón de Barracas.")
            .categoria("Emergencia")
            .latitud(-34.6399)
            .longitud(-58.3707)
            .fechaAcontecimiento(LocalDateTime.now().minusHours(6))
            .fechaCarga(LocalDateTime.now().minusHours(5))
            .origen(Origen.CONTRIBUYENTE)
            .build(),

        new HechoBuilder()
            .titulo("Evento cultural en Recoleta")
            .descripcion("Festival de música independiente en Plaza Francia.")
            .categoria("Cultural")
            .latitud(-34.5875)
            .longitud(-58.3931)
            .fechaAcontecimiento(LocalDateTime.now().plusDays(1))
            .fechaCarga(LocalDateTime.now())
            .origen(Origen.SERVICIOEXTERNO)
            .build()
    );
  }
}
*/