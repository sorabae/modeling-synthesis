package fr.inria.gforge.spoon.mutation;


public class MutationTester<T> {
    private java.lang.String sourceCodeToBeMutated;

    private fr.inria.gforge.spoon.mutation.TestDriver testDriver;

    private spoon.processing.Processor mutator;

    private final java.util.List<spoon.reflect.declaration.CtClass> mutants = new java.util.ArrayList<spoon.reflect.declaration.CtClass>();

    public final java.util.List<T> mutantInstances = new java.util.ArrayList<T>();

    public MutationTester(java.lang.String src, fr.inria.gforge.spoon.mutation.TestDriver tester, spoon.processing.Processor mutator) {
        this.sourceCodeToBeMutated = src;
        this.testDriver = tester;
        this.mutator = mutator;
    }

    public void generateMutants() {
        spoon.Launcher l = new spoon.Launcher();
        l.addInputResource(sourceCodeToBeMutated);
        l.buildModel();
        spoon.reflect.declaration.CtClass origClass = ((spoon.reflect.declaration.CtClass) (l.getFactory().Package().getRootPackage().getElements(new spoon.reflect.visitor.filter.TypeFilter(spoon.reflect.declaration.CtClass.class)).get(0)));
        java.util.List<spoon.reflect.declaration.CtElement> elementsToBeMutated = origClass.getElements(new spoon.reflect.visitor.Filter<spoon.reflect.declaration.CtElement>() {
            @java.lang.Override
            public boolean matches(spoon.reflect.declaration.CtElement arg0) {
                return mutator.isToBeProcessed(arg0);
            }
        });
        for (spoon.reflect.declaration.CtElement e : elementsToBeMutated) {
            spoon.reflect.declaration.CtElement op = l.getFactory().Core().clone(e);
            mutator.process(op);
            replace(e, op);
            spoon.reflect.declaration.CtClass klass = l.getFactory().Core().clone(op.getParent(spoon.reflect.declaration.CtClass.class));
            klass.setParent(origClass.getParent());
            mutants.add(klass);
            replace(op, e);
        }
    }

    public java.util.List<spoon.reflect.declaration.CtClass> getMutants() {
        return java.util.Collections.unmodifiableList(mutants);
    }

    private void replace(spoon.reflect.declaration.CtElement e, spoon.reflect.declaration.CtElement op) {
        if ((e instanceof spoon.reflect.code.CtStatement) && (op instanceof spoon.reflect.code.CtStatement)) {
            ((spoon.reflect.code.CtStatement) (e)).replace(((spoon.reflect.code.CtStatement) (op)));
            return ;
        }
        if ((e instanceof spoon.reflect.code.CtExpression) && (op instanceof spoon.reflect.code.CtExpression)) {
            ((spoon.reflect.code.CtExpression) (e)).replace(((spoon.reflect.code.CtExpression) (op)));
            return ;
        }
        throw new java.lang.IllegalArgumentException((((e.getClass()) + " ") + (op.getClass())));
    }

    public void killMutants() throws java.lang.Exception {
        java.util.List<java.lang.Class> compiledMutants = compileMutants(mutants);
        java.util.List<T> mutantInstances = instantiateMutants(compiledMutants);
        runTestsOnEachMutantInstance(mutantInstances);
    }

    public void runTestsOnEachMutantInstance(java.util.List<T> mutantInstances) throws java.lang.Exception {
        for (T t : mutantInstances) {
            try {
                testDriver.test(t);
                throw new fr.inria.gforge.spoon.mutation.MutantNotKilledException();
            } catch (java.lang.AssertionError expected) {
                java.lang.System.out.println("mutant killed!");
            }
        }
    }

    public java.util.List<T> instantiateMutants(java.util.List<java.lang.Class> compiledMutants) throws java.lang.Exception {
        for (java.lang.Class mutantClass : compiledMutants) {
            mutantInstances.add(((T) (mutantClass.newInstance())));
        }
        return mutantInstances;
    }

    public java.util.List<java.lang.Class> compileMutants(java.util.List<spoon.reflect.declaration.CtClass> mutants) throws java.lang.Exception {
        java.util.List<java.lang.Class> compiledMutants = new java.util.ArrayList<java.lang.Class>();
        for (spoon.reflect.declaration.CtClass mutantClass : mutants) {
            java.lang.Class<?> klass = org.mdkt.compiler.InMemoryJavaCompiler.compile(mutantClass.getQualifiedName(), ((("package " + (mutantClass.getPackage().getQualifiedName())) + ";") + mutantClass));
            compiledMutants.add(klass);
        }
        return compiledMutants;
    }
}

