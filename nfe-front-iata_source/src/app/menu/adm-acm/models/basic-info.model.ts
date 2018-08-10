import { Agent } from '../../../shared/components/agent/models/agent.model';

import { Contact } from '../../../shared/components/contact/models/contact.model';
import { CurrencyPost } from '../../../shared/components/currency/models/currency-post.model';

export class BasicInfoModel {
  id: number; // Number
  ticketDocumentNumber: string;

  transactionCode: string; // Type

  billingPeriod: number; // tres campos

  // commissionType: string;//¿?¿?¿?

  isoCountryCode: string;
  concernsIndicator: string; // SPDR
  tocaType: string;
  stat: string;
  netReporting: boolean;

  airlineRegistrationNumber: string;
  airlineVatNumber: string; // on Company but you can modify

  agentRegistrationNumber: string;
  agentVatNumber: string; // on Company but you can modify

  currency: CurrencyPost = new CurrencyPost();
  airlineContact: Contact = new Contact();
  /* company: CompanyModel = new CompanyModel(); */
  agent: Agent = new Agent();

  constructor() {}
}
