package org.salgar.pekko.fsm.xtend.uml.templates.model.pojo

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.salgar.pekko.fsm.xtend.uml.templates.Naming

class PojoTemplate {
	@Inject extension Naming

	def doGenerate(Resource input, IFileSystemAccess fsa) {
        input
            .allContents
            .filter(org.eclipse.uml2.uml.Class).filter[i|!(i instanceof org.eclipse.uml2.uml.StateMachine)]
            .forEach[
                val content = generate
                fsa.generateFile(packagePath+"/model/"+name+".java", content)
            ]
    }

    def generate (org.eclipse.uml2.uml.Class it) '''
        package «packageName».model;

        import lombok.Builder;
        import lombok.Data;
        import lombok.RequiredArgsConstructor;

        @Builder(toBuilder = true)
        @Data
        @RequiredArgsConstructor
        public class «name» {
            «FOR org.eclipse.uml2.uml.Property property : getAllAttributes.filter(org.eclipse.uml2.uml.Property)»
                «IF property.upper === -1»
                    private final «IF property.isOrdered»java.util.Set<«ELSE»java.util.List<«ENDIF» «packageName(property.type)».model.«property.type.name»> «property.association.name»;
                «ELSEIF property.association === null»
                    private final «typeName(property.type)» «property.name»;
                «ELSE»
                    private final «packageName(property.type)».model.«property.type.name» «property.association.name»;
                «ENDIF»
            «ENDFOR»
        }
    '''
}