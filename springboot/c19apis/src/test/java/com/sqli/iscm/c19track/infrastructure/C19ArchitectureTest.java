package com.sqli.iscm.c19track.infrastructure;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Service;

import javax.ws.rs.Path;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.sqli.iscm.c19track")
public class C19ArchitectureTest {
    @ArchTest
    static ArchRule services_should_be_sufixed =
            classes()
                    .that().resideInAPackage("..service..")
                    .and().areAnnotatedWith(Service.class)
                    .should().haveSimpleNameEndingWith("Service");

    @ArchTest
    static ArchRule endpoint_resource_should_not_have_ui_in_name =
            classes()
                    .that().resideInAPackage("..rest..")
                    .should().haveSimpleNameNotContaining("UI");

    @ArchTest
    static ArchRule endpoint_resources_should_be_suffixed =
            classes()
                    .that().resideInAPackage("..endpoint..")
                    .or().areAnnotatedWith(Path.class)
                    .should().haveSimpleNameEndingWith("Resource");

    @ArchTest
    static ArchRule classes_named_resource_should_be_in_a_endpoint_package =
            classes()
                    .that().haveSimpleNameEndingWith("Resource")
                    .should().resideInAPackage("..endpoint..");
}
