package dev.priporov.formatconverter.text_area

import com.intellij.lang.Language
import com.intellij.openapi.observable.util.whenItemSelected
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.ui.EditorTextField
import com.intellij.ui.EditorTextFieldProvider
import com.intellij.ui.HorizontalScrollBarEditorCustomization
import com.intellij.ui.MonospaceEditorCustomization
import dev.priporov.formatconverter.common.Languages
import dev.priporov.formatconverter.main.CustomDialogWrapper
import javax.swing.JPanel

private val editorCustomizations = listOf(
    MonospaceEditorCustomization.getInstance(),
    HorizontalScrollBarEditorCustomization.ENABLED
)

class LangTextFieldPanel(
    var language: Language,
    project: Project,
    comboBox: ComboBox<Languages>,
    private val customDialogWrapper: CustomDialogWrapper,
) : JPanel() {

    private var textField = editorTextField(language, project)

    init {
        layout = VerticalFlowLayout().apply {
            verticalFill = true
            horizontalFill = true
        }
        add(textField)
        comboBox.whenItemSelected {
            val text = textField.text
            val oldLanguage = language
            language = it.language
            removeAll()
            textField = editorTextField(it.language, project)
            setText(text)
            if(text.isNotBlank()){
                customDialogWrapper.convertFormat(this, oldLanguage, this, language)
            }
            add(textField)
        }
    }

    private fun editorTextField(
        language: Language,
        project: Project
    ): EditorTextField {
        return EditorTextFieldProvider.getInstance().getEditorField(language, project, editorCustomizations).apply {
            preferredSize = size
            background = background.darker()
        }
    }

    fun getText() = textField.text
    fun setText(value: String) {
        textField.text = value
    }

}
