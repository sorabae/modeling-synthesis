package fr.inria.gforge.spoon.bound.src;


public class Main {
    public static void main(java.lang.String[] args) {
        for (int i = 0; i < 10; i++) {
            try {
                new fr.inria.gforge.spoon.bound.src.Main().m(i);
            } catch (java.lang.RuntimeException e) {
                java.lang.System.out.println(e.getMessage());
            }
        }
    }

    public void m(int a) {
        if (a > 8.0)
            throw new RuntimeException("out of max bound (" + a + " > 8.0");
        
        if (a < 2.0)
            throw new RuntimeException("out of min bound (" + a + " < 2.0");
        
        java.lang.System.out.println("Great method!");
    }
}

