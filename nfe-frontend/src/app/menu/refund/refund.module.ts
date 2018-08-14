import { NgModule } from '@angular/core';

import { IssueSharedModule } from '../../shared/issue-shared.module';
import { DecimalsFormatterPipe } from '../../shared/pipes/decimals-formatter.pipe';
import { AmountRefundComponent } from './components/amount-refund/amount-refund.component';
import { BasicInfoRefundComponent } from './components/basic-info-refund/basic-info-refund.component';
import { DetailsRefundComponent } from './components/details-refund/details-refund.component';
import { FormOfPaymentRefundComponent } from './components/form-of-payment-refund/form-of-payment-refund.component';
import { RefundRoutingModule } from './refund-routing.module';
import { RefundComponent } from './refund.component';
import { RefundConfigurationService } from './services/refund-configuration.service';
import { RefundIndirectService } from './services/refund-indirect.service';
import { RefundIsuePermissionService } from './services/refund-isue-permission.service';
import { UtilsService } from '../../shared/services/utils.service';

@NgModule({
  imports: [RefundRoutingModule, IssueSharedModule],
  providers: [
    DecimalsFormatterPipe,
    RefundConfigurationService,
    RefundIsuePermissionService,
    RefundIndirectService,
    UtilsService
  ],
  declarations: [
    RefundComponent,
    FormOfPaymentRefundComponent,
    DetailsRefundComponent,
    AmountRefundComponent,
    BasicInfoRefundComponent
  ],
  exports: [],
  entryComponents: [RefundComponent]
})
export class RefundModule {}
