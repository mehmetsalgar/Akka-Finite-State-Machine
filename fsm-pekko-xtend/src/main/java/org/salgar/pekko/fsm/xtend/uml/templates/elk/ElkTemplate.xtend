package org.salgar.pekko.fsm.xtend.uml.templates.elk

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.salgar.pekko.fsm.xtend.uml.templates.Naming

class ElkTemplate {
	extension Naming = new Naming

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

        import org.springframework.data.elasticsearch.annotations.Field;
        import org.springframework.data.elasticsearch.annotations.FieldType;

        public class «name»ELK {
            private final «packageName».model.«name» _«name.toFirstLower»;
            private «name»ELK() {
                this._«name.toFirstLower» = new «packageName».model.«name»();
            }
            public «name»ELK(«packageName».model.«name» _«name.toFirstLower») {
                this._«name.toFirstLower» = _«name.toFirstLower»;
            }
            «FOR org.eclipse.uml2.uml.Property property : getAllAttributes.filter(org.eclipse.uml2.uml.Property)»
                «IF property.upper === -1»
                    @Field(type = FieldType.«property.type.fieldType»)
                    public «IF property.isOrdered»java.util.Set<«ELSE»java.util.List<«ENDIF»«packageName(property.type)».model.«property.type.name»> get«property.association.name.toFirstUpper»() {
                        return this._«name.toFirstLower».get«property.association.name.toFirstUpper»();
                    }
                «ELSEIF property.association === null»
                    @Field(type = FieldType.«property.type.fieldType»)
                    public «typeName(property.type)» get«property.name.toFirstUpper»() {
                        return this._«name.toFirstLower».get«property.name.toFirstUpper»();
                    }
                «ELSE»
                    @Field(type = FieldType.«property.type.fieldType»)
                    public «packageName(property.type)».model.«property.type.name» get«property.association.name.toFirstUpper»() {
                        return this._«name.toFirstLower».get«property.association.name.toFirstUpper»();
                    }
                «ENDIF»
            «ENDFOR»
        }
    '''
}