package org.salgar.pekko.fsm.xtend.uml.templates

import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess

class AsciidocFsmTemplate {
	@Inject AsciidocTemplate asciidocTemplate

	def doGenerate(Resource input, IFileSystemAccess fsa) {
	    asciidocTemplate.doGenerate(input,fsa)

	    for(it : input.allContents.toIterable.filter(org.eclipse.uml2.uml.Model)) {
	        val content = generate
            fsa.generateFile("/fsm-pekko-base.adoc", content)
        }
	}

    def generate(org.eclipse.uml2.uml.Model it) '''
        = Main Use Cases

        «FOR sm : model.allOwnedElements.filter(org.eclipse.uml2.uml.StateMachine)»

            include::«sm.name.toLowerCase()»/«sm.name».adoc[leveloffset=+1]

        «ENDFOR»
    '''
}