package src;


public class Main {
    public static void main(java.lang.String[] args) {
        if (args == null) throw new IllegalArgumentException("[Spoon inserted check] null passed as parameter");;
        try {
            @java.lang.SuppressWarnings(value = "unused")
            java.util.Vector<?> v = null;
            src.Main.m1();
        } catch (java.lang.Exception ignored) {
        }
    }

    public static void m1() throws java.lang.Exception {
        src.Main.m2();
    }

    public static void m2() throws java.lang.Exception {
        throw new java.lang.RuntimeException();
    }

    public void m(src.p3.C c) throws java.lang.Exception {
        if (c == null) throw new IllegalArgumentException("[Spoon inserted check] null passed as parameter");;
    }
}

