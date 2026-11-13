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

### Reglas aplicadas

* **layers**: comprueba la arquitectura por capas (`web` → `service` → `mapper` → `domain`). La capa web no
  puede ser accedida por ninguna otra, los servicios solo desde web, los mappers solo desde los servicios y el
  dominio desde web, service y mapper.

* **controllersShouldNotUseMappers**: los controladores no pueden acceder directamente a los mappers, deben pasar
  por la capa de servicio.

* **domainShouldBeIndependent**: las clases del dominio solo dependen del JDK y del propio dominio (son POJOs).

* **controllersNaming**: las clases de `web.controllers` terminan en `Controller` y llevan la anotación
  `@Controller`.

* **controllersShouldResideInControllersPackage**: toda clase con `@Controller` debe estar en
  `web.controllers`, que es el paquete que escanea Spring.

* **servicesNaming**: las clases de `service` terminan en `Service` y llevan la anotación `@Service`.

* **servicesShouldResideInServicePackage**: toda clase con `@Service` debe estar en el paquete `service`, que es
  el paquete que escanea Spring.

* **servicesShouldOnlyDependOnAllowedPackages**: los servicios solo dependen del JDK, del dominio, de los
  mappers y de las anotaciones de Spring `@Service` y `@Transactional`, de forma que no conocen nada de HTTP.

* **transactionalOnlyInServices**: la anotación `@Transactional` solo se utiliza en la capa de servicio.

* **mappersShouldBeInterfaces**: los mappers son interfaces terminadas en `Mapper`, para que MyBatis las
  implemente a partir de su XML.

* **mappersShouldOnlyDependOnDomain**: los mappers solo dependen del JDK y del dominio, sin usar Spring, HTTP ni
  otras capas.

* **noCycles**: no existen dependencias cíclicas entre los paquetes de primer nivel (`web`, `service`, `mapper`,
  `domain`...).

### Cobertura

ArchUnit **no aporta cobertura de código**: analiza el bytecode de las clases sin ejecutarlas. Medido de forma
aislada con JaCoCo, ejecutando solo `ArchitectureTest`, la cobertura es del **0 % (0 de 2.028 instrucciones)**.

Por eso la cobertura se mide de forma **estructural**, contando a qué clases de producción se aplica cada regla:

| Métrica | Valor |
|---|---|
| Clases de producción analizadas | 26 de 26 (100 %) |
| Comprobaciones clase-regla | 124 |
| Media de reglas por clase | 4,77 |

Este cálculo se hizo con un programa externo que replica los filtros `.that()` de las 12 reglas usando el
`ClassFileImporter` de ArchUnit.

Las interfaces del paquete `mapper` no aparecen en el informe de JaCoCo, porque no tienen instrucciones que
ejecutar, pero ArchUnit sí las analiza: se les aplican 5 reglas a cada una.

### Resultados

| Regla | Resultado |
|---|---|
| layers | ✅ Pasa |
| controllersShouldNotUseMappers | ✅ Pasa |
| domainShouldBeIndependent | ✅ Pasa |
| controllersNaming | ✅ Pasa |
| controllersShouldResideInControllersPackage | ✅ Pasa |
| servicesNaming | ✅ Pasa |
| servicesShouldResideInServicePackage | ✅ Pasa |
| servicesShouldOnlyDependOnAllowedPackages | ✅ Pasa |
| transactionalOnlyInServices | ✅ Pasa |
| mappersShouldBeInterfaces | ✅ Pasa |
| mappersShouldOnlyDependOnDomain | ✅ Pasa |
| noCycles | ✅ Pasa |

Las **12 reglas** incluidas en `ArchitectureTest` pasan: el código de JPetStore respeta la arquitectura por
capas, las convenciones de nombres y anotaciones, y no tiene ciclos entre paquetes.

Además, se probaron otras reglas que no se incluyeron en el test:

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

La arquitectura de JPetStore es limpia: las dependencias van siempre en el sentido `web` → `service` → `mapper`
→ `domain`, el dominio está formado por POJOs independientes y las transacciones se gestionan solo en la capa de
servicio.

El único problema encontrado es la **inyección por campo** en los controladores. Se decidió no incluir esa regla
en el test porque:

* solo afecta a cómo se construyen los objetos en los tests (que usan `@InjectMocks`), no al funcionamiento de la
  aplicación;
* corregirlo exige modificar código de producción del repositorio original.

Para corregirlo habría que declarar los atributos de los controladores como `private final` e inyectarlos por
constructor, o bien usar `FreezingArchRule` para congelar las 5 violaciones actuales y que solo fallen las nuevas.

### Limitaciones

* **La capa web no tiene lista blanca de dependencias.** `domain`, `service` y `mapper` tienen una regla que
  limita de qué pueden depender, pero `web` no. Por ejemplo, un controlador que abriese una conexión JDBC directa
  (`java.sql`) pasaría todas las reglas: no es un mapper, así que `controllersShouldNotUseMappers` no lo detecta,
  y `java.sql` no es ninguna capa, así que `layers` tampoco (por `consideringOnlyDependenciesInLayers()`).

* **`noCycles` solo detecta ciclos entre paquetes de primer nivel**, no dentro de un mismo paquete (por ejemplo,
  entre `CatalogService` y `OrderService`). Se comprobó aparte que actualmente no hay ciclos entre clases, pero la
  regla no lo vigila.

* **No sustituye a las pruebas funcionales.** ArchUnit comprueba la estructura del código, no su comportamiento:
  una aplicación puede respetar todas las reglas y tener errores de lógica.
