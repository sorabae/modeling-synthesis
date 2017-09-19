package fr.inria.gforge.spoon.bound.processing;


public class BoundTemplateProcessor extends spoon.processing.AbstractAnnotationProcessor<fr.inria.gforge.spoon.bound.annotation.Bound, spoon.reflect.declaration.CtParameter<?>> {
    public void process(fr.inria.gforge.spoon.bound.annotation.Bound annotation, spoon.reflect.declaration.CtParameter<?> element) {
        spoon.reflect.declaration.CtExecutable<?> e = element.getParent();
        if ((e.getBody()) == null) {
            return ;
        }
        spoon.reflect.declaration.CtClass<?> type = ((spoon.reflect.declaration.CtClass<?>) (e.getParent(spoon.reflect.declaration.CtClass.class)));
        spoon.template.Template t = new fr.inria.gforge.spoon.bound.template.BoundTemplate(getFactory().Type().createReference(java.lang.Double.class), element.getSimpleName(), annotation.min(), annotation.max());
        final spoon.reflect.code.CtBlock apply = ((spoon.reflect.code.CtBlock) (t.apply(type)));
        for (int i = (apply.getStatements().size()) - 1; i >= 0; i--) {
            final spoon.reflect.code.CtStatement statement = apply.getStatement(i);
            e.getBody().insertBegin(statement);
            statement.setParent(e.getBody());
        }
    }
}

