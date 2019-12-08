package ru.ifmo.rain.khusainov.mock_testing.rule;

import org.junit.Assume;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

public class HostReachableRule implements TestRule {
    private static final int TIMEOUT = 1_000;

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    public @interface HostReachable {
        String value();
    }

    private static boolean checkHost(String host) {
        return nativePing(host) || nativePing6(host);
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        HostReachable hostReachable
                = description.getAnnotation(HostReachable.class);

        if (hostReachable == null) {
            return statement;
        } else if (!checkHost(hostReachable.value())) {
            return new SkipStatement(hostReachable.value());
        }

        return statement;
    }

    private static class SkipStatement extends Statement {
        private final String host;

        public SkipStatement(String host) {
            this.host = host;
        }

        @Override
        public void evaluate() throws Throwable {
            Assume.assumeTrue("Skipped, because following host " +
                    "is not available at the moment: " + host, false);
        }
    }

    private static boolean nativePing(String host) {
        return nativePingImpl("", host);
    }

    private static boolean nativePing6(String host) {
        return nativePingImpl("-6", host);
    }

    private static boolean nativePingImpl(String params, String host) {
        try {
            Process pingProcess = new ProcessBuilder("ping", params, "-n", "1", host).start();
            if (!pingProcess.waitFor(TIMEOUT, TimeUnit.MILLISECONDS)) {
                return false;
            }
            return pingProcess.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

}