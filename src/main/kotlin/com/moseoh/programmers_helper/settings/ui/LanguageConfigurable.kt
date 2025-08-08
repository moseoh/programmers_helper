package com.moseoh.programmers_helper.settings.ui

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.CheckBox
import com.intellij.ui.dsl.builder.*
import com.moseoh.programmers_helper._common.PluginBundle.get
import com.moseoh.programmers_helper.settings.model.Language
import com.moseoh.programmers_helper.settings.model.ProgrammersHelperSettings
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

class LanguageConfigurable : Configurable {
    private val settings = ProgrammersHelperSettings.state

    private val languageComboBox = ComboBox(Language.values())
    private val useClipboardCheckBox = CheckBox(get("useClipboardCheckBox"))
    private val useFolderCheckBox = CheckBox(get("useFolderCheckBox"))
    private val useNameSpacingCheckBox = CheckBox(get("useNameSpacingCheckBox"))
    private val useMainFunctionCheckbox = CheckBox(get("useMainFunctionCheckbox"))
    private val useHelpCommentCheckbox = CheckBox(get("use"))
    private val allowCopyCommentsCheckbox = CheckBox(get("allowed"))

    override fun createComponent(): JComponent {
        return panel {
            group(get("importProblem"), true) {
                row("${get("language")}: ") {
                    cell(languageComboBox).comment(get("languageDesc"))
                }
                row("${get("copyMode")}: ") {
                    cell(useClipboardCheckBox).comment(get("copyModeDesc"))
                }
                row("${get("createMode")}: ") {
                    cell(useFolderCheckBox).comment(get("createModeDesc"))
                }
                row("${get("naming")}: ") {
                    cell(useNameSpacingCheckBox).comment(get("namingDesc"))
                }
                row("${get("mainFunction")}: ") {
                    cell(useMainFunctionCheckbox).comment(get("mainFunctionDesc"))
                }
                row("${get("helpComment")}: ") {
                    cell(useHelpCommentCheckbox)
                }
            }

            group(get("copyAnswer"), true) {
                row("${get("copyComments")}: ") {
                    cell(allowCopyCommentsCheckbox).comment(get("copyCommentsDesc"))
                }
            }

            row {
                link("${get("github")} ") { BrowserUtil.browse("https://github.com/moseoh/programmers_helper") }
            }
        }
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    override fun getDisplayName(): String {
        return get("displayName")
    }

    override fun isModified(): Boolean {
        return settings.language != languageComboBox.selectedItem ||
                settings.useClipboard != useClipboardCheckBox.isSelected ||
                settings.useFolder != useFolderCheckBox.isSelected ||
                settings.useNameSpacing != useNameSpacingCheckBox.isSelected ||
                settings.useMainFunction != useMainFunctionCheckbox.isSelected ||
                settings.useHelpComment != useHelpCommentCheckbox.isSelected ||
                settings.allowCopyComment != allowCopyCommentsCheckbox.isSelected
    }

    @Throws(ConfigurationException::class)
    override fun apply() {
        val selectedLanguage = languageComboBox.selectedItem as Language

        settings.language = selectedLanguage
        settings.useClipboard = useClipboardCheckBox.isSelected
        settings.useFolder = useFolderCheckBox.isSelected
        settings.useNameSpacing = useNameSpacingCheckBox.isSelected
        settings.useMainFunction = useMainFunctionCheckbox.isSelected
        settings.useHelpComment = useHelpCommentCheckbox.isSelected
        settings.allowCopyComment = allowCopyCommentsCheckbox.isSelected
    }

    override fun reset() {
        languageComboBox.selectedItem = settings.language
        useClipboardCheckBox.isSelected = settings.useClipboard
        useFolderCheckBox.isSelected = settings.useFolder
        useNameSpacingCheckBox.isSelected = settings.useNameSpacing
        useMainFunctionCheckbox.isSelected = settings.useMainFunction
        useHelpCommentCheckbox.isSelected = settings.useHelpComment
        allowCopyCommentsCheckbox.isSelected = settings.allowCopyComment
    }
}