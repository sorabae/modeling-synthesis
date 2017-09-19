package fr.inria.gforge.spoon.bound.processing;


public class BoundProcessor extends spoon.processing.AbstractAnnotationProcessor<fr.inria.gforge.spoon.bound.annotation.Bound, spoon.reflect.declaration.CtParameter<?>> {
    public void process(fr.inria.gforge.spoon.bound.annotation.Bound annotation, spoon.reflect.declaration.CtParameter<?> element) {
        final spoon.reflect.declaration.CtMethod parent = element.getParent(spoon.reflect.declaration.CtMethod.class);
        spoon.reflect.code.CtIf anIf = getFactory().Core().createIf();
        anIf.setCondition(getFactory().Code().<java.lang.Boolean>createCodeSnippetExpression((((element.getSimpleName()) + " < ") + (annotation.min()))));
        spoon.reflect.code.CtThrow throwStmt = getFactory().Core().createThrow();
        throwStmt.setThrownExpression(((spoon.reflect.code.CtExpression<? extends java.lang.Throwable>) (getFactory().Core().createCodeSnippetExpression().setValue((((("new RuntimeException(\"out of min bound (\" + " + (element.getSimpleName())) + " + \" < ") + (annotation.min())) + "\")")))));
        anIf.setThenStatement(throwStmt);
        parent.getBody().insertBegin(anIf);
        anIf.setParent(parent);
        anIf = getFactory().Core().createIf();
        anIf.setCondition(getFactory().Code().<java.lang.Boolean>createCodeSnippetExpression((((element.getSimpleName()) + " > ") + (annotation.max()))));
        anIf.setThenStatement(getFactory().Code().createCtThrow((((("new RuntimeException(\"out of max bound (\" + " + (element.getSimpleName())) + " + \" > ") + (annotation.max())) + "\")")));
        parent.getBody().insertBegin(anIf);
        anIf.setParent(parent);
    }
}

