package fr.inria.gforge.spoon.transformation.retry;


public class RetryProcessor extends spoon.processing.AbstractAnnotationProcessor<fr.inria.gforge.spoon.transformation.retry.RetryOnFailure, spoon.reflect.declaration.CtMethod<?>> {
    @java.lang.Override
    public void process(fr.inria.gforge.spoon.transformation.retry.RetryOnFailure retryOnFailure, spoon.reflect.declaration.CtMethod<?> ctMethod) {
        fr.inria.gforge.spoon.transformation.retry.template.RetryTemplate template = new fr.inria.gforge.spoon.transformation.retry.template.RetryTemplate(ctMethod.getBody(), retryOnFailure.attempts(), retryOnFailure.delay(), retryOnFailure.verbose());
        spoon.reflect.code.CtBlock newBody = template.apply(ctMethod.getDeclaringType());
        ctMethod.setBody(newBody);
    }
}

