import { TicketDocument } from '../../../../shared/components/multitabs/models/ticket-document.model';

export class RefundRelatedDocument extends TicketDocument {
  relatedTicketCoupon1: boolean;
  relatedTicketCoupon2: boolean;
  relatedTicketCoupon3: boolean;
  relatedTicketCoupon4: boolean;

  constructor() {
    super();

    this.relatedTicketCoupon1 = false;
    this.relatedTicketCoupon2 = false;
    this.relatedTicketCoupon3 = false;
    this.relatedTicketCoupon4 = false;
  }
}
