package org.salgar.pekko.fsm.xtend.uml.templates

import org.eclipse.xtext.generator.IFileSystemAccess

class SnapshotTemplate {
	extension Naming = new Naming

	def doGenerate (org.eclipse.uml2.uml.StateMachine it, IFileSystemAccess fsa) {
        val content = generate
        fsa.generateFile(packagePath+"/"+name+"Snapshot.scala", content)
    }

    def generate (org.eclipse.uml2.uml.StateMachine it) '''
        package «packageName»

        import java.util

        import com.fasterxml.jackson.databind.annotation.JsonDeserialize
        import org.salgar.pekko.fsm.base.CborSerializable

        case class «name»Snapshot(
                                       state : String,
                                       @JsonDeserialize(as = classOf[util.Map[java.lang.String, AnyRef]]) controlObject: util.Map[java.lang.String, AnyRef])
          extends CborSerializable
    '''
}