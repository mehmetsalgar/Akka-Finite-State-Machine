package org.salgar.pekko.fsm.xtend.uml.templates.model.protobuf

import java.util.Map
import java.util.List

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.util.Pair
import org.salgar.pekko.fsm.xtend.uml.templates.Naming

class ProtobufTemplate {
	@Inject extension Naming
	@Inject extension ProtobufHelper

	def doGenerate(Resource input, IFileSystemAccess fsa) {
	    val Map<Pair<String, String>, List<org.eclipse.uml2.uml.Class>> map =
	        populatePackageClassMap(input.allContents.filter(org.eclipse.uml2.uml.Class))

        for(Pair<String, String> key : map.keySet) {
            val List<org.eclipse.uml2.uml.Class> clazzes = map.get(key)

            val content = generateAll(key, clazzes)
            fsa.generateFile(packagePath(clazzes.get(0))+"/protobuf/"+key.first.toLowerCase.toFirstUpper+".proto", content)
        }
    }

    def generateAll (Pair<String, String> key, List<org.eclipse.uml2.uml.Class> clazzes) '''
        syntax = "proto3";
        option java_multiple_files = true;

        package «key.second».protobuf;

        import "google/protobuf/any.proto";

        «FOR org.eclipse.uml2.uml.Class clazz : clazzes»
            «generate(clazz)»
        «ENDFOR»
    '''

    def dispatch generate (org.eclipse.uml2.uml.StateMachine sm) '''
        «IF sm.active»
            message «sm.name»Command {
                string useCaseKey = 1 ;
                string command = 2;
                map<string, google.protobuf.Any> payload = 3;
            }
        «ENDIF»
    '''

    def dispatch generate (org.eclipse.uml2.uml.Class clazz) '''
        message «clazz.name» {
            «var count=1»
            «FOR org.eclipse.uml2.uml.Property property : clazz.getAllAttributes.filter(org.eclipse.uml2.uml.Property)»
                «IF property.upper === -1»
                    repeated «packageName(property.type)».protobuf.«property.type.name» «property.association.name» = «count++»;
                «ELSEIF property.association === null»
                    «protobufTypeName(property.type)» «property.name» = «count++»;
                «ELSE»
                    «packageName(property.type)».protobuf.«property.type.name» «property.association.name» = «count++»;
                «ENDIF»
            «ENDFOR»
        }
    '''
}