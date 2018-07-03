package org.iata.bsplink.filemanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.iata.bsplink.filemanager.configuration.BsplinkFileBasicConfig;
import org.iata.bsplink.filemanager.exception.BsplinkValidationException;
import org.iata.bsplink.filemanager.model.entity.BsplinkConfig;
import org.iata.bsplink.filemanager.model.repository.BsplinkConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BsplinkFileConfigService {

    public static final String ERROR_MSG_PATTERN_NULL =
            "pattern in fileNamePatterns must not be null";
    public static final String ERROR_MSG_EXTENSION_NULL =
            "extension in allowedFileExtensions must not be null";
    public static final String ERROR_MSG_SYNTAX = "Syntax Error";

    private static final String FILE_CONFIGURATION_ID = "FILE";

    @Autowired
    private BsplinkConfigRepository fileConfigurationRepository;

    /**
     * Returns BSPlink File Configurations.
     */
    public BsplinkFileBasicConfig find() {
        Optional<BsplinkConfig> fileConfiguration =
                fileConfigurationRepository.findById(FILE_CONFIGURATION_ID);
        if (fileConfiguration.isPresent()) {
            return fileConfiguration.get().getConfig();
        }
        return null;
    }

    /**
     * Updates BSPlink File Configurations.
     *
     * @throws BsplinkValidationException Exception validating new BSPlink File Configurations.
     */
    public ResponseEntity<String> update(BsplinkFileBasicConfig newFileConfiguration)
            throws BsplinkValidationException {
        validate(newFileConfiguration);
        BsplinkConfig bsplinkConfig = new BsplinkConfig();
        bsplinkConfig.setId(FILE_CONFIGURATION_ID);
        bsplinkConfig.setConfig(newFileConfiguration);
        fileConfigurationRepository.save(bsplinkConfig);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validate(BsplinkFileBasicConfig fileConfiguration)
            throws BsplinkValidationException {
        List<String> errors = new ArrayList<>();

        if (fileConfiguration.getFileNamePatterns().contains(null)) {
            errors.add(ERROR_MSG_PATTERN_NULL);
        }

        if (fileConfiguration.getAllowedFileExtensions().contains(null)) {
            errors.add(ERROR_MSG_EXTENSION_NULL);
        }

        for (String regex : fileConfiguration.getFileNamePatterns()) {
            if (regex != null) {
                try {
                    Pattern.compile(regex);
                } catch (PatternSyntaxException e) {
                    errors.add(ERROR_MSG_SYNTAX + " " + e.getPattern());
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new BsplinkValidationException(errors);
        }
    }
}
