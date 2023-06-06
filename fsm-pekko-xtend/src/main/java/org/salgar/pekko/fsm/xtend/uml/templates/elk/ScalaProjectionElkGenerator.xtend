package org.salgar.pekko.fsm.xtend.uml.templates.elk

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IGeneratorContext

class ScalaProjectionElkGenerator extends AbstractGenerator {
    @Inject ElkPersistEventProcessorTemplate elkPersistEventProcessorTemplate

    override doGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {
        elkPersistEventProcessorTemplate.doGenerate(input, fsa)
    }
}