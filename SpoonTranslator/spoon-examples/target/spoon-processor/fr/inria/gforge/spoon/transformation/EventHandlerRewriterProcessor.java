package fr.inria.gforge.spoon.transformation;


public class EventHandlerRewriterProcessor extends spoon.processing.AbstractProcessor<spoon.reflect.declaration.CtMethod<?>> {
    private int id = 0;

    @java.lang.Override
    public boolean isToBeProcessed(spoon.reflect.declaration.CtMethod<?> element) {
        spoon.reflect.declaration.CtClass parent = null;
        if ((element.getParent()) instanceof spoon.reflect.declaration.CtClass) {
            parent = ((spoon.reflect.declaration.CtClass) (element.getParent()));
        }
        return (isOnClick(element, parent)) || (isOnItemClick(element, parent));
    }

    public boolean isOnClick(spoon.reflect.declaration.CtMethod<?> element, spoon.reflect.declaration.CtClass parent) {
        return ((element.getSimpleName().equals("onClick")) && (parent != null)) && (!(parent.isAnonymous()));
    }

    public boolean isOnItemClick(spoon.reflect.declaration.CtMethod<?> element, spoon.reflect.declaration.CtClass parent) {
        return ((element.getSimpleName().equals("onItemClick")) && (parent != null)) && (parent.isAnonymous());
    }

    @java.lang.Override
    public void process(spoon.reflect.declaration.CtMethod<?> element) {
        java.lang.System.out.println(("Processing " + (element.getSimpleName())));
        switch (element.getSimpleName()) {
            case "onClick" :
                rewriteOnClick(element);
                break;
            case "onItemClick" :
                rewriteOnItemClick(element);
                break;
            default :
                break;
        }
    }

    public void rewriteOnClick(spoon.reflect.declaration.CtMethod<?> element) {
        spoon.reflect.declaration.CtMethod<java.lang.Void> wrapper = createWrapper(element);
        wrapper.setSimpleName(("$wrapper_" + (id)));
        final spoon.reflect.declaration.CtParameter<java.lang.Integer> parameter = getFactory().Core().<java.lang.Integer>createParameter();
        final spoon.reflect.reference.CtTypeReference<java.lang.Integer> integerRef = getFactory().Code().createCtTypeReference(java.lang.Integer.class);
        parameter.<spoon.reflect.declaration.CtParameter>setType(integerRef);
        parameter.setSimpleName("$id");
        wrapper.addParameter(parameter);
        wrapper.getBody().getElements(new spoon.reflect.visitor.filter.AbstractFilter<spoon.reflect.code.CtInvocation<?>>(spoon.reflect.code.CtInvocation.class) {
            @java.lang.Override
            public boolean matches(spoon.reflect.code.CtInvocation element) {
                return (element.getTarget().toString().equals("view")) && (element.getExecutable().getSimpleName().equals("getId"));
            }
        }).forEach(( e) -> e.replace(getFactory().Code().createVariableRead(parameter.getReference(), false)));
        insertWrapperHint(element);
        (id)++;
    }

    public void rewriteOnItemClick(spoon.reflect.declaration.CtMethod<?> element) {
        spoon.reflect.declaration.CtMethod<java.lang.Void> wrapper = createWrapper(element);
        wrapper.setSimpleName(("$wrapper_" + (id)));
        insertWrapperHint(element);
        (id)++;
    }

    public spoon.reflect.declaration.CtMethod<java.lang.Void> createWrapper(spoon.reflect.declaration.CtMethod<?> element) {
        spoon.reflect.declaration.CtMethod<java.lang.Void> wrapper = getFactory().Core().createMethod();
        element.getParent(spoon.reflect.declaration.CtClass.class).addMethod(wrapper);
        wrapper.setModifiers(element.getModifiers());
        final spoon.reflect.reference.CtTypeReference<java.lang.Void> voidRef = getFactory().Code().createCtTypeReference(void.class);
        wrapper.setType(voidRef);
        wrapper.setParameters(element.getParameters());
        wrapper.setBody(element.getBody().clone());
        return wrapper;
    }

    public void insertWrapperHint(spoon.reflect.declaration.CtMethod<?> element) {
        final spoon.reflect.reference.CtTypeReference<java.lang.String> stringRef = getFactory().Code().createCtTypeReference(java.lang.String.class);
        final spoon.reflect.code.CtLocalVariable<java.lang.String> variable = getFactory().Code().createLocalVariable(stringRef, "$wrapper", getFactory().Code().createLiteral(("$wrapper_" + (id))));
        final spoon.reflect.code.CtCodeSnippetStatement usage = getFactory().Code().createCodeSnippetStatement("int $usage = $wrapper.length()");
        element.getBody().insertBegin(usage);
        element.getBody().insertBegin(variable);
    }
}

