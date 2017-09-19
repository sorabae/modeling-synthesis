package fr.inria.gforge.spoon.assertgenerator.workflow;


public class Analyzer {
    java.util.List<spoon.reflect.code.CtLocalVariable> analyze(spoon.reflect.declaration.CtMethod testMethod) {
        spoon.reflect.visitor.filter.TypeFilter filterLocalVar = new spoon.reflect.visitor.filter.TypeFilter(spoon.reflect.code.CtLocalVariable.class) {
            public boolean matches(spoon.reflect.code.CtLocalVariable localVariable) {
                return !(localVariable.getType().isPrimitive());
            }
        };
        return testMethod.getElements(filterLocalVar);
    }

    public java.util.Map<spoon.reflect.declaration.CtMethod, java.util.List<spoon.reflect.code.CtLocalVariable>> analyze(spoon.reflect.declaration.CtType<?> ctClass) {
        spoon.reflect.reference.CtTypeReference<org.junit.Test> reference = ctClass.getFactory().Type().createReference(org.junit.Test.class);
        return ctClass.getMethods().stream().filter(( ctMethod) -> (ctMethod.getAnnotation(reference)) != null).collect(java.util.stream.Collectors.toMap(( ctMethod) -> ctMethod, this::analyze));
    }
}

