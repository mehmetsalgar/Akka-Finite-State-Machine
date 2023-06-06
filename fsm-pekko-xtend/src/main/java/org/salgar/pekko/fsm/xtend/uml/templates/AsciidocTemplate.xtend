package org.salgar.pekko.fsm.xtend.uml.templates

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess

import org.eclipse.uml2.uml.State

class AsciidocTemplate {
	@Inject AsciidocsUsecasesTemplate asciidocsUsecasesTemplate
	@Inject AsciidocsTransitionTemplate asciidocsTransitionTemplate

	def doGenerate(Resource input, IFileSystemAccess fsa) {
	    for(sm : input.allContents.toIterable.filter(org.eclipse.uml2.uml.StateMachine)) {
            asciidocsUsecasesTemplate.generate(sm, fsa)
            asciidocsTransitionTemplate.generate(sm, fsa)

            val content = generate(sm)
            fsa.generateFile("/"+sm.name.toLowerCase()+"/"+sm.name+".adoc", content)
        }
	}

    def generate (org.eclipse.uml2.uml.StateMachine it) '''
        = Use Case : «name»

        [plantuml, "«name»", "png"]
        ----
        include::../../puml/«name.toLowerCase()»/«name».puml[]
        ----

        «FOR comment: getOwnedComments»
            «comment.body»
        «ENDFOR»
        «FOR state : allOwnedElements().filter(State)»

            //«state.name»
            include::usecases/«state.name».adoc[leveloffset=+1]

        «ENDFOR»
    '''
}