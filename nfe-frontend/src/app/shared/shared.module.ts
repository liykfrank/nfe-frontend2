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
import { jqxMaskedInputComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxmaskedinput';

import { CodeHighlighterModule } from 'primeng/codehighlighter';
import { GrowlModule } from 'primeng/growl';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { DialogModule } from 'primeng/dialog';
import { DropdownModule } from 'primeng/dropdown';
import { FileUploadModule } from 'primeng/fileupload';
import { CalendarModule } from 'primeng/calendar';

import { l10nConfig } from './base/conf/l10n.config';
import { FilterCrumbsComponent } from './components/filter-crumbs/filter-crumbs.component';
import { JqxNwComboComponent } from './components/jqx-nw-combo/jqx-nw-combo.component';
import { SafeUrlPipe } from './directives/safe-url.pipe';
import { TablePaginationComponent } from './components/table-pagination/table-pagination.component';
import { UtilsService } from './services/utils.service';

import { DivCollapsableComponent } from './components/div-collapsable/div-collapsable.component';
import { ContactComponent } from './components/contact/contact.component';
import { AirlineComponent } from './components/airline/airline.component';
import { AgentComponent } from './components/agent/agent.component';
import { AbsolutePipe } from './directives/absolute.pipe';
import { PatternDirective } from './directives/pattern.directive';
import { ForceWidthDirective } from './directives/force-width.directive';
import { DecimalsFormatterPipe } from './directives/decimals-formatter.pipe';
import { DecimalsFormatterDirectiveDirective } from './directives/decimals-formatter-directive.directive';
// import { InputStatusStyleDirective } from './directives/input-status-style.directive';


@NgModule({
  imports: [
    HttpClientModule,
    CommonModule,
    FormsModule,
    LocalizationModule,
    TranslationModule.forRoot(l10nConfig),
    FileUploadModule,
    CalendarModule,
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
    jqxDockingComponent,
    jqxMaskedInputComponent,
    AgentComponent,
    ContactComponent,
    AirlineComponent,
    AbsolutePipe,
    PatternDirective,
    ForceWidthDirective,
    DecimalsFormatterPipe,
    DecimalsFormatterDirectiveDirective
  ],
  exports: [
    CommonModule,
    FormsModule,
    jqxTabsComponent,
    jqxInputComponent,
    jqxCheckBoxComponent,
    jqxMenuComponent,
    jqxGridComponent,
    TranslationModule,
    LocalizationModule,
    HttpClientModule,
    jqxNumberInputComponent,
    jqxCalendarComponent,
    jqxRadioButtonComponent,
    jqxProgressBarComponent,
    jqxDropDownListComponent,
    jqxDateTimeInputComponent,
    jqxFileUploadComponent,
    jqxButtonComponent,
    FilterCrumbsComponent,
    FileUploadModule,
    CalendarModule,
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
    ScrollPanelModule,
    DialogModule,
    jqxDockingComponent,
    jqxMaskedInputComponent,
    AgentComponent,
    ContactComponent,
    AirlineComponent,
    AbsolutePipe,
    PatternDirective,
    ForceWidthDirective,
    DecimalsFormatterPipe,
    DecimalsFormatterDirectiveDirective
  ],
  providers: [
    UtilsService,
    DecimalsFormatterPipe,
    DecimalsFormatterDirectiveDirective
  ]
})
export class SharedModule {}
