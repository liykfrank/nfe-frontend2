package org.iata.bsplink.refund.loader.builder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.iata.bsplink.refund.loader.model.record.RecordIt0h;
import org.junit.Test;

public class RemarkBuilderTest {

    @Test
    public void testBuild() {
        String s = "ABCDEF";

        RecordIt0h it0h1 = new RecordIt0h();
        it0h1.setReasonForMemoInformation1(s);
        it0h1.setReasonForMemoInformation2(s);
        it0h1.setReasonForMemoInformation3(s);
        it0h1.setReasonForMemoInformation4(s);
        it0h1.setReasonForMemoInformation5(s);

        RecordIt0h it0h2 = new RecordIt0h();
        it0h2.setReasonForMemoInformation1(s);
        it0h2.setReasonForMemoInformation2(s);
        it0h2.setReasonForMemoInformation3("");
        it0h2.setReasonForMemoInformation4("");
        it0h2.setReasonForMemoInformation5("");

        List<RecordIt0h> it0hs = Arrays.asList(it0h1, it0h2);

        RemarkBuilder builder = new RemarkBuilder();
        builder.setIt0hs(it0hs);
        String remark = builder.build();

        assertNotNull(remark);
        assertThat(remark, is(StringUtils.repeat(s, 7)));
    }
}
