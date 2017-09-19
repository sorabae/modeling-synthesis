package fr.inria.gforge.spoon.analysis;


public class CatchProcessor extends spoon.processing.AbstractProcessor<spoon.reflect.code.CtCatch> {
    public final java.util.List<spoon.reflect.code.CtCatch> emptyCatchs = new java.util.ArrayList<spoon.reflect.code.CtCatch>();

    @java.lang.Override
    public boolean isToBeProcessed(spoon.reflect.code.CtCatch candidate) {
        return (candidate.getBody().getStatements().size()) == 0;
    }

    @java.lang.Override
    public void process(spoon.reflect.code.CtCatch element) {
        getEnvironment().report(this, org.apache.log4j.Level.WARN, element, "empty catch clause");
        emptyCatchs.add(element);
    }
}

