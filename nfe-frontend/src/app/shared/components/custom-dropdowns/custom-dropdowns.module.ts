import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TranslationModule } from 'angular-l10n';
import { ScrollPanelModule } from 'primeng/scrollpanel';

import { DropdownSingleComponent } from './dropdown-single/dropdown-single.component';
import { DropdownMultiComponent } from './dropdown-multi/dropdown-multi.component';

@NgModule({
    imports: [
        CommonModule,
        ScrollPanelModule,
        FormsModule,
        TranslationModule
    ],
    providers: [],
    declarations: [
        DropdownSingleComponent,
        DropdownMultiComponent
    ],
    exports: [
        DropdownSingleComponent,
        DropdownMultiComponent
    ]
})
export class CustomDropdownsModule { }
