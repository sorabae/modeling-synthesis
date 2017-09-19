package fr.inria.gforge.spoon.assertgenerator.workflow;


public class Collector {
    private spoon.reflect.factory.Factory factory;

    public Collector(spoon.reflect.factory.Factory factory) {
        this.factory = factory;
    }

    public void collect(spoon.Launcher launcher, spoon.reflect.declaration.CtMethod<?> testMethod, java.util.List<spoon.reflect.code.CtLocalVariable> localVariables) {
        final spoon.reflect.declaration.CtClass testClass = testMethod.getParent(spoon.reflect.declaration.CtClass.class);
        testClass.removeMethod(testMethod);
        final spoon.reflect.declaration.CtMethod<?> clone = testMethod.clone();
        instrument(clone, localVariables);
        testClass.addMethod(clone);
        java.lang.System.out.println(clone);
        run(launcher, testClass, clone);
        testClass.removeMethod(clone);
        testClass.addMethod(testMethod);
    }

    public void run(spoon.Launcher launcher, spoon.reflect.declaration.CtClass testClass, spoon.reflect.declaration.CtMethod<?> clone) {
        final java.lang.String fullQualifiedName = testClass.getQualifiedName();
        final java.lang.String testMethodName = clone.getSimpleName();
        try {
            final spoon.SpoonModelBuilder compiler = launcher.createCompiler();
            compiler.compile(spoon.SpoonModelBuilder.InputType.CTTYPES);
            fr.inria.gforge.spoon.assertgenerator.test.TestRunner.runTest(fullQualifiedName, testMethodName, new java.lang.String[]{ "spooned-classes" });
        } catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    @java.lang.SuppressWarnings(value = "unchecked")
    public void instrument(spoon.reflect.declaration.CtMethod<?> testMethod, java.util.List<spoon.reflect.code.CtLocalVariable> ctLocalVariables) {
        ctLocalVariables.forEach(( ctLocalVariable) -> this.instrument(testMethod, ctLocalVariable));
    }

    void instrument(spoon.reflect.declaration.CtMethod testMethod, spoon.reflect.code.CtLocalVariable localVariable) {
        java.util.List<spoon.reflect.declaration.CtMethod> getters = fr.inria.gforge.spoon.assertgenerator.Util.getGetters(localVariable);
        getters.forEach(( getter) -> {
            spoon.reflect.code.CtInvocation invocationToGetter = fr.inria.gforge.spoon.assertgenerator.Util.invok(getter, localVariable);
            spoon.reflect.code.CtInvocation invocationToObserve = createObserve(getter, invocationToGetter);
            testMethod.getBody().insertEnd(invocationToObserve);
        });
    }

    spoon.reflect.code.CtInvocation createObserve(spoon.reflect.declaration.CtMethod getter, spoon.reflect.code.CtInvocation invocationToGetter) {
        spoon.reflect.code.CtTypeAccess accessToLogger = factory.createTypeAccess(factory.createCtTypeReference(fr.inria.gforge.spoon.assertgenerator.Logger.class));
        spoon.reflect.reference.CtExecutableReference refObserve = factory.Type().get(fr.inria.gforge.spoon.assertgenerator.Logger.class).getMethodsByName("observe").get(0).getReference();
        return factory.createInvocation(accessToLogger, refObserve, factory.createLiteral(fr.inria.gforge.spoon.assertgenerator.Util.getKey(getter)), invocationToGetter);
    }
}

