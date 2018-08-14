import { Component, OnInit } from '@angular/core';
import { FormArray } from '@angular/forms';

import { EnvironmentType } from '../../../../shared/enums/environment-type.enum';
import { AcdmDetailsForm } from '../../models/acdm-details-form.model';
import { AdmAcmConfiguration } from '../../models/adm-acm-configuration.model';
import { AcdmConfigurationService } from '../../services/adm-acm-configuration.service';
import { ReactiveFormHandler } from './../../../../shared/base/reactive-form-handler';

@Component({
  selector: 'bspl-details-adm-acm',
  templateUrl: './details-adm-acm.component.html',
  styleUrls: ['./details-adm-acm.component.scss']
})
export class DetailsAdmAcmComponent extends ReactiveFormHandler
  implements OnInit {
  private model = new AcdmDetailsForm();
  acdmDetailsForm = this.model.acdmDetailsFormGroup;

  public type = EnvironmentType.ACDM;
  configuration: AdmAcmConfiguration;

  constructor(private _admAcmConfigurationService: AcdmConfigurationService) {
    super();
    this.subscriptions.push(
      this._admAcmConfigurationService.getConfiguration().subscribe(data => {
        this.configuration = data;
        this.acdmDetailsForm.reset();
      })
    );

    this.subscribe(this.acdmDetailsForm);
  }

  ngOnInit() {}


  buttonRelatedTickedDocumentDisabled() {
    const str = this.acdmDetailsForm.get('ticketDocumentNumberToAdd').value;
    const nrArrays = (this.acdmDetailsForm.get(
      'relatedTicketDocuments'
    ) as FormArray).length;

    if (
      str && str.length == 13 &&
      (nrArrays < this.configuration.maxNumberOfRelatedDocuments ||
        this.configuration.maxNumberOfRelatedDocuments == -1)
    ) {
      return false;
    }

    return true;
  }

  onClickAddRelatedTicketDocument() {
    const val = this.acdmDetailsForm.get('ticketDocumentNumberToAdd').value;
    const idx = this.acdmDetailsForm.get('relatedTicketDocuments').value.findIndex(x => x.relatedTicketDocumentNumber == val);

    if (idx < 0) {
      this.model.addTicketDocument(val);
    }
    this.acdmDetailsForm.get('ticketDocumentNumberToAdd').reset();
  }

  onSelectorChange(event) {
    const val = this.acdmDetailsForm.get('reasonForMemo');
    val.setValue(val.value + '\n' + event);
  }

  showError() {
    const err = this.acdmDetailsForm.get('reasonForMemo');
    return (err.dirty || err.touched) && err.errors;
  }

  private _setCustomError(msg: string) {
    return {
      customError: {
        invalid: true,
        message: msg
      }
    };
  }

}
