package org.salgar.pekko.fsm.xtend.uml.templates

import javax.inject.Inject
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext

import org.eclipse.uml2.uml.Pseudostate
import org.eclipse.uml2.uml.State

class SnapshotAdapterTemplate {
	@Inject extension Naming
	@Inject extension GlobalVariableHelper

	def doGenerate (org.eclipse.uml2.uml.StateMachine it, IFileSystemAccess2 fsa, IGeneratorContext context) {
        val content = generate(context)
        fsa.generateFile(packagePath+"/"+name+"SnapshotAdapter.scala", content)
    }

    def generate (org.eclipse.uml2.uml.StateMachine it, IGeneratorContext context) '''
        package «packageName»

        import java.util

        import org.apache.pekko.persistence.typed.SnapshotAdapter
        import «packageName».«name»._

        object «name»SnapshotAdapter extends SnapshotAdapter[State] {
            override def toJournal(state: State): Any = {
                «name»Snapshot(state.getClass.getSimpleName, state.controlObject)
            }

            override def fromJournal(from: Any): State = {
                from match {
                    case «name.toLowerCase()»Snapshot: «name»Snapshot =>
                        «name.toLowerCase()»Snapshot.state match {
                            «FOR state : allOwnedElements().filter(Pseudostate)»
                                case "«state.name.toUpperCase()»" => «state.name.toUpperCase()»(new util.HashMap[java.lang.String, AnyRef])
                            «ENDFOR»
                            «FOR state : allOwnedElements().filter(State).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])»
                                «generateSubStates(name, state, state.name.toUpperCase(), context)»
                            «ENDFOR»
                                case _und @ _ => throw new IllegalStateException("Unidentified Event for Snapshot, may be an Event/Schema evolution occurring  Type: " + _und.toString)
                        }
                    case _unk @ _  => throw new IllegalStateException("Unknown Snapshot Type: " + _unk.toString)
                }
            }
        }
    '''

    def String generateSubStates(String name, State state, String previousStates, IGeneratorContext context)'''
        «IF state.getSubmachine() !== null»
            «FOR subState : state.getSubmachine().allOwnedElements().filter(State)»
                «generateSubStates(name, subState, previousStates + context.getGlobalVariable('submachineSeperator') + subState.name.toUpperCase(), context)»
            «ENDFOR»
        «ELSE»
            case "«previousStates»" => «previousStates»(«name.toLowerCase()»Snapshot.controlObject)
        «ENDIF»
    '''
}