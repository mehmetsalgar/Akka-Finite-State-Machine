package org.salgar.pekko.fsm.xtend.uml.templates.elk

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.salgar.pekko.fsm.xtend.uml.templates.Naming

class ElkTemplate {
	@Inject extension Naming

	def doGenerate(Resource input, IFileSystemAccess fsa) {
        input
            .allContents
            .filter(org.eclipse.uml2.uml.Class).filter[i|!(i instanceof org.eclipse.uml2.uml.StateMachine)]
            .forEach[
                val content = generate
                fsa.generateFile(packagePath+"/elasticsearch/model/"+name+"ELK.java", content)
            ]
    }

    def generate (org.eclipse.uml2.uml.Class it) '''
        package «packageName».elasticsearch.model;

        import lombok.Getter;
        import org.springframework.data.elasticsearch.annotations.Field;
        import org.springframework.data.elasticsearch.annotations.FieldType;

        @Getter
        public class «name»ELK {
            private «packageName».model.«name» _«name.toFirstLower»;
            private «name»ELK() {
            }
            public «name»ELK(«packageName».model.«name» _«name.toFirstLower») {
                this._«name.toFirstLower» = _«name.toFirstLower»;
            }
            «FOR org.eclipse.uml2.uml.Property property : getAllAttributes.filter(org.eclipse.uml2.uml.Property)»
                «IF property.upper === -1»
                    @Field(type = FieldType.«property.type.fieldType»)
                    private final «IF property.isOrdered»java.util.Set<«ELSE»java.util.List<«ENDIF» «packageName(property.type)».model.«property.type.name»> «property.association.name» = _«name.toFirstLower».get«property.association.name.toFirstUpper»();
                «ELSEIF property.association === null»
                    @Field(type = FieldType.«property.type.fieldType»)
                    private final «typeName(property.type)» «property.name» = _«name.toFirstLower».get«property.name.toFirstUpper»() ;
                «ELSE»
                    @Field(type = FieldType.«property.type.fieldType»)
                    private final «packageName(property.type)».model.«property.type.name» «property.association.name» = _«name.toFirstLower».get«property.association.name.toFirstUpper»();
                «ENDIF»
            «ENDFOR»
        }
    '''
}