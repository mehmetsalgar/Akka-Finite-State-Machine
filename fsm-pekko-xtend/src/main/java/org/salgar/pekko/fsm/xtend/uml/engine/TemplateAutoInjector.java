package org.salgar.pekko.fsm.xtend.uml.engine;

import org.reflections.Reflections;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;

public class TemplateAutoInjector {

    /**
     * Auto-injects @Inject fields that are currently null
     * - If field is a class: instantiate directly
     * - If field is an interface: auto-detect single concrete implementation (fail if multiple)
     */
    public static void injectTemplates(Object generator) {
        if (generator == null) return;

        Class<?> clazz = generator.getClass();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Inject.class)) {
                    try {
                        field.setAccessible(true);
                        if (field.get(generator) == null) {
                            Object instance = createInstanceForType(field.getType());
                            field.set(generator, instance);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(
                                "Failed to auto-inject field " + field.getName() +
                                        " in " + clazz.getName(), e);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    private static Object createInstanceForType(Class<?> type) throws Exception {
        // If it's a concrete class, just instantiate it directly
        if (!type.isInterface() && !java.lang.reflect.Modifier.isAbstract(type.getModifiers())) {
            return type.getDeclaredConstructor().newInstance();
        }

        // For abstract classes or interfaces, search for implementations
        Reflections reflections = new Reflections("org.salgar.pekko.fsm"); // TODO: adjust this root package!

        @SuppressWarnings("unchecked")
        Set<Class<?>> implementations =
                (Set<Class<?>>)(Set<?>) reflections.getSubTypesOf((Class<Object>) type);

        // Filter only concrete types
        implementations = implementations.stream()
                .filter(c -> !c.isInterface() && !java.lang.reflect.Modifier.isAbstract(c.getModifiers()))
                .collect(Collectors.toSet());

        if (implementations.size() == 1) {
            return implementations.iterator().next().getDeclaredConstructor().newInstance();
        } else if (implementations.isEmpty()) {
            throw new RuntimeException("No implementation found for interface: " + type.getName());
        } else {
            throw new RuntimeException(
                    "Multiple implementations found for interface: " + type.getName() +
                            ". Cannot auto-select: " + implementations
            );
        }
    }
}
