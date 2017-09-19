package fr.inria.gforge.spoon.transformation;


public class LogProcessor extends spoon.processing.AbstractProcessor<spoon.reflect.declaration.CtExecutable> {
    @java.lang.Override
    public void process(spoon.reflect.declaration.CtExecutable element) {
        spoon.reflect.code.CtCodeSnippetStatement snippet = getFactory().Core().createCodeSnippetStatement();
        final java.lang.String value = java.lang.String.format("System.out.println(\"Enter in the method %s from the class %s\");", element.getSimpleName(), element.getParent(spoon.reflect.declaration.CtClass.class).getSimpleName());
        snippet.setValue(value);
        if ((element.getBody()) != null) {
            element.getBody().insertBegin(snippet);
        }
    }
}

