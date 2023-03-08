package dev.priporov.formatconverter.common

import com.intellij.lang.Language

enum class Languages(val language: Language) {
    JSON(Language.findLanguageByID("JSON")!!),
    XML(Language.findLanguageByID("XML")!!),
    YAML(Language.findLanguageByID("yaml")!!);

    companion object {
        val list = values().toList()
    }

}