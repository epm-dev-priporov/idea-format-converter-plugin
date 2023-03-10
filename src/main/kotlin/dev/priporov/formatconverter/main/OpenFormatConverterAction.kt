package dev.priporov.formatconverter.main

import com.intellij.ide.impl.DataManagerImpl
import com.intellij.lang.Language
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.wm.WindowManager
import com.intellij.openapi.wm.impl.WindowManagerImpl
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.AlignY
import com.intellij.ui.dsl.builder.panel
import dev.priporov.formatconverter.common.Languages
import dev.priporov.formatconverter.converter.Converter
import dev.priporov.formatconverter.converter.JsonConverter
import dev.priporov.formatconverter.converter.XmlConverter
import dev.priporov.formatconverter.converter.YamlConverter
import dev.priporov.formatconverter.text_area.LangTextFieldPanel
import javax.swing.JComponent


class CustomDialogWrapper(project: Project) : DialogWrapper(true) {
    private var leftComboBox: ComboBox<Languages> = ComboBox(Languages.values())
    private var rightComboBox: ComboBox<Languages> = ComboBox(Languages.values()).apply { item = Languages.YAML }
    private var leftEditorField = LangTextFieldPanel(Languages.JSON.language, project, leftComboBox, this)
    private var rightEditorField = LangTextFieldPanel(Languages.YAML.language, project, rightComboBox, this)
    private val mapOfConverters: Map<Language, Converter> = mapOf(
        Languages.JSON.language to service<JsonConverter>(),
        Languages.YAML.language to service<YamlConverter>(),
        Languages.XML.language to service<XmlConverter>(),
    )

    init {
        init()
        title = "Format Converter"
        val size = (WindowManager.getInstance() as WindowManagerImpl).getIdeFrame(project)?.component?.size!!
        setSize((size.width / 1.1).toInt(), (size.height / 1.1).toInt())
        setAutoAdjustable(false)
        setOKButtonText("Convert")
        setCancelButtonText("Close")
    }


    override fun createCenterPanel(): JComponent {
        val dialog = dialog()
        return dialog
    }

    private fun dialog() = panel {
        row {
            cell(leftEditorField)
                .align(AlignX.LEFT)
                .align(Align.CENTER)
                .align(Align.FILL).resizableColumn()
                .align(AlignY.FILL)

            cell(rightEditorField)
                .align(AlignX.RIGHT)
                .align(Align.CENTER)
                .align(Align.FILL).resizableColumn()
                .align(AlignY.FILL)

        }.resizableRow()

        row {
            cell(leftComboBox)
                .align(AlignX.LEFT)
                .align(Align.CENTER)
            cell(rightComboBox)
                .align(AlignX.RIGHT)
                .align(Align.CENTER)
        }
    }

    override fun doOKAction() {
        convertFormat(leftEditorField, targetEditorField = rightEditorField)
    }

    fun convertFormat(
        sourceEditorField: LangTextFieldPanel,
        sourceLanguage: Language = sourceEditorField.language,
        targetEditorField: LangTextFieldPanel,
        targetLanguage: Language = targetEditorField.language
    ) {
        try {
            val sourceText = sourceEditorField.getText()
            if(sourceText.isNullOrBlank()){
                return
            }
            val value = mapOfConverters[sourceLanguage]!!.toAny(sourceText)
            val convertedText = mapOfConverters[targetLanguage]?.toString(value)
            convertedText?.also { text ->
                targetEditorField.setText(text)
            }
        } catch (e: Exception) {
            targetEditorField.setText(e.message ?: "")
        }
    }

}

class OpenFormatConverterAction : AnAction("Format Converter") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = DataManagerImpl.getInstance()
            .dataContextFromFocusAsync
            .blockingGet(0)
            ?.getData(CommonDataKeys.PROJECT)
        if (project != null) {
            CustomDialogWrapper(project).show()
        }
    }
}

