package fr.inria.gforge.spoon.assertgenerator.workflow;


public class AssertionAdder {
    private spoon.reflect.factory.Factory factory;

    public AssertionAdder(spoon.reflect.factory.Factory factory) {
        this.factory = factory;
    }

    @java.lang.SuppressWarnings(value = "unchecked")
    public void addAssertion(spoon.reflect.declaration.CtMethod<?> testMethod, java.util.List<spoon.reflect.code.CtLocalVariable> ctLocalVariables) {
        ctLocalVariables.forEach(( ctLocalVariable) -> this.addAssertion(testMethod, ctLocalVariable));
        java.lang.System.out.println(testMethod);
    }

    @java.lang.SuppressWarnings(value = "unchecked")
    void addAssertion(spoon.reflect.declaration.CtMethod testMethod, spoon.reflect.code.CtLocalVariable localVariable) {
        java.util.List<spoon.reflect.declaration.CtMethod> getters = fr.inria.gforge.spoon.assertgenerator.Util.getGetters(localVariable);
        getters.forEach(( getter) -> {
            java.lang.String key = fr.inria.gforge.spoon.assertgenerator.Util.getKey(getter);
            spoon.reflect.code.CtInvocation invocationToGetter = fr.inria.gforge.spoon.assertgenerator.Util.invok(getter, localVariable);
            spoon.reflect.code.CtInvocation invocationToAssert = fr.inria.gforge.spoon.assertgenerator.workflow.AssertionAdder.createAssert("assertEquals", factory.createLiteral(fr.inria.gforge.spoon.assertgenerator.Logger.observations.get(key)), invocationToGetter);
            testMethod.getBody().insertEnd(invocationToAssert);
        });
    }

    public static spoon.reflect.code.CtInvocation createAssert(java.lang.String name, spoon.reflect.code.CtExpression... parameters) {
        final spoon.reflect.factory.Factory factory = parameters[0].getFactory();
        spoon.reflect.code.CtTypeAccess accessToAssert = factory.createTypeAccess(factory.createCtTypeReference(org.junit.Assert.class));
        spoon.reflect.reference.CtExecutableReference assertEquals = factory.Type().get(org.junit.Assert.class).getMethodsByName(name).get(0).getReference();
        return factory.createInvocation(accessToAssert, assertEquals, parameters[0], parameters[1]);
    }
}

