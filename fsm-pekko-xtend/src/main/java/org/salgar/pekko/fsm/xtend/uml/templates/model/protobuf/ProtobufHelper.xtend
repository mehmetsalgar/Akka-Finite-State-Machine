package org.salgar.pekko.fsm.xtend.uml.templates.model.protobuf

import java.util.Iterator
import java.util.List
import java.util.ArrayList
import java.util.Map
import java.util.HashMap

import org.eclipse.xtext.util.Tuples
import org.eclipse.xtext.util.Pair

import org.salgar.pekko.fsm.xtend.uml.templates.Naming
import javax.inject.Inject

class ProtobufHelper {
    @Inject extension Naming

    def Map<Pair<String,String>, List<org.eclipse.uml2.uml.Class>> populatePackageClassMap(Iterator<org.eclipse.uml2.uml.Class> clazzes) {
        val Map<Pair<String, String>, List<org.eclipse.uml2.uml.Class>> map = new HashMap

        while(clazzes.hasNext) {
            val org.eclipse.uml2.uml.Class clazz = clazzes.next
            val Pair<String, String> key = Tuples.pair(clazz.nearestPackage.name, packageName(clazz))
            val List<org.eclipse.uml2.uml.Class> _clazzes = map.getOrDefault(key, new ArrayList)
            _clazzes.add(clazz)
            map.put(key, _clazzes)
        }

        return map;
    }
}