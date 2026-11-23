package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StatsBarTest {
    @Test
    public void percentRoundsAndClamps() {
        assertEquals(75, StatsBar.percent(1530, 2048));
        assertEquals(0, StatsBar.percent(10, 0));
        assertEquals(100, StatsBar.percent(3000, 2048));
        assertEquals(0, StatsBar.percent(-5, 2048));
    }
}
