package io.github.numq.protobufblueprint.feature.generator.presentation.view

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.numq.protobufblueprint.common.core.proto.ProtoStandard
import io.github.numq.protobufblueprint.feature.generator.presentation.GeneratorCommand
import io.github.numq.protobufblueprint.feature.generator.presentation.GeneratorFeature
import io.github.numq.protobufblueprint.feature.generator.presentation.GeneratorState
import kotlinx.coroutines.launch
import org.jetbrains.jewel.foundation.ExperimentalJewelApi
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextArea
import org.jetbrains.jewel.ui.component.Tooltip

@OptIn(ExperimentalJewelApi::class, ExperimentalFoundationApi::class)
@Composable
internal fun GeneratorViewReady(
    state: GeneratorState.Ready, feature: GeneratorFeature, panelBackground: Color, contentColor: Color
) {
    val coroutineScope = rememberCoroutineScope()

    val editorFont = remember(contentColor) {
        TextStyle(fontFamily = FontFamily.Monospace, fontSize = 13.sp, color = contentColor)
    }

    val (inputFieldValue, setInputFieldValue) = remember {
        mutableStateOf(TextFieldValue(state.record.input))
    }

    LaunchedEffect(inputFieldValue.text) {
        feature.execute(GeneratorCommand.GenerateProto(input = inputFieldValue.text))
    }

    val hasLongLines = remember(state.record.output) {
        state.record.output.lines().any { it.length > ProtoStandard.MAX_LINE_LENGTH }
    }

    val outputAnnotated = remember(state.record.output, contentColor) {
        buildAnnotatedString {
            val lines = state.record.output.lines()
            lines.forEachIndexed { index, line ->
                if (line.length > ProtoStandard.MAX_LINE_LENGTH) {
                    withStyle(SpanStyle(background = Color(0x40DB5860))) {
                        append(line)
                    }
                } else {
                    append(line)
                }
                if (index != lines.lastIndex) append("\n")
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(panelBackground).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().background(contentColor.copy(alpha = 0.06f), RoundedCornerShape(6.dp))
                .padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = "Format Requirements:", fontSize = 11.sp, color = contentColor.copy(alpha = 0.6f))
            Text("• syntax versionName (e.g. syntax proto3)", fontSize = 11.sp, color = contentColor.copy(alpha = 0.6f))
            Text(
                text = "• package packageName (e.g. package com.example)",
                fontSize = 11.sp,
                color = contentColor.copy(alpha = 0.6f)
            )
            Text(
                text = "• message messageName messageName messageName (e.g. message token userData)",
                fontSize = 11.sp,
                color = contentColor.copy(alpha = 0.6f)
            )
            Text(
                text = "• enum enumName enum_value enum_value enum_value (e.g. enum token access refresh)",
                fontSize = 11.sp,
                color = contentColor.copy(alpha = 0.6f)
            )
            Text(
                text = "• service serviceName rpcName rpcName rpcName (e.g. service user create read update delete)",
                fontSize = 11.sp,
                color = contentColor.copy(alpha = 0.6f)
            )

            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (hasLongLines) {
                    Text(
                        text = "⚠️ Line > ${ProtoStandard.MAX_LINE_LENGTH} chars",
                        fontSize = 10.sp,
                        color = Color(0xFFDB5860)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier.weight(1f).fillMaxHeight()
                    .background(contentColor.copy(alpha = 0.06f), RoundedCornerShape(6.dp)).padding(12.dp),
                contentAlignment = Alignment.TopStart
            ) {
                if (inputFieldValue.text.isEmpty()) {
                    Text(
                        text = "Enter DSL here...", style = editorFont, color = contentColor.copy(alpha = 0.4f)
                    )
                }

                TextArea(
                    value = inputFieldValue,
                    onValueChange = setInputFieldValue,
                    modifier = Modifier.fillMaxSize(),
                    undecorated = true,
                    textStyle = editorFont
                )
            }

            val outputIsEmpty = state.record.output.isBlank()

            Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                val outputPanel = @Composable {
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .background(contentColor.copy(alpha = 0.02f), RoundedCornerShape(6.dp)).clickable(
                                enabled = !outputIsEmpty,
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                coroutineScope.launch {
                                    feature.execute(GeneratorCommand.CopyOutput)
                                }
                            }, contentAlignment = Alignment.TopStart
                    ) {
                        Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                            if (outputIsEmpty) {
                                Text(
                                    text = "Awaiting valid input...",
                                    style = editorFont,
                                    color = contentColor.copy(alpha = 0.4f)
                                )
                            }

                            Text(
                                text = outputAnnotated,
                                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                                style = editorFont
                            )
                        }

                        if (state.copyFeedback) {
                            Box(
                                modifier = Modifier.fillMaxSize()
                                    .background(panelBackground.copy(alpha = 0.8f), RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier.background(Color(0xFF6AAB73), RoundedCornerShape(16.dp))
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(text = "✓ Copied to clipboard", fontSize = 12.sp, color = Color.White)
                                }
                            }
                        }
                    }
                }

                when {
                    outputIsEmpty || state.copyFeedback -> outputPanel()

                    else -> Tooltip(tooltip = { Text("Click to copy") }, content = outputPanel)
                }
            }
        }
    }
}