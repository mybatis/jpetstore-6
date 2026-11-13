## ArchUnit

ArchUnit es una herramienta que permite comprobar la **arquitectura** del código Java mediante tests de JUnit.
Analiza el bytecode de las clases y verifica reglas sobre dependencias entre capas, nombres, anotaciones,
ubicación de las clases y ciclos entre paquetes.

### Configuración

La dependencia se encuentra añadida en:

```text
pom.xml
```

```xml
<dependency>
  <groupId>com.tngtech.archunit</groupId>
  <artifactId>archunit-junit5</artifactId>
  <version>1.5.0</version>
  <scope>test</scope>
</dependency>
```

Las reglas de ArchUnit se encuentran definidas en:

```text
src/test/java/org/mybatis/jpetstore/ArchitectureTest.java
```

Se analizan todas las clases del paquete `org.mybatis.jpetstore`, excluyendo las clases de test
(`ImportOption.DoNotIncludeTests`).

### Ejecución

Para ejecutar solo las reglas de ArchUnit:

```bash
./mvnw test -Dtest=ArchitectureTest
```

En Windows:

```powershell
.\mvnw.cmd test -Dtest=ArchitectureTest
```

También se puede ejecutar mediante Maven:

```bash
mvn test -Dtest=ArchitectureTest
```

Al ser tests de JUnit, también se ejecutan junto con el resto de pruebas del proyecto:

```bash
./mvnw test
```

El resultado de la ejecución se genera en:

```text
target/surefire-reports/TEST-org.mybatis.jpetstore.ArchitectureTest.xml
target/surefire-reports/org.mybatis.jpetstore.ArchitectureTest.txt
```

Si alguna regla se incumple, el test falla e indica la clase y la dependencia que la viola.

#### Reglas incumplidas: `@ArchIgnore`

Las reglas que detectan un problema real en el código están incluidas en el test, pero marcadas con
`@ArchIgnore`, cuyo motivo indica la issue de GitHub donde está documentado el problema:

```java
@ArchTest
@ArchIgnore(reason = "Incumplida: OrderService.getNextId escribe sin @Transactional (issue #15)")
static final ArchRule serviceMethodsThatWriteShouldBeTransactional = ...
```

Así el build no falla mientras el problema esté abierto, y en el informe la regla aparece como omitida
(*skipped*) junto con su motivo. Cuando se corrija la issue, basta con borrar la línea `@ArchIgnore` para que la
regla vuelva a ejecutarse.

Para ver la violación, se quita el `@ArchIgnore` de la regla y se ejecuta el test.

### Reglas aplicadas

El test contiene **19 reglas**: 16 se ejecutan y pasan, y 3 están marcadas con `@ArchIgnore` porque detectan
problemas reales.

#### Arquitectura por capas

* **layers**: comprueba la arquitectura por capas (`web` → `service` → `mapper` → `domain`). La capa web no
  puede ser accedida por ninguna otra, los servicios solo desde web, los mappers solo desde los servicios y el
  dominio desde web, service y mapper.

* **controllersShouldNotUseMappers**: los controladores no pueden acceder directamente a los mappers, deben pasar
  por la capa de servicio.

* **noCycles**: no existen dependencias cíclicas entre los paquetes de primer nivel (`web`, `service`, `mapper`,
  `domain`...).

#### Listas blancas de dependencias

Cada capa tiene una regla que indica **de qué paquetes puede depender**; cualquier otra dependencia está
prohibida, incluidas las que no se hayan previsto.

* **domainShouldBeIndependent**: las clases del dominio solo dependen del JDK y del propio dominio (son POJOs).

* **servicesShouldOnlyDependOnAllowedPackages**: los servicios solo dependen del JDK, del dominio, de los
  mappers y de las anotaciones de Spring `@Service` y `@Transactional`, de forma que no conocen nada de HTTP.

* **mappersShouldOnlyDependOnDomain**: los mappers solo dependen del JDK y del dominio, sin usar Spring, HTTP ni
  otras capas.

* **webShouldOnlyDependOnAllowedPackages**: la capa web solo depende del JDK, del dominio, de los servicios, de
  la API de servlets y de Spring MVC, de forma que los controladores no pueden usar MyBatis ni otras librerías
  directamente.

#### Nombres, anotaciones y ubicación

* **controllersNaming**: las clases de `web.controllers` terminan en `Controller` y llevan la anotación
  `@Controller`.

* **controllersShouldResideInControllersPackage**: toda clase con `@Controller` debe estar en
  `web.controllers`, que es el paquete que escanea Spring.

* **servicesNaming**: las clases de `service` terminan en `Service` y llevan la anotación `@Service`.

* **servicesShouldResideInServicePackage**: toda clase con `@Service` debe estar en el paquete `service`, que es
  el paquete que escanea Spring.

* **mappersShouldBeInterfaces**: los mappers son interfaces terminadas en `Mapper`, para que MyBatis las
  implemente a partir de su XML.

#### Spring y transacciones

* **transactionalOnlyInServices**: la anotación `@Transactional` solo se utiliza en la capa de servicio.

* **transactionalMethodsShouldBePublic**: `@Transactional` solo se pone en métodos públicos. En Spring 5 la
  anotación se ignora sin avisar en los métodos que no son públicos, y el método se ejecutaría sin transacción.

* **noSelfInvocationOfTransactionalMethods**: ningún método llama a un método `@Transactional` de su misma
  clase. Spring aplica la transacción mediante un *proxy*, y una llamada interna no pasa por él, así que la
  anotación no tendría efecto. Usa la regla predefinida de ArchUnit `ProxyRules`.

* **springBeansShouldBeStateless**: los controladores y servicios no guardan estado. Spring crea una sola
  instancia de cada uno y la comparten todas las peticiones, así que sus atributos solo pueden ser constantes
  (`static`), `final` o inyectados (`@Autowired`).

#### Reglas que detectan problemas (con `@ArchIgnore`)

* **controllersShouldNotDependOnEachOther**: los controladores no dependen unos de otros. Cada controlador,
  junto con sus clases anidadas, forma un grupo, y ningún grupo puede usar clases de otro. Como todos los
  controladores están en el mismo paquete, los grupos no se pueden formar por paquete con `slices().matching()`,
  así que se usa un `SliceAssignment` propio que agrupa cada clase por su clase más externa.

* **serviceMethodsThatWriteShouldBeTransactional**: los métodos públicos de los servicios que escriben en la base
  de datos (llaman a un `insert…`, `update…` o `delete…` de un mapper, también dentro de lambdas) llevan
  `@Transactional`, para que todas sus escrituras se confirmen o se deshagan juntas. Usa una `ArchCondition`
  propia, porque la condición depende de lo que hace el método y no de una propiedad fija.

* **noLocaleSensitiveCaseConversion**: no se usa `toLowerCase()` ni `toUpperCase()` sin `Locale`, porque el
  resultado depende del idioma del servidor (en turco, `"I".toLowerCase()` da `"ı"`, una i sin punto). Las
  versiones con `Locale` sí están permitidas.

### Cobertura

ArchUnit **no aporta cobertura de código**: analiza el bytecode de las clases sin ejecutarlas. Medido de forma
aislada con JaCoCo, ejecutando solo `ArchitectureTest`, la cobertura es del **0 % (0 de 2.028 instrucciones)**.

Por eso la cobertura se mide de forma **estructural**, contando a qué clases de producción se aplica cada regla:

| Métrica | Valor |
|---|---|
| Clases de producción analizadas | 26 de 26 (100 %) |
| Comprobaciones clase-regla | 124 |
| Media de reglas por clase | 4,77 |

Este cálculo se hizo con un programa externo que replica los filtros `.that()` de las 12 reglas iniciales usando
el `ClassFileImporter` de ArchUnit. Las 7 reglas añadidas después no están incluidas en estas cifras.

Las interfaces del paquete `mapper` no aparecen en el informe de JaCoCo, porque no tienen instrucciones que
ejecutar, pero ArchUnit sí las analiza: se les aplican 5 reglas a cada una.

### Resultados

Resultado de `./mvnw test -Dtest=ArchitectureTest`:

```text
Tests run: 19, Failures: 0, Errors: 0, Skipped: 3
```

| Regla | Resultado |
|---|---|
| layers | ✅ Pasa |
| controllersShouldNotUseMappers | ✅ Pasa |
| noCycles | ✅ Pasa |
| domainShouldBeIndependent | ✅ Pasa |
| servicesShouldOnlyDependOnAllowedPackages | ✅ Pasa |
| mappersShouldOnlyDependOnDomain | ✅ Pasa |
| webShouldOnlyDependOnAllowedPackages | ✅ Pasa |
| controllersNaming | ✅ Pasa |
| controllersShouldResideInControllersPackage | ✅ Pasa |
| servicesNaming | ✅ Pasa |
| servicesShouldResideInServicePackage | ✅ Pasa |
| mappersShouldBeInterfaces | ✅ Pasa |
| transactionalOnlyInServices | ✅ Pasa |
| transactionalMethodsShouldBePublic | ✅ Pasa |
| noSelfInvocationOfTransactionalMethods | ✅ Pasa |
| springBeansShouldBeStateless | ✅ Pasa |
| controllersShouldNotDependOnEachOther | ❌ Incumplida (`@ArchIgnore`, issue #14) |
| serviceMethodsThatWriteShouldBeTransactional | ❌ Incumplida (`@ArchIgnore`, issue #15) |
| noLocaleSensitiveCaseConversion | ❌ Incumplida (`@ArchIgnore`, issue #3) |

#### Violaciones encontradas

* **controllersShouldNotDependOnEachOther** ([#14](https://github.com/ivanafons0/jpetstore-6/issues/14)): `OrderController` usa la clase anidada
  `AccountController.AccountSession` para comprobar si el usuario ha iniciado sesión, en 6 llamadas
  (`OrderController.java`, líneas 78, 83, 104, 111, 225 y 229).

  ```text
  Slice OrderController depends on Slice AccountController
  ```

* **serviceMethodsThatWriteShouldBeTransactional** ([#15](https://github.com/ivanafons0/jpetstore-6/issues/15)): `OrderService.getNextId` es público
  y actualiza la secuencia de pedidos sin `@Transactional` (`OrderService.java:135`). Aunque se llama `get…`,
  reserva el siguiente número de pedido, así que tiene que escribir en la base de datos.

  ```text
  org.mybatis.jpetstore.service.OrderService.getNextId(java.lang.String) writes to the database [SequenceMapper.updateSequence] but is not @Transactional
  ```

* **noLocaleSensitiveCaseConversion** ([#3](https://github.com/ivanafons0/jpetstore-6/issues/3)): `toLowerCase()` sin `Locale` en
  `CatalogService.java:114` y `CatalogController.java:132`.

  ```text
  Method <org.mybatis.jpetstore.service.CatalogService.searchProductList(java.lang.String)> calls method <java.lang.String.toLowerCase()> in (CatalogService.java:114)
  Method <org.mybatis.jpetstore.web.controllers.CatalogController.searchProducts(java.lang.String, org.springframework.ui.Model)> calls method <java.lang.String.toLowerCase()> in (CatalogController.java:132)
  ```

#### Otras reglas probadas y no incluidas

* **NO_CLASSES_SHOULD_USE_FIELD_INJECTION** (de `GeneralCodingRules`): **falla**. Hay 5 `@Autowired` sobre
  atributos en los controladores:
  * `AccountController` (2)
  * `CartController` (1)
  * `CatalogController` (1)
  * `OrderController` (1)

  Los servicios, en cambio, ya usan inyección por constructor.

* **Clases de dominio `Serializable`**: pasa. Las 9 clases de dominio son `Serializable`.

* **Ciclos a nivel de clase** (`matching("org.mybatis.jpetstore.(**)")`): pasa. No hay ciclos entre clases.

### Análisis

La arquitectura de JPetStore es limpia en lo general: las dependencias van siempre en el sentido `web` →
`service` → `mapper` → `domain`, cada capa depende solo de lo que necesita, el dominio está formado por POJOs
independientes y las transacciones se gestionan solo en la capa de servicio, en métodos públicos y sin
auto-invocaciones.

Las reglas más específicas encontraron **3 problemas**, documentados como issues en GitHub:

* **#14, acoplamiento entre controladores** (diseño): `OrderController` depende de `AccountController` a través
  de `AccountSession`. Se corregiría sacando `AccountSession` a su propia clase fuera de `web.controllers`.

* **#15, método público que escribe sin transacción** (diseño): hoy funciona porque `getNextId` solo se llama
  desde `insertOrder`, que sí es transaccional, pero al ser público se puede llamar sin transacción, como ya hace
  el test de rendimiento de la rama `JETM`.

* **#3, conversión de mayúsculas sin `Locale`** (fallo): es el mismo problema que encontró **SpotBugs**
  (`DM_CONVERT_CASE`), en los mismos dos puntos. Dos herramientas distintas llegan al mismo resultado: SpotBugs
  con un detector predefinido y ArchUnit con una regla escrita para ello. La regla, además, impide que el
  problema vuelva a aparecer.

La **inyección por campo** en los controladores también se detectó, pero se decidió no incluir esa regla en el
test porque:

* solo afecta a cómo se construyen los objetos en los tests (que usan `@InjectMocks`), no al funcionamiento de la
  aplicación;

Para corregirlo habría que declarar los atributos de los controladores como `private final` e inyectarlos por
constructor, o bien usar `FreezingArchRule` para congelar las 5 violaciones actuales y que solo fallen las nuevas.

### Limitaciones

* **Las listas blancas permiten todo el JDK** (`java..`), en las cuatro capas. Por ejemplo, un controlador que
  abriese una conexión JDBC directa (`java.sql`) seguiría pasando las reglas. Para evitarlo habría que sustituir
  `java..` por los paquetes concretos del JDK que se usan.

* **Mientras una regla está marcada con `@ArchIgnore` no se ejecuta**, así que tampoco detecta violaciones nuevas
  de esa misma regla. La alternativa que sí lo haría es `FreezingArchRule`, a cambio de guardar las violaciones
  actuales en ficheros dentro del repositorio.

* **`serviceMethodsThatWriteShouldBeTransactional` se basa en la convención de nombres** de los mappers: considera
  escrituras los métodos que empiezan por `insert`, `update` o `delete`. Un método de escritura con otro nombre no
  se detectaría.

* **Las lambdas dependen del compilador.** Con javac (Maven), ArchUnit atribuye al método las llamadas hechas
  dentro de sus lambdas; con el compilador de Eclipse (VS Code), las lambdas aparecen como métodos ocultos aparte
  (`lambda$0`, `lambda$1`...). La regla `serviceMethodsThatWriteShouldBeTransactional` lo tiene en cuenta y
  funciona con los dos; se comprobó con ambos compiladores.

* **`noCycles` solo detecta ciclos entre paquetes de primer nivel**, no dentro de un mismo paquete (por ejemplo,
  entre `CatalogService` y `OrderService`). Se comprobó aparte que actualmente no hay ciclos entre clases, pero la
  regla no lo vigila.

* **No sustituye a las pruebas funcionales.** ArchUnit comprueba la estructura del código, no su comportamiento:
  una aplicación puede respetar todas las reglas y tener errores de lógica.
