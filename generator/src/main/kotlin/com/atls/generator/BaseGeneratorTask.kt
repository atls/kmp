package com.atls.generator

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.options.Option

abstract class BaseGeneratorTask : DefaultTask() {

    @get:Input
    @get:Option(option = "moduleName", description = "Name of the module to generate")
    abstract val moduleName: Property<String>

    @get:Input
    @get:Option(option = "organization", description = "Organization (package prefix)")
    abstract val organization: Property<String>
}
