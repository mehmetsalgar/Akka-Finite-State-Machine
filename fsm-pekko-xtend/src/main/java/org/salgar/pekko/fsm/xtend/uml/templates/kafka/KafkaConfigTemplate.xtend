package org.salgar.pekko.fsm.xtend.uml.templates.kafka

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import com.google.common.base.CaseFormat
import org.salgar.pekko.fsm.xtend.uml.templates.Naming

class KafkaConfigTemplate {
	@Inject extension Naming


	def doGenerate(Resource input, IFileSystemAccess fsa) {
	    val Iterable<org.eclipse.uml2.uml.StateMachine> sms =
	        input
               .allContents
               .toIterable
               .filter(org.eclipse.uml2.uml.StateMachine).filter[i|i.active]
        val masterSm = sms.filter[s|!s.abstract].head
	    val content = generate(sms, masterSm)
        fsa.generateFile(packagePath(masterSm)+"/kafka/"+"KafkaConfig.java", content)
    }

    def generate(Iterable<org.eclipse.uml2.uml.StateMachine> sms, org.eclipse.uml2.uml.StateMachine masterSm)'''
        package «packageName(masterSm)».kafka.config;

        import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
        import org.springframework.boot.context.properties.ConfigurationProperties;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;

        @Configuration
        public class KafkaConfig {
            @Bean
            @ConfigurationProperties("spring.kafka.consumer.«CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, masterSm.name.substring(0, masterSm.name.length-1) + Character.toLowerCase(masterSm.name.charAt(masterSm.name.length-1)))»")
            public KafkaProperties.Consumer «masterSm.name.toFirstLower»Properties() {
                return new KafkaProperties.Consumer();
            }

            «FOR sm : sms»
                @Bean
                @ConfigurationProperties("spring.kafka.consumer.«CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, sm.name.substring(0, sm.name.length-1) + Character.toLowerCase(sm.name.charAt(sm.name.length-1)))»")
                public KafkaProperties.Consumer «sm.name.toFirstLower»Properties() {
                    return new KafkaProperties.Consumer();
                }
            «ENDFOR»
        }
    '''
}