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

// In order to model the "View" argument of event handlers such as onClick,
// translate the event handler to take another parameter, directly indicating the id of View,
// and to replace system calls of obtaining the id of View with this parameter.
public class EventHandlerRewriterProcessor extends AbstractProcessor<CtMethod<?>> {
	private int id = 0;

	@Override
	public boolean isToBeProcessed(CtMethod<?> element) {
		CtClass parent = null;
		if (element.getParent() instanceof CtClass) {
			parent = (CtClass) element.getParent();
		}
		if (parent == null) return false;
		return isOnClick(element, parent) || isOnItemClick(element, parent) || isOnOptionsItemSelected(element, parent);
	}
	public boolean isOnClick(CtMethod<?> element, CtClass parent) {
		return element.getSimpleName().equals("onClick") && !parent.isAnonymous();
	}
	public boolean isOnItemClick(CtMethod<?> element, CtClass parent) {
		return element.getSimpleName().equals("onItemClick") && parent.isAnonymous();
	}
	public boolean isOnOptionsItemSelected(CtMethod<?> element, CtClass parent) {
		return element.getSimpleName().equals("onOptionsItemSelected") && !parent.isAnonymous();
	}

	@Override
	public void process(CtMethod<?> element) {
		System.out.println("Processing " + element.getSimpleName());
		switch (element.getSimpleName()) {
			case "onClick": rewriteOnClick(element); break;
			case "onItemClick": rewriteOnItemClick(element); break;
			case "onOptionsItemSelected": rewriteOnOptionsItemSelected(element); break;
			default: break;
		}
	}

	public void rewriteOnClick(CtMethod<?> element) {
		// Create a wrapper for an onClick event handler
		CtMethod<Void> wrapper = createWrapper(element);
		wrapper.setSimpleName("$wrapper_" + id);

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

		insertWrapperHint(element);
		id++;
	}

	public void rewriteOnItemClick(CtMethod<?> element) {
		// Create a wrapper for an onItemClick event handler
		CtMethod<Void> wrapper = createWrapper(element);
		wrapper.setSimpleName("$wrapper_" + id);

		insertWrapperHint(element);
		id++;
	}

	public void rewriteOnOptionsItemSelected(CtMethod<?> element) {
		// Create a wrapper for an onOptionsItemSelected event handler
		CtMethod<Boolean> wrapper = createBooleanWrapper(element);
		wrapper.setSimpleName("$wrapper_" + id);

		final CtParameter<Integer> parameter = getFactory().Core().<Integer>createParameter();
		final CtTypeReference<Integer> integerRef = getFactory().Code().createCtTypeReference(Integer.class);
		parameter.<CtParameter>setType(integerRef);
		parameter.setSimpleName("$id");
		wrapper.addParameter(parameter);

		wrapper.getBody().getElements(new AbstractFilter<CtInvocation<?>>(CtInvocation.class) {
			@Override
			public boolean matches(CtInvocation element) {
				return element.getTarget().toString().equals("item") && element.getExecutable().getSimpleName().equals("getItemId");
			}
		}).forEach(e -> e.replace(getFactory().Code().createVariableRead(parameter.getReference(), false)));

		insertWrapperHint(element);
		id++;
	}

	public CtMethod<Void> createWrapper(CtMethod<?> element) {
		CtMethod<Void> wrapper = getFactory().Core().createMethod();
		element.getParent(CtClass.class).addMethod(wrapper);

		wrapper.setModifiers(element.getModifiers());
		final CtTypeReference<Void> voidRef = getFactory().Code().createCtTypeReference(void.class);
		wrapper.setType(voidRef);
		wrapper.setParameters(element.getParameters());
		wrapper.setBody(element.getBody().clone());

		return wrapper;
	}

	public CtMethod<Boolean> createBooleanWrapper(CtMethod<?> element) {
		CtMethod<Boolean> wrapper = getFactory().Core().createMethod();
		element.getParent(CtClass.class).addMethod(wrapper);

		wrapper.setModifiers(element.getModifiers());
		final CtTypeReference<Boolean> boolRef = getFactory().Code().createCtTypeReference(boolean.class);
		wrapper.setType(boolRef);
		wrapper.setParameters(element.getParameters());
		wrapper.setBody(element.getBody().clone());

		return wrapper;
	}

	public void insertWrapperHint(CtMethod<?> element) {
		// Insert an assignment to this class to let Gator know the wrapper
		final CtTypeReference<String> stringRef = getFactory().Code().createCtTypeReference(String.class);
		final CtLocalVariable<String> variable = getFactory().Code().createLocalVariable(stringRef, "$wrapper", getFactory().Code().createLiteral("$wrapper_" + id));

		// Use this created variable so that compilers do not erase the assignment
		final CtCodeSnippetStatement usage = getFactory().Code().createCodeSnippetStatement("int $usage = $wrapper.length()");
		element.getBody().insertBegin(usage);
		element.getBody().insertBegin(variable);
	}
}
