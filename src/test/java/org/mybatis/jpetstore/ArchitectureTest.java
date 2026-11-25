/*
 *    Copyright 2010-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.jpetstore;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaCodeUnit;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaMethodCall;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchIgnore;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.library.ProxyRules;
import com.tngtech.archunit.library.dependencies.SliceAssignment;
import com.tngtech.archunit.library.dependencies.SliceIdentifier;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AnalyzeClasses(packages = "org.mybatis.jpetstore", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

  // comprobar dependencias entre capas, por ejemplo que los controladores no accedan a los mappers directamente, sino
  // que pasen por el servicio
  @ArchTest
  static final ArchRule layers = layeredArchitecture().consideringOnlyDependenciesInLayers().layer("Web")
      .definedBy("..web..").layer("Service").definedBy("..service..").layer("Mapper").definedBy("..mapper..")
      .layer("Domain").definedBy("..domain..").whereLayer("Web").mayNotBeAccessedByAnyLayer().whereLayer("Service")
      .mayOnlyBeAccessedByLayers("Web").whereLayer("Mapper").mayOnlyBeAccessedByLayers("Service").whereLayer("Domain")
      .mayOnlyBeAccessedByLayers("Web", "Service", "Mapper");

  // comprobar que los controladores no accedan a los mappers directamente, sino que pasen por el servicio
  @ArchTest
  static final ArchRule controllersShouldNotUseMappers = noClasses().that().resideInAPackage("..web..").should()
      .dependOnClassesThat().resideInAPackage("..mapper..").because("controllers must go through the service layer");

  // comprobar que los objetos de dominio son POJOs y no dependen de otras capas
  @ArchTest
  static final ArchRule domainShouldBeIndependent = classes().that().resideInAPackage("..domain..").should()
      .onlyDependOnClassesThat().resideInAnyPackage("java..", "..domain..").because("domain objects are plain POJOs");

  // comprobar que las clases principales de web.controllers terminan en Controller y llevan @Controller
  @ArchTest
  static final ArchRule controllersNaming = classes().that().resideInAPackage("..web.controllers..").and()
      .areTopLevelClasses().should().haveSimpleNameEndingWith("Controller").andShould()
      .beAnnotatedWith(Controller.class);

  // comprobar que toda clase con @Controller está en web.controllers, que es donde Spring la busca
  @ArchTest
  static final ArchRule controllersShouldResideInControllersPackage = classes().that()
      .areAnnotatedWith(Controller.class).should().resideInAPackage("..web.controllers..")
      .because("Spring only scans that package for controllers");

  // comprobar que las clases principales de service terminan en Service y llevan @Service
  @ArchTest
  static final ArchRule servicesNaming = classes().that().resideInAPackage("..service..").and().areTopLevelClasses()
      .should().haveSimpleNameEndingWith("Service").andShould().beAnnotatedWith(Service.class);

  // comprobar que toda clase con @Service está en service, que es donde Spring la busca
  @ArchTest
  static final ArchRule servicesShouldResideInServicePackage = classes().that().areAnnotatedWith(Service.class).should()
      .resideInAPackage("..service..").because("Spring only scans that package for services");

  // comprobar que los servicios solo dependen del JDK, del dominio, de los mappers y de las anotaciones de Spring que
  // necesitan (@Service y @Transactional), para que no usen nada de HTTP
  @ArchTest
  static final ArchRule servicesShouldOnlyDependOnAllowedPackages = classes().that().resideInAPackage("..service..")
      .should().onlyDependOnClassesThat().resideInAnyPackage("java..", "..domain..", "..mapper..", "..service..",
          "org.springframework.stereotype..", "org.springframework.transaction..")
      .because("services must not know about HTTP");

  // comprobar que @Transactional (en métodos o en clases) solo se usa en los servicios
  @ArchTest
  static final ArchRule transactionalOnlyInServices = noClasses().that().resideOutsideOfPackage("..service..").should()
      .dependOnClassesThat().haveSimpleName("Transactional")
      .because("transaction boundaries belong to the service layer");

  // comprobar que los mappers son interfaces terminadas en Mapper, para que MyBatis las implemente con su XML
  @ArchTest
  static final ArchRule mappersShouldBeInterfaces = classes().that().resideInAPackage("..mapper..").should()
      .beInterfaces().andShould().haveSimpleNameEndingWith("Mapper");

  // comprobar que los mappers solo dependen del JDK y del dominio, para que no usen Spring, HTTP ni otras capas
  @ArchTest
  static final ArchRule mappersShouldOnlyDependOnDomain = classes().that().resideInAPackage("..mapper..").should()
      .onlyDependOnClassesThat().resideInAnyPackage("java..", "..domain..", "..mapper..")
      .because("mappers only translate between the database and domain objects");

  // comprobar que la capa web solo depende del JDK, del dominio, de los servicios, de los servlets y de Spring MVC,
  // para que los controladores no usen JDBC, MyBatis ni otras librerías directamente
  @ArchTest
  static final ArchRule webShouldOnlyDependOnAllowedPackages = classes().that().resideInAPackage("..web..").should()
      .onlyDependOnClassesThat()
      .resideInAnyPackage("java..", "..domain..", "..service..", "..web..", "javax.servlet..",
          "org.springframework.stereotype..", "org.springframework.beans.factory.annotation..",
          "org.springframework.ui..", "org.springframework.web..")
      .because("controllers only translate HTTP requests into service calls");

  // comprobar que no hay dependencias cíclicas entre los paquetes de primer nivel (web, service, mapper, domain...)
  @ArchTest
  static final ArchRule noCycles = slices().matching("org.mybatis.jpetstore.(*)..").should().beFreeOfCycles();

  // comprobar que los controladores no dependen unos de otros: cada controlador, junto con sus clases anidadas, forma
  // un grupo y ningún grupo puede usar clases de otro
  @ArchTest
  @ArchIgnore(reason = "Incumplida: OrderController usa AccountController.AccountSession (issue #14)")
  static final ArchRule controllersShouldNotDependOnEachOther = slices().assignedFrom(controllerSlices()).should()
      .notDependOnEachOther().because("each controller must be independent of the others");

  // comprobar que los métodos públicos de los servicios que escriben en la base de datos (llaman a un insert, update
  // o delete de un mapper, también dentro de lambdas) llevan @Transactional, para que todas sus escrituras se
  // confirmen o se deshagan juntas
  @ArchTest
  @ArchIgnore(reason = "Incumplida: OrderService.getNextId escribe sin @Transactional (issue #15)")
  static final ArchRule serviceMethodsThatWriteShouldBeTransactional = methods().that().areDeclaredInClassesThat()
      .areAnnotatedWith(Service.class).and().arePublic().should(beTransactionalIfTheyWriteThroughAMapper())
      .because("all the writes of a service method must be committed or rolled back together");

  // comprobar que no se usa toLowerCase() ni toUpperCase() sin Locale, porque el resultado depende del idioma del
  // servidor (en turco, "I".toLowerCase() da "ı", una i sin punto); las versiones con Locale sí están permitidas
  @ArchTest
  @ArchIgnore(reason = "Incumplida: CatalogService y CatalogController usan toLowerCase() sin Locale (issue #3)")
  static final ArchRule noLocaleSensitiveCaseConversion = noClasses().should().callMethod(String.class, "toLowerCase")
      .orShould().callMethod(String.class, "toUpperCase")
      .because("the result must not depend on the default locale of the server");

  // comprobar que @Transactional solo se pone en métodos públicos: en Spring 5 la anotación se ignora sin avisar en
  // los métodos que no son públicos, y el método se ejecutaría sin transacción
  @ArchTest
  static final ArchRule transactionalMethodsShouldBePublic = methods().that().areAnnotatedWith(Transactional.class)
      .should().bePublic().because("Spring ignores @Transactional on non-public methods");

  // comprobar que ningún método llama a un método @Transactional de su misma clase: la llamada interna no pasa por el
  // proxy de Spring y la anotación no tendría efecto
  @ArchTest
  static final ArchRule noSelfInvocationOfTransactionalMethods = ProxyRules
      .no_classes_should_directly_call_other_methods_declared_in_the_same_class_that_are_annotated_with(
          Transactional.class)
      .because("calls inside the same class bypass the Spring proxy that opens the transaction");

  // comprobar que los controladores y servicios no guardan estado: Spring crea una sola instancia de cada uno y la
  // comparten todas las peticiones, así que sus atributos solo pueden ser constantes (static), final o inyectados
  @ArchTest
  static final ArchRule springBeansShouldBeStateless = fields().that().areDeclaredInClassesThat()
      .areMetaAnnotatedWith(Component.class).and().areNotStatic().and().areNotAnnotatedWith(Autowired.class).should()
      .beFinal().because("controllers and services are singletons shared by all requests");

  // agrupa cada clase por su clase más externa si es un @Controller (AccountController.AccountSession va al grupo
  // AccountController); el resto de clases no pertenece a ningún grupo
  private static SliceAssignment controllerSlices() {
    return new SliceAssignment() {
      @Override
      public SliceIdentifier getIdentifierOf(JavaClass javaClass) {
        JavaClass outermost = javaClass;
        while (outermost.getEnclosingClass().isPresent()) {
          outermost = outermost.getEnclosingClass().get();
        }
        return outermost.isAnnotatedWith(Controller.class) ? SliceIdentifier.of(outermost.getSimpleName())
            : SliceIdentifier.ignore();
      }

      @Override
      public String getDescription() {
        return "controllers";
      }
    };
  }

  // condición propia para serviceMethodsThatWriteShouldBeTransactional: el método incumple si escribe en la base de
  // datos y ni él ni su clase llevan @Transactional
  private static ArchCondition<JavaMethod> beTransactionalIfTheyWriteThroughAMapper() {
    return new ArchCondition<JavaMethod>("be @Transactional if they write to the database through a mapper") {
      @Override
      public void check(JavaMethod method, ConditionEvents events) {
        List<String> writes = new ArrayList<>();
        for (JavaCodeUnit codeUnit : withItsLambdas(method)) {
          for (JavaMethodCall call : codeUnit.getMethodCallsFromSelf()) {
            if (call.getTargetOwner().getPackageName().endsWith(".mapper")
                && call.getName().matches("(insert|update|delete).*")) {
              writes.add(call.getTargetOwner().getSimpleName() + "." + call.getName());
            }
          }
        }
        boolean transactional = method.isAnnotatedWith(Transactional.class)
            || method.getOwner().isAnnotatedWith(Transactional.class);
        if (!writes.isEmpty() && !transactional) {
          events.add(SimpleConditionEvent.violated(method,
              method.getFullName() + " writes to the database " + writes + " but is not @Transactional"));
        }
      }
    };
  }

  // devuelve el método junto con sus lambdas. Con javac (Maven) ArchUnit ya atribuye al método las llamadas de sus
  // lambdas, pero con el compilador de Eclipse (VS Code) las lambdas aparecen como métodos ocultos aparte (lambda$0,
  // lambda$1...) y hay que buscarlas siguiendo las referencias del método a esos métodos sintéticos
  private static Set<JavaCodeUnit> withItsLambdas(JavaCodeUnit method) {
    Set<JavaCodeUnit> result = new HashSet<>();
    Deque<JavaCodeUnit> pending = new ArrayDeque<>();
    pending.push(method);
    while (!pending.isEmpty()) {
      JavaCodeUnit codeUnit = pending.pop();
      if (result.add(codeUnit)) {
        codeUnit.getMethodReferencesFromSelf().forEach(reference -> reference.getTarget().resolveMember()
            .filter(target -> target.getModifiers().contains(JavaModifier.SYNTHETIC)).ifPresent(pending::push));
      }
    }
    return result;
  }

}
