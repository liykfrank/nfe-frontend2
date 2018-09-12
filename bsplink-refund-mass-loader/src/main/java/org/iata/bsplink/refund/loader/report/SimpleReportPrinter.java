package org.iata.bsplink.refund.loader.report;

import java.util.List;
import java.util.stream.Collectors;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;

public class SimpleReportPrinter extends BaseErrorsReportPrinter {

    @Override
    protected List<String> getLinesToPrint(List<RefundLoaderError> errors) {

        return errors.stream().map(x -> x.getMessage()).collect(Collectors.toList());
    }

}
