package fr.inria.gforge.spoon.assertgenerator;


public class Util {
    public static java.lang.String getKey(spoon.reflect.declaration.CtMethod method) {
        return ((method.getParent(spoon.reflect.declaration.CtClass.class).getSimpleName()) + "#") + (method.getSimpleName());
    }

    public static spoon.reflect.code.CtInvocation invok(spoon.reflect.declaration.CtMethod method, spoon.reflect.code.CtLocalVariable localVariable) {
        final spoon.reflect.reference.CtExecutableReference reference = method.getReference();
        final spoon.reflect.code.CtVariableAccess variableRead = method.getFactory().createVariableRead(localVariable.getReference(), false);
        return method.getFactory().createInvocation(variableRead, reference);
    }

    public static java.util.List<spoon.reflect.declaration.CtMethod> getGetters(spoon.reflect.code.CtLocalVariable localVariable) {
        return ((java.util.Set<spoon.reflect.declaration.CtMethod<?>>) (localVariable.getType().getDeclaration().getMethods())).stream().filter(( method) -> ((method.getParameters().isEmpty()) && ((method.getType()) != (localVariable.getFactory().Type().VOID_PRIMITIVE))) && ((method.getSimpleName().startsWith("get")) || (method.getSimpleName().startsWith("is")))).collect(java.util.stream.Collectors.toList());
    }
}

