import { AmountRefundFormModel } from './../../models/amount-refund-form.model';
import { ReactiveFormHandler } from '../../../../shared/base/reactive-form-handler';
import { Component, EventEmitter, OnDestroy, Output, Input } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { RefundConfiguration } from '../../models/refund-configuration.model';
import { RefundConfigurationService } from '../../services/refund-configuration.service';

@Component({
  selector: 'bspl-amount-refund',
  templateUrl: './amount-refund.component.html',
  styleUrls: ['./amount-refund.component.scss']
})
export class AmountRefundComponent extends ReactiveFormHandler implements OnDestroy {

  private refundConfiguration: RefundConfiguration;
  private amountRefundFormModel: AmountRefundFormModel = new AmountRefundFormModel();

  @Output() returnAmount = new EventEmitter();

  constructor(private refundConfigurationService: RefundConfigurationService) {
    super();
    this.refundConfigurationService.getConfiguration().subscribe(config => this.refundConfiguration = config );
   }


   /* TODO: este método tiene que implementar toda la lógica del ckeck partial refund
    hay que comprobar si los cupones del objeto releatedDocument de la vista details están todos seleccionados,

    si estan seleccionados el checked debe ser false
    si es false y estan todos los cupones seleccionados y se pone a true hay que mostrar error

   */
   getPartialRefundConfig() {
    return true;
   }

   getNetRemitConfig() {
    return false;
   }



}
