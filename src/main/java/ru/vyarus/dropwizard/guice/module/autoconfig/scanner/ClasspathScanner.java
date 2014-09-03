package ru.vyarus.dropwizard.guice.module.autoconfig.scanner;

import ru.vyarus.dropwizard.guice.module.autoconfig.util.OReflectionHelper;

import java.util.List;

/**
 * Classpath scanner, reduced to provided packages.
 * Ignores classes annotated with {@code InvisibleForScanner}.
 *
 * @author Vyacheslav Rusakov
 * @since 31.08.2014
 */
public class ClasspathScanner {
    private final List<String> packages;

    public ClasspathScanner(final List<String> packages) {
        this.packages = packages;
    }

    /**
     * Scan configured classpath packages.
     *
     * @param visitor visitor to investigate found classes
     */
    public void scan(final ClassVisitor visitor) {
        for (String pkg : packages) {
            List<Class<?>> found;
            try {
                found = OReflectionHelper.getClassesFor(pkg, Thread.currentThread().getContextClassLoader());
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Failed to scan classpath", e);
            }
            for (Class<?> cls : found) {
                if (!cls.isAnnotationPresent(InvisibleForScanner.class)) {
                    visitor.visit(cls);
                }
            }
        }
    }
}
