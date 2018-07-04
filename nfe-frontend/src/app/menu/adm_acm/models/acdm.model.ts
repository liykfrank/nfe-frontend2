
import { TicketDocument } from './ticket-document.model';
import { InputAmountServer } from './input-amount-server.model';
import { TaxAmountServer } from './tax-amount-server';
import { Contact } from '../../../shared/models/contact.model';
import { AttachedFile } from './attached-file.model';
import { CurrencyServer } from './currency-server.model';

export class Acdm {
  //Generic
  id: number;
  ticketDocumentNumber: string;
  dateOfIssue: string; // ISO Format 2018-06-13T11:20:01.736Z;

  //BasicInfo
  isoCountryCode: string;
  billingPeriod: number;
  agentCode: string;
  agentRegistrationNumber: string;
  agentVatNumber: string;

  transactionCode: string;
  airlineCode: string;
  airlineRegistrationNumber: string;
  airlineVatNumber: string;
  airlineContact: Contact;

  concernsIndicator: string; // SPDR for
  taxOnCommissionType: string;
  currency: CurrencyServer;
  netReporting: boolean;
  statisticalCode: string;

  //Amount
  agentCalculations: InputAmountServer;
  airlineCalculations: InputAmountServer;
  taxMiscellaneousFees: TaxAmountServer[];
  amountPaidByCustomer: number; //TOTAL => Cuidado decimales
  regularized: boolean; // Se rellena al comparar agente y aerolínea, solicitar aceptación al usuario antes de cambiar esta var

  //Details
  dateOfIssueRelatedDocument: string; // ISO Format 2018-06-13T11:20:01.736Z;
  passenger: string;
  relatedTicketDocuments: TicketDocument[];
  reasonForMemoIssuanceCode: string;
  reasonForMemo: string;

  //Files
  attachedFiles: AttachedFile[];

  //Comments
  comments: Comment[];
}
