package org.morshed;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("org.morshed");

        noClasses()
            .that()
            .resideInAnyPackage("org.morshed.service..")
            .or()
            .resideInAnyPackage("org.morshed.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..org.morshed.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
