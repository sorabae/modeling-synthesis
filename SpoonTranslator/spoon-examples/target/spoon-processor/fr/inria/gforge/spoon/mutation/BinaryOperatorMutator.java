package fr.inria.gforge.spoon.mutation;


public class BinaryOperatorMutator extends spoon.processing.AbstractProcessor<spoon.reflect.declaration.CtElement> {
    @java.lang.Override
    public boolean isToBeProcessed(spoon.reflect.declaration.CtElement candidate) {
        return candidate instanceof spoon.reflect.code.CtBinaryOperator;
    }

    @java.lang.Override
    public void process(spoon.reflect.declaration.CtElement candidate) {
        if (!(candidate instanceof spoon.reflect.code.CtBinaryOperator)) {
            return ;
        }
        spoon.reflect.code.CtBinaryOperator op = ((spoon.reflect.code.CtBinaryOperator) (candidate));
        op.setKind(spoon.reflect.code.BinaryOperatorKind.MINUS);
    }
}

