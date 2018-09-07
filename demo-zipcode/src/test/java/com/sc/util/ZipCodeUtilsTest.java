package com.sc.util;

import com.sc.model.ZipCodeRange;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;

import static com.sc.util.ZipCodeUtils.isExcluded;
import static com.sc.util.ZipCodeUtils.isInRange;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static com.sc.util.ZipCodeUtils.consolidate;

public class ZipCodeUtilsTest {
    // --------------------------------------------------
    // Reusable
    // --------------------------------------------------

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    // --------------------------------------------------
    // Exceptions - from a String
    // --------------------------------------------------

    @Test
    public void consolidateRangeFromNull() {
        List<ZipCodeRange> list = consolidate(null);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void consolidateRangeFromOne() {
        ZipCodeRange zcr1 = new ZipCodeRange("10100,10150");
        List<ZipCodeRange> list = consolidate(Arrays.asList(zcr1));
        assertNotNull(list);
        assertEquals(1, list.size());
        assertArrayEquals(new int[] {10100, 10150}, list.get(0).getRangeArray());
    }

    @Test
    public void consolidateRangeNoOverlap() {
        ZipCodeRange zcr1 = new ZipCodeRange("10100,10150");
        ZipCodeRange zcr2 = new ZipCodeRange("20100,20150");
        List<ZipCodeRange> list = consolidate(Arrays.asList(zcr1, zcr2));
        assertNotNull(list);
        assertEquals(2, list.size());
        assertArrayEquals(new int[] {10100, 10150}, list.get(0).getRangeArray());
        assertArrayEquals(new int[] {20100, 20150}, list.get(1).getRangeArray());
    }

    @Test
    public void consolidateRangeNoOverlapCorrectOrder() {
        ZipCodeRange zcr1 = new ZipCodeRange("10100,10150");
        ZipCodeRange zcr2 = new ZipCodeRange("20100,20150");
        List<ZipCodeRange> list = consolidate(Arrays.asList(zcr2, zcr1));
        assertNotNull(list);
        assertEquals(2, list.size());
        assertArrayEquals(new int[] {10100, 10150}, list.get(0).getRangeArray());
        assertArrayEquals(new int[] {20100, 20150}, list.get(1).getRangeArray());
    }

    @Test
    public void consolidateRangeAdjacentStart() {
        ZipCodeRange zcr1 = new ZipCodeRange("10100,10150");
        ZipCodeRange zcr2 = new ZipCodeRange("10001,10099");
        List<ZipCodeRange> list = consolidate(Arrays.asList(zcr1, zcr2));
        assertNotNull(list);
        assertEquals(1, list.size());
        assertArrayEquals(new int[] {10001, 10150}, list.get(0).getRangeArray());
    }

    @Test
    public void consolidateRangeOverlapStart() {
        ZipCodeRange zcr1 = new ZipCodeRange("10100,10150");
        ZipCodeRange zcr2 = new ZipCodeRange("10001,10101");
        List<ZipCodeRange> list = consolidate(Arrays.asList(zcr1, zcr2));
        assertNotNull(list);
        assertEquals(1, list.size());
        assertArrayEquals(new int[] {10001, 10150}, list.get(0).getRangeArray());
    }

    @Test
    public void consolidateRangeOverlapStartAndEnd() {
        ZipCodeRange zcr1 = new ZipCodeRange("10100,10150");
        ZipCodeRange zcr2 = new ZipCodeRange("10001,20101");
        List<ZipCodeRange> list = consolidate(Arrays.asList(zcr1, zcr2));
        assertNotNull(list);
        assertEquals(1, list.size());
        assertArrayEquals(new int[] {10001, 20101}, list.get(0).getRangeArray());
    }

    // --------------------------------------------------
    // Typical usage - isExcluded(String)
    // --------------------------------------------------

    @Test
    public void checkIfNullExcludedRange1() {
        ZipCodeRange zcr1 = new ZipCodeRange("10000, 19999");
        ZipCodeRange zcr2 = new ZipCodeRange("20001, 29999");
        assertFalse(isExcluded(null, Arrays.asList(zcr1, zcr2)));
    }

    @Test
    public void checkIfStringExcludedNullRange() {
        assertFalse(isExcluded("11111", null));
    }

    @Test
    public void checkIfStringExcludedRange1() {
        ZipCodeRange zcr1 = new ZipCodeRange("10000, 19999");
        ZipCodeRange zcr2 = new ZipCodeRange("20001, 29999");
        assertTrue(isExcluded("11111", Arrays.asList(zcr1, zcr2)));
    }

    @Test
    public void checkIfStringExcludedRange2() {
        ZipCodeRange zcr1 = new ZipCodeRange("10000, 19999");
        ZipCodeRange zcr2 = new ZipCodeRange("20001, 29999");
        assertTrue(isExcluded("22222", Arrays.asList(zcr1, zcr2)));
    }

    @Test
    public void checkIfStringNotExcluded() {
        ZipCodeRange zcr1 = new ZipCodeRange("10000, 19999");
        ZipCodeRange zcr2 = new ZipCodeRange("20001, 29999");
        assertFalse(isExcluded("20000", Arrays.asList(zcr1, zcr2)));
    }

    @Test
    public void checkIfBadStringExcluded() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(containsString("Invalid ZIP code: 123456"));
        ZipCodeRange zcr1 = new ZipCodeRange("10000, 19999");
        ZipCodeRange zcr2 = new ZipCodeRange("20001, 29999");
        assertFalse(isExcluded("123456", Arrays.asList(zcr1, zcr2)));
    }

    // --------------------------------------------------
    // Typical usage - isExcluded(int)
    // --------------------------------------------------

    @Test
    public void checkIfIntExcludedNullRange() {
        assertFalse(isExcluded(11111, null));
    }

    @Test
    public void checkIfIntExcludedRange1() {
        ZipCodeRange zcr1 = new ZipCodeRange("10000, 19999");
        ZipCodeRange zcr2 = new ZipCodeRange("20001, 29999");
        assertTrue(isExcluded(11111, Arrays.asList(zcr1, zcr2)));
    }

    @Test
    public void checkIfIntExcludedRange2() {
        ZipCodeRange zcr1 = new ZipCodeRange("10000, 19999");
        ZipCodeRange zcr2 = new ZipCodeRange("20001, 29999");
        assertTrue(isExcluded(22222, Arrays.asList(zcr1, zcr2)));
    }

    @Test
    public void checkIfIntNotExcluded() {
        ZipCodeRange zcr1 = new ZipCodeRange("10000, 19999");
        ZipCodeRange zcr2 = new ZipCodeRange("20001, 29999");
        assertFalse(isExcluded(20000, Arrays.asList(zcr1, zcr2)));
    }

    @Test
    public void checkIfNegativeIntExcluded() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(containsString("Invalid ZIP code: -12345"));
        ZipCodeRange zcr1 = new ZipCodeRange("10000, 19999");
        ZipCodeRange zcr2 = new ZipCodeRange("20001, 29999");
        assertFalse(isExcluded(-12345, Arrays.asList(zcr1, zcr2)));
    }

    @Test
    public void checkIfBadIntExcluded() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(containsString("Invalid ZIP code: 123456"));
        ZipCodeRange zcr1 = new ZipCodeRange("10000, 19999");
        ZipCodeRange zcr2 = new ZipCodeRange("20001, 29999");
        assertFalse(isExcluded(123456, Arrays.asList(zcr1, zcr2)));
    }

    // --------------------------------------------------
    // Typical usage - isInRange()
    // --------------------------------------------------

    @Test
    public void checkInRangeWithNull() {
        assertFalse(isInRange(10001, null));
    }

    @Test
    public void checkInRangeLower() {
        assertFalse(isInRange(10099, new ZipCodeRange("10100,10150")));
    }

    @Test
    public void checkInRangeEqualsStart() {
        assertTrue(isInRange(10100, new ZipCodeRange("10100,10150")));
    }

    @Test
    public void checkInRangeInBetween() {
        assertTrue(isInRange(10125, new ZipCodeRange("10100,10150")));
    }

    @Test
    public void checkInRangeEqualsEnd() {
        assertTrue(isInRange(10150, new ZipCodeRange("10100,10150")));
    }

    @Test
    public void checkInRangeHigher() {
        assertFalse(isInRange(10151, new ZipCodeRange("10100,10150")));
    }
}
