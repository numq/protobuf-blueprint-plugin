package io.github.numq.protobufblueprint.feature.generator.core

import arrow.core.Either
import io.github.numq.protobufblueprint.common.core.proto.ProtoStandard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.*

class DefaultGeneratorService : GeneratorService {
    private val mutex = Mutex()

    private val _record = MutableStateFlow(GeneratorRecord())

    override val record = _record.asStateFlow()

    private fun parseLines(input: String): List<String> {
        val validTypes = listOf("syntax", "package", "enum", "message", "service")

        return input.split(Regex("\n+")).filter { line ->
            validTypes.any { type ->
                line.trim().startsWith(prefix = type)
            }
        }.filter(String::isNotBlank)
    }

    private fun formatPascalCase(value: String) = value.replaceFirstChar { char ->
        when {
            char.isLowerCase() -> char.titlecase(Locale.getDefault())

            else -> char.toString()
        }
    }.trim()

    private fun generateEnum(name: String, values: List<String>) = buildString {
        append("enum ${formatPascalCase(value = name)} {\n")

        append(values.mapIndexed { idx, i ->
            "${ProtoStandard.INDENT}${i.uppercase()} = $idx;"
        }.joinToString("\n"))

        append("\n}")
    }

    private fun generateMessage(message: String) = buildString {
        append("message ${formatPascalCase(value = message)} {\n${ProtoStandard.INDENT}\n}")
    }

    private fun generateService(serviceName: String, rpcNames: List<String>) = buildString {
        rpcNames.forEach { name ->
            append("message ${formatPascalCase(value = name)}Request {\n${ProtoStandard.INDENT}\n}")

            append("\n\n")

            append("message ${formatPascalCase(value = name)}Response {\n${ProtoStandard.INDENT}\n}")

            append("\n\n")
        }

        append("service ${formatPascalCase(value = serviceName)}Service {\n")

        append(rpcNames.joinToString("\n") { name ->
            "${ProtoStandard.INDENT}rpc ${formatPascalCase(value = name)}(${formatPascalCase(value = name)}Request) returns (${
                formatPascalCase(value = name)
            }Response);"
        })

        append("\n}")
    }

    private fun format(line: String) =
        line.trim().split(Regex("[\\s,]+")).takeIf(List<String>::isNotEmpty)?.let { parts ->
            parts.firstOrNull()?.let { type ->
                when (type) {
                    "enum" -> {
                        val name = parts.getOrNull(1)

                        val values = parts.drop(2)

                        when {
                            name != null && values.isNotEmpty() -> generateEnum(name = name, values = values)

                            else -> ""
                        }
                    }

                    "message" -> parts.drop(1).joinToString(separator = "\n\n", transform = ::generateMessage)

                    "service" -> {
                        val serviceName = parts.getOrNull(1)

                        val rpcs = parts.drop(2)

                        when {
                            serviceName != null && rpcs.isNotEmpty() -> generateService(
                                serviceName = serviceName, rpcNames = rpcs
                            )

                            else -> ""
                        }
                    }

                    else -> ""
                }
            }
        } ?: ""

    private fun generateProtoCode(input: String): String {
        val lines = parseLines(input = input)

        val syntaxLine = lines.firstOrNull { line ->
            line.trim().startsWith(prefix = "syntax")
        }

        val packageLine = lines.firstOrNull { line ->
            line.trim().startsWith(prefix = "package")
        }

        val otherLines = lines.filterNot { line ->
            val trimmed = line.trim()

            trimmed.startsWith(prefix = "syntax") || trimmed.startsWith(prefix = "package")
        }

        return buildString {
            syntaxLine?.let { line ->
                val syntaxName = line.trim().split(Regex("[\\s,]+")).getOrNull(1)

                when {
                    syntaxName != null -> append("syntax = \"$syntaxName\";\n\n")
                }
            }

            packageLine?.let { line ->
                val packageName = line.trim().split(Regex("[\\s,]+")).getOrNull(1)

                when {
                    packageName != null -> append("package $packageName;\n\n")
                }
            }

            val body = otherLines.map(::format).filter(String::isNotBlank).joinToString(separator = "\n\n")

            append(body)
        }.trim()
    }

    override suspend fun generate(input: String): Either<Throwable, Unit> = Either.catch {
        mutex.withLock {
            _record.update { state ->
                state.copy(input = input, isProcessing = true, error = null)
            }

            try {
                val protoCode = generateProtoCode(input = input)

                _record.update { state ->
                    state.copy(output = protoCode, isProcessing = false, error = null)
                }
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Generation failed"

                _record.update { state ->
                    state.copy(output = "", isProcessing = false, error = errorMessage)
                }

                throw e
            }
        }
    }

    override suspend fun clear(): Either<Throwable, Unit> = Either.catch {
        mutex.withLock {
            _record.update { state ->
                state.copy(input = "", output = "", error = null, isProcessing = false)
            }
        }
    }
}