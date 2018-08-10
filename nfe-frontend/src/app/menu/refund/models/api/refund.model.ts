import { Contact } from '../../../../shared/components/contact/models/contact.model';
import { CurrencyPost } from '../../../../shared/components/currency/models/currency-post.model';
import { RefundAmount } from './refund-amount.model';
import { RefundFormOfPaymentAmounts } from './refund-form-of-payment-amounts';
import { RefundOriginalIssue } from './refund-original-issue.model';
import { RefundRelatedDocument } from './refund-related-document.model';
import { RefundTaxMiscellaneousFees } from './refund-tax-miscellaneous-fees.model';

export class Refund {
  id: number;
  isoCountryCode: string;
  status: string;
  dateOfIssue: string;

  airlineRemark: string;
  dateOfAirlineAction: string;
  rejectionReason: string;

  /* BASIC INFO */
  agentCode: string;
  agentContact: Contact;
  agentRegistrationNumber: string;
  agentVatNumber: string;
  airlineCode: string;
  airlineContact: Contact;
  airlineRegistrationNumber: string;
  airlineVatNumber: string;

  /* DETAILS*/
  statisticalCode: string;
  passenger: string;
  issueReason: string;
  airlineCodeRelatedDocument: string;
  dateOfIssueRelatedDocument: string;
  waiverCode: string;
  relatedDocument: RefundRelatedDocument;
  conjunctions: RefundRelatedDocument[];
  exchange: boolean;
  originalIssue: RefundOriginalIssue;

  /* FORM OF PAYMENT */
  formOfPaymentAmounts: RefundFormOfPaymentAmounts[];
  tourCode: string;
  currency: CurrencyPost;
  customerFileReference: string;
  settlementAuthorisationCode: string;

  /* AMOUNT */
  partialRefund: boolean;
  netReporting: boolean;
  amounts: RefundAmount;
  taxMiscellaneousFees: RefundTaxMiscellaneousFees[];

  constructor() {
    this.conjunctions = [];
    this.formOfPaymentAmounts = [];
    this.taxMiscellaneousFees = [];

    this.agentContact = new Contact();
    this.airlineContact = new Contact();

    this.amounts = new RefundAmount();
    this.currency = new CurrencyPost();
    this.originalIssue = new RefundOriginalIssue();
    this.relatedDocument = new RefundRelatedDocument();

    this.exchange = false;
    this.netReporting = false;

    this.agentCode = '';
    this.agentRegistrationNumber = '';
    this.agentVatNumber = '';
    this.airlineCode = '';
    this.airlineRegistrationNumber = '';
    this.airlineVatNumber = '';
    this.airlineCodeRelatedDocument = '';
    this.airlineRemark = '';
    this.customerFileReference = '';
    this.dateOfAirlineAction = '';
    this.dateOfIssue = '';
    this.dateOfIssueRelatedDocument = '';
    this.isoCountryCode = '';
    this.issueReason = '';
    this.passenger = '';
    this.rejectionReason = '';
    this.settlementAuthorisationCode = '';
    this.statisticalCode = '';
    this.status = '';
    this.tourCode = '';
    this.waiverCode = '';
  }
}
