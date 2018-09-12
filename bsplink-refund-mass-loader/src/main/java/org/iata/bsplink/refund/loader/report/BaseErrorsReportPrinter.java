package org.iata.bsplink.refund.loader.report;

import static org.apache.commons.io.FileUtils.writeLines;

import java.io.File;
import java.util.List;

import lombok.extern.apachecommons.CommonsLog;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.exception.RefundLoaderException;

@CommonsLog
public abstract class BaseErrorsReportPrinter implements ErrorsReportPrinter {

    /**
     * Returns the lines to be printed.
     */
    protected abstract List<String> getLinesToPrint(List<RefundLoaderError> errors);

    @Override
    public void print(List<RefundLoaderError> errors, String reportFileName) {

        log.info("Refund loader errors: " + errors.size());
        errors.stream().forEach(x -> log.error(x.toString()));

        File reportFile = new File(reportFileName);

        try {

            writeLines(reportFile, getLinesToPrint(errors));

        } catch (Exception exception) {

            throw new RefundLoaderException(exception);
        }

        log.info("File generated: " + reportFileName);
    }
}
