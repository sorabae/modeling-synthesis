package fr.inria.gforge.spoon.analysis;


public class DocProcessor extends spoon.processing.AbstractProcessor<spoon.reflect.declaration.CtElement> {
    public final java.util.List<spoon.reflect.declaration.CtElement> undocumentedElements = new java.util.ArrayList<spoon.reflect.declaration.CtElement>();

    public void process(spoon.reflect.declaration.CtElement element) {
        if (((element instanceof spoon.reflect.declaration.CtNamedElement) || (element instanceof spoon.reflect.declaration.CtField)) || (element instanceof spoon.reflect.declaration.CtExecutable)) {
            if ((((spoon.reflect.declaration.CtModifiable) (element)).getModifiers().contains(spoon.reflect.declaration.ModifierKind.PUBLIC)) || (((spoon.reflect.declaration.CtModifiable) (element)).getModifiers().contains(spoon.reflect.declaration.ModifierKind.PROTECTED))) {
                if (((element.getDocComment()) == null) || (element.getDocComment().equals(""))) {
                    undocumentedElements.add(element);
                }
            }
        }
    }
}

