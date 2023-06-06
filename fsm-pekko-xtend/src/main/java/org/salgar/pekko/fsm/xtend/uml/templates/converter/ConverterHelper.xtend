package org.salgar.pekko.fsm.xtend.uml.templates.converter

import javax.inject.Inject
import org.eclipse.uml2.uml.PrimitiveType
import org.eclipse.uml2.uml.Type
import org.salgar.pekko.fsm.xtend.uml.templates.Naming

class ConverterHelper {
    @Inject extension Naming
    def String calculateReturnType(org.eclipse.uml2.uml.Property property) {
        if(property.getUpper() === -1) {
            if(property.isOrdered) {
                "java.util.Set<" + calculateType(property.type) + ">"
            } else {
                "java.util.List<" + calculateType(property.type) + ">"
            }
        } else if(property.association === null) {
            calculateType(property.type)
        } else {
            calculateType(property.type)
        }
    }

    def dispatch String calculateType(PrimitiveType it) {
        switch (name) {
            case "Real": "Double"
            case "Unlimited Natural" : "Long"
            default: name
        }
    }

    def dispatch String calculateType(Type it) {
        if(it.eAllContents.filter(org.eclipse.uml2.uml.Property).size() === 1) {
            "String"
        } else {
            packageName + ".model." + name
        }
    }
}