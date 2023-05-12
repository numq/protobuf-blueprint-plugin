package com.numq.protobufblueprint.services

import com.intellij.openapi.components.Service
import com.numq.protobufblueprint.standard.ProtobufStandard
import java.util.*

@Service(Service.Level.PROJECT)
class GeneratorService {

    private val indent = ProtobufStandard.INDENT

    private fun parseLines(input: String): List<String> {
        val patternEnum = Regex("\\benum\\b(\\s+\\b[a-zA-Z_]+\\b)+\\s*")
        val patternMessage = Regex("\\bmessage\\b(\\s+\\b[a-zA-Z]+\\b)+\\s*")
        val patternService = Regex("\\bservice\\b(\\s+\\b[a-zA-Z]+\\b)+\\s*")
        return input
            .split(Regex("\n+"))
            .filter { arrayOf(patternEnum, patternMessage, patternService).any { pattern -> pattern.matches(it) } }
            .filter { it.isNotBlank() }
    }

    private fun formatPascalCase(value: String) = value.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }.trim()

    private fun generateEnum(name: String, values: List<String>) = buildString {
        append("enum ${formatPascalCase(name)} {\n")
        append(values.mapIndexed { idx, i ->
            "$indent${i.uppercase()} = $idx;"
        }.joinToString("\n"))
        append("\n}")
    }

    private fun generateMessage(message: String) = buildString {
        append("message ${formatPascalCase(message)} {\n$indent\n}")
    }

    private fun generateService(serviceName: String, rpcNames: List<String>) = buildString {
        rpcNames.forEach { name ->
            append("message ${formatPascalCase(name)}Request {\n$indent\n}")
            append("\n\n")
            append("message ${formatPascalCase(name)}Response {\n$indent\n}")
            append("\n\n")
        }
        append("service ${formatPascalCase(serviceName)}Service {\n")
        append(rpcNames.joinToString("\n") { name ->
            "${indent}rpc ${formatPascalCase(name)}(${formatPascalCase(name)}Request) returns (${formatPascalCase(name)}Response);"
        })
        append("\n}")
    }

    private fun format(line: String) = line.trim().split(" ").takeIf { it.isNotEmpty() }?.let { parts ->
        parts.firstOrNull()?.let { type ->
            when (type) {
                "enum" -> {
                    val name = parts.getOrNull(1)
                    val values = parts.drop(2)
                    if (name != null && values.isNotEmpty()) generateEnum(name, values)
                    else ""
                }
                "message" -> {
                    parts.drop(1).joinToString("\n\n") {
                        generateMessage(it)
                    }
                }
                "service" -> {
                    val serviceName = parts.getOrNull(1)
                    val rpcs = parts.drop(2)
                    if (serviceName != null && rpcs.isNotEmpty()) generateService(serviceName, rpcs)
                    else ""
                }
                else -> ""
            }
        }
    } ?: ""

    fun generate(input: String) = parseLines(input).joinToString("\n\n", transform = ::format)
}