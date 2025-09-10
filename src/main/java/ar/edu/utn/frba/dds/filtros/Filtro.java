package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.dominio.Hecho;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_filtro",  discriminatorType = DiscriminatorType.STRING)
public abstract class Filtro {
  @Id
  @GeneratedValue
  private Long id;

  public abstract boolean cumple(Hecho hecho);
}

//yo puedo mandarle ya los hechos que cumplen con cierta coleccion para que luego haga el calculo en la
//base de datos de en que provincia hay mayor ocurrencia?

/* MUNDO POSTGRE
SELECT top 1 provincia, COUNT(*)
FROM hechos
WHERE id IN (lista de IDs filtrados)
GROUP BY provincia
ORDER BY COUNT(*) DESC;
*/

/* MUNDO JAVA
 List<Long> idsFiltrados = /* tu lista de IDs ;
List<Object[]> resultados = entityManager.createQuery(
        "SELECT h.provincia, COUNT(h) FROM Hecho h WHERE h.id IN :ids GROUP BY h.provincia ORDER BY COUNT(h) DESC"
    )
    .setParameter("ids", idsFiltrados)
    .getResultList();
*/
