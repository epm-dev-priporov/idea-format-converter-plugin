package dev.priporov.formatconverter.converter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

class YamlConverter : Converter {

    private val mapper = ObjectMapper(YAMLFactory())

    override fun toAny(string: String): Any {
        return mapper.readValue(string, Any::class.java)
    }

    override fun toString(any: Any): String {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(any)
    }

}