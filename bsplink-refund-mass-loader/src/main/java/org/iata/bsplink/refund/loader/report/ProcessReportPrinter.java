package org.iata.bsplink.refund.loader.report;

import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.apache.commons.lang.StringUtils.capitalize;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

import lombok.extern.apachecommons.CommonsLog;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.iata.bsplink.refund.loader.exception.RefundLoaderException;
import org.springframework.stereotype.Component;

/**
 * Generates the report file with the results of the refund file load.
 */
@Component
@CommonsLog
public class ProcessReportPrinter {

    private static final String SUCCESSFULLY_MESSAGE =
            "All transactions have been processed successfully.\n";

    private RefundLoaderErrorToFieldLayoutMapper layoutFieldMapper;

    public ProcessReportPrinter(RefundLoaderErrorToFieldLayoutMapper layoutFieldMapper) {

        this.layoutFieldMapper = layoutFieldMapper;
    }

    /**
     * Generates the report file.
     */
    public void print(List<RefundLoaderError> errors, String reportFileName) {

        layoutFieldMapper.addFieldLayouts(errors);

        log.info("Refund loader errors: " + errors.size());
        errors.stream().forEach(x -> log.error(x.toString()));

        File reportFile = new File(reportFileName);

        if (errors.isEmpty()) {

            generateReportProcessedSuccessfully(reportFile, SUCCESSFULLY_MESSAGE);

        } else {

            generateReportProcessedWithErrors(reportFile, errors);
        }

        log.info("Result file generated: " + reportFileName);

    }

    private void generateReportProcessedSuccessfully(File reportFile, String content) {

        try {

            writeStringToFile(reportFile, content, Charset.defaultCharset());

        } catch (Exception exception) {

            throw new RefundLoaderException(exception);
        }
    }

    private void generateReportProcessedWithErrors(File reportFile,
            List<RefundLoaderError> errors) {

        try (FileWriter writer = new FileWriter(reportFile);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                PrintWriter printWriter = new PrintWriter(bufferedWriter);) {

            for (RefundLoaderError error : errors) {

                printError(error, printWriter);
            }

        } catch (Exception exception) {

            throw new RefundLoaderException(exception);
        }

    }

    private void printError(RefundLoaderError error, PrintWriter printWriter) {

        String recordIdentifierName = error.getRecordIdentifier() != null
                ? error.getRecordIdentifier().name()
                : "";

        String elementNumber = "";
        String abbreviation = "";

        if (error.getFieldLayout() != null) {

            elementNumber = error.getFieldLayout().getElementNumber().toString();
            abbreviation = error.getFieldLayout().getAbbreviation();
        }

        printWriter.printf(
                "Line: %5s TRNN: %6s    Record: %4s    Element: #%2s    %4s%n",
                Objects.toString(error.getLineNumber(), ""),
                Objects.toString(error.getTransactionNumber(), ""),
                recordIdentifierName,
                elementNumber,
                abbreviation);

        printWriter.printf("ERROR: %s%n%n", capitalize(error.getMessage()));
    }

}
