import { NgModule } from '@angular/core';
import { DialogModule } from 'primeng/components/dialog/dialog';

import { SharedModule } from './../../shared.module';
import { ContactModule } from './../contact/contact.module';
import { AirlineComponent } from './airline.component';
import { AirlineService } from './services/airline.service';

@NgModule({
  imports: [SharedModule, ContactModule, DialogModule],
  declarations: [AirlineComponent],
  providers: [AirlineService],
  exports: [AirlineComponent]
})
export class AirlineModule {}
