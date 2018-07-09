package org.iata.bsplink.refund.loader.validation;

import com.google.common.base.Optional;

import java.io.File;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

public class RefundLoaderParametersValidator implements JobParametersValidator {

    private static final String FILE_PARAMETER = "file";

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {

        throwExceptionIfParametersIsNull(parameters);

        Optional<String> optionalFileName =
                Optional.fromNullable(parameters.getString(FILE_PARAMETER));

        throwExceptionIfFileParameterIsMissing(optionalFileName);

        File file = new File(optionalFileName.get());

        throwExceptionIfFileDoesNotExist(file);
    }

    private void throwExceptionIfParametersIsNull(JobParameters parameters)
            throws JobParametersInvalidException {

        if (parameters == null) {
            throw new JobParametersInvalidException("The JobParameters can not be null");
        }
    }

    private void throwExceptionIfFileParameterIsMissing(Optional<String> optionalFileName)
            throws JobParametersInvalidException {

        if (!optionalFileName.isPresent()) {

            throw new JobParametersInvalidException(
                    "A refund file must be passed with the option file=REFUND_FILE");
        }
    }

    private void throwExceptionIfFileDoesNotExist(File file) throws JobParametersInvalidException {

        if (!file.exists()) {

            throw new JobParametersInvalidException(
                    String.format("File %s does not exist", file.getPath()));
        }
    }

}