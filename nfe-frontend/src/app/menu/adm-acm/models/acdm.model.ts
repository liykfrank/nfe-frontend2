import { Contact } from '../../../shared/components/contact/models/contact.model';
import { FileDataServer } from '../../../shared/components/multitabs/models/file-data-server.model';
import { TicketDocument } from '../../../shared/components/multitabs/models/ticket-document.model';

import { InputAmountServer } from './input-amount-server.model';
import { TaxAmountServer } from './tax-amount-server';
import { CurrencyPost } from '../../../shared/components/currency/models/currency-post.model';

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

  concernsIndicator: string; // ADM/ACM for
  taxOnCommissionType: string;
  currency: CurrencyPost;
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
  attachedFiles: FileDataServer[];

  //Comments
  comments: Comment[];

  constructor() {
    this.id = 0;
    this.ticketDocumentNumber = '';
    this.dateOfIssue = new Date().toISOString();

    this.isoCountryCode = '';
    this.billingPeriod = 0;
    this.agentCode = '';
    this.agentRegistrationNumber = '';
    this.agentVatNumber = '';

    this.transactionCode = '';
    this.airlineCode = '';
    this.airlineRegistrationNumber = '';
    this.airlineVatNumber = '';
    this.airlineContact = new Contact();

    this.concernsIndicator = '';
    this.taxOnCommissionType = '';
    this.currency = new CurrencyPost();
    this.netReporting = false;
    this.statisticalCode = '';

    this.agentCalculations = new InputAmountServer();
    this.airlineCalculations = new InputAmountServer();
    this.taxMiscellaneousFees = [];
    this.amountPaidByCustomer = 0;
    this.regularized = false;

    this.dateOfIssueRelatedDocument = '';
    this.passenger = '';
    this.relatedTicketDocuments = [];
    this.reasonForMemoIssuanceCode = '';
    this.reasonForMemo = '';

    this.attachedFiles = [];
    this.comments = [];
  }
}
