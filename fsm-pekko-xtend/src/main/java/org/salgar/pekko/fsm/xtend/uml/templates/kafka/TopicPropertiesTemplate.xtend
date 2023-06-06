package org.salgar.pekko.fsm.xtend.uml.templates.kafka

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.salgar.pekko.fsm.xtend.uml.templates.Naming

class TopicPropertiesTemplate {
	@Inject extension Naming

	def doGenerate(Resource input, IFileSystemAccess fsa) {
	    val Iterable<org.eclipse.uml2.uml.StateMachine> sms =
	        input
               .allContents
               .toIterable
               .filter(org.eclipse.uml2.uml.StateMachine).filter[i|i.active]
        val masterSm = sms.filter[s|!s.abstract].head
	    val content = generate(sms, masterSm)
        fsa.generateFile(packagePath(masterSm)+"/kafka/"+"TopicProperties.java", content)
    }

    def generate(Iterable<org.eclipse.uml2.uml.StateMachine> sms, org.eclipse.uml2.uml.StateMachine masterSm)'''
        package «packageName(masterSm)».kafka.config;

        import lombok.Data;
        import org.springframework.boot.context.properties.ConfigurationProperties;
        import org.springframework.stereotype.Component;

        @Component
        @ConfigurationProperties("«packageName(masterSm)».kafka.topics")
        @Data
        public class TopicProperties {
            private String «masterSm.name.toFirstLower»;
            «FOR sm : sms»
                private String «sm.name.toFirstLower»;
            «ENDFOR»
        }
    '''
}