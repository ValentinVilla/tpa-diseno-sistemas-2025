# Justificación de Decisiones de Diseño

## Hechos

### 1. Modelado de Hecho con una clase abstracta
Se decidió modelar a los **Hechos** con una clase abstracta dado que se espera dar soporte tanto a hechos de texto como a hechos con contenido multimedia.  
De esta forma, las subclases `HechoDeTexto` y `HechoConMultimedia` permiten una sencilla implementación en futuras entregas, sin romper la lógica existente.

### 2. Uso de enumeraciones para el origen de los hechos
Se introdujo un `enum` llamado `Origen` que permite identificar si un hecho proviene de un archivo CSV, fue ingresado manualmente o por un contribuyente, encapsulando el origen de forma clara.

### 3. Gestión de solicitud de eliminación
Se implementó una clase `SolicitudDeEliminacion` que permite gestionar las solicitudes de eliminación de hechos.  
Esta clase permite mantener un registro de las solicitudes y su estado (mediante el `enum` `EstadoSolicitud`), facilitando su gestión.

---

## Colecciones

La clase `Coleccion` representa una agrupación de hechos basada en algún criterio de pertenencia y proveniente de una fuente asignada.  
La implementación busca organizar los hechos de forma flexible y dinámica. Por esta razón, se decide no utilizar una lista de hechos como atributo, evitando tener que actualizarla cada vez que se incorpora un nuevo hecho.  
Además, si una colección requiere modificar su criterio (por ejemplo, actualizar un rango de fechas), no es necesario rehacer ni borrar hechos manualmente.

### 1. Aplicación del patrón Strategy en criterio de pertenencia
La lógica que determina si un hecho pertenece o no a una colección fue modelada mediante el patrón **Strategy**.  
Esto permite crear distintos criterios sin acoplarlos directamente a la clase `Coleccion`.

### 2. Abstracción de Fuente
Se definió `Fuente` como una clase abstracta, permitiendo la implementación de distintas fuentes de datos (por ejemplo, CSV, API, etc.) sin modificar la lógica de la aplicación.  
Esto otorga mayor flexibilidad y escalabilidad en el futuro.

---

## Contribuyentes

Se modeló la abstracción `Contribuyente` como una **interfaz marcador** (*marker interface*) que representa a toda persona humana que aporta hechos al sistema, cumpliendo con el requerimiento de admitir tanto contribuyentes anónimos como identificados.  
Se distinguieron dos implementaciones concretas: `Visualizador` y `ContribuyenteIdentificado`.

Esta separación permite modelar correctamente los distintos niveles de información asociados a cada tipo de contribuyente:

- Los **contribuyentes anónimos** no requieren ningún dato personal.
- Los **identificados** almacenan información adicional como nombre (obligatorio), apellido y edad.  
  Esta información **no será pública**, pero sí accesible para las personas administradoras del sistema.

---

## 📌 Diagrama de Clases General

A continuación se presenta el diagrama UML general del dominio del sistema, en el que se modelan los principales conceptos como hechos, colecciones, contribuyentes, fuentes de datos y solicitudes de eliminación.

![Diagrama UML General](/Diagramas/classDiagram.png)

---

## 🧩 Diagrama de Clases por Paquetes

En esta versión del diagrama, los elementos fueron organizados en distintos *packages* para reflejar mejor la separación de responsabilidades y modularidad del sistema.

![Diagrama UML por Paquetes](/Diagramas/package.png)
