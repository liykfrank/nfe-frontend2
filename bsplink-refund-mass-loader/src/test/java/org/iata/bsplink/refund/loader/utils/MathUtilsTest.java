package org.iata.bsplink.refund.loader.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.refund.loader.utils.MathUtils.applyDecimals;
import static org.iata.bsplink.refund.loader.utils.MathUtils.parseInt;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testParseInt() {
        assertThat(parseInt("8"), equalTo(8));
    }

    @Test
    public void testParseIntNotValid() {
        assertNull(parseInt("F"));
    }

    @Test
    public void testParseIntBlank() {
        assertNull(parseInt(""));
    }

    @Test
    public void testApplyDecimals() {
        assertThat(applyDecimals("00000012345", 2), equalTo(BigDecimal.valueOf(123.45)));
    }

    @Test
    public void testApplyDecimalsMaxNumber() {
        assertThat(applyDecimals("99999999999", 2), equalTo(BigDecimal.valueOf(999999999.99)));
    }

    @Test
    public void testApplyDecimals0Decimals() {
        assertThat(applyDecimals("0010", 0), equalTo(BigDecimal.valueOf(10)));
    }

    @Test
    public void testApplyDecimalsWithNullDecimals() {
        assertNull(applyDecimals("00000012345", null));
    }

    @Test
    public void testApplyDecimalsWithNullAmount() {
        assertNull(applyDecimals(null, 8));
    }

    @Test
    public void testApplyDecimalsWithNotValidAmount() {
        assertNull(applyDecimals("ABCDFEF", 7));
    }

    @Test
    public void testApplyDecimalsWithBlankAmount() {
        assertNull(applyDecimals("", 7));
    }
}
