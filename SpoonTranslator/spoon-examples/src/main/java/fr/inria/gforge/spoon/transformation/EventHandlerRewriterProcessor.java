package fr.inria.gforge.spoon.transformation;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtNewClass;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.AbstractFilter;
import spoon.reflect.visitor.filter.TypeFilter;
import android.view.View.OnClickListener;

// In order to model the "View" argument of event handlers such as onClick,
// translate the event handler to take another parameter, directly indicating the id of View,
// and to replace system calls of obtaining the id of View with this parameter.
// TODO: Translatable event handlers should take the "View" parameter as a "view" identifier
// TODO: Current translation changes WTG analysis results. May needs a wrapper. How to let a synthesizer know the wrapper? because changing method names requires to change XML file contents as well.
public class EventHandlerRewriterProcessor extends AbstractProcessor<CtMethod<?>> {
	private int id = 0;

	@Override
	// public boolean isToBeProcessed(CtNewClass<OnClickListener> element) {
	// 	return element.getType().getActualClass() == OnClickListener.class;
	// }
	public boolean isToBeProcessed(CtMethod<?> element) {
		CtClass parent = null;
		if (element.getParent() instanceof CtClass) {
			parent = (CtClass) element.getParent();
		}
		return element.getSimpleName().equals("onClick") && parent != null && !parent.isAnonymous();
	}

	@Override
	// public void process(CtNewClass<OnClickListener> element) {
	// 	final CtTypeReference<OnClickListener> listenerRef = getFactory().Code().createCtTypeReference(OnClickListener.class);
	// 	CtLocalVariable<OnClickListener> variable = getFactory().Code().createLocalVariable(listenerRef, id + "_listener", element.clone());
	//
	// 	if (element.getParent().getParent(CtExecutable.class).getBody() != null) {
	// 		element.getParent().getParent(CtExecutable.class).getBody().insertBegin(variable);
	// 		System.out.println(element);
	// 		element.replace(getFactory().Code().createVariableRead(variable.getReference(), false));
	// 	}
	// 	id++;
	// }
	public void process(CtMethod<?> element) {
		System.out.println("Processing " + element.getSimpleName());

		// Create a wrapper for an onClick event handler
		// CtMethod<?> wrapper = element.clone();
		CtMethod<Void> wrapper = getFactory().Core().createMethod();
		element.getParent(CtClass.class).addMethod(wrapper);

		wrapper.setModifiers(element.getModifiers());
		final CtTypeReference<Void> voidRef = getFactory().Code().createCtTypeReference(void.class);
		wrapper.setType(voidRef);
		wrapper.setParameters(element.getParameters());
		wrapper.setBody(element.getBody().clone());

		final CtParameter<Integer> parameter = getFactory().Core().<Integer>createParameter();
		final CtTypeReference<Integer> integerRef = getFactory().Code().createCtTypeReference(Integer.class);
		parameter.<CtParameter>setType(integerRef);
		parameter.setSimpleName("$id");
		wrapper.addParameter(parameter);

		wrapper.getBody().getElements(new AbstractFilter<CtInvocation<?>>(CtInvocation.class) {
			@Override
			public boolean matches(CtInvocation element) {
				return element.getTarget().toString().equals("view") && element.getExecutable().getSimpleName().equals("getId");
			}
		}).forEach(e -> e.replace(getFactory().Code().createVariableRead(parameter.getReference(), false)));

		wrapper.setSimpleName("$wrapper_" + id);

		// Insert an assignment to this class to let Gator know the wrapper
		final CtTypeReference<String> stringRef = getFactory().Code().createCtTypeReference(String.class);
		final CtLocalVariable<String> variable = getFactory().Code().createLocalVariable(stringRef, "$wrapper", getFactory().Code().createLiteral("$wrapper_" + id));

		// Use this created variable so that compilers do not erase the assignment
		final CtCodeSnippetStatement usage = getFactory().Code().createCodeSnippetStatement("int $usage = $wrapper.length()");
		element.getBody().insertBegin(usage);
		element.getBody().insertBegin(variable);

		id++;
	}
}
