import { NgModule } from '@angular/core';

import { SharedModule } from '../../shared.module';
import { ContactModule } from '../contact/contact.module';
import { AgentComponent } from './agent.component';
import { AgentService } from './services/agent.service';


@NgModule({
  imports: [SharedModule, ContactModule],
  declarations: [AgentComponent],
  providers: [AgentService],
  exports: [AgentComponent]
})
export class AgentModule {}
