import { log } from 'util';
import { NgModel } from '@angular/forms/src/directives';
import { Component, Injector, OnInit, ViewChild } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { jqxDropDownListComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxdropdownlist';
import { jqxTextAreaComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxtextarea';

import { NwAbstractComponent } from '../../../../shared/base/abstract-component';
import { TicketDocument } from '../../models/ticket-document.model';
import { Configuration } from './../../models/configuration.model';
import { AdmAcmService } from './../../services/adm-acm.service';
import { DetailsService } from './../../services/details.service';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss']
})
export class DetailsComponent extends NwAbstractComponent implements OnInit {
  @ViewChild('textArea') textArea: jqxTextAreaComponent;
  @ViewChild('documentNumberInput') documentNumberInput;
  @ViewChild('digit') digit: jqxDropDownListComponent;

  public documentsList: string[] = [];
  public documentNumber = '';
  private conf: Configuration;

  topTenList = ['a', 'b'];
  checkDigitList: string[];
  maxLengthDocumentNumber = 13;
  detailsForm: FormGroup;
  checkDigit: number;
  checkDigitIsValid = false;
  selectedCheckDigit: String;

  constructor(
    private _Injector: Injector,
    private _DetailsService: DetailsService,
    private _AdmAcmService: AdmAcmService
  ) {
    super(_Injector);
    this._AdmAcmService
      .getConfiguration()
      .subscribe(data => (this.conf = data));

    this.checkDigitIsValid = false;
  }

  ngOnInit() {
    this.checkDigitList = this._DetailsService.getCheckDigitList();
  }

  source = (query, response) => {
    let item = query.split(/,\s*/).pop();
    // update the search query.
    this.textArea.query(item);
  };

  addDocument() {
    if (this.checkDigitIsValid) {
      this.documentsList.push(
        this.documentNumber + this.digit.getSelectedItem().value
      );

      const ticket: TicketDocument = {
        checkDigit: Number(this.digit.getSelectedItem().value),
        relatedTicketDocumentNumber: this.documentNumber
      };
      this._DetailsService.pushRelatedTicketDocument(ticket);
      this.documentNumber = '';
      this.digit.setContent('');
    }
  }

  onCheckDigitSelected(event: any) {
    this.checkCheckDigit();
  }

  onDocumentNumberInput(event: any) {
    // Blotched job to avoid inconsitent behaviours with the directive appNumbersOnly
    this.documentNumber = event.target.value;

    this.checkCheckDigit();
  }

  checkCheckDigit(): void {
    const selectedCheckDigit = Number(this.digit.getSelectedItem().value);
    const moduleResult = +this.documentNumber % 7;

    this.checkDigitIsValid = moduleResult === selectedCheckDigit;
    this.selectedCheckDigit = this.digit.getSelectedItem().value;
  }

  deleteElem(item) {
    console.log('on deleteElem');
    console.log(item);
    this.documentsList.splice(item, 1);
  }

  generateSource(): string[] {
    let quotes: string[] = [];
    quotes.push(
      'Life is a dream for the wise, a game for the fool, a comedy for the rich, a tragedy for the poor.'
    );
    quotes.push(
      'Yesterday is not ours to recover, but tomorrow is ours to win or lose.'
    );
    quotes.push(
      'It does not matter how slowly you go as long as you do not stop.'
    );
    quotes.push(
      'Success depends upon previous preparation, and without such preparation there is sure to be failure.'
    );
    quotes.push('Better a diamond with a flaw than a pebble without.');
    quotes.push(
      'To succeed in life, you need two things: ignorance and confidence.'
    );
    quotes.push(
      'A successful man is one who can lay a firm foundation with the bricks others have thrown at him.'
    );
    quotes.push('Sleep is the best meditation.');

    return quotes;
  }
}
