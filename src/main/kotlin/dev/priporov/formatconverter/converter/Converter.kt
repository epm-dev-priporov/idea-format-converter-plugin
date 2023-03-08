package dev.priporov.formatconverter.converter

interface Converter {

    fun toAny(string: String): Any
    fun toString(any: Any): String

}