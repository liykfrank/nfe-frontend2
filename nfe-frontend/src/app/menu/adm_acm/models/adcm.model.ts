import { TicketDocument } from './ticket-document.model';
import { InputAmountServer } from './input-amount-server.model';
import { TaxAmountServer } from './tax-amount-server';
import { Contact } from './contact.model';

export class Adcm {
  transactionCode: string;
  airlineContact: Contact;

  agentCalculations: InputAmountServer;
  airlineCalculations: InputAmountServer;
  taxMiscellaneousFees: TaxAmountServer[];

  relatedTicketDocuments: TicketDocument[];
}
