package fr.inria.gforge.spoon.analysis;


public class FactoryProcessor extends spoon.processing.AbstractProcessor<spoon.reflect.code.CtConstructorCall<?>> {
    public java.util.List<spoon.reflect.code.CtConstructorCall> listWrongUses = new java.util.ArrayList<spoon.reflect.code.CtConstructorCall>();

    private spoon.reflect.reference.CtTypeReference factoryTypeRef;

    public FactoryProcessor(spoon.reflect.reference.CtTypeReference factoryTypeRef) {
        this.factoryTypeRef = factoryTypeRef;
    }

    public void process(spoon.reflect.code.CtConstructorCall<?> newClass) {
        if (newClass.getExecutable().getDeclaringType().isSubtypeOf(getFactoryType()))
            return ;
        
        if (((spoon.reflect.declaration.CtClass<?>) (newClass.getParent(spoon.reflect.declaration.CtClass.class))).isSubtypeOf(getFactoryType()))
            return ;
        
        for (spoon.reflect.reference.CtTypeReference<?> t : getCreatedTypes()) {
            if (newClass.getType().isSubtypeOf(t)) {
                this.listWrongUses.add(newClass);
            }
        }
    }

    protected spoon.reflect.reference.CtTypeReference<?> getFactoryType() {
        return this.factoryTypeRef;
    }

    java.util.List<spoon.reflect.reference.CtTypeReference<?>> createdTypes;

    private java.util.List<spoon.reflect.reference.CtTypeReference<?>> getCreatedTypes() {
        if ((createdTypes) == null) {
            createdTypes = new java.util.ArrayList<spoon.reflect.reference.CtTypeReference<?>>();
            for (spoon.reflect.reference.CtExecutableReference<?> m : getFactoryType().getDeclaredExecutables()) {
                createdTypes.add(m.getType());
            }
        }
        return createdTypes;
    }
}

