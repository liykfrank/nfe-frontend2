import { NgModule } from '@angular/core';

import { SharedModule } from '../../shared.module';
import { ContactComponent } from './contact.component';

@NgModule({
  imports: [SharedModule],
  declarations: [ContactComponent],
  exports: [ContactComponent]
})
export class ContactModule {}
