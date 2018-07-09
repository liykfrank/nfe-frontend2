package org.iata.bsplink.refund.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.iata.bsplink.refund.model.entity.FormOfPaymentAmount;
import org.iata.bsplink.refund.model.entity.FormOfPaymentType;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class FormOfPaymentConsistentDataValidatorTest {

    private static final String ANY_CREDIT_CARD_NUMBER = "0000000000000000000";
    private static final String ANY_VENDOR_CODE = "00";

    @Test
    @Parameters
    public void testValidatesObject(FormOfPaymentType formOfPaymentType, String vendorCode,
            String creditCardNumber, boolean expected) {

        FormOfPaymentAmount formOfPaymentAmount = new FormOfPaymentAmount();

        formOfPaymentAmount.setType(formOfPaymentType);
        formOfPaymentAmount.setVendorCode(vendorCode);
        formOfPaymentAmount.setNumber(creditCardNumber);

        FormOfPaymentConsistentDataValidator validator = new FormOfPaymentConsistentDataValidator();
        ConstraintValidatorContext contraintValidatorContext =
                mock(ConstraintValidatorContext.class);

        assertThat(validator.isValid(formOfPaymentAmount, contraintValidatorContext),
                equalTo(expected));
    }

    @SuppressWarnings("unused")
    private Object[][] parametersForTestValidatesObject() {

        List<Object[]> testCases = new ArrayList<>();

        List<FormOfPaymentType> onCreditTypes =
                Arrays.asList(FormOfPaymentType.CC, FormOfPaymentType.EP, FormOfPaymentType.MSCC);

        // on credit card payments test cases
        for (FormOfPaymentType type : onCreditTypes) {

            testCases.add(new Object[] { type, ANY_CREDIT_CARD_NUMBER, ANY_VENDOR_CODE, true });
            testCases.add(new Object[] { type, null, null, false });
            testCases.add(new Object[] { type, "", "", false });
            testCases.add(new Object[] { type, ANY_CREDIT_CARD_NUMBER, null, false });
            testCases.add(new Object[] { type, ANY_CREDIT_CARD_NUMBER, "", false });
            testCases.add(new Object[] { type, null, ANY_VENDOR_CODE, false });
            testCases.add(new Object[] { type, "", ANY_VENDOR_CODE, false });
        }

        List<FormOfPaymentType> onCashTypes =
                Arrays.asList(FormOfPaymentType.CA, FormOfPaymentType.MSCA);

        // on cash payments test cases
        for (FormOfPaymentType type : onCashTypes) {

            testCases.add(new Object[] { type, null, null, true });
            testCases.add(new Object[] { type, "", "", true });
            testCases.add(new Object[] { type, ANY_CREDIT_CARD_NUMBER, null, false });
            testCases.add(new Object[] { type, ANY_CREDIT_CARD_NUMBER, "", false });
            testCases.add(new Object[] { type, null, ANY_VENDOR_CODE, false });
            testCases.add(new Object[] { type, "", ANY_VENDOR_CODE, false });
        }

        return testCases.toArray(new Object[][] {});
    }

}
