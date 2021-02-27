package com.makotora.cardcostapi.ratelimit;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.RateLimiter;

/** A class that wraps the Guava RateLimiter in order to allow
 * for rate limiting that is not necessarily per second.
 */
public class PeriodRateLimiter
{
    private final long numDosesPerPeriod;
    private final RateLimiter rateLimiter;
    private long numDosesAvailable;

    public PeriodRateLimiter(long numDosesPerPeriod, Duration period)
    {
        this.numDosesPerPeriod = numDosesPerPeriod;
        double numSeconds = period.getSeconds() + period.getNano() / 1000000000d;
        rateLimiter = RateLimiter.create(1 / numSeconds);
        numDosesAvailable = 0L;
    }

    public synchronized void consume()
    {
        if (numDosesAvailable == 0) {
            rateLimiter.acquire();
            numDosesAvailable += numDosesPerPeriod;
        }
        numDosesAvailable--;
    }

    public synchronized boolean tryConsume(long timeout, TimeUnit unit)
    {
        if (numDosesAvailable == 0) {
            boolean acquired = rateLimiter.tryAcquire(timeout, unit);
            if (!acquired) {
                return false;
            }

            numDosesAvailable += numDosesPerPeriod;
        }
        numDosesAvailable--;

        return true;
    }
}
