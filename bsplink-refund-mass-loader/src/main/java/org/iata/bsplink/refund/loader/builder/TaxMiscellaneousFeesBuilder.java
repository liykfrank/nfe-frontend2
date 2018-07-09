package org.iata.bsplink.refund.loader.builder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.iata.bsplink.refund.loader.dto.TaxMiscellaneousFee;
import org.iata.bsplink.refund.loader.model.record.RecordIt05;
import org.iata.bsplink.refund.utils.MathUtils;

@Setter
@RequiredArgsConstructor
public class TaxMiscellaneousFeesBuilder {
    private final List<RecordIt05> it05s;
    private Integer numDecimals;

    /**
     * Creates list of TaxMiscellaneousFee.
     * @return the list of TaxMiscellaneousFee.
     */
    public List<TaxMiscellaneousFee> build() {
        return removeTrailingBlanks(it05s.stream().flatMap(this::taxMiscellaneousFees)
                .collect(Collectors.toList()));
    }



    private List<TaxMiscellaneousFee> removeTrailingBlanks(List<TaxMiscellaneousFee> taxes) {
        if (taxes.isEmpty()) {
            return taxes;
        }
        int lastIndex = taxes.size() - 1;
        TaxMiscellaneousFee lastTax = taxes.get(lastIndex);
        if (!StringUtils.isBlank(lastTax.getType()) || lastTax.getAmount() == null
                || lastTax.getAmount().signum() != 0) {
            return taxes;
        }
        taxes.remove(lastIndex);
        return removeTrailingBlanks(taxes);
    }


    private Stream<TaxMiscellaneousFee> taxMiscellaneousFees(RecordIt05 it05) {
        return Stream.of(
                taxMiscellaneousFee(it05.getTaxMiscellaneousFeeAmount1(),
                        it05.getTaxMiscellaneousFeeType1()),
                taxMiscellaneousFee(it05.getTaxMiscellaneousFeeAmount2(),
                        it05.getTaxMiscellaneousFeeType2()),
                taxMiscellaneousFee(it05.getTaxMiscellaneousFeeAmount3(),
                        it05.getTaxMiscellaneousFeeType3()),
                taxMiscellaneousFee(it05.getTaxMiscellaneousFeeAmount4(),
                        it05.getTaxMiscellaneousFeeType4()),
                taxMiscellaneousFee(it05.getTaxMiscellaneousFeeAmount5(),
                        it05.getTaxMiscellaneousFeeType5()),
                taxMiscellaneousFee(it05.getTaxMiscellaneousFeeAmount6(),
                        it05.getTaxMiscellaneousFeeType6()));
    }

    private TaxMiscellaneousFee taxMiscellaneousFee(String amount, String type) {
        TaxMiscellaneousFee tax = new TaxMiscellaneousFee();
        tax.setType(type);
        tax.setAmount(MathUtils.applyDecimals(amount, numDecimals));
        return tax;
    }
}
