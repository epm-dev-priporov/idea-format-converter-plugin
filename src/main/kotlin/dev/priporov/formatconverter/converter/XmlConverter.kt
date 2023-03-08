package dev.priporov.formatconverter.converter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper

class XmlConverter:Converter {

    private val mapper = XmlMapper()

    override fun toAny(string: String): Any {
        return mapper.readValue(string, Any::class.java)
    }

    override fun toString(any: Any): String {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(any)
    }

}