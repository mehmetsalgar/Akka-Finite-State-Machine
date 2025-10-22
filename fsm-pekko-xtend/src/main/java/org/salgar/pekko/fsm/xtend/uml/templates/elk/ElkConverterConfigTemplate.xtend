package org.salgar.pekko.fsm.xtend.uml.templates.elk

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.salgar.pekko.fsm.xtend.uml.templates.Naming

import org.salgar.pekko.fsm.xtend.uml.engine.TemplateAutoInjector

import java.util.List
import java.util.ArrayList

class ElkConverterConfigTemplate {
	extension Naming = new Naming

	def doGenerate(Resource input, IFileSystemAccess fsa) {
	    TemplateAutoInjector.injectTemplates(this)

        val clazzes = new ArrayList

        input
            .allContents
            .filter(org.eclipse.uml2.uml.Class).filter[i|!(i instanceof org.eclipse.uml2.uml.StateMachine)]
            .forEach[ clazz | clazzes.add(clazz)]

        val content = generate(clazzes)
        fsa.generateFile("org/salgar/fsm/pekko/foureyes/elasticsearch/converter/CustomConvertersConfig.java", content)

    }

    def String generate(List<org.eclipse.uml2.uml.Class> clazzes) '''
        package org.salgar.fsm.pekko.foureyes.elasticsearch.converter;

        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;

        import java.util.List;

        @Configuration
        public class CustomConvertersConfig {
            @Bean
            ElasticsearchCustomConversions elasticsearchCustomConversions(
                 «FOR org.eclipse.uml2.uml.Class clazz : clazzes.iterator.toIterable SEPARATOR ','»
                    «clazz.packageName».elasticsearch.converter.«clazz.name»ElkConverter «clazz.name.toFirstLower»ElkConverter
                 «ENDFOR»
            ) {
                return new ElasticsearchCustomConversions(
                    List.of(
                        «FOR org.eclipse.uml2.uml.Class clazz : clazzes.iterator.toIterable SEPARATOR ','»
                            «clazz.name.toFirstLower»ElkConverter
                        «ENDFOR»
                    )
                );
            }
        }
    '''
}