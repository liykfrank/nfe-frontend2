import { CurrencyServer } from './currency-server.model';
import { AgentModel } from "./agent.model";
import { CompanyModel } from "./company.model";
import { Contact } from "../../../shared/models/contact.model";

export class BasicInfoModel {
  id: number; // Number
  ticketDocumentNumber: string;

  transactionCode: string; // Type

  billingPeriod: number; // tres campos

  //commissionType: string;//¿?¿?¿?

  isoCountryCode: string;
  concernsIndicator: string; // SPDR
  tocaType: string;
  stat: string;
  netReporting: boolean;
  
  airlineRegistrationNumber: string;
  airlineVatNumber: string;//on Company but you can modify
  
  agentRegistrationNumber: string;
  agentVatNumber: string; //on Company but you can modify
  
  currency: CurrencyServer = new CurrencyServer();
  airlineContact: Contact = new Contact();
  company: CompanyModel = new CompanyModel();
  agent: AgentModel = new AgentModel();

  constructor() { }
}
