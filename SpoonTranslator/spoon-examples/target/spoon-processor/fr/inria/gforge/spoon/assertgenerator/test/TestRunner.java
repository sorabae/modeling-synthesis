package fr.inria.gforge.spoon.assertgenerator.test;


public class TestRunner {
    private static java.util.function.Function<java.lang.String[], java.net.URL[]> arrayStringToArrayUrl = ( arrayStr) -> java.util.Arrays.stream(arrayStr).map(java.io.File::new).map(java.io.File::toURI).map(( uri) -> {
        try {
            return uri.toURL();
        } catch (java.net.MalformedURLException e) {
            throw new java.lang.RuntimeException(e);
        }
    }).toArray(java.net.URL[]::new);

    public static java.util.List<org.junit.runner.notification.Failure> runTest(java.lang.String fullQualifiedName, java.lang.String testCaseName, java.lang.String[] classpath) throws java.lang.ClassNotFoundException, java.net.MalformedURLException {
        java.lang.ClassLoader classLoader = new java.net.URLClassLoader(fr.inria.gforge.spoon.assertgenerator.test.TestRunner.arrayStringToArrayUrl.apply(classpath), java.lang.ClassLoader.getSystemClassLoader());
        org.junit.runner.Request request = org.junit.runner.Request.method(classLoader.loadClass(fullQualifiedName), testCaseName);
        org.junit.runner.Runner runner = request.getRunner();
        org.junit.runner.notification.RunNotifier fNotifier = new org.junit.runner.notification.RunNotifier();
        final fr.inria.gforge.spoon.assertgenerator.test.TestListener listener = new fr.inria.gforge.spoon.assertgenerator.test.TestListener();
        fNotifier.addFirstListener(listener);
        fNotifier.fireTestRunStarted(runner.getDescription());
        runner.run(fNotifier);
        return listener.getTestFails();
    }

    public static java.util.List<org.junit.runner.notification.Failure> runTest(java.lang.String fullQualifiedName, java.lang.String[] classpath) throws java.lang.ClassNotFoundException, java.net.MalformedURLException {
        java.lang.ClassLoader classLoader = new java.net.URLClassLoader(fr.inria.gforge.spoon.assertgenerator.test.TestRunner.arrayStringToArrayUrl.apply(classpath), java.lang.ClassLoader.getSystemClassLoader());
        org.junit.runner.Request request = org.junit.runner.Request.classes(classLoader.loadClass(fullQualifiedName));
        org.junit.runner.Runner runner = request.getRunner();
        org.junit.runner.notification.RunNotifier fNotifier = new org.junit.runner.notification.RunNotifier();
        final fr.inria.gforge.spoon.assertgenerator.test.TestListener listener = new fr.inria.gforge.spoon.assertgenerator.test.TestListener();
        fNotifier.addFirstListener(listener);
        fNotifier.fireTestRunStarted(runner.getDescription());
        runner.run(fNotifier);
        return listener.getTestFails();
    }
}

