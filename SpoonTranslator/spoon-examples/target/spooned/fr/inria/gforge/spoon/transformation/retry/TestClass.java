package fr.inria.gforge.spoon.transformation.retry;


public class TestClass {
    private java.util.Collection<java.lang.Long> result = new java.util.ArrayList<java.lang.Long>();

    public void retry() {
        int attempt = 0;
        java.lang.Throwable lastTh = null;
        while ((attempt++) < 3) {
            try {
                {
                    result.add(java.lang.System.currentTimeMillis());
                    java.lang.String nullObject = null;
                    nullObject.toLowerCase();
                }
            } catch (java.lang.Throwable ex) {
                lastTh = ex;
                if (false) {
                    ex.printStackTrace();
                }
                try {
                    java.lang.Thread.sleep(10L);
                } catch (java.lang.InterruptedException ex2) {
                }
            }
        } 
        if (lastTh != null) {
            throw new java.lang.RuntimeException(lastTh);
        }
    }
}

