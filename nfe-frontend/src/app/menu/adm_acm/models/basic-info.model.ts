import { AgentModel } from "./agent.model";
import { CompanyModel } from "./company.model";
import { Contact } from "./contact.model";

export class BasicInfoModel {
  id: number;//Number

  transactionCode: string; //Type

  billingPeriod: number; //tres campos

  airlineCode: string;
  amountPaidByCustomer: number;
  commissionType: string;

  isoCountryCode: string;
  concernsIndicator: string; //SPDR
  tocaType: string;
  currency: {code: string, decimals: number};
  stat: string;
  netReporting: boolean;

  airlineContact: Contact = new Contact();
  company: CompanyModel;
  agent: AgentModel;

  constructor() {
    this.agent = new AgentModel();
    this.company = new CompanyModel();
  }
}
