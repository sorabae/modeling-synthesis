package factory.src.impl1;


public class FactoryImpl1 implements factory.src.Factory {
    public factory.src.A createA() {
        return new factory.src.impl1.AImpl1();
    }

    public factory.src.B createB() {
        return new factory.src.impl1.BImpl1();
    }
}

