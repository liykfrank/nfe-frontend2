package org.iata.bsplink.refund.loader.report;

import java.util.List;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;

public interface ErrorsReportPrinter {

    public void print(List<RefundLoaderError> errors, String reportFileName);
}
