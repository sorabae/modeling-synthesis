package fr.inria.gforge.spoon.transformation.retry.template;


public class RetryTemplate extends spoon.template.BlockTemplate {
    public RetryTemplate(spoon.reflect.code.CtBlock _original_, int _attempts_, long _delay_, boolean _verbose_) {
        this._original_ = _original_;
        this._attempts_ = _attempts_;
        this._delay_ = _delay_;
        this._verbose_ = _verbose_;
    }

    @spoon.template.Parameter
    spoon.reflect.code.CtBlock _original_;

    @spoon.template.Parameter
    int _attempts_;

    @spoon.template.Parameter
    long _delay_;

    @spoon.template.Parameter
    boolean _verbose_;

    @java.lang.Override
    public void block() {
        int attempt = 0;
        java.lang.Throwable lastTh = null;
        while ((attempt++) < (_attempts_)) {
            try {
                _original_.S();
            } catch (java.lang.Throwable ex) {
                lastTh = ex;
                if (_verbose_) {
                    ex.printStackTrace();
                }
                try {
                    java.lang.Thread.sleep(_delay_);
                } catch (java.lang.InterruptedException ex2) {
                }
            }
        } 
        if (lastTh != null) {
            throw new java.lang.RuntimeException(lastTh);
        }
    }
}

