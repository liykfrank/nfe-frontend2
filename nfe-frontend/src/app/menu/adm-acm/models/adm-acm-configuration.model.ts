export class AdmAcmConfiguration {

  agentVatNumberEnabled: boolean;
  airlineVatNumberEnabled: boolean;
  companyRegistrationNumberEnabled: boolean;
  cpPermittedForConcerningIssue: boolean;
  cpPermittedForConcerningRefund: boolean;
  defaultStat: string;
  freeStat: boolean;
  isoCountryCode: string;
  maxNumberOfRelatedDocuments: number;
  mfPermittedForConcerningIssue: boolean;
  mfPermittedForConcerningRefund: boolean;
  nridAndSpamEnabled: boolean;
  taxOnCommissionEnabled: boolean;
  taxOnCommissionSign: number;

  constructor() {
    this.agentVatNumberEnabled = false;
    this.airlineVatNumberEnabled = false;
    this.companyRegistrationNumberEnabled = false;
    this.cpPermittedForConcerningIssue = false;
    this.cpPermittedForConcerningRefund = false;
    this.defaultStat = '';
    this.freeStat = true;
    this.isoCountryCode = '';
    this.maxNumberOfRelatedDocuments = -1;
    this.mfPermittedForConcerningIssue = false;
    this.mfPermittedForConcerningRefund = false;
    this.nridAndSpamEnabled = false;
    this.taxOnCommissionEnabled = false;
    this.taxOnCommissionSign = -1;
  }
}

