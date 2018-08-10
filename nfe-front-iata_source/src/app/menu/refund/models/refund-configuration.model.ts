export class RefundConfiguration {

  isoCountryCode: string;
  defaultCurrency: string; // *************

  // Basic Info
  agentVatNumberEnabled: boolean;
  airlineVatNumberEnabled: boolean;
  companyRegistrationNumberEnabled: boolean;

  // Details
  defaultStat: string;
  freeStat: boolean;
  maxConjunctions: number;

  // Form of Payment
  electronicTicketValidationsEnabled: boolean;

  // Amount

  airlineMfAndCpthisigurationAllowed: boolean;
  allGdsAgentsIssueRefundNoticeAllowed: boolean;
  commissionOnCpAndMfEnabled: boolean;
  creditOnDirectRefundsEnabled: boolean;
  creditOnIndirectRefundsEnabled: boolean;
  easyPayEnabled: boolean;
  fareAdjustmentAmountEnabled: boolean; // Amount?
  handlingFeeEnabled: boolean;
  issueRefundsWithoutCouponsAllowed: boolean;
  maxCouponsInRelatedDocuments: number;
  mfAmount: number;
  mixedTaxesAllowed: boolean;
  nrRefundsAllowed: boolean;
  penaltyChargeEnabled: boolean; // Cancellation penalty?
  refundNoticeNumberConsidered: boolean;
  tctpForVatOnCp: string;
  tctpForVatOnMf: string;
  vatOnMfAndVatOnCpEnabled: boolean;

constructor() {
  this.agentVatNumberEnabled = false;
  this.airlineMfAndCpthisigurationAllowed = false;
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


