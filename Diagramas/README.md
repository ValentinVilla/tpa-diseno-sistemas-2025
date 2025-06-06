# Justificación de Decisiones de Diseño

---

# Paquete dominio

---

## Builders (`ColeccionBuilder`, `HechoBuilder`)
Se utiliza el patrón Builder para la creación de objetos inmutables de las clases `Coleccion` y `Hecho`.  
Esto permite:
- Instanciación legible y segura, validando campos obligatorios antes de la construcción.
- Centralizar la lógica de validación y lanzar excepciones claras ante errores de datos (aplicando fallando lo antes posible de ser necesario).
- Facilitar la extensión futura de los modelos agregando atributos opcionales sin romper código existente.

---

## Hechos
Modela la unidad de información principal del sistema. Pertenecen a una `Coleccion` y se obtienen de una `Fuente` de forma dinámica.
Para asegurar la unicidad de los hechos incluso con múltiples fuentes, se utiliza un identificador único (`id`) generado mediante UUID.

### 1. Uso de enumeraciones para el origen de los hechos
Se introdujo un `enum` llamado `Origen` que permite identificar si un hecho proviene de un archivo CSV, fue ingresado manualmente o por un contribuyente, encapsulando el origen de forma clara.

### 2. Gestión de solicitud de eliminación
Se implementó una clase `SolicitudDeEliminacion` que permite gestionar las solicitudes de eliminación de hechos.
Esta clase permite instanciar las solicitudes y su estado (mediante el `enum` `EstadoSolicitud`), facilitando su gestión.
Esta clase se explica mejor en la sección de **Solicitudes**.

### 3. Creacion del campo Visibilidad:
Es un booleano que nos va a permitir diferenciar cuando un hecho es valido o no ya que en el caso de no ser valido no tiene que mostrarse cuando se busquen hechos que pertenezcan a esa coleccion. Util cuando se gestionan las solicitudes.

## HechoContribuyente
Hereda de `Hecho` agregando información de creador y lógica específica (por ejemplo, plazo de edición solo para hechos subidos por usuarios registrados).
Esto nos permite:
- Encapsular diferencias de comportamiento y atributos sin romper el polimorfismo para poder tratar las responsabilidades nuevas de los hechos asociados a un contribuyente.
- Encapsular la lógica de las responsabilidades mencionadas sin que los hechos normales tengan que tener métodos y atributos que no todos usan.

---

## Colecciones
La clase `Coleccion` representa una agrupación de hechos basada en algún criterio de pertenencia y proveniente de una fuente asignada.  
La implementación busca organizar los hechos de forma flexible y dinámica. Por esta razón, se decide no utilizar una lista de hechos como atributo, evitando tener que actualizarla cada vez que se incorpora un nuevo hecho.
Permite que las colecciones reflejen siempre el estado actual de la fuente y su criterio, facilitando mantenimiento y consistencia al obtener los hechos dinámicamente de la fuente
Además, si una colección requiere modificar su criterio (por ejemplo, actualizar un rango de fechas), no es necesario rehacer ni borrar hechos manualmente.
Agregamos también un dentificador `handle` único para integraciones futuras de las API, facilitando referenciación y extensibilidad.

### 1. Aplicación del patrón Strategy en criterio de pertenencia
La lógica que determina si un hecho pertenece o no a una colección fue modelada mediante el patrón **Strategy**.  
Esto permite crear distintos criterios sin acoplarlos directamente a la clase `Coleccion`.

### 2. Abstracción de Fuente
Se definió `Fuente` como una clase abstracta, permitiendo la implementación de distintas fuentes de datos (por ejemplo, CSV, API, etc.) sin modificar la lógica de la aplicación.  
Esto otorga mayor flexibilidad y escalabilidad en el futuro, aunque para esta empresa solo trabajamos con CSV.
Estas fuentes estarán mejor explicadas en la sección de **Fuentes**.

---

# Paquete filtros

---

## Interfaz Filtro
Se define la interfaz `Filtro` con un único método `cumple(Hecho hecho)`, que permite determinar si un hecho cumple cierto criterio.

**Justificación:**
- Implementa el **Patrón Strategy**, permitiendo encapsular y abstraer el criterio de filtrado respecto del resto del sistema.
- Facilita la **extensibilidad y el polimorfismo**: agregar nuevos tipos de filtros no requiere modificar código existente, solo implementar la interfaz.
- Permite inyectar filtros tanto en colecciones (como criterio de pertenencia) como en búsquedas, haciendo el sistema mucho más flexible y desacoplado.

---

## Filtros concretos
Se implementan variantes concretas de filtro:
### FiltroCategoria
Filtra hechos por categoría.
### FiltroFecha
Filtra hechos por fecha exacta.
### FiltroUbicacion
Filtra hechos dentro de un rango de latitud y longitud.

La ventaja de esta solución es que cada clase tiene **alta cohesión**: una sola responsabilidad, lo que facilita mantenimiento, pruebas y futuras extensiones.

---

# Paquete solicitudes

---


## Abstracción Solicitud y Subtipos

Decidimos modelar la clase abstracta `Solicitud` para capturar la noción general de una solicitud sobre un hecho, con subclases para los tipos específicos.
Cada solicitud tiene un estado (`EstadoSolicitud`), una referencia al hecho afectado y define el flujo de aceptación/rechazo a través de template methods.

**Justificación:**
- **Abstracción y polimorfismo:** Todas las solicitudes comparten estado y comportamiento básico, pero las acciones concretas al aceptar/rechazar dependen de su tipo. La herencia y los métodos abstractos (`aplicarAceptacion`, `aplicarRechazo`) permiten que cada subtipo defina su lógica específica, cumpliendo el **Patrón Template Method**.
- **Extensibilidad:** Es sencillo agregar nuevas solicitudes implementando la abstracción.
- **Centralización:** El flujo de procesamiento de solicitudes (aceptar/rechazar) queda unificado y centralizado, favoreciendo el mantenimiento.

---

## SolicitudEliminacion
Encapsula la fundamentación textual y la acción de ocultar el hecho al ser aceptada.

**Justificación:**
- Permite la trazabilidad y la rendición de cuentas: los hechos no se eliminan físicamente, sino que se ocultan.
- Puede evolucionar para agregar lógica de auditoría, notificaciones, etc.

---

## SolicitudModificacion y SolicitudSubida
Estas clases permiten aceptar, rechazar o aceptar con sugerencia (en cuyo caso almacenan la sugerencia para el contribuyente, al cual luego habría que notificar).

**Justificación:**
- Facilita el feedback para un usuario contribuyente y encapsula la lógica para que un administrador pueda manejar la subida y modificación de hechos de parte de contribuyentes.
- La lógica de visibilidad y sugerencias queda encapsulada en cada subtipo, favoreciendo la evolución futura (por ejemplo, notificaciones, auditorías, etc.).





<!--Cosas viejas que aún no integré:
## LectorCSV
El lector cumple la funcion de leer un archivo CSV y pasar los datos a un objeto Hecho, utilizamos librerias para el manejo de estos tipos de archivos. Planteamos que si un archivo CSV no contiene los campos que utiliza un Hecho, tire una excepcion de que el archivo es incompleto.

## Atributo categoria en Fuente
Este atributo nos va a permitir diferenciar a que coleccion van a pertenecer los hechos. Para en el caso de sea un archivo SCV de incenidos forestales este campo va a valer "Incendios Forestales"
-->
---
## 📌 Diagrama de Clases General

A continuación se presenta el diagrama UML general del dominio del sistema, en el que se modelan los principales conceptos como hechos, colecciones, contribuyentes, fuentes de datos y solicitudes de eliminación.

![Diagrama UML General](/Diagramas/classDiagram.png)

---

## 👤 Casos de uso
A continuación se presentan los casos de uso, que describen las interacciones entre los actores y el sistema.

![Casos de uso](/Diagramas/casosDeUso.png)
