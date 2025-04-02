package com.rees46.ios

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

abstract class IosPluginExtension @Inject constructor(objects: ObjectFactory) {
    val baseName: Property<String> = objects.property(String::class.java)
}
