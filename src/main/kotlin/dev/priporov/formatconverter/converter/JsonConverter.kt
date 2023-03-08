package dev.priporov.formatconverter.converter

import com.fasterxml.jackson.databind.ObjectMapper

class JsonConverter:Converter {

    private val mapper = ObjectMapper()

    override fun toAny(string: String): Any {
        return mapper.readValue(string, Any::class.java)
    }

    override fun toString(any: Any): String {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(any)
    }

}