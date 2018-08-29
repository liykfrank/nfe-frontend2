package org.iata.bsplink.refund.loader.test.fixtures;

public class Constants {

    public static final Long ID = (long)1;
    public static final String ISO_COUNTRY_CODE = "ES";
    public static final String AIRLINE_CODE = "001";
    public static final String AGENT_CODE = "00000011";
    public static final String TRANSACTION_NUMBER_1 = "000001";
    public static final String TRANSACTION_NUMBER_2 = "000002";
    public static final String TICKET_DOCUMENT_NUMBER_1 = AIRLINE_CODE + "0000000001";

    private static final String BASE_WRONG_TEST_FILE = "wrong/XXe9EARS_20180101_0011_001";

    public static final String FILE_WRONG_RECORD_COUNTER =
            BASE_WRONG_TEST_FILE + "_WRONG_RECORD_COUNTER";
    public static final String FILE_NO_FILE_TRAILER_RECORD =
            BASE_WRONG_TEST_FILE + "_NO_FILE_TRAILER_RECORD";
    public static final String FILE_EMPTY_FILE =
            BASE_WRONG_TEST_FILE + "_EMPTY_FILE";
    public static final String FILE_WRONG_RECORD_COUNT_FORMAT =
            BASE_WRONG_TEST_FILE + "_WRONG_RECORD_COUNT_FORMAT";
    public static final String FILE_NO_HEADER_RECORD =
            BASE_WRONG_TEST_FILE + "_NO_HEADER_RECORD";
    public static final String FILE_INVALID_TRANSACTIONS =
            "wrong/FRe9EARS_20150602_0302_INVALID";
    public static final String FILE_WITH_MORE_THAN_127_RECORDS =
            "AAe9EARS_20080225_2015_005_MORE_THAN_127_RECORDS";
    public static final String FILE_WITHOUT_EOL =
            "ALe9EARS_20170410_0744_016_NOEOL";
    public static final String FILE_WITH_WRONG_LENGTH =
            BASE_WRONG_TEST_FILE + "_INVALID_LENGTH";

    private Constants() {
        // empty
    }
}
