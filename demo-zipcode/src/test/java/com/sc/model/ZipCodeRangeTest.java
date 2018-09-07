package com.sc.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.hamcrest.number.OrderingComparison.lessThan;

public class ZipCodeRangeTest {
    // --------------------------------------------------
    // Reusable
    // --------------------------------------------------

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    // --------------------------------------------------
    // Exceptions - ZipCodeRange(String)
    // --------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void createZipCodeRangeFromStringWithNull() {
        new ZipCodeRange((String)null);
    }

    @Test
    public void createZipCodeRangeFromStringEmpty() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(containsString("Invalid ZIP code range: "));
        new ZipCodeRange("");
    }

    @Test
    public void createZipCodeRangeFromStringWithOneNumber() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(containsString("Invalid ZIP code range: 10000"));
        new ZipCodeRange("10000");
    }

    @Test
    public void createZipCodeRangeFromStringWithThreeNumbers() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(containsString("Invalid ZIP code range: 10000, 20000, 30000"));
        new ZipCodeRange("10000, 20000, 30000");
    }

    @Test
    public void createZipCodeRangeFromStringWithBadFormat1() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(containsString("Invalid ZIP code range: 10000 10000"));
        new ZipCodeRange("10000 10000");
    }

    @Test
    public void createZipCodeRangeFromStringWithBadFormat2() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(containsString("Invalid ZIP code range: 10000 - 10000"));
        new ZipCodeRange("10000 - 10000");
    }

    @Test
    public void createZipCodeRangeFromStringWithNegativeStart() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(containsString("Invalid ZIP code range: -10000, 22222"));
        new ZipCodeRange("-10000, 22222");
    }

    @Test
    public void createZipCodeRangeFromStringWithNegativeEnd() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(containsString("Invalid ZIP code range: 10000, -22222"));
        new ZipCodeRange("10000, -22222");
    }

    @Test
    public void createZipCodeRangeFromStringWithStartTooLarge() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(containsString("Invalid ZIP code range: 123456, 10000"));
        new ZipCodeRange("123456, 10000");
    }

    @Test
    public void createZipCodeRangeFromStringWithEndTooLarge() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(containsString("Invalid ZIP code range: 10000, 567890"));
        new ZipCodeRange("10000, 567890");
    }

    // --------------------------------------------------
    // Typical usage - ZipCodeRange(String)
    // --------------------------------------------------

    @Test
    public void createZipCodeRangeFromStringNoBrackets() {
        ZipCodeRange zcr = new ZipCodeRange("10001,22222");
        assertEquals("[10001,22222]", zcr.getRangeStr());
    }

    @Test
    public void createZipCodeRangeFromStringNoBracketsAndWhitespace() {
        ZipCodeRange zcr = new ZipCodeRange("\t 10002\t ,\t 22222\t ");
        assertEquals("[10002,22222]", zcr.getRangeStr());
    }

    @Test
    public void createZipCodeRangeFromStringWithOpeningBracket() {
        ZipCodeRange zcr = new ZipCodeRange("[10003,22222");
        assertEquals("[10003,22222]", zcr.getRangeStr());
    }

    @Test
    public void createZipCodeRangeFromStringWithClosingBracket() {
        ZipCodeRange zcr = new ZipCodeRange("10004,22222]");
        assertEquals("[10004,22222]", zcr.getRangeStr());
    }

    @Test
    public void createZipCodeRangeFromStringWithBothBrackets() {
        ZipCodeRange zcr = new ZipCodeRange("[10005,22222]");
        assertEquals("[10005,22222]", zcr.getRangeStr());
    }

    @Test
    public void createZipCodeRangeFromStringWithBothBracketsAndWhitespace() {
        ZipCodeRange zcr = new ZipCodeRange("[ \t10006 \t, \t22222 \t]");
        assertEquals("[10006,22222]", zcr.getRangeStr());
    }

    @Test
    public void createZipCodeRangeFromStringSingleValue() {
        ZipCodeRange zcr = new ZipCodeRange("10007,10007]");
        assertEquals("[10007,10007]", zcr.getRangeStr());
    }

    @Test
    public void createZipCodeRangeFromStringFullRange() {
        ZipCodeRange zcr = new ZipCodeRange("0,99999]");
        assertEquals("[00000,99999]", zcr.getRangeStr());
    }

    @Test
    public void createZipCodeRangeFromStringReversed() {
        ZipCodeRange zcr = new ZipCodeRange("33333,22222]");
        assertEquals("[22222,33333]", zcr.getRangeStr());
    }

    // --------------------------------------------------
    // Exceptions - ZipCodeRange(int[])
    // --------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void createZipCodeRangeFromArrayNull() {
        new ZipCodeRange((int[])null);
    }

    @Test
    public void createZipCodeRangeFromArrayEmpty() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid ZIP code range - exactly two values must be provided: []");
        new ZipCodeRange(new int[] {});
    }

    @Test
    public void createZipCodeRangeFromArrayOneNumber() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid ZIP code range - exactly two values must be provided: [10000]");
        new ZipCodeRange(new int[] {10000});
    }

    @Test
    public void createZipCodeRangeFromArrayThreeNumbers() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid ZIP code range - exactly two values must be provided: [10000, 20000, 30000]");
        new ZipCodeRange(new int[] {10000, 20000, 30000});
    }

    @Test
    public void createZipCodeRangeFromArrayWithNegativeStart() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid start value for ZIP code range: -10000");
        new ZipCodeRange(new int[] {-10000, 22222});
    }

    @Test
    public void createZipCodeRangeFromArrayWithNegativeEnd() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid end value for ZIP code range: -22222");
        new ZipCodeRange(new int[] {10001, -22222});
    }

    @Test
    public void createZipCodeRangeFromArrayWithStartTooLarge() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid start value for ZIP code range: 123456");
        new ZipCodeRange(new int[] {123456, 10000});
    }

    @Test
    public void createZipCodeRangeFromArrayWithEndTooLarge() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid end value for ZIP code range: 567890");
        new ZipCodeRange(new int[] {10000, 567890});
    }


    // --------------------------------------------------
    // Typical usage - ZipCodeRange(int[])
    // --------------------------------------------------

    @Test
    public void createZipCodeRangeFromArray() {
        ZipCodeRange zcr = new ZipCodeRange(new int[] {10001, 22222});
        assertEquals("[10001,22222]", zcr.getRangeStr());
    }

    @Test
    public void createZipCodeRangeFromArraySingleValue() {
        ZipCodeRange zcr = new ZipCodeRange(new int[] {10002, 10002});
        assertEquals("[10002,10002]", zcr.getRangeStr());
    }

    @Test
    public void createZipCodeRangeFromArrayFullRange() {
        ZipCodeRange zcr = new ZipCodeRange(new int[] {0, 99999});
        assertEquals("[00000,99999]", zcr.getRangeStr());
    }

    @Test
    public void createZipCodeRangeFromArrayReversed() {
        ZipCodeRange zcr = new ZipCodeRange(new int[] {33333, 22222});
        assertEquals("[22222,33333]", zcr.getRangeStr());
    }

    // --------------------------------------------------
    // Exceptions - ZipCodeRange(List<Integer>)
    // --------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void createZipCodeRangeFromListNull() {
        new ZipCodeRange((ArrayList<Integer>)null);
    }

    @Test
    public void createZipCodeRangeFromListEmpty() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid ZIP code range - exactly two values must be provided: []");
        new ZipCodeRange(Collections.emptyList());
    }

    @Test
    public void createZipCodeRangeFromListOneNumber() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid ZIP code range - exactly two values must be provided: [10000]");
        new ZipCodeRange(Collections.singletonList(10000));
    }

    @Test
    public void createZipCodeRangeFromListThreeNumbers() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid ZIP code range - exactly two values must be provided: [10000, 20000, 30000]");
        new ZipCodeRange(Arrays.asList(10000, 20000, 30000));
    }

    @Test
    public void createZipCodeRangeFromListWithNegativeStart() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid start value for ZIP code range: -10000");
        new ZipCodeRange(Arrays.asList(-10000, 22222));
    }

    @Test
    public void createZipCodeRangeFromListWithNegativeEnd() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid end value for ZIP code range: -22222");
        new ZipCodeRange(Arrays.asList(10000, -22222));
    }

    @Test
    public void createZipCodeRangeFromListWithStartTooLarge() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid start value for ZIP code range: 123456");
        new ZipCodeRange(Arrays.asList(123456, 10000));
    }

    @Test
    public void createZipCodeRangeFromListWithEndTooLarge() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid end value for ZIP code range: 567890");
        new ZipCodeRange(Arrays.asList(10000, 567890));
    }

    // --------------------------------------------------
    // Typical usage - ZipCodeRange(List<Integer>)
    // --------------------------------------------------

    @Test
    public void createZipCodeRangeFromList() {
        ZipCodeRange zcr = new ZipCodeRange(Arrays.asList(10001, 22222));
        assertEquals("[10001,22222]", zcr.getRangeStr());
    }

    @Test
    public void createZipCodeRangeFromListSingleValue() {
        ZipCodeRange zcr = new ZipCodeRange(Arrays.asList(10002, 10002));
        assertEquals("[10002,10002]", zcr.getRangeStr());
    }

    @Test
    public void createZipCodeRangeFromListFullRange() {
        ZipCodeRange zcr = new ZipCodeRange(Arrays.asList(0, 99999));
        assertEquals("[00000,99999]", zcr.getRangeStr());
    }

    @Test
    public void createZipCodeRangeFromListReversed() {
        ZipCodeRange zcr = new ZipCodeRange(Arrays.asList(33333, 22222));
        assertEquals("[22222,33333]", zcr.getRangeStr());
    }

    // --------------------------------------------------
    // Typical usage - copy()
    // --------------------------------------------------

    @Test
    public void createCopy() {
        ZipCodeRange zcr = new ZipCodeRange("12345, 67890");
        ZipCodeRange clone = ZipCodeRange.copy(zcr);
        assertEquals(12345, zcr.getStart());
        assertEquals(12345, clone.getStart());
        assertEquals(67890, zcr.getEnd());
        assertEquals(67890, clone.getEnd());
        assertNotEquals(zcr, clone);
    }

    // --------------------------------------------------
    // Typical usage - compareTo()
    // --------------------------------------------------

    @Test
    public void compareToEqual() {
        ZipCodeRange zcr1 = new ZipCodeRange("11111, 22222");
        ZipCodeRange zcr1a = new ZipCodeRange("11111, 22222");
        assertEquals(0, zcr1.compareTo(zcr1a));
    }

    @Test
    public void compareToLessThanStart() {
        ZipCodeRange zcr1 = new ZipCodeRange("11111, 12222");
        ZipCodeRange zcr2 = new ZipCodeRange("22222, 23333");
        assertThat(zcr1.compareTo(zcr2), is(lessThan(0)));
    }

    @Test
    public void compareToLessThanEnd() {
        ZipCodeRange zcr1 = new ZipCodeRange("11111, 12222");
        ZipCodeRange zcr2 = new ZipCodeRange("11111, 13333");
        assertThat(zcr1.compareTo(zcr2), is(lessThan(0)));
    }

    @Test
    public void compareToGreaterThanStart() {
        ZipCodeRange zcr1 = new ZipCodeRange("11111, 12222");
        ZipCodeRange zcr2 = new ZipCodeRange("22222, 23333");
        assertThat(zcr2.compareTo(zcr1), is(greaterThan(0)));
    }

    @Test
    public void compareToGreaterThanEnd() {
        ZipCodeRange zcr1 = new ZipCodeRange("11111, 12222");
        ZipCodeRange zcr2 = new ZipCodeRange("11111, 13333");
        assertThat(zcr2.compareTo(zcr1), is(greaterThan(0)));
    }

    // --------------------------------------------------
    // Typical usage - getStart()
    // --------------------------------------------------

    @Test
    public void getStartValue() {
        ZipCodeRange zcr = new ZipCodeRange("12345, 67890");
        assertEquals(12345, zcr.getStart());
    }

    // --------------------------------------------------
    // Typical usage - getEnd()
    // --------------------------------------------------

    @Test
    public void getEndValue() {
        ZipCodeRange zcr = new ZipCodeRange("12345, 67890");
        assertEquals(67890, zcr.getEnd());
    }

    // --------------------------------------------------
    // Typical usage - getRangeArray()
    // --------------------------------------------------

    @Test
    public void getRangeArray() {
        ZipCodeRange zcr = new ZipCodeRange("12345, 67890");
        assertArrayEquals(new int[] {12345, 67890}, zcr.getRangeArray());
    }

    // --------------------------------------------------
    // Typical usage - getRangeStr()
    // --------------------------------------------------

    @Test
    public void getRangeStr() {
        ZipCodeRange zcr = new ZipCodeRange("12345, 67890");
        assertEquals("[12345,67890]", zcr.getRangeStr());
    }

    @Test
    public void getRangeStrWithPaddedZeroes() {
        ZipCodeRange zcr = new ZipCodeRange("1, 500");
        assertEquals("[00001,00500]", zcr.getRangeStr());
    }

    // --------------------------------------------------
    // Typical usage - isMergeable()
    // --------------------------------------------------

    @Test
    public void checkMergeWithNull() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertFalse(zcr.isMergeable(null));
    }

    // mergeStart, mergeEnd, rangeStart, rangeEnd
    @Test
    public void checkMerge_mergeEndEqualRangeStartMinus2() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertFalse(zcr.isMergeable(new ZipCodeRange("10098, 10098")));
    }

    @Test
    public void checkMerge_mergeEndEqualRangeStartMinus1() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10098, 10099")));
    }

    @Test
    public void checkMerge_mergeEndEqualRangeStart() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10098, 10100")));
    }

    // mergeStart, rangeStart, mergeEnd, rangeEnd
    @Test
    public void checkMerge_mergeStartEqualRangeStartMinus2() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10098, 10101")));
    }

    @Test
    public void checkMerge_mergeStartEqualRangeStartMinus1() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10099, 10150")));
    }

    @Test
    public void checkMerge_mergeStartEqualRangeStart() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10100, 10150")));
    }

    // mergeStart, rangeStart, rangeEnd, mergeEnd
    @Test
    public void checkMerge_mergeStartEqualRangeStartMinus2_mergeEndEqualRangeEnd() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10098, 10150")));
    }

    @Test
    public void checkMerge_mergeStartEqualRangeStartMinus2_mergeEndEqualRangeEndPlus1() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10098, 10151")));
    }

    @Test
    public void checkMerge_mergeStartEqualRangeStartMinus1_mergeEndEqualRangeEnd() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10099, 10150")));
    }

    @Test
    public void checkMerge_mergeStartEqualRangeStartMinus1_mergeEndEqualRangeEndPlus1() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10099, 10151")));
    }

    @Test
    public void checkMerge_mergeStartEqualRangeStart_mergeEndEqualRangeEndPlus1() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10100, 10151")));
    }

    // rangeStart, mergeStart, mergeEnd, rangeEnd
    @Test
    public void checkMerge_mergeStartEqualRangeStart_mergeEndLessThanRangeEnd() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10100, 10101")));
    }

    @Test
    public void checkMerge_mergeStartEqualRangeStartPlus1_mergeEndLessThanRangeEnd() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10101, 10149")));
    }

    @Test
    public void checkMerge_mergeStartEqualRangeStartPlus2_mergeEndEqualRangeEnd() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10101, 10149")));
    }

    // rangeStart, mergeStart, rangeEnd, mergeEnd
    @Test
    public void checkMerge_mergeEndEqualRangeEnd() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10150, 10150")));
    }

    @Test
    public void checkMerge_mergeEndEqualRangeEndPlus1() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10150, 10151")));
    }

    @Test
    public void checkMerge_mergeEndEqualRangeEndPlus2() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10150, 10152")));
    }

    // rangeStart, mergeStart, rangeEnd, mergeEnd
    @Test
    public void checkMerge_mergeStartEqualRangeEnd() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10150, 10151")));
    }

    @Test
    public void checkMerge_mergeStartEqualRangeEndPlus1() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10151, 10152")));
    }

    @Test
    public void checkMerge_mergeStartEqualRangeEndPlus2() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertFalse(zcr.isMergeable(new ZipCodeRange("10152, 10153")));
    }

    @Test
    public void checkMerge_outerRangeWithInner() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10101, 10102")));
    }

    @Test
    public void checkMerge_innerRangeWithOuter() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10001, 20101")));
    }

    @Test
    public void checkMerge_mergeStartInRange() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10101, 10125")));
    }

    @Test
    public void checkMerge_mergeEndInRange() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10001, 10125")));
    }

    @Test
    public void checkMerge_rangeStartInMerge() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10001, 10125")));
    }

    @Test
    public void checkMerge_rangeEndInMerge() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10125, 10500")));
    }

    @Test
    public void checkMerge_mergeStartAdjacentRange() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        assertTrue(zcr.isMergeable(new ZipCodeRange("10098, 10099")));
    }

    // --------------------------------------------------
    // Typical usage - merge()
    // --------------------------------------------------

    @Test
    public void mergeNull() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        zcr.merge(null);
        assertEquals("[10100,10150]", zcr.getRangeStr());
    }

    @Test
    public void mergeStartBeforeRangeStart_mergeEndEqualRangeStartMinus2() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        zcr.merge(new ZipCodeRange("10095, 10098"));
        assertEquals("[10100,10150]", zcr.getRangeStr());
    }

    @Test
    public void mergeStartBeforeRangeStart_mergeEndEqualRangeStartMinus1() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        zcr.merge(new ZipCodeRange("10095, 10099"));
        assertEquals("[10095,10150]", zcr.getRangeStr());
    }

    @Test
    public void mergeStartBeforeRangeStart_mergeEndEqualRangeStart() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        zcr.merge(new ZipCodeRange("10095, 10100"));
        assertEquals("[10095,10150]", zcr.getRangeStr());
    }

    @Test
    public void mergeStartBeforeRangeStart_mergeEndAfterRangeStart() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        zcr.merge(new ZipCodeRange("10095, 10101"));
        assertEquals("[10095,10150]", zcr.getRangeStr());
    }

    @Test
    public void mergeStartBeforeRangeStart_mergeEndEqualRangeEnd() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        zcr.merge(new ZipCodeRange("10095, 10150"));
        assertEquals("[10095,10150]", zcr.getRangeStr());
    }

    @Test
    public void mergeStartBeforeRangeStart_mergeEndAfterRangeEnd() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        zcr.merge(new ZipCodeRange("10095, 10151"));
        assertEquals("[10095,10151]", zcr.getRangeStr());
    }

    @Test
    public void mergeStartEqualRangeStart_mergeEndEqualRangeStartMinus1() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        zcr.merge(new ZipCodeRange("10100, 10149"));
        assertEquals("[10100,10150]", zcr.getRangeStr());
    }

    @Test
    public void mergeStartEqualRangeStart_mergeEndEqualRangeStart() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        zcr.merge(new ZipCodeRange("10100, 10150"));
        assertEquals("[10100,10150]", zcr.getRangeStr());
    }

    @Test
    public void mergeStartEqualRangeStart_mergeEndEqualRangeStartPlus1() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        zcr.merge(new ZipCodeRange("10100, 10151"));
        assertEquals("[10100,10151]", zcr.getRangeStr());
    }

    @Test
    public void mergeStartEqualRangeEndMinus1_mergeEndEqualRangeEndPlus25() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        zcr.merge(new ZipCodeRange("10149, 10175"));
        assertEquals("[10100,10175]", zcr.getRangeStr());
    }

    @Test
    public void mergeStartEqualRangeEnd_mergeEndEqualRangeEndPlus25() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        zcr.merge(new ZipCodeRange("10150, 10175"));
        assertEquals("[10100,10175]", zcr.getRangeStr());
    }

    @Test
    public void mergeStartEqualRangeEndPlus1_mergeEndEqualRangeEndPlus25() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        zcr.merge(new ZipCodeRange("10151, 10175"));
        assertEquals("[10100,10175]", zcr.getRangeStr());
    }

    @Test
    public void mergeStartEqualRangeEndPlus2_mergeEndEqualRangeEndPlus25() {
        ZipCodeRange zcr = new ZipCodeRange("10100, 10150");
        zcr.merge(new ZipCodeRange("10152, 10175"));
        assertEquals("[10100,10150]", zcr.getRangeStr());
    }

    // --------------------------------------------------
    // Typical usage - toString()
    // --------------------------------------------------

    @Test
    public void getToString() {
        ZipCodeRange zcr = new ZipCodeRange("12345, 67890");
        assertEquals("ZipCodeRange {\n\tstart: 12345\n\tend: 67890\n}", zcr.toString());
    }

    @Test
    public void getToStringWithPaddedZeroes() {
        ZipCodeRange zcr = new ZipCodeRange("1, 500");
        assertEquals("ZipCodeRange {\n\tstart: 00001\n\tend: 00500\n}", zcr.toString());
    }

}