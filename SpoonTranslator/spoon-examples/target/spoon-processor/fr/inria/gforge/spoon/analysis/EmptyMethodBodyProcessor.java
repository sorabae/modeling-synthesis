package fr.inria.gforge.spoon.analysis;


public class EmptyMethodBodyProcessor extends spoon.processing.AbstractProcessor<spoon.reflect.declaration.CtMethod<?>> {
    public final java.util.List<spoon.reflect.declaration.CtMethod> emptyMethods = new java.util.ArrayList<spoon.reflect.declaration.CtMethod>();

    public void process(spoon.reflect.declaration.CtMethod<?> element) {
        if ((((element.getParent(spoon.reflect.declaration.CtClass.class)) != null) && (!(element.getModifiers().contains(spoon.reflect.declaration.ModifierKind.ABSTRACT)))) && ((element.getBody().getStatements().size()) == 0)) {
            emptyMethods.add(element);
        }
    }
}

