package org.salgar.pekko.fsm.xtend.uml.templates.model.protobuf

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IGeneratorContext

class ProtobufGenerator extends AbstractGenerator {
    @Inject ProtobufTemplate protobufTemplate

    override doGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {
        protobufTemplate.doGenerate(input, fsa)
    }
}