package factory.src;


public class Main {
    public static void main(java.lang.String[] args) {
        factory.src.Main.execute(new factory.src.impl1.FactoryImpl1());
        factory.src.Main.execute(new factory.src.impl2.FactoryImpl2());
    }

    public static void execute(factory.src.Factory f) {
        java.lang.System.out.println(("======= running program with factory " + f));
        factory.src.A a = f.createA();
        a.m1();
        factory.src.B b = f.createB();
        b.m2();
        new factory.src.impl1.BImpl1().m2();
        new factory.src.impl1.AImpl1().m1();
        new java.lang.Object();
    }
}

