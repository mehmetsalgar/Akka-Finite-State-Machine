package org.salgar.pekko.fsm.xtend.uml.templates.kafka

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IGeneratorContext

class ScalaKafkaGenerator extends AbstractGenerator {
    @Inject TopicConfigTemplate topicConfigTemplate
    @Inject ConsumerConfigTemplate consumerConfigTemplate
    @Inject StreamConfigTemplate streamConfigTemplate
    @Inject AskFacadeTemplate askFacadeTemplate

    override doGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {
        topicConfigTemplate.doGenerate(input, fsa)
        consumerConfigTemplate.doGenerate(input, fsa)
        streamConfigTemplate.doGenerate(input, fsa)
        askFacadeTemplate.doGenerate(input, fsa)
    }
}