package fr.inria.gforge.spoon.bound.annotation;


@java.lang.annotation.Target(value = java.lang.annotation.ElementType.PARAMETER)
public @interface Bound {
    double max();

    double min();
}

