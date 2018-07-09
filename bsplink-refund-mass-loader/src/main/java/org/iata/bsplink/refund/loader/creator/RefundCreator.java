package org.iata.bsplink.refund.loader.creator;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.iata.bsplink.refund.loader.builder.FormOfPaymentAmountsBuilder;
import org.iata.bsplink.refund.loader.builder.RefundAmountsBuilder;
import org.iata.bsplink.refund.loader.builder.RefundBuilder;
import org.iata.bsplink.refund.loader.builder.RefundCurrencyBuilder;
import org.iata.bsplink.refund.loader.builder.RelatedDocumentsBuilder;
import org.iata.bsplink.refund.loader.builder.RemarkBuilder;
import org.iata.bsplink.refund.loader.builder.TaxMiscellaneousFeesBuilder;
import org.iata.bsplink.refund.loader.dto.FormOfPaymentAmount;
import org.iata.bsplink.refund.loader.dto.Refund;
import org.iata.bsplink.refund.loader.dto.RefundAmounts;
import org.iata.bsplink.refund.loader.dto.RefundCurrency;
import org.iata.bsplink.refund.loader.dto.RelatedDocument;
import org.iata.bsplink.refund.loader.dto.TaxMiscellaneousFee;
import org.iata.bsplink.refund.loader.model.RefundDocument;

@Setter
@RequiredArgsConstructor
public class RefundCreator {
    private final RefundDocument refundDocument;

    /**
     * Creates a Refund.
     * @return The created Refund.
     */
    public Refund create() {
        RefundBuilder refundBuilder = new RefundBuilder(refundDocument);


        RefundCurrency currency = currency();
        refundBuilder.setCurrency(currency);

        Integer numDecimals = currency == null ? null : currency.getDecimals();

        refundBuilder.setFormOfPaymentAmounts(formOfPaymentAmounts(numDecimals));

        refundBuilder.setRelatedDocuments(relatedDocuments());

        refundBuilder.setRemark(remark());

        List<TaxMiscellaneousFee> taxes = taxes(numDecimals);
        refundBuilder.setTaxMiscellaneousFees(taxMiscellaneousFees(taxes));
        refundBuilder.setAmounts(amounts(taxes, numDecimals));

        return refundBuilder.build();
    }


    private List<FormOfPaymentAmount> formOfPaymentAmounts(Integer numDecimals) {
        FormOfPaymentAmountsBuilder fopBuilder =
                new FormOfPaymentAmountsBuilder(refundDocument.getRecordsIt08());
        fopBuilder.setNumDecimals(numDecimals);
        return fopBuilder.build();
    }


    private String remark() {
        RemarkBuilder remarkBuilder = new RemarkBuilder(refundDocument.getRecordsIt0h());
        return remarkBuilder.build();
    }


    private RefundCurrency currency() {
        if (refundDocument.getRecordsIt05().isEmpty()) {
            return null;
        }
        RefundCurrencyBuilder refundCurrencyBuilder =
                new RefundCurrencyBuilder(refundDocument.getRecordsIt05().get(0));
        return refundCurrencyBuilder.build();
    }


    private List<TaxMiscellaneousFee> taxes(Integer numDecimals) {
        TaxMiscellaneousFeesBuilder taxBuilder =
                new TaxMiscellaneousFeesBuilder(refundDocument.getRecordsIt05());
        taxBuilder.setNumDecimals(numDecimals);
        return taxBuilder.build();
    }


    private List<TaxMiscellaneousFee> taxMiscellaneousFees(List<TaxMiscellaneousFee> taxes) {
        return taxes.stream().filter(this::normalTaxFilter).collect(Collectors.toList());
    }


    private boolean normalTaxFilter(TaxMiscellaneousFee tax) {
        return !"CP".equals(tax.getType()) && !"MF".equals(tax.getType());
    }


    private RefundAmounts amounts(List<TaxMiscellaneousFee> taxes, Integer numDecimals) {
        if (refundDocument.getRecordsIt05().isEmpty()) {
            return null;
        }

        RefundAmountsBuilder amountsBuilder =
                new RefundAmountsBuilder(refundDocument.getRecordsIt05().get(0));
        amountsBuilder.setTax(taxSum(taxes.stream().filter(this::normalTaxFilter)));
        amountsBuilder.setCancellationPenalty(
                taxSum(taxes.stream().filter(tax -> "CP".equals(tax.getType()))));
        amountsBuilder.setMiscellaneousFee(
                taxSum(taxes.stream().filter(tax -> "MF".equals(tax.getType()))));
        amountsBuilder.setNumDecimals(numDecimals);
        return amountsBuilder.build();
    }


    private BigDecimal taxSum(Stream<TaxMiscellaneousFee> taxes) {
        try {
            return taxes.map(TaxMiscellaneousFee::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (NullPointerException e) {
            return null;
        }
    }


    private List<RelatedDocument> relatedDocuments() {
        RelatedDocumentsBuilder relatedDocumentBuilder =
                new RelatedDocumentsBuilder(refundDocument.getRecordsIt03());
        return relatedDocumentBuilder.build();
    }
}
