import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LocalizationModule, TranslationModule } from 'angular-l10n';
import { jqxCalendarComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxcalendar';
import { jqxCheckBoxComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxcheckbox';
import { jqxDateTimeInputComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxdatetimeinput';
import { jqxDropDownListComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxdropdownlist';
import { jqxGridComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxgrid';
import { jqxInputComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxinput';
import { jqxMenuComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxmenu';
import { jqxNotificationComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxnotification';
import { jqxNumberInputComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxnumberinput';
import { jqxProgressBarComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxprogressbar';
import { jqxRadioButtonComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxradiobutton';
import { jqxTabsComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxtabs';
import { CodeHighlighterModule } from 'primeng/codehighlighter';
import { GrowlModule } from 'primeng/growl';

import { jqxButtonComponent } from '../../../node_modules/jqwidgets-scripts/jqwidgets-ts/angular_jqxbuttons';
import { jqxFileUploadComponent } from '../../../node_modules/jqwidgets-scripts/jqwidgets-ts/angular_jqxfileupload';
import { DropdownModule } from '../../../node_modules/primeng/dropdown';
import { FileUploadModule } from '../../../node_modules/primeng/fileupload';
import { l10nConfig } from './base/conf/l10n.config';
import { ButtonsComponent } from './components/buttons/buttons.component';
import { CalendarComponent } from './components/calendar/calendar.component';
import { CheckboxComponent } from './components/checkbox/checkbox.component';
import { FilterCrumbsComponent } from './components/filter-crumbs/filter-crumbs.component';
import { GridComponent } from './components/grid/grid.component';
import { InputComponent } from './components/input/input.component';
import { JqxNwComboComponent } from './components/jqx-nw-combo/jqx-nw-combo.component';
import { JqxNwGridComponent } from './components/jqx-nw-grid/jqx-nw-grid.component';
import { ListComponent } from './components/list/list.component';
import { NumberComponent } from './components/number/number.component';
import { SafeUrlPipe } from './components/pipes/safe-url.pipe';
import { ProgressBarComponent } from './components/progress-bar/progress-bar.component';
import { RadiobuttonComponent } from './components/radiobutton/radiobutton.component';
import { TablePaginationComponent } from './components/table-pagination/table-pagination.component';
import { UtilsService } from './services/utils.service';

@NgModule({
  imports: [
    HttpClientModule,
    CommonModule,
    FormsModule,
    LocalizationModule,
    TranslationModule.forRoot(l10nConfig),
    FormsModule,
    FileUploadModule,
    DropdownModule,
    GrowlModule,
    CodeHighlighterModule
  ],
  declarations: [
    jqxFileUploadComponent,
    jqxGridComponent,
    jqxTabsComponent,
    jqxCheckBoxComponent,
    jqxMenuComponent,
    jqxInputComponent,
    jqxNumberInputComponent,
    jqxCalendarComponent,
    jqxRadioButtonComponent,
    jqxDateTimeInputComponent,
    RadiobuttonComponent,
    jqxProgressBarComponent,
    jqxButtonComponent,
    jqxDropDownListComponent,
    GridComponent,
    CheckboxComponent,
    InputComponent,
    NumberComponent,
    CalendarComponent,
    RadiobuttonComponent,
    ProgressBarComponent,
    JqxNwGridComponent,
    ButtonsComponent,
    ListComponent,
    FilterCrumbsComponent,
    JqxNwComboComponent,
    jqxNotificationComponent,
    TablePaginationComponent,
    SafeUrlPipe
  ],
  exports: [
    CommonModule,
    FormsModule,
    GridComponent,
    CheckboxComponent,
    jqxTabsComponent,
    jqxInputComponent,
    jqxCheckBoxComponent,
    jqxMenuComponent,
    jqxGridComponent,
    TranslationModule,
    LocalizationModule,
    HttpClientModule,
    FormsModule,
    jqxNumberInputComponent,
    jqxCalendarComponent,
    jqxRadioButtonComponent,
    jqxProgressBarComponent,
    jqxDropDownListComponent,
    jqxDateTimeInputComponent,
    jqxFileUploadComponent,
    RadiobuttonComponent,
    InputComponent,
    NumberComponent,
    CalendarComponent,
    ProgressBarComponent,
    JqxNwGridComponent,
    jqxButtonComponent,
    ButtonsComponent,
    ListComponent,
    FilterCrumbsComponent,
    FileUploadModule,
    DropdownModule,
    JqxNwComboComponent,
    jqxNotificationComponent,
    GrowlModule,
    CodeHighlighterModule,
    TablePaginationComponent,
    SafeUrlPipe
  ],
  providers: [UtilsService]
})
export class SharedModule {}
