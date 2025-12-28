package com.ffrontera._generales.backend;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

public class ModularityTests {

    // 1. Verifica que la estructura de módulos cumple las reglas
    @Test
    void verifiesModularStructure() {
        ApplicationModules modules = ApplicationModules.of(BackendApplication.class);
        modules.verify();
    }

    // 2. Genera diagramas de tus módulos automáticamente
    @Test
    void createModuleDocumentation() {
        ApplicationModules modules = ApplicationModules.of(BackendApplication.class);
        new Documenter(modules)
                .writeDocumentation()
                .writeIndividualModulesAsPlantUml();
    }
}
