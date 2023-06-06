package org.salgar.pekko.fsm.xtend.uml.templates.command

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IGeneratorContext

class CommandGenerator extends AbstractGenerator {
    @Inject CommandTemplate commandTemplate
    @Inject CommandConstantTemplate commandConstantTemplate

    override doGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {
        commandTemplate.doGenerate(input, fsa)
        commandConstantTemplate.doGenerate(input, fsa)
    }
}