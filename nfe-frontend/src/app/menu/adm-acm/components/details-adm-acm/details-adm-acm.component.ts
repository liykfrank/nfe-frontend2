import { Component, OnInit, Input } from '@angular/core';
import { FormArray, FormGroup, FormControl } from '@angular/forms';

import { EnvironmentType } from '../../../../shared/enums/environment-type.enum';
import { AcdmDetailsForm } from '../../models/acdm-details-form.model';
import { AdmAcmConfiguration } from '../../models/adm-acm-configuration.model';
import { AcdmConfigurationService } from '../../services/acdm-configuration.service';
import { ReactiveFormHandler } from './../../../../shared/base/reactive-form-handler';
import { GLOBALS } from '../../../../shared/constants/globals';

@Component({
  selector: 'bspl-details-adm-acm',
  templateUrl: './details-adm-acm.component.html',
  styleUrls: ['./details-adm-acm.component.scss']
})
export class DetailsAdmAcmComponent extends ReactiveFormHandler<AcdmDetailsForm>
  implements OnInit {

  acdmDetailsForm: FormGroup;

  type = EnvironmentType.ACDM;
  PT_PASSENGER = GLOBALS.HTML_PATTERN.PASSSENGER;
  PT_NUMERIC = GLOBALS.HTML_PATTERN.NUMERIC;
  PT_ALPHANUMERIC_UPPERCASE = GLOBALS.HTML_PATTERN.ALPHANUMERIC_UPPERCASE;

  configuration: AdmAcmConfiguration;

  constructor(private _admAcmConfigurationService: AcdmConfigurationService) {
    super();
  }

  ngOnInit() {
    this.acdmDetailsForm = this.model.acdmDetailsFormGroup;

    this.subscriptions.push(
      this._admAcmConfigurationService.getConfiguration().subscribe(data => {
        this.configuration = data;
        this.acdmDetailsForm.reset();
      })
    );
  }

  buttonRelatedTickedDocumentDisabled() {
    const str = this.model.ticketDocumentNumberToAdd.value;
    const nrArrays = this.model.relatedTicketDocuments.controls.length;

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
    const val = this.model.ticketDocumentNumberToAdd.value;
    const idx = this.model.relatedTicketDocuments.controls.findIndex(x => x.get('relatedTicketDocumentNumber').value == val);

    if (idx < 0) {
      this.model.addTicketDocument(val);
    }
    this.model.ticketDocumentNumberToAdd.reset();
  }

  onCkickTrashIcon(pos: number) {
    this.model.removeTicketDocument(pos);
  }

  onSelectorChange(event) {
    const val = this.acdmDetailsForm.get('reasonForMemo');
    val.setValue((val.value ? val.value : '') + '\n' + event);
  }

  showError() {
    const err = this.acdmDetailsForm.get('reasonForMemo');
    return (err.dirty || err.touched) && err.errors;
  }

}
