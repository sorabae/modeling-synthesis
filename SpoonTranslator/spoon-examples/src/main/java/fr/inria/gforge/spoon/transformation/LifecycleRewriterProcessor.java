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
	@Override
	public void process(CtConstructorCall<?> element) {
		// CtClass<?> innerClass = getFactory().Class().get(element.getExecutable().getDeclaringType().getQualifiedName());
		CtClass<?> clazz = element.getExecutable().getParent(CtClass.class);
		String cname = element.getExecutable().getDeclaringType().getSimpleName();

		if (clazz.getField("$instance_" + cname) != null) return;

		// field to capture
		CtField<Dialog> field = getFactory().Core().createField();
		clazz.addField(field);

		final CtTypeReference<Dialog> dialogRef = getFactory().Code().createCtTypeReference(Dialog.class);
		field.setType(dialogRef);
		field.setSimpleName("$instance_" + cname);
		field.addModifier(ModifierKind.STATIC);

		// capture Dialog instances using this method, where instances are stored in the field
		CtMethod<Dialog> setter = getFactory().Core().createMethod();
		clazz.addMethod(setter);

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
}
