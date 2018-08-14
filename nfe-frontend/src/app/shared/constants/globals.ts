export const GLOBALS = {
   ICONS: {
        COLLAPSE_SRC: '../../../../assets/images/utils/ico_collapse.svg',
        EXPAND_SRC:  '../../../../assets/images/utils/ico_expand.svg'
    },
    PATTERNS : {
        ALPHANUMERIC: '^[a-zA-Z0-9_]+$',
        TREE_CAPS: '[A-Z]{3}',
        TAX : '/^(([O][ABC][A-Z0-9./-]{0,6})|([A-Z0-9]{2})|([X][F](([A-Z]{3})([0-9]{1,3})?)?))$/',
        AGENT: '[0-9]{7}$',
        AGENT_CHECK_DIGIT: '[0-9]{1}$',
        AIRLINE: '[A-Z0-9]{3}$',
        FREE_STAT: '[DI][A-Z0-9]{2}$',
    },
    DATE_FORMAT : {
        DATE : 'ddMy'
    },
    FORM_OF_PAYMENT : {
      MSCA_VALUE: 'msca',
      MSCC_VALUE: 'mscc',
      CC_VALUE: 'cc', // credit card
      CA_VALUE: 'ca', // cash
      EP_VALUE: 'ep' // easy pay
    }
};
