package org.salgar.pekko.fsm.xtend.uml.templates.command

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.uml2.uml.Transition
import org.salgar.pekko.fsm.xtend.uml.templates.Naming
import org.salgar.pekko.fsm.xtend.uml.templates.StateMachineHelper

class CommandConstantTemplate {
	@Inject extension Naming
	@Inject extension StateMachineHelper

	def doGenerate(Resource input, IFileSystemAccess fsa) {
        input
            .allContents
            .filter(org.eclipse.uml2.uml.StateMachine).filter[i|i.active]
            .forEach[
                generate(fsa)
            ]
    }

    def generate(org.eclipse.uml2.uml.StateMachine it, IFileSystemAccess fsa) {
        val content = generate
        fsa.generateFile(packagePath+"/command/"+name+"CommandConstants.java", content)
    }

    def String generate(org.eclipse.uml2.uml.StateMachine it) '''
        package «packageName».command;

        public class «name»CommandConstants {
            «FOR trigger : giveTransitionWithTrigger(allOwnedElements().filter(Transition)).sortWith([o1, o2 | o1.getName().compareTo(o2.getName())])»
                public static final String «trigger.name.substring(0, 2).toUpperCase»_«trigger.name.substring(2, trigger.name.length).toUpperCase» = "«trigger.name»";
            «ENDFOR»
        }
    '''
}