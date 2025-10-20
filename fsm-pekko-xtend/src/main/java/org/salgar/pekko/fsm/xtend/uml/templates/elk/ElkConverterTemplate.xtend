package org.salgar.pekko.fsm.xtend.uml.templates.elk

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.salgar.pekko.fsm.xtend.uml.templates.Naming

import org.salgar.pekko.fsm.xtend.uml.engine.TemplateAutoInjector

class ElkConverterTemplate {
	extension Naming = new Naming

	def doGenerate(Resource input, IFileSystemAccess fsa) {
	    TemplateAutoInjector.injectTemplates(this)
        input
            .allContents
            .filter(org.eclipse.uml2.uml.Class).filter[i|!(i instanceof org.eclipse.uml2.uml.StateMachine)]
            .forEach[
                val content = generate
                fsa.generateFile(packagePath+"/elasticsearch/converter/"+name+"ElkConverter.java", content)
            ]
    }


    def String generate(org.eclipse.uml2.uml.Class it) '''
        package «packageName».elasticsearch.converter;

        import org.springframework.core.convert.converter.Converter;
        import org.springframework.data.convert.WritingConverter;
        import org.springframework.stereotype.Component;
        import «packageName».model.«name»;
        import «packageName».elasticsearch.model.«name»ELK;

        @Component
        @WritingConverter
        public class «name»ElkConverter implements Converter<«name», «name»ELK> {
            @Override
            public «name»ELK convert(«name» «name.toFirstLower») {
                return new «name»ELK(«name.toFirstLower»);
            }
        }
    '''
}