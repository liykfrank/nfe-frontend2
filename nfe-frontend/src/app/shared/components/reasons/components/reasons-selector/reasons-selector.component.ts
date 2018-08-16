import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';

import { Reason } from '../../models/reason.model';
import { ReasonsService } from '../../services/reasons.service';
import { EnvironmentType } from '../../../../enums/environment-type.enum';

@Component({
  selector: 'bspl-reasons-selector',
  templateUrl: './reasons-selector.component.html',
  styleUrls: ['./reasons-selector.component.scss']
})
export class ReasonsSelectorComponent implements  OnChanges {

  private reasonList: Reason[];
  @Input()
  public  dropdownTitle: string;

  @Input()
  public type: EnvironmentType;

  @Input()
  public isoCode: string;
  
  @Output() changeSelect: EventEmitter<string> = new EventEmitter();


  constructor(private _reasonsService: ReasonsService) { }

  ngOnChanges(changes: SimpleChanges): void {
    if ( changes.isoCode) {
      this._findReasonList();
    }
  }

  private _findReasonList() {
    if (this.isoCode && this.type == EnvironmentType.ACDM) {
      this._reasonsService.getReasonsByAdcms(this.isoCode).subscribe(reasons => {
        this.reasonList = reasons;
      });
    } else if ( this.isoCode  && this.type == EnvironmentType.REFUND_INDIRECT) {
      this._reasonsService.getReasonsByIndirectRefund(this.isoCode).subscribe(reasons => {
          this.reasonList = reasons;
        });
    }
  }

  onChangeSelector(value) {
      this.changeSelect.emit(value);
  }






}
