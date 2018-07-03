

import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LocalizationModule, TranslationModule } from 'angular-l10n';

import { jqxCalendarComponent }       from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxcalendar';
import { jqxCheckBoxComponent }       from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxcheckbox';
import { jqxDateTimeInputComponent }  from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxdatetimeinput';
import { jqxDropDownListComponent }   from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxdropdownlist';
import { jqxGridComponent }           from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxgrid';
import { jqxInputComponent }          from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxinput';
import { jqxMenuComponent }           from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxmenu';
import { jqxNotificationComponent }   from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxnotification';
import { jqxNumberInputComponent }    from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxnumberinput';
import { jqxProgressBarComponent }    from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxprogressbar';
import { jqxRadioButtonComponent }    from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxradiobutton';
import { jqxTabsComponent }           from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxtabs';
import { jqxDataTableComponent }      from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxdatatable';
import { jqxTextAreaComponent }       from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxtextarea';
import { jqxButtonComponent }         from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxbuttons';
import { jqxFileUploadComponent }     from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxfileupload';
import { jqxExpanderComponent }       from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxexpander';
import { jqxPopoverComponent }        from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxpopover';
import { jqxButtonGroupComponent }    from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxbuttongroup';
import { jqxDockingComponent }        from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxdocking';

import { CodeHighlighterModule } from 'primeng/codehighlighter';
import { GrowlModule } from 'primeng/growl';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { DialogModule } from 'primeng/dialog';
import { DropdownModule } from 'primeng/dropdown';
import { FileUploadModule } from 'primeng/fileupload';

import { l10nConfig } from './base/conf/l10n.config';
import { ButtonsComponent } from './components/buttons/buttons.component';
import { CalendarComponent } from './components/calendar/calendar.component';
import { CheckboxComponent } from './components/checkbox/checkbox.component';
import { FilterCrumbsComponent } from './components/filter-crumbs/filter-crumbs.component';
import { GridComponent } from './components/grid/grid.component';
import { InputComponent } from './components/input/input.component';
import { JqxNwComboComponent } from './components/jqx-nw-combo/jqx-nw-combo.component';
import { ListComponent } from './components/list/list.component';
import { NumberComponent } from './components/number/number.component';
import { SafeUrlPipe } from './components/pipes/safe-url.pipe';
import { ProgressBarComponent } from './components/progress-bar/progress-bar.component';
import { TablePaginationComponent } from './components/table-pagination/table-pagination.component';
import { UtilsService } from './services/utils.service';

import { DivCollapsableComponent } from './components/div-collapsable/div-collapsable.component';
import { NumbersOnlyDirective } from './directives/numbers-only.directive';


@NgModule({
  imports: [
    HttpClientModule,
    CommonModule,
    FormsModule,
    LocalizationModule,
    TranslationModule.forRoot(l10nConfig),
    FormsModule,
    FileUploadModule,
    InputTextModule,
    PasswordModule,
    ScrollPanelModule,
    DropdownModule,
    GrowlModule,
    CodeHighlighterModule,
    ScrollPanelModule,
    DialogModule
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
    jqxProgressBarComponent,
    jqxButtonComponent,
    jqxDropDownListComponent,
    GridComponent,
    CheckboxComponent,
    InputComponent,
    NumberComponent,
    CalendarComponent,
    ProgressBarComponent,
    ButtonsComponent,
    ListComponent,
    FilterCrumbsComponent,
    JqxNwComboComponent,
    jqxNotificationComponent,
    jqxDockingComponent,
    TablePaginationComponent,
    SafeUrlPipe,
    jqxDataTableComponent,
    jqxTextAreaComponent,
    DivCollapsableComponent,
    jqxExpanderComponent,
    jqxPopoverComponent,
    jqxButtonGroupComponent,
    NumbersOnlyDirective,
    jqxDockingComponent
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
    InputComponent,
    NumberComponent,
    CalendarComponent,
    ProgressBarComponent,
    jqxButtonComponent,
    ButtonsComponent,
    ListComponent,
    FilterCrumbsComponent,
    FileUploadModule,
    InputTextModule,
    PasswordModule,
    ScrollPanelModule,
    DropdownModule,
    JqxNwComboComponent,
    jqxNotificationComponent,
    jqxDockingComponent,
    GrowlModule,
    CodeHighlighterModule,
    TablePaginationComponent,
    SafeUrlPipe,
    jqxDataTableComponent,
    jqxTextAreaComponent,
    DivCollapsableComponent,
    jqxExpanderComponent,
    jqxPopoverComponent,
    jqxButtonGroupComponent,
    NumbersOnlyDirective,
    ScrollPanelModule,
    DialogModule,
    jqxDockingComponent
  ],
  providers: [
    UtilsService
  ]
})
export class SharedModule {}
