# Justificación de Decisiones de Diseño

## Hechos

### 1. Modelado de Hecho con una clase abstracta
Se decidió modelar a los **Hechos** con una clase abstracta dado que se espera dar soporte tanto a hechos de texto como a hechos con contenido multimedia.  
De esta forma, las subclases `HechoDeTexto` y `HechoConMultimedia` permiten una sencilla implementación en futuras entregas, sin romper la lógica existente.

### 2. Uso de enumeraciones para el origen de los hechos
Se introdujo un `enum` llamado `Origen` que permite identificar si un hecho proviene de un archivo CSV, fue ingresado manualmente o por un contribuyente, encapsulando el origen de forma clara.

### 3. Gestión de solicitud de eliminación
Se implementó una clase `SolicitudDeEliminacion` que permite gestionar las solicitudes de eliminación de hechos.  
Esta clase permite instanciar las solicitudes y su estado (mediante el `enum` `EstadoSolicitud`), facilitando su gestión.
El estado de una solicitud puede ser ACEPTADA, RECHAZADA o PENDIENTE. De momento no guardamos las solicitudes en una lista sino que solo asociamos la solicitud a un hecho


### 4. Builder para la creacion de hecho
Se implementó un patrón builder para la ceacion de hechos ya que nos permite una instanciacion mas prolija y nos permite manejar errores de manera mas ordenada.

### 5. Creacion del campo Visibilidad:
Es un booleano que nos va a permitir diferenciar cuando un hecho es valido o no ya que en el caso de no ser valido no tiene que mostrarse cuando se busquen hechos que pertenezcan a esa coleccion. Uil cuando se gestionan las solicitudes.

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
Esto otorga mayor flexibilidad y escalabilidad en el futuro, aunque para esta empresa solo trabajamos con CSV.

---

## Contribuyentes

Se modeló la abstracción `Contribuyente` como una **interfaz marcador** (*marker interface*) que representa a toda persona humana que aporta hechos al sistema, cumpliendo con el requerimiento de admitir tanto contribuyentes anónimos como identificados.  
Se distinguieron dos implementaciones concretas: `Visualizador` y `ContribuyenteIdentificado`.

Esta separación permite modelar correctamente los distintos niveles de información asociados a cada tipo de contribuyente:

- Los **contribuyentes anónimos** no requieren ningún dato personal.
- Los **identificados** almacenan información adicional como nombre (obligatorio), apellido y edad.  
  Esta información **no será pública**, pero sí accesible para las personas administradoras del sistema.

## LectorCSV
El lector cumple la funcion de leer un archivo CSV y pasar los datos a un objeto Hecho, utilizamos librerias para el manejo de estos tipos de archivos. Planteamos que si un archivo CSV no contiene los campos que utiliza un Hecho, tire una excepcion de que el archivo es incompleto.

## Atributo categoria en Fuente
Este atributo nos va a permitir diferenciar a que coleccion van a pertenecer los hechos. Para en el caso de sea un archivo SCV de incenidos forestales este campo va a valer "Incendios Forestales"

---
## 📌 Diagrama de Clases General

A continuación se presenta el diagrama UML general del dominio del sistema, en el que se modelan los principales conceptos como hechos, colecciones, contribuyentes, fuentes de datos y solicitudes de eliminación.

![Diagrama UML General](/Diagramas/classDiagram.png)

---

## 👤 Casos de uso
A continuación se presentan los casos de uso, que describen las interacciones entre los actores y el sistema.

![Casos de uso](/Diagramas/casosDeUso.png)
