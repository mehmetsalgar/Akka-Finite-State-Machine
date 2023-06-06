package org.salgar.pekko.fsm.xtend.uml.templates.elk

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess

import org.salgar.pekko.fsm.xtend.uml.templates.Naming
import org.salgar.pekko.fsm.xtend.uml.templates.SubStateMachineHelper

import org.eclipse.uml2.uml.Pseudostate
import org.eclipse.uml2.uml.State
import org.eclipse.uml2.uml.Transition

class ElkPersistEvent2StateTransformTemplate {
	@Inject extension Naming
	@Inject extension SubStateMachineHelper

	def doGenerate(Resource input, IFileSystemAccess fsa) {
        input
            .allContents
            .filter(org.eclipse.uml2.uml.StateMachine)
            .filter(s | s.isActive)
            .forEach[
                val content = generate
                fsa.generateFile(packagePath+"/elasticsearch/transform/"+name+"ElasticsearchTransform.scala", content)
            ]
    }

    def generate (org.eclipse.uml2.uml.StateMachine it) '''
        package «packageName».elasticsearch.transform

        import «packageName».«name»

        object «name»ElasticsearchTransform {
            def transform(persistEvent: «name».PersistEvent ) : String = {
                persistEvent match {
                «FOR Transition transition : giveTransitionsRecursive(allOwnedElements().filter(Pseudostate), allOwnedElements().filter(State)).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])»
                    «IF transition.triggers !== null && !transition.triggers.empty»
                        case «name».«transition.triggers.get(0).event.name»(payload) =>
                            "«transition.target.name.toUpperCase()»"

                    «ENDIF»
                «ENDFOR»
                    case _ => null
                }
            }
        }
    '''
}