package fr.inria.gforge.spoon.assertgenerator;


public class Logger {
    public static java.util.Map<java.lang.String, java.lang.Object> observations = new java.util.HashMap<java.lang.String, java.lang.Object>();

    public static void observe(java.lang.String name, java.lang.Object object) {
        fr.inria.gforge.spoon.assertgenerator.Logger.observations.put(name, object);
    }
}

