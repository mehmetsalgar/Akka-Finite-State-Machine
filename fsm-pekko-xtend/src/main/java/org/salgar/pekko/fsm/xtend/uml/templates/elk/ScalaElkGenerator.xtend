package org.salgar.pekko.fsm.xtend.uml.templates.elk

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IGeneratorContext

class ScalaElkGenerator extends AbstractGenerator {
    @Inject ElkPersistEvent2StateTransformTemplate elkPersistEvent2StateTransformTemplate

    override doGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {
        elkPersistEvent2StateTransformTemplate.doGenerate(input, fsa)
    }
}