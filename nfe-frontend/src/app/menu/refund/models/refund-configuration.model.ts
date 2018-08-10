export class RefundConfiguration {

  isoCountryCode: string;
  defaultCurrency: string; // TODO:

  // Basic Info
  agentVatNumberEnabled: boolean;
  airlineVatNumberEnabled: boolean;
  companyRegistrationNumberEnabled: boolean;

  // Details
  defaultStat: string;
  freeStat: boolean;
  maxConjunctions: number; // CNJ
  issueRefundsWithoutCouponsAllowed: boolean; // Sin checks en línea (En validación formulario)
  maxCouponsInRelatedDocuments: number; // TODO: Máximo de checks a marcar entre todos (En validación formulario, RefundComponent)

  // Form of Payment
  electronicTicketValidationsEnabled: boolean; // Electronic Ticket Auth Code
  easyPayEnabled: boolean; // Form of Payment *SHOW ON SELECT

  // Amount
  nrRefundsAllowed: boolean; // TODO:
  commissionOnCpAndMfEnabled: boolean; // Total Comm. CP and MF
  handlingFeeEnabled: boolean; // Misc. Fee
  mfAmount: number; // Misc. Fee valor por defecto
  penaltyChargeEnabled: boolean; // Cancellation penalty
  vatOnMfAndVatOnCpEnabled: boolean; // Tax on MF  && Tax on CP


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  airlineMfAndCpConfigurationAllowed: boolean; // TODO: without use yet
  allGdsAgentsIssueRefundNoticeAllowed: boolean; // TODO: without use yet
  creditOnDirectRefundsEnabled: boolean; // TODO: without use yet
  creditOnIndirectRefundsEnabled: boolean; // TODO:
  fareAdjustmentAmountEnabled: boolean; // Amount?TODO:
  mixedTaxesAllowed: boolean; // PROPORCION una en cada lado BACK
  refundNoticeNumberConsidered: boolean; // TODO: without use yet DIRECTO
  tctpForVatOnCp: string; // TODO: without use yet
  tctpForVatOnMf: string; // TODO: without use yet

constructor() {
  this.agentVatNumberEnabled = false;
  this.airlineMfAndCpConfigurationAllowed = false;
  this.airlineVatNumberEnabled = false;
  this.allGdsAgentsIssueRefundNoticeAllowed = false;
  this.commissionOnCpAndMfEnabled = false;
  this.companyRegistrationNumberEnabled = false;
  this.creditOnDirectRefundsEnabled = false;
  this.creditOnIndirectRefundsEnabled = false;
  this.defaultCurrency = '';
  this.defaultStat = '';
  this.easyPayEnabled = false;
  this.electronicTicketValidationsEnabled = false;
  this.fareAdjustmentAmountEnabled = false;
  this.freeStat = false;
  this.handlingFeeEnabled = false;
  this.isoCountryCode = '';
  this.issueRefundsWithoutCouponsAllowed = false;
  this.maxConjunctions = 0;
  this.maxCouponsInRelatedDocuments = 0;
  this.mfAmount = 0;
  this.mixedTaxesAllowed = false;
  this.nrRefundsAllowed = false;
  this.penaltyChargeEnabled = false;
  this.refundNoticeNumberConsidered = false;
  this.tctpForVatOnCp = '';
  this.tctpForVatOnMf = '';
  this.vatOnMfAndVatOnCpEnabled = false;
}



}


