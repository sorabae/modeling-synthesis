package fr.inria.gforge.spoon.bound.template;


public class BoundTemplate extends spoon.template.AbstractTemplate<spoon.reflect.code.CtStatement> {
    @spoon.template.Parameter
    public spoon.reflect.reference.CtTypeReference<java.lang.Double> typeReference;

    @spoon.template.Parameter
    public java.lang.String _parameter_;

    @spoon.template.Parameter
    public double _minBound_;

    @spoon.template.Parameter
    public double _maxBound_;

    @spoon.template.Local
    public BoundTemplate(spoon.reflect.reference.CtTypeReference<java.lang.Double> typeReference, java.lang.String parameterName, double minBound, double maxBound) {
        this.typeReference = typeReference;
        _parameter_ = parameterName;
        _maxBound_ = maxBound;
        _minBound_ = minBound;
    }

    public void test(java.lang.Double _parameter_) throws java.lang.Throwable {
        if (_parameter_ > (_maxBound_)) {
            throw new java.lang.RuntimeException(((("out of max bound (" + _parameter_) + ">") + (_maxBound_)));
        }
        if (_parameter_ < (_minBound_)) {
            throw new java.lang.RuntimeException(((("out of min bound (" + _parameter_) + "<") + (_minBound_)));
        }
    }

    @java.lang.Override
    public spoon.reflect.code.CtStatement apply(spoon.reflect.declaration.CtType<?> targetType) {
        return spoon.template.Substitution.substituteMethodBody(((spoon.reflect.declaration.CtClass) (targetType)), this, "test", typeReference);
    }
}

