package fr.inria.gforge.spoon.transformation;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtComment;
import spoon.reflect.code.CtComment.CommentType;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;

import java.util.Set;

import android.app.Dialog;
// In order to synthesize "Dialog", capture dialog creation.
public class LifecycleRewriterProcessor extends AbstractProcessor<CtConstructorCall<?>> {
	private int id = 0;

	@Override
	public boolean isToBeProcessed(CtConstructorCall<?> element) {
		CtTypeReference<?> cls = element.getExecutable().getDeclaringType();

		if (cls.getPackage() != null || cls.getSuperclass() == null) return false;
		// if it is an inner class, has a super class of Dialog
		return cls.getSuperclass().getQualifiedName().equals("android.app.Dialog");
	}

	// TODO: what if several Dialog instances are created?
	// TODO: this... (make the assignment visible globally)
	// TODO: field... type reference makes me frustrated!!!!!
	@Override
	public void process(CtConstructorCall<?> element) {
		// CtClass<?> innerClass = getFactory().Class().get(element.getExecutable().getDeclaringType().getQualifiedName());
		CtClass<?> clazz = element.getExecutable().getParent(CtClass.class);
		String cname = element.getExecutable().getDeclaringType().getSimpleName();

		if (clazz.getField("$instance_" + cname) != null) return;

		CtField<Dialog> field = getFactory().Core().createField();
		clazz.addField(field);

		final CtTypeReference<Dialog> dialogRef = getFactory().Code().createCtTypeReference(Dialog.class);
		field.setType(dialogRef);
		field.setSimpleName("$instance_" + cname);
		// field.addModifier(ModifierKind.PUBLIC);
		field.addModifier(ModifierKind.STATIC);

		// if this call is generated from spoon, pass it.
		// if (element.getExecutable().getComments().stream().filter(comment -> comment.getContent().equals("Spooned constructor")).count() != 0)
		// 	return;
		//
		// innerClass.getConstructors().forEach(c -> System.out.println(c.getComments()));
		// if (innerClass.getConstructors().stream().filter(constructor -> constructor.getParameters().isEmpty()).count() != 0) {
		// 	System.out.println("what happend?");
		// 	return;
		// }
		//
		// final CtConstructor constructor = getFactory().Core().createConstructor();
		// final CtConstructorCall<?> capture = getFactory().Code().createConstructorCall(element.getExecutable().getDeclaringType(), element.getArguments().toArray(new CtExpression<?>[element.getArguments().size()]));
		// capture.addComment(getFactory().Code().createComment("Spooned constructor", CtComment.CommentType.INLINE));
		// // constructor.setBody(element.clone());
		// constructor.setBody(capture);
		// constructor.addModifier(ModifierKind.PUBLIC);
		// System.out.println(constructor.getComments());


		CtMethod<Dialog> setter = getFactory().Core().createMethod();
		clazz.addMethod(setter);

		// final CtTypeReference<Void> voidRef = getFactory().Code().createCtTypeReference(void.class);
		setter.setType(dialogRef);
		setter.setSimpleName("$setInstance_" + cname);
		setter.addModifier(ModifierKind.STATIC);

		final CtParameter<Dialog> parameter = getFactory().Core().<Dialog>createParameter();
		parameter.<CtParameter>setType(dialogRef);
		parameter.setSimpleName("instance");
		setter.addParameter(parameter);

		final CtCodeSnippetStatement setting = getFactory().Code().createCodeSnippetStatement("$instance_" + cname + " = instance");
		setter.setBody(getFactory().Code().createCtBlock(setting));
		setting.insertAfter(getFactory().Code().createCodeSnippetStatement("return instance"));

		element.replace(getFactory().Code().createInvocation(getFactory().Code().createCodeSnippetExpression(clazz.getQualifiedName()), setter.getReference(), element.clone()));
	}

	// public void rewriteOnClick(CtMethod<?> element) {
	// 	// Create a wrapper for an onClick event handler
	// 	CtMethod<Void> wrapper = createWrapper(element);
	// 	wrapper.setSimpleName("$wrapper_" + id);
	//
	// 	final CtParameter<Integer> parameter = getFactory().Core().<Integer>createParameter();
	// 	final CtTypeReference<Integer> integerRef = getFactory().Code().createCtTypeReference(Integer.class);
	// 	parameter.<CtParameter>setType(integerRef);
	// 	parameter.setSimpleName("$id");
	// 	wrapper.addParameter(parameter);
	//
	// 	wrapper.getBody().getElements(new AbstractFilter<CtInvocation<?>>(CtInvocation.class) {
	// 		@Override
	// 		public boolean matches(CtInvocation element) {
	// 			return element.getTarget().toString().equals("view") && element.getExecutable().getSimpleName().equals("getId");
	// 		}
	// 	}).forEach(e -> e.replace(getFactory().Code().createVariableRead(parameter.getReference(), false)));
	//
	// 	insertWrapperHint(element);
	// 	id++;
	// }
	//
	// public void rewriteOnItemClick(CtMethod<?> element) {
	// 	// Create a wrapper for an onItemClick event handler
	// 	CtMethod<Void> wrapper = createWrapper(element);
	// 	wrapper.setSimpleName("$wrapper_" + id);
	//
	// 	insertWrapperHint(element);
	// 	id++;
	// }
	//
	// public CtMethod<Void> createWrapper(CtMethod<?> element) {
	// 	CtMethod<Void> wrapper = getFactory().Core().createMethod();
	// 	element.getParent(CtClass.class).addMethod(wrapper);
	//
	// 	wrapper.setModifiers(element.getModifiers());
	// 	final CtTypeReference<Void> voidRef = getFactory().Code().createCtTypeReference(void.class);
	// 	wrapper.setType(voidRef);
	// 	wrapper.setParameters(element.getParameters());
	// 	wrapper.setBody(element.getBody().clone());
	//
	// 	return wrapper;
	// }
	//
	// public void insertWrapperHint(CtMethod<?> element) {
	// 	// Insert an assignment to this class to let Gator know the wrapper
	// 	final CtTypeReference<String> stringRef = getFactory().Code().createCtTypeReference(String.class);
	// 	final CtLocalVariable<String> variable = getFactory().Code().createLocalVariable(stringRef, "$wrapper", getFactory().Code().createLiteral("$wrapper_" + id));
	//
	// 	// Use this created variable so that compilers do not erase the assignment
	// 	final CtCodeSnippetStatement usage = getFactory().Code().createCodeSnippetStatement("int $usage = $wrapper.length()");
	// 	element.getBody().insertBegin(usage);
	// 	element.getBody().insertBegin(variable);
	// }
}
