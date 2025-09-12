# Justificación de Decisiones de Diseño

---

# Main
Orquesta la inicialización y ejecución continua de los procesos de actualización de estadísticas y vistas materializadas, utilizando un scheduler interno (`ScheduledExecutorService`).

**Justificación:**
- **Centralización de procesos críticos:** La clase principal actúa como punto único de entrada para la inicialización del sistema y la programación de tareas periódicas, asegurando que las estadísticas y vistas agregadas estén siempre actualizadas.
- **Integración con persistencia y análisis de datos:** El scheduler se integra con el repositorio de estadísticas, disparando la actualización de vistas materializadas y tablas agregadas en intervalos regulares.
- **Robustez y control de errores:** El diseño contempla el manejo de excepciones y la notificación de estado, permitiendo monitorear el correcto funcionamiento del sistema y facilitar la observabilidad y el debugging.
- **Escalabilidad:** La arquitectura soporta la incorporación de nuevas tareas programadas o la modificación de los intervalos de ejecución sin afectar el resto del sistema.

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

### 1. Uso de enumeraciones para el origen de los hechos
Se introdujo un `enum` llamado `Origen` que permite identificar si un hecho proviene de un archivo CSV, fue ingresado manualmente o por un contribuyente, encapsulando el origen de forma clara.

### 2. Gestión de solicitud de eliminación
Se implementó una clase `SolicitudDeEliminacion` que permite gestionar las solicitudes de eliminación de hechos.
Esta clase permite instanciar las solicitudes y su estado (mediante el `enum` `EstadoSolicitud`), facilitando su gestión.
Esta clase se explica mejor en la sección de **Solicitudes**.

### 3. Creacion del campo Visibilidad:
Es un booleano que nos va a permitir diferenciar cuando un hecho es valido o no ya que en el caso de no ser valido no tiene que mostrarse cuando se busquen hechos que pertenezcan a esa coleccion. Util cuando se gestionan las solicitudes.

### 4. Persistencia:
- Se definieron las relaciones en el modelo relacional: cada hecho puede estar asociado a una colección, a etiquetas (many-to-many), a un contribuyente y a solicitudes de eliminación.
- El campo `visibilidad` se persiste para distinguir si el hecho debe ser mostrado o no, en base a la gestión de solicitudes de eliminación. Así, los hechos eliminados continúan existiendo en el sistema pero no se muestran, cumpliendo requerimientos de rendición de cuentas y auditabilidad.
- Se implementaron índices sobre campos relevantes (título, descripción) para soportar full text search, facilitando la usabilidad.

## HechoContribuyente
Hereda de `Hecho` agregando información de creador y lógica específica (por ejemplo, plazo de edición solo para hechos subidos por usuarios registrados).
Esto nos permite:
- Encapsular diferencias de comportamiento y atributos sin romper el polimorfismo para poder tratar las responsabilidades nuevas de los hechos asociados a un contribuyente.
- Encapsular la lógica de las responsabilidades mencionadas sin que los hechos normales tengan que tener métodos y atributos que no todos usan.
### Persistencia
La herencia entre entidades (`HechoContribuyente` hereda de `Hecho`) se mapea en Hibernate, permitiendo:
- Persistir de manera eficiente los atributos específicos de hechos subidos por usuarios registrados.
- Mantener el polimorfismo en las consultas.

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

### 3. Persistencia
- Las colecciones no mantienen una lista fija de hechos como atributo persistido, sino que la pertenencia se determina dinámicamente a partir de los criterios y la fuente. Esto favorece la consistencia y evita problemas de sincronización o duplicidad al persistir datos.
- Se normalizan las relaciones entre colecciones y hechos, facilitando consultas y generación de estadísticas por colección.

---

# Paquete filtros

---

## Interfaz Filtro
Se define la interfaz `Filtro` con un único método `cumple(Hecho hecho)`, que permite determinar si un hecho cumple cierto criterio.

**Justificación:**
- Implementa el **Patrón Strategy**, permitiendo encapsular y abstraer el criterio de filtrado respecto del resto del sistema.
- Facilita la **extensibilidad y el polimorfismo**: agregar nuevos tipos de filtros no requiere modificar código existente, solo implementar la interfaz.
- Permite inyectar filtros tanto en colecciones (como criterio de pertenencia) como en búsquedas, haciendo el sistema mucho más flexible y desacoplado.
- Se utiliza herencia con estrategia `SINGLE_TABLE` y discriminador (`tipo_filtro`), lo que permite centralizar todos los filtros en una única tabla, simplificando la administración, consultas y extensibilidad.
- La persistencia permite que los filtros sean reutilizados y compartidos entre distintas colecciones y búsquedas, optimizando el acceso a criterios ya definidos.

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

## Persistencia
Todas las solicitudes (y sus subtipos) se persisten mediante Hibernate, utilizando herencia con estrategia `SINGLE_TABLE` y discriminador `tipo_solicitud`.  
Esta decisión permite centralizar toda la información en una única tabla, diferenciando los tipos de solicitud y facilitando la extensibilidad y el mantenimiento.
La relación entre solicitudes y hechos se normaliza, permitiendo la trazabilidad y la auditoría completa de todas las acciones realizadas sobre los hechos.  
El estado de cada solicitud (pendiente, aceptada, rechazada) se persiste, garantizando la rendición de cuentas y la posibilidad de reconstruir el historial de decisiones tomadas por administradores y contribuyentes.
El modelo evita la desnormalización y la duplicidad, y facilita la generación de estadísticas, reportes y exportación de datos, alineándose con los requerimientos de transparencia y accountability planteados por MetaMapa.
La estructura persistente soporta la incorporación de nuevos subtipos de solicitud y nuevas acciones, sin modificar la arquitectura principal, asegurando la escalabilidad y la robustez del sistema.

---

# Paquete Fuentes

---

## Interfaz Fuente
Se define la interfaz `Fuente` como punto de entrada único para la obtención de hechos desde cualquier origen.  
Incluye el método:
- `cargarHechos(ParametrosConsulta parametros)`: permite la obtención de hechos con o sin parámetros de consulta, admitiendo filtrado y consultas flexibles.

**Justificación:**
- **Polimorfismo:** Permite tratar todas las fuentes (estáticas, dinámicas, proxy) de la misma manera desde el resto del sistema, desacoplando el código cliente de la implementación concreta.
- **Extensibilidad:** Agregar nuevas fuentes (por ejemplo, una API externa, una base de datos, etc.) solo requiere implementar la interfaz, sin modificar el resto del sistema.
- **Inyección de dependencias y testeo:** Facilita el uso de mocks o stubs en tests al programar contra la interfaz.

---

## FuenteEstatica y LectorCSV
`FuenteEstatica` implementa `Fuente` para cargar hechos desde archivos CSV, delegando el parseo a la clase `LectorCSV`.  
`LectorCSV` abstrae el proceso de lectura y transformación de datos externos al modelo interno (`Hecho`).

**Justificación:**
- **Single Responsibility Principle (SRP):** Cada clase tiene una responsabilidad única: `FuenteEstatica` representa la fuente, mientras que `LectorCSV` se encarga de la lectura y transformación del CSV. De esta forma, el parseo y la lógica de negocio se mantienen claramente separados.
- **Extensibilidad:** Cambios en el formato de los archivos o en la lógica de transformación sólo afectan a `LectorCSV`.

Cabe aclarar que para el manejo de los CSVs se utiliza una librería externa y además, para poder soportar cualquier tipo de CSV, se le permite al usuario definir los campos que va a tener el CSV y cuáles de estos se corresponden con cada atributo de Hecho.

---

## FuenteDinamica
Modela el flujo de subida y gestión de hechos provistos por contribuyentes, recurriendo a un repositorio en memoria (`RepositorioHechos`).  
Incluye métodos para subir y modificar hechos, validando permisos y plazos.

**Justificación:**
- **Responsabilidad clara:** Separa la lógica de negocio de la fuente (subida, modificación, permisos) del almacenamiento concreto, permitiendo evolucionar el repositorio a una base de datos en el futuro sin afectar la fuente.
- **Facilita testing:** Permite simular diferentes escenarios de contribución y modificación.
- **Cohesión:** Todas las reglas de pertenencia y modificación de hechos por contribuyentes se concentran en un mismo lugar.

---

## FuenteProxy (abstracta) - FuenteDemo y FuenteMetaMapa

**Decisión:**  
`FuenteProxy` es una abstracción para fuentes intermediarias (integraciones con APIs externas o sistemas de terceros).
- `FuenteMetaMapa` integra el sistema con otras instancias de MetaMapa mediante una API REST, usando un cliente HTTP y adaptador de datos.
- `FuenteDemo` modela la integración con un sistema externo ficticio, gestionando caché y antigüedad de datos.

**Justificación:**
- **Strategy y Template Method:** Permiten encapsular la lógica común de proxy y delegar los detalles concretos en subclases.
- **Desacoplamiento:** La lógica de integración y de parseo queda aislada en clientes y adaptadores, permitiendo modificar protocolos o formatos sin afectar el resto del sistema.
- **Caché y control de antigüedad:** En `FuenteDemo`, la gestión del caché interno y el control de tiempo permite cumplir con requisitos funcionales y de performance.
- **Testabilidad:** Permite simular fuentes externas y probar comportamientos de integración.

---

## Adaptadores de datos (`AdaptadorHechoDemo`, `AdaptadorHechoMetaMapa`)
Se crean adaptadores para convertir datos externos (por ejemplo, JSON, mapas de datos de APIs, etc.) al modelo interno `Hecho`.

**Justificación:**
- **Responsabilidades y aislamiento:** Permiten centralizar y aislar la lógica de transformación, facilitando los cambios si varía el formato externo y respetando el principio de responsabilidad única.
- **Reutilización:** Distintos clientes/fuentes pueden reutilizar el mismo adaptador, evitando duplicación de lógica.

## Persistencia
Todas las fuentes del sistema se persisten como entidades utilizando Hibernate y la estrategia `TABLE_PER_CLASS`. Esto permite mantener un registro auditable y flexible de todos los orígenes de hechos, facilitando la trazabilidad y la rendición de cuentas.  
Las relaciones entre fuentes (por ejemplo, en el `ServicioAgregacion`) se modelan y persisten mediante asociaciones relacionales (`@ManyToMany`), manteniendo la integridad y evitando duplicidad de datos.
La obtención de hechos se realiza dinámicamente en base a parámetros y criterios, evitando la persistencia redundante de listas de hechos en cada fuente.  
Componentes como el cache en memoria y las tareas programadas para actualización optimizan el rendimiento y cumplen los requerimientos funcionales y de arquitectura propuestos.
La persistencia soporta estrategias de búsqueda por texto libre y componentes estadísticos, permitiendo integrar y analizar datos provenientes de múltiples orígenes.
El diseño extensible y desacoplado permite incorporar fácilmente nuevas fuentes y adaptadores, evolucionando el sistema sin afectar su núcleo ni la estructura relacional principal.

---

# Paquete clientes

---

## Encapsulamiento de la integración con sistemas externos
Se crean clases específicas (`ClienteDemo`, `ClienteMetaMapa`) para encapsular y aislar la lógica de integración con sistemas externos, ya sean APIs ficticias o servicios REST reales.

**Justificación:**
- **Desacoplamiento:** Las fuentes (`FuenteDemo`, `FuenteMetaMapa`) delegan toda la lógica de acceso, comunicación y parseo de datos a los clientes. De esta manera, el sistema central nunca interactúa directamente con protocolos de red ni mapeos de datos externos.
- **Extensibilidad:** Permite agregar fácilmente nuevas integraciones (nuevos clientes) para distintos servicios externos sin modificar el resto del sistema.
- **Testabilidad:** Facilita la simulación o reemplazo de clientes en tests, permitiendo probar el sistema sin necesidad de acceder a servicios reales.
- **Responsabilidades:** Cada cliente tiene la única responsabilidad de interactuar con una API/servicio externo y transformar/obtener los datos necesarios, sin acoplarse a la lógica de negocio.
- **Adaptabilidad:** Si cambia el formato de la API externa, solo el cliente y su adaptador requieren cambios, minimizando el impacto en otras partes del sistema.

---

## ClienteDemo
Encapsula el consumo de una fuente externa ficticia, utilizando un adaptador para transformar el mapa de datos recibido en objetos del dominio (`Hecho`).

**Justificación:**
- **Aislamiento:** El sistema nunca interactúa directamente con la estructura de datos de la API demo; todo pasa por el cliente y el adaptador.
- **Reutilización:** El mismo cliente puede usarse en diferentes contextos o ser extendido para soportar nuevas formas de interacción.
- **Simulación:** Permite simular el funcionamiento de integraciones reales en entornos de desarrollo o prueba.

---

## ClienteMetaMapa
Responsable de la integración vía HTTP con instancias externas de MetaMapa, manejando la construcción de URLs, el envío de solicitudes, el parseo de respuestas JSON y el envío de solicitudes de eliminación.

**Justificación:**
- **Centralización:** Toda la lógica de comunicación y transformación con la API MetaMapa está en un único lugar.
- **Robustez y control de errores:** El cliente maneja errores de red y de parseo de manera centralizada, facilitando el manejo de fallos y la trazabilidad.
- **Escalabilidad:** Si la API MetaMapa evoluciona, los cambios de integración quedan aislados en esta clase.

## Persistencia
Los clientes no interactúan directamente con la base de datos; su responsabilidad es obtener, transformar y adaptar datos externos al modelo de dominio, que luego será persistido por las fuentes o servicios correspondientes según las reglas de negocio.

---

# Paquete repositorios

---

## Patrón Repositorio y centralización de la persistencia
Se implementan clases repositorio para abstraer y centralizar el acceso, almacenamiento y gestión de las entidades principales del dominio (colecciones, hechos y solicitudes).

**Justificación:**
- **Patrón Repositorio:** Separa la lógica de acceso y manipulación de datos del resto del dominio, permitiendo que las entidades y servicios no conozcan los detalles de la persistencia.
- **Centralización:** Todos los accesos y modificaciones sobre las colecciones, hechos y solicitudes se realizan a través de sus respectivos repositorios, evitando dispersión de lógica y facilitando el mantenimiento.
- **Desacoplamiento:** Permite cambiar la forma de almacenamiento (de memoria a base de datos real, archivos, etc.) sin impactar en el dominio ni en los servicios que consumen los repositorios.
- **Singletons:** Cada repositorio se implementa como un singleton, asegurando una única instancia que maneje la persistencia.

---

## RepositorioColecciones
Gestiona las colecciones mediante un mapa indexado por `handle`, permitiendo operaciones de guardar, buscar, listar, eliminar y actualizar colecciones.

**Justificación:**
- **Acceso eficiente:** El uso de un mapa por `handle` permite búsquedas rápidas y únicas, usando identificadores únicos para colecciones.
- **Responsabilidad:** La clase tiene una única responsabilidad: manejar el almacenamiento y recuperación de colecciones.

---

## RepositorioHechos
Almacena los hechos agrupados por fuente. Permite obtener todos los hechos, obtener hechos de una fuente específica, guardar, eliminar (lógico, vía visibilidad) y actualizar hechos.

**Justificación:**
- **Organización por fuente:** Refleja el modelo descentralizado del sistema, donde los hechos pueden provenir de distintas fuentes.
- **Soporte a eliminación lógica:** La función `eliminar` marca el hecho como no visible en lugar de borrarlo físicamente, pudiendo cumplir posibles requerimientos futuros de accountability y trazabilidad.
- **Cohesión:** Toda la lógica de pertenencia, actualización y eliminación de hechos se encuentra concentrada en una única clase.

---

## RepositorioSolicitudes
Puede gestionar cualquier tipo de solicitud (por ejemplo, de eliminación, revisión, edición, etc.) por cada instancia del mismo mediante el manejo de una lista de objetos del tipo `Solicitud`, en vez de limitarlo a un subtipo específico.

**Justificación:**
- **Flexibilidad y extensibilidad:** Permite que el repositorio soporte de manera uniforme cualquier clase que herede de `Solicitud`, independientemente de su tipo o propósito.
- **Reutilización:** Evita la necesidad de crear un repositorio diferente para cada subtipo de solicitud, promoviendo la reutilización de código y la simplicidad arquitectónica.
- **Desacoplamiento:** Los servicios que consumen el repositorio pueden operar sobre la abstracción general (`Solicitud`), sin requerir conocimiento de los detalles concretos de cada subtipo.

---

## RepositorioEstadisticas

Gestiona el acceso y actualización de los componentes estadísticos del sistema, permitiendo responder consultas agregadas sobre hechos, colecciones, categorías, provincias, horas y solicitudes de spam.

**Justificación:**
- **Responsabilidad:** Centraliza la lógica de obtención, refresco y consulta de estadísticas en una única clase, facilitando mantenimiento, extensión y reutilización.
- **Optimización y rendimiento:** Utiliza consultas nativas y vistas materializadas para calcular y almacenar resultados agregados, permitiendo responder rápidamente a consultas frecuentes y exportar a CSV sin recalcular todo el histórico.
- **Desnormalización controlada:** Las estadísticas se almacenan en vistas materializadas separadas del modelo normalizado, lo que permite acelerar la consulta y mantener actualizados los resultados.
- **Integridad y consistencia:** El repositorio se encarga de refrescar periódicamente los datos estadísticos y asegurar la coherencia entre los datos persistidos y los resultados mostrados en la interfaz.
- **Extensibilidad:** Permite incorporar nuevas métricas y consultas estadísticas agregando nuevos métodos y vistas, sin modificar el modelo principal ni la lógica de negocio.
- **Alineación con requerimientos:** Permite responder preguntas clave del dominio (top de provincias por colección, categoría más reportada, cantidad de solicitudes de spam, etc.) en forma eficiente y auditable.

## Persistencia
Los repositorios gestionan la persistencia de las entidades principales del dominio mediante el uso de `EntityManager` y transacciones de JPA/Hibernate.  
Esto asegura la integridad de las operaciones y permite desacoplar el acceso a la base de datos del resto del sistema, facilitando el mantenimiento y la evolución del modelo relacional.
Los repositorios implementan consultas especializadas para soportar búsqueda por full text search.

---

# Paquete dtos

---

## Desacoplamiento entre capas y protección del dominio
Se implementan clases DTO (Data Transfer Object), para trasladar datos entre capas del sistema y/o hacia el exterior (por ejemplo, APIs o interfaces gráficas), sin exponer directamente las entidades internas del dominio.

**Justificación:**
- **Desacoplamiento:** Los DTO actúan como una barrera entre las entidades de dominio y las interfaces externas, evitando filtrar detalles internos o lógica de negocio fuera de la capa correspondiente.
- **Protección del modelo:** Permite exponer solo los datos relevantes, omitiendo atributos sensibles, métodos o lógica que no deben ser accesibles desde el exterior.
- **Flexibilidad:** Si la estructura de las entidades de dominio cambia, los DTO pueden evolucionar de manera independiente, manteniendo la compatibilidad con clientes externos o APIs.
- **Facilita la serialización y deserialización:** Los DTO están preparados para ser convertidos fácilmente en formatos como JSON o XML, facilitando la comunicación con otros sistemas o capas.

---

## HechoDTO
Encapsula los datos de un hecho que se desean exponer (título, descripción, categoría, ubicación, fechas), construyéndose a partir de una instancia de `Hecho`.

**Justificación:**
- **Encapsulamiento de información:** Solo los datos seleccionados son expuestos, permitiendo cumplir con requerimientos de privacidad y claridad.
- **Prevención de fugas de lógica:** No se expone lógica de negocio ni métodos de la entidad, solo información de lectura.

---

## ParametrosConsulta
Encapsula los criterios de búsqueda y filtrado de hechos (categoría, fechas, ubicación, colección), facilitando la transferencia de estos criterios entre capas y la construcción dinámica de consultas.

**Justificación:**
- **Claridad de interfaz:** Agrupa todos los filtros posibles en un solo objeto, simplificando interfaces y métodos.
- **Extensibilidad:** Se pueden agregar nuevos criterios de filtrado sin modificar todas las firmas de métodos que los consumen.
- **Comodidad para integración:** El método `comoMapa()` permite transformar fácilmente los parámetros para construir URLs o consultas en APIs externas.

---

# Paquete DetectorSpam

---

## Abstracción de la lógica de detección de spam
Se define la interfaz `DetectorDeSpam` para abstraer la lógica de detección de spam, permitiendo desacoplar la validación de spam del resto del sistema.

**Justificación:**
- **Desacoplamiento:** Al definir una interfaz, la lógica de detección de spam no queda acoplada a una implementación particular. Esto permite que los servicios que la consumen (como el manejo de solicitudes) puedan trabajar con cualquier implementación.
- **Intercambiabilidad:** Facilita el reemplazo de la implementación.
- **Testabilidad:** Permite inyectar implementaciones de prueba para validar el funcionamiento del sistema sin depender de la lógica real de spam.

---

## Alternativas de implementación
La interfaz propuesta:
- **Permite implementar fácilmente ambas variantes:** una implementación local (TF-IDF u otro algoritmo simple), o una integración con APIs externas.
- **Reduce el impacto de cambios futuros:** Si se decide migrar de un detector local a un servicio externo (o viceversa), solo se debe cambiar la clase concreta, no el resto del sistema.
- **Aborda preocupaciones de privacidad y costos:** El encapsulamiento de la lógica permite evaluar y gestionar los riesgos asociados (privacidad de los textos enviados, costos de consumo de APIs externas, etc.) en un único lugar.

---

# Paquete Consenso

---

## Abstracción AlgoritmoConsenso y subtipos
Se define la clase abstracta `AlgoritmoConsenso` como el núcleo de la lógica de consenso en MetaMapa, junto con subclases que implementan algoritmos concretos.  
Cada uno de estos algoritmos responde a criterios distintos para determinar si un hecho debe ser considerado consensuado en el contexto de una colección.

**Justificación:**
- **Polimorfismo y extensibilidad:** El diseño permite que cada colección o servicio agregador seleccione el algoritmo de consenso más adecuado según sus necesidades, simplemente asociando una instancia concreta de `AlgoritmoConsenso`. Para agregar nuevos algoritmos solo es necesario definir una nueva subclase.
- **Desacoplamiento:** La lógica de consenso queda separada de las colecciones y de la obtención de hechos, facilitando el mantenimiento y la evolución futura del sistema sin modificar el resto del dominio.
- **Patrón Strategy:** Cada algoritmo puede ser intercambiado y configurado en tiempo de ejecución, cumpliendo los principios de diseño orientados a objetos y permitiendo experimentar o ajustar el nivel de confianza requerido por cada colección.
- **Centralización y control:** La abstracción asegura que todas las decisiones de consenso se tomen a través de una única interfaz (`tieneConsenso`), favoreciendo la trazabilidad y el control de las reglas aplicadas.
- **Persistencia y diseño relacional:** Los algoritmos de consenso se persisten mediante Hibernate usando herencia con la estrategia `SINGLE_TABLE` y discriminador de tipo, permitiendo rastrear y auditar qué algoritmo se aplicó en cada colección y facilitando la evolución del modelo relacional.

---

## ConsensoAbsoluto
Exige el máximo nivel de acuerdo: un hecho debe estar presente en todas las fuentes para considerarse consensuado. Es útil para colecciones que requieren máxima confiabilidad y minimizan el riesgo de reportar información falsa o dudosa.

## ConsensoMayoriaSimple
Determina consenso cuando más de la mitad de las fuentes contienen el mismo hecho, balanceando confiabilidad y flexibilidad. Permite aceptar hechos con alta probabilidad de veracidad sin requerir unanimidad.

## ConsensoMultiplesMenciones
Apunta a reducir la desconfianza en hechos dudosos, exigiendo que al menos dos fuentes incluyan hechos con igual título y que no haya versiones contradictorias. Es útil para detectar consensos en reportes redundantes y evitar la propagación de información falsa por error o duplicación.

## ConsensoDefault
-Considera todos los hechos como consensuados, útil para colecciones que no requieren curación o para situaciones de configuración rápida. Puede ser usado como fallback.

---

## Persistencia y trazabilidad

- **Herencia relacional:** Todos los algoritmos se persisten en una única tabla, diferenciados por un campo discriminador, facilitando la consulta, auditoría y evolución del modelo.
- **Configurabilidad por colección:** Cada colección puede asociar el algoritmo de consenso que desee, y esto queda registrado en la base de datos, permitiendo auditar las decisiones y modificar el nivel de confianza requerido de manera sencilla.
Usamos single table en los algoritmos de consenso, ya que, no tienen atributos propios, lo cual va a generar que no hayan campos en null en la tabla de consenso.

---

## ModoNavegacion

Se define la enumeración `ModoNavegacion` para modelar los dos modos de navegación de hechos en una colección:
- **Irrestricta:** muestra todos los hechos sin curación ni filtro de consenso.
- **Curada:** muestra solo los hechos consensuados según el algoritmo especificado.

**Justificación:**
- **Claridad de interfaz:** Permite a los usuarios elegir el modo de navegación según sus preferencias de confiabilidad y exhaustividad.

---

# Paquete usuarios

---

## Contribuyente
Se modela la clase `Contribuyente` como la entidad que representa a las personas que aportan información al sistema MetaMapa, sean anónimas o identificadas.

**Justificación:**
- **Responsabilidad y claridad:** La clase encapsula los datos personales relevantes de cada contribuyente (nombre, apellido, edad), permitiendo diferenciar entre contribuyentes anónimos (solo nombre obligatorio) y registrados (nombre, apellido y edad).
- **Privacidad y protección de datos:** Siguiendo los lineamientos del sistema, los datos personales de los contribuyentes nunca se exponen públicamente ni se utilizan para la navegación o consulta de información. Se almacenan únicamente para fines de administración y trazabilidad, cumpliendo con las exigencias de protección de identidad.
- **Persistencia y accountability:** Los atributos del contribuyente se persisten mediante ORM, permitiendo auditar quién aportó cada hecho y facilitando la rendición de cuentas ante solicitudes de revisión o eliminación de información, sin exponer estos datos a terceros.
- **Extensibilidad:** El modelo permite agregar fácilmente nuevos atributos para los contribuyentes (por ejemplo, email, roles, validaciones adicionales) sin afectar la lógica principal de subida y gestión de hechos.
- **Integración con el dominio:** La relación entre contribuyentes y hechos queda claramente establecida, permitiendo que los hechos subidos por usuarios registrados tengan funcionalidades adicionales (modificación, seguimiento de solicitudes, etc.), como requiere el sistema MetaMapa.
- **Control de acceso y edición:** El diseño contempla la posibilidad de asignar permisos diferenciados a contribuyentes registrados (por ejemplo, edición durante un plazo determinado), alineándose con los requerimientos de colaboración y control de calidad del sistema.

---

# Paquete servicios

---

## ExportadorCSV
Encapsula la lógica de exportación de datos estadísticos y resultados de consultas a archivos CSV, permitiendo la interoperabilidad con otras instituciones y el análisis externo de los datos del sistema.

**Justificación:**
- **Desacoplamiento y reutilización:** Centraliza la funcionalidad de exportación, evitando duplicación de lógica y permitiendo su reutilización en distintos módulos del sistema (estadísticas, reportes, backups).
- **Interoperabilidad:** El formato CSV es ampliamente aceptado, permitiendo la integración sencilla con otras ONG, universidades o sistemas de análisis de datos.
- **Compatibilidad con vistas materializadas:** El diseño permite exportar directamente desde vistas o tablas agregadas, optimizando la performance y facilitando la actualización periódica de los datos expuestos.
- **Privacidad y control:** El servicio puede ser configurado para omitir o filtrar datos sensibles, alineándose con los requerimientos de protección de identidad de MetaMapa.

---

## GeorefAPI
Gestiona la integración con servicios externos para obtener información geográfica (por ejemplo, provincia a partir de latitud y longitud).

**Justificación:**
- **Aislamiento de integración externa:** Encapsula el acceso a APIs externas, desacoplando la lógica de georreferenciación del resto del sistema y facilitando su evolución ante cambios en el servicio externo.
- **Facilita el enriquecimiento de datos:** Permite agregar valor a los hechos mediante la asignación automática de información geográfica relevante para estadísticas y visualizaciones.
- **Robustez y control de errores:** Centraliza el manejo de errores y la validación de respuestas, evitando impactos negativos sobre el dominio ante fallos o cambios en la API externa.

---

## HechoService
Orquesta la lógica de búsqueda y consulta avanzada de hechos persistidos, integrando algoritmos de búsqueda por texto libre y similitud.

**Justificación:**
- **Centralización y claridad:** El servicio actúa como punto único de acceso para las búsquedas de hechos, simplificando la interacción desde la interfaz y otros módulos.
- **Optimización y experiencia de usuario:** Implementa full text search para mejorar la relevancia de los resultados y la experiencia de las personas usuarias.
- **Extensibilidad:** Permite incorporar nuevos métodos de búsqueda, filtros o criterios sin afectar el resto de la lógica de persistencia ni la interfaz de usuario.
- **Interacción desacoplada con el repositorio:** El servicio utiliza el repositorio como backend, respetando el patrón de separación de capas y facilitando testing y mantenimiento.

---

# Paquete cron

---

## TareasCronometradas
Encapsula la lógica de ejecución de tareas programadas en el sistema MetaMapa, permitiendo la calendarización y automatización de procesos críticos como la actualización de cachés, la curación de hechos y la sincronización periódica de fuentes externas.

**Justificación:**
- **Automatización y eficiencia:** El diseño permite que operaciones costosas (por ejemplo, actualización de cachés, curación de hechos, sincronización con fuentes proxy) se realicen en horarios de baja carga o de forma periódica, optimizando el uso de recursos y mejorando la experiencia de usuario.
- **Desacoplamiento:** Las tareas cronometradas operan sobre servicios y entidades preexistentes (fuentes, colecciones, agregadores), manteniendo la lógica de negocio desacoplada del mecanismo de ejecución de las tareas. Esto facilita el mantenimiento y la evolución del sistema sin modificar el core.
- **Configurabilidad y extensibilidad:** El uso de argumentos en la línea de comandos habilita la ejecución flexible de distintas rutinas desde scripts o sistemas operativos.
- **Accountability y trazabilidad:** La ejecución programada de tareas garantiza la regularidad y el registro de procesos críticos, permitiendo auditar cuándo y cómo se realizó cada operación importante.

---

# Paquete estadisticas

---

## Componentes estadísticos y persistencia
Se modelan entidades estadísticas específicas (`EstadisticaCategoriaTop`, `EstadisticaHoraPorCategoriaTop`, `EstadisticaProvinciaPorCategoriaTop`, `EstadisticaProvinciaPorColeccion`, `EstadisticaSolicitudesSpam`) que representan respuestas a las principales preguntas de análisis requeridas por MetaMapa.  
Cada clase se corresponde con una consulta a una vista materializada en la base de datos, la cual se actualiza periódicamente usando tareas programadas.

**Justificación:**
- **Optimización y rendimiento:** Utilizar vistas materializadas permite precalcular y almacenar resultados agregados, optimizando el acceso y la consulta de estadísticas, especialmente cuando se trata de grandes volúmenes de datos.
- **Modularidad y claridad:** Cada entidad estadística encapsula una métrica clave del dominio, facilitando la consulta, visualización y exportación de resultados, y permitiendo ampliar el sistema con nuevas métricas sin impactar el modelo principal.
- **Persistencia desacoplada:** Las entidades estadísticas no modifican la información de hechos o colecciones, sino que reflejan el resultado de cálculos agregados, preservando la integridad y evitando redundancias en el modelo relacional.
- **Actualización periódica:** Las vistas materializadas se refrescan mediante tareas programadas, asegurando que los resultados sean representativos de los datos actuales y que la carga de cálculo no impacte la experiencia de usuario.
- **Inmutabilidad y auditoría:** Las entidades se modelan como `@Immutable`, garantizando que los resultados estadísticos sean consistentes y sólo se modifiquen mediante el refresco programado, facilitando la trazabilidad y la rendición de cuentas.
- **Soporte para exportación:** Al estar directamente vinculadas a consultas sobre vistas materializadas, las entidades estadísticas pueden ser exportadas fácilmente en formato CSV, cumpliendo los requerimientos de interoperabilidad y análisis externo de MetaMapa.

---

# 📌 Diagrama de Clases General

A continuación se presenta el diagrama UML general del dominio del sistema, en el que se modelan los principales conceptos como hechos, colecciones, contribuyentes, fuentes de datos y solicitudes de eliminación.

![Diagrama UML General](/Diagramas/classDiagram.png)

---

# 👤 Casos de uso
A continuación se presentan los casos de uso, que describen las interacciones entre los actores y el sistema.

![Casos de uso](/Diagramas/casosDeUso.png)

---