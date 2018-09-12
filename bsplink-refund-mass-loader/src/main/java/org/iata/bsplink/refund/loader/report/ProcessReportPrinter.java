package org.iata.bsplink.refund.loader.report;

import static org.apache.commons.lang.StringUtils.capitalize;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.iata.bsplink.refund.loader.error.RefundLoaderError;
import org.springframework.stereotype.Component;

/**
 * Generates the report file with the results of the refund file load.
 */
@Component
public class ProcessReportPrinter extends BaseErrorsReportPrinter {

    private static final String SUCCESSFULLY_MESSAGE =
            "All transactions have been processed successfully.";

    private RefundLoaderErrorToFieldLayoutMapper layoutFieldMapper;

    public ProcessReportPrinter(RefundLoaderErrorToFieldLayoutMapper layoutFieldMapper) {

        this.layoutFieldMapper = layoutFieldMapper;
    }

    @Override
    protected List<String> getLinesToPrint(List<RefundLoaderError> errors) {

        layoutFieldMapper.addFieldLayouts(errors);

        List<String> stringErrors = new ArrayList<>();

        if (errors.isEmpty()) {

            stringErrors.add(SUCCESSFULLY_MESSAGE);

        } else {

            addFormattedListOfErrors(errors, stringErrors);
        }

        return stringErrors;
    }

    private void addFormattedListOfErrors(List<RefundLoaderError> errors,
            List<String> stringErrors) {

        for (RefundLoaderError error : errors) {

            stringErrors.add(formatError(error));
        }
    }

    private String formatError(RefundLoaderError error) {

        String recordIdentifierName = error.getRecordIdentifier() != null
                ? error.getRecordIdentifier().name()
                : "";

        String elementNumber = "";
        String abbreviation = "";

        if (error.getFieldLayout() != null) {

            elementNumber = error.getFieldLayout().getElementNumber().toString();
            abbreviation = error.getFieldLayout().getAbbreviation();
        }

        String formattedError = String.format(
                "Line: %5s TRNN: %6s    Record: %4s    Element: #%2s    %4s%n",
                Objects.toString(error.getLineNumber(), ""),
                Objects.toString(error.getTransactionNumber(), ""),
                recordIdentifierName,
                elementNumber,
                abbreviation);

        formattedError += String.format("ERROR: %s%n", capitalize(error.getMessage()));

        return formattedError;
    }

}
