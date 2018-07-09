package org.iata.bsplink.refund.loader.builder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.iata.bsplink.refund.loader.dto.FormOfPaymentAmount;
import org.iata.bsplink.refund.loader.dto.FormOfPaymentType;
import org.iata.bsplink.refund.loader.model.record.RecordIt08;
import org.iata.bsplink.refund.utils.MathUtils;

@Setter
@RequiredArgsConstructor
public class FormOfPaymentAmountsBuilder {
    private final List<RecordIt08> it08s;
    private Integer numDecimals;

    /**
     * Creates the list of FormOfPaymentAmount.
     * @return The created list of FormOfPaymentAmount.
     */
    public List<FormOfPaymentAmount> build() {
        return removeTrailingBlanks(it08s.stream().flatMap(this::formOfPaymentAmounts)
                .collect(Collectors.toList()));
    }



    private List<FormOfPaymentAmount> removeTrailingBlanks(List<FormOfPaymentAmount> fops) {
        if (fops.isEmpty()) {
            return fops;
        }
        int lastIndex = fops.size() - 1;
        FormOfPaymentAmount lastFop = fops.get(lastIndex);
        if (!StringUtils.isBlank(lastFop.getNumber())
                || (lastFop.getAmount() == null || lastFop.getAmount().signum() != 0)
                || (lastFop.getType() != null)) {
            return fops;
        }
        fops.remove(lastIndex);
        return removeTrailingBlanks(fops);
    }


    private Stream<FormOfPaymentAmount> formOfPaymentAmounts(RecordIt08 it08) {
        return Stream.of(
                formOfPaymentAmount(it08.getFormOfPaymentAmount1(),
                        it08.getFormOfPaymentAccountNumber1(),
                        it08.getFormOfPaymentType1()),
                formOfPaymentAmount(it08.getFormOfPaymentAmount2(),
                        it08.getFormOfPaymentAccountNumber2(),
                        it08.getFormOfPaymentType2()));
    }


    private FormOfPaymentAmount formOfPaymentAmount(String amount, String number, String fptp) {
        FormOfPaymentAmount fop = new FormOfPaymentAmount();

        if (fptp.startsWith("CC")) {
            fop.setType(FormOfPaymentType.CC);
        } else if (fptp.startsWith("CA")) {
            fop.setType(FormOfPaymentType.CA);
        } else if (fptp.startsWith("MSCA")) {
            fop.setType(FormOfPaymentType.MSCA);
        } else if (fptp.startsWith("MSCC")) {
            fop.setType(FormOfPaymentType.MSCC);
        } else if (fptp.startsWith("EP")) {
            fop.setType(FormOfPaymentType.EP);
        }

        if (fop.getType() != null && !fop.getType().isCash()) {
            fop.setNumber(number);
            if (fop.getType().equals(FormOfPaymentType.MSCC)) {
                if (fptp.length() > 5) {
                    fop.setVendorCode(fptp.substring(4, 6));
                }
            } else if (fptp.length() > 4) {
                fop.setVendorCode(fptp.substring(2, 4));
            }
        }

        fop.setAmount(MathUtils.applyDecimals(amount, numDecimals));
        return fop;
    }

}
