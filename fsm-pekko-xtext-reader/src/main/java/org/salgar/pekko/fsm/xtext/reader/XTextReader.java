package org.salgar.pekko.fsm.xtext.reader;

import org.apache.log4j.Logger;
import org.eclipse.xtext.mwe.Reader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class XTextReader extends Reader {
    private static final Logger LOGGER = Logger.getLogger(XTextReader.class);

    @Override
    public void setUseJavaClassPath(boolean isUse) {
        if (isUse) {
            Set<String> classPathEntries = new HashSet();
            try {
                retrieveClassPathEntries(Thread.currentThread().getContextClassLoader(), classPathEntries);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            List<String> tmp = new ArrayList<>(classPathEntries);

            for (String entry : tmp) {
                addPath(entry);
            }
        }
    }

    private Set<String> retrieveClassPathEntries(ClassLoader classLoader, final Set<String> classPathEntries) throws IllegalAccessException, NoSuchFieldException {
        final List<String> givenLoaderClassPathEntries = new ArrayList<String>();

        if (classLoader instanceof URLClassLoader) {
            URLClassLoader tmp = (URLClassLoader) classLoader;
            for(URL classPathURL : tmp.getURLs()) {
                String classPath = classPathURL.getPath().trim().toLowerCase();

                if (classPath.endsWith("/") || classPath.endsWith(".jar")) {
                    givenLoaderClassPathEntries.add(classPathURL.getFile());
                }
            }
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("Classpath entries from thread context loader: {" + givenLoaderClassPathEntries.toString() + "}");
            }
            classPathEntries.addAll(givenLoaderClassPathEntries);
        }

        classPathEntries.addAll(givenLoaderClassPathEntries);

        if (givenLoaderClassPathEntries.isEmpty()) {
            givenLoaderClassPathEntries.addAll(retrieveSystemClassPathEntries());

            LOGGER.debug("System classpath entries from thread context loader: {" + givenLoaderClassPathEntries + "}");

            classPathEntries.addAll(givenLoaderClassPathEntries);
            return classPathEntries;
        }
        if( classLoader.getParent() != null) {
            retrieveClassPathEntries(classLoader.getParent(), classPathEntries);
        }
        return classPathEntries;
    }

    private static List<String> retrieveSystemClassPathEntries() {
        List<String> paths = new ArrayList<>();
        String classPath = System.getProperty("java.class.path");
        String separator = System.getProperty("path.separator");
        String[] strings = classPath.split(separator);

        for(String path : strings) {
            paths.add(path);
        }

        return paths;
    }
}