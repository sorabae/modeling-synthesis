package factory.src.impl2;


public class FactoryImpl2 implements factory.src.Factory {
    public factory.src.A createA() {
        return new factory.src.impl2.AImpl2();
    }

    public factory.src.B createB() {
        return new factory.src.impl2.BImpl2();
    }
}

