package org.salgar.pekko.fsm.xtend.uml.templates.converter

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IGeneratorContext

class ConverterGenerator extends AbstractGenerator {
    @Inject ConverterTemplate converterTemplate

    override doGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {
        converterTemplate.doGenerate(input, fsa)
    }
}