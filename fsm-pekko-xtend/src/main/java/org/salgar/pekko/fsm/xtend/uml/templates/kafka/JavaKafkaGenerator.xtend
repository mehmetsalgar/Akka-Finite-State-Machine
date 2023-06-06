package org.salgar.pekko.fsm.xtend.uml.templates.kafka

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IGeneratorContext

class JavaKafkaGenerator extends AbstractGenerator {
    @Inject KafkaConfigTemplate kafkaConfigTemplate
    @Inject TopicPropertiesTemplate topicPropertiesTemplate

    override doGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {
        kafkaConfigTemplate.doGenerate(input, fsa)
        topicPropertiesTemplate.doGenerate(input, fsa)
    }
}