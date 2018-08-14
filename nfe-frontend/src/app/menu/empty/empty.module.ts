import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { EmptyComponent } from './empty.component';
import { EmptyRoutingModule } from './empty-routing.module';


@NgModule({
  imports: [CommonModule, EmptyRoutingModule],
  declarations: [EmptyComponent],
  entryComponents: [EmptyComponent],
})

export class EmptyModule {}
