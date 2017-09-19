package fr.inria.gforge.spoon.assertgenerator.test;


class TestListener extends org.junit.runner.notification.RunListener {
    private java.util.List<org.junit.runner.Description> testRun;

    private java.util.List<org.junit.runner.notification.Failure> testFails;

    TestListener() {
        this.testRun = new java.util.ArrayList<>();
        this.testFails = new java.util.ArrayList<>();
    }

    @java.lang.Override
    public synchronized void testFinished(org.junit.runner.Description description) throws java.lang.Exception {
        this.testRun.add(description);
    }

    @java.lang.Override
    public synchronized void testFailure(org.junit.runner.notification.Failure failure) throws java.lang.Exception {
        this.testFails.add(failure);
    }

    @java.lang.Override
    public synchronized void testAssumptionFailure(org.junit.runner.notification.Failure failure) {
    }

    java.util.List<org.junit.runner.notification.Failure> getTestFails() {
        return testFails;
    }
}

