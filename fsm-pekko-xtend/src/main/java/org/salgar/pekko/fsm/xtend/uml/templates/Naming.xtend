package org.salgar.pekko.fsm.xtend.uml.templates

import org.eclipse.uml2.uml.PackageableElement
import org.eclipse.uml2.uml.PrimitiveType
import org.eclipse.uml2.uml.Type
import org.eclipse.uml2.uml.Property

class Naming {

	/**
	 * Strip off Model name
	 */
	def packageName (PackageableElement it) {
		val qnNearestPck = it.nearestPackage.qualifiedName
		qnNearestPck.substring(qnNearestPck.indexOf("::")+2).replace('::','.')
	}

	def packagePath (PackageableElement it) {
		packageName.replace('.','/')
	}

	def dispatch String typeName (PrimitiveType it) {
		switch (name) {
			case "Real": "Double"
			case "Unlimited Natural" : "Long"
			default: name
		}
	}

	def dispatch String typeName (Type it) {
		packageName + "." + name
	}

	def dispatch String typeName (Property it) {
		if (multivalued) {
			val collectionType = if (unique) "java.util.Set" else "java.util.List"
			collectionType +"<"+type.typeName+">"
		} else {
			type.typeName
		}
	}

    def dispatch String fieldType (PrimitiveType it) {
        switch (name) {
            case "Real": "Double"
            case "Unlimited Natural" : "Long"
            case "String": "Text"
            default: "Text"
        }
    }

    def dispatch String fieldType (Type it) {
        "Nested"
    }

    def dispatch String fieldType (Property it) {
        if (multivalued) {
            "Object"
        } else {
            type.typeName
        }
    }

	def nearestPackageName (PackageableElement it) {
        it.nearestPackage.name
    }

    def dispatch String protobufTypeName (Type it) {
        name.toLowerCase
    }

    def dispatch String protobufTypeName (PrimitiveType it) {
        switch (name) {
            case "Boolean": "bool"
            case "Real": "double"
            case "Unlimited Natural" : "uint64"
            default: name.toLowerCase
        }
    }
}