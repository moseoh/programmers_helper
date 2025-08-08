package com.moseoh.programmers_helper.actions.import_problem.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.moseoh.programmers_helper._common.Utils
import com.moseoh.programmers_helper.actions.import_problem.service.dto.JavaTemplateMapper
import com.moseoh.programmers_helper.actions.import_problem.service.dto.KotlinTemplateMapper
import com.moseoh.programmers_helper.actions.import_problem.service.dto.PythonTemplateMapper
import com.moseoh.programmers_helper.actions.import_problem.service.dto.ProblemDto
import com.moseoh.programmers_helper.settings.model.Language
import com.moseoh.programmers_helper.settings.model.ProgrammersHelperSettings

@Service
class ContentService(
    private val settings: ProgrammersHelperSettings.State = ProgrammersHelperSettings.state,
    private val javaTemplateMapper: JavaTemplateMapper = service<JavaTemplateMapper>(),
    private val kotlinTemplateMapper: KotlinTemplateMapper = service<KotlinTemplateMapper>(),
    private val pythonTemplateMapper: PythonTemplateMapper = service<PythonTemplateMapper>()
) {
    fun get(project: Project, directory: VirtualFile, problemDto: ProblemDto): String {
        val dto = when (settings.language) {
            Language.Java -> {
                javaTemplateMapper.toDto(project.basePath!!, directory.path, problemDto)
            }

            Language.Kotlin -> {
                kotlinTemplateMapper.toDto(project.basePath!!, directory.path, problemDto)
            }
            
            Language.Python3 -> {
                pythonTemplateMapper.toDto(project.basePath!!, directory.path, problemDto)
            }
        }
        return Utils.convert(settings.language.template, dto)
    }
}