export const GLOBALS = {
  ICONS: {
    COLLAPSE_SRC: '../../../../assets/images/utils/ico_collapse.svg',
    EXPAND_SRC: '../../../../assets/images/utils/ico_expand.svg'
  },
  PATTERNS: {
    ALPHANUMERIC: /^[a-zA-Z0-9_]+$/,
    TREE_CAPS: /[A-Z]{3}/,
    TAX: /^(([O][ABC][A-Z0-9./-]{0,6})|([A-Z0-9]{2})|([X][F](([A-Z]{3})([0-9]{1,3})?)?))$/,
    AGENT: /[0-9]{7}$/,
    AIRLINE: /[A-Z0-9]{3}$/,
    FREE_STAT: /[DI][A-Z0-9]{2}$/,
    PERCENT_MAX_99_99: /^(?:99|[1-9]?[0-9])(([\.][0-9]{1,2})?)$/,
    WAIVER_CODE: /[A-Za-z0-9 /.-]{0,14}/,
    TOUR_CODE: /[ \!#-~]{0,15}/,
    ELECTRONIC_TICKET_AUTH: /[A-Za-z0-9 ]{0,14}/,
    PASSSENGER: /[0-9A-Za-z\-\.\/ ]{0,49}/,
    EMAIL: /[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,3}$/
  },
  HTML_PATTERN: {
    WAIVER_CODE: '^[A-Za-z0-9 /.-]*$',
    INPUT_PERCENT_MAX_99_99: '^[.0-9]*$',
    CAPITALTEXT: '^[A-Z]*$',
    TEXT: '^[A-Za-z]*$',
    PASSSENGER: '^[0-9A-Za-z-.//\\s]*$',
    ALPHANUMERIC_LOWERCASE: '^[a-zA-Z0-9]*$',
    ALPHANUMERIC_LOWERCASE_WITH_SPACE: '^[a-zA-Z0-9 ]*$',
    ALPHANUMERIC_UPPERCASE: '^[A-Z0-9]*$',

    TOUR_CODE: '^[ \!#-~]*$',
    NUMERIC: '^[0-9]*$'
  },
  DATE_FORMAT: {
    DATE: 'ddMy'
  },
  FORM_OF_PAYMENT: ['CA', 'CC', 'MSCA', 'MSCC', 'EP'],
  FORM_OF_PAYMENT_EP: 'EP',
  TYPES_OF_USER: {
    AGENT: 'Agent',
    AIRLINE: 'Airline',
    IATA: 'IATA',
    DPC: 'DPC',
    GDS: 'GDS',
    THIRD_PARTY: 'Third Party'
  },
  REACTIVE_FORMS: {
    EMIT_EVENT_FALSE: {
      emitEvent: false
    }
  },
  ACDM: {
    ADM: ['ADMA', 'SPDR', 'ADMD'],
    ACM: ['ACMA', 'SPCR', 'ACMD'],
    COMPLEX_TYPE: ['SPDR', 'SPCR', 'ACMA', 'ADMA'],
    FOR: ['I', 'R', 'X', 'E'],
    FOR_REFUND: 'R',
    FOR_ISSUE: 'I'
  }
};
