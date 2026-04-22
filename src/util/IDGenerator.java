package util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility class for generating unique IDs.
 * Uses timestamp + counter to ensure uniqueness.
 */
public class IDGenerator {

    private static final AtomicLong counter = new AtomicLong(0);

    public static String generateUserId() {
        return "USR" + System.currentTimeMillis() + counter.incrementAndGet();
    }

    public static String generateItemId() {
        return "ITM" + System.currentTimeMillis() + counter.incrementAndGet();
    }

    public static String generateClaimId() {
        return "CLM" + System.currentTimeMillis() + counter.incrementAndGet();
    }
}
