package fr.inria.gforge.spoon.transformation.retry;


@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target(value = java.lang.annotation.ElementType.METHOD)
public @interface RetryOnFailure {
    int attempts() default 3;

    long delay() default 50L;

    boolean verbose() default false;
}

