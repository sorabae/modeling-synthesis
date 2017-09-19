package fr.inria.gforge.spoon.transformation;


public class NotNullCheckAdderProcessor extends spoon.processing.AbstractProcessor<spoon.reflect.declaration.CtParameter<?>> {
    @java.lang.Override
    public boolean isToBeProcessed(spoon.reflect.declaration.CtParameter<?> element) {
        return !(element.getType().isPrimitive());
    }

    @java.lang.Override
    public void process(spoon.reflect.declaration.CtParameter<?> element) {
        spoon.reflect.code.CtCodeSnippetStatement snippet = getFactory().Core().createCodeSnippetStatement();
        final java.lang.String value = java.lang.String.format(("if (%s == null) " + "throw new IllegalArgumentException(\"[Spoon inserted check] null passed as parameter\");"), element.getSimpleName());
        snippet.setValue(value);
        if ((element.getParent(spoon.reflect.declaration.CtExecutable.class).getBody()) != null) {
            element.getParent(spoon.reflect.declaration.CtExecutable.class).getBody().insertBegin(snippet);
        }
    }
}

