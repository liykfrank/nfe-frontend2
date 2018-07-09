package org.iata.bsplink.refund.loader.builder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.iata.bsplink.refund.loader.dto.RelatedDocument;
import org.iata.bsplink.refund.loader.model.record.RecordIt03;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Setter
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RelatedDocumentsBuilder {
    private List<RecordIt03> it03s;

    /**
     * Creates the list of RelatedDocument.
     * @return The create list of RelatedDocument.
     */
    public List<RelatedDocument> build() {
        return removeTrailingBlanks(it03s.stream().flatMap(this::relatedDocuments)
                .collect(Collectors.toList()));
    }



    private List<RelatedDocument> removeTrailingBlanks(List<RelatedDocument> relatedDocuments) {
        if (relatedDocuments.isEmpty()) {
            return relatedDocuments;
        }
        int lastIndex = relatedDocuments.size() - 1;
        RelatedDocument lastRelatedDocument = relatedDocuments.get(lastIndex);
        if (!StringUtils.isBlank(lastRelatedDocument.getRelatedTicketDocumentNumber())
                || couponNotFalse(lastRelatedDocument.getRelatedTicketCoupon1())
                || couponNotFalse(lastRelatedDocument.getRelatedTicketCoupon2())
                || couponNotFalse(lastRelatedDocument.getRelatedTicketCoupon3())
                || couponNotFalse(lastRelatedDocument.getRelatedTicketCoupon4())) {
            return relatedDocuments;
        }
        relatedDocuments.remove(lastIndex);
        return removeTrailingBlanks(relatedDocuments);
    }

    private boolean couponNotFalse(Boolean c) {
        return c == null || c;
    }


    private Stream<RelatedDocument> relatedDocuments(RecordIt03 it03) {
        return Stream.of(
                relatedDocument(it03.getRelatedTicketDocumentNumber1(),
                        it03.getRelatedTicketDocumentCouponNumberIdentifier1()),
                relatedDocument(it03.getRelatedTicketDocumentNumber2(),
                        it03.getRelatedTicketDocumentCouponNumberIdentifier2()),
                relatedDocument(it03.getRelatedTicketDocumentNumber3(),
                        it03.getRelatedTicketDocumentCouponNumberIdentifier3()),
                relatedDocument(it03.getRelatedTicketDocumentNumber4(),
                        it03.getRelatedTicketDocumentCouponNumberIdentifier4()),
                relatedDocument(it03.getRelatedTicketDocumentNumber5(),
                        it03.getRelatedTicketDocumentCouponNumberIdentifier5()),
                relatedDocument(it03.getRelatedTicketDocumentNumber6(),
                        it03.getRelatedTicketDocumentCouponNumberIdentifier6()),
                relatedDocument(it03.getRelatedTicketDocumentNumber7(),
                        it03.getRelatedTicketDocumentCouponNumberIdentifier7()));
    }

    private RelatedDocument relatedDocument(String rtdn, String rcpn) {
        RelatedDocument relatedDocument = new RelatedDocument();

        if (rtdn.length() > 3) {
            relatedDocument.setRelatedTicketDocumentNumber(rtdn.substring(3));
        }
        if (rcpn.matches("[01]...")) {
            relatedDocument.setRelatedTicketCoupon1(rcpn.charAt(0) == '1');
        }
        if (rcpn.matches(".[01]..")) {
            relatedDocument.setRelatedTicketCoupon2(rcpn.charAt(1) == '2');
        }
        if (rcpn.matches("..[01].")) {
            relatedDocument.setRelatedTicketCoupon3(rcpn.charAt(2) == '3');
        }
        if (rcpn.matches("...[01]")) {
            relatedDocument.setRelatedTicketCoupon4(rcpn.charAt(3) == '4');
        }
        return relatedDocument;
    }
}
