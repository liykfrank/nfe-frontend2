import { async, ComponentFixture, TestBed, getTestBed } from '@angular/core/testing';
import { Observable } from 'rxjs';

import { DivCollapsableComponent } from '../../../../shared/components/div-collapsable/div-collapsable.component';

import { SharedModule } from '../../../../shared/shared.module';

import { AdmAcmComponent } from './adm-acm.component';
import { MessageService } from 'primeng/components/common/messageservice';
import { ResumeBarComponent } from '../resume-bar/resume-bar.component';

import { BasicInfoComponent } from '../basic-info/basic-info.component';
import { AmountComponent } from '../amount/amount.component';
import { DetailsComponent } from '../details/details.component';
import { MultitabsComponent } from '../multitabs/multitabs.component';
import { HistoryComponent } from '../history/history.component';
import { DocumentsComponent } from '../documents/documents.component';
import { CommentsComponent } from '../comments/comments.component';
import { FilesComponent } from '../files/files.component';
import { DocumentPopUpComponent } from '../document-pop-up/document-pop-up.component';

import { ScreenType } from '../../../../shared/models/screen-type.enum';

import { AmountService } from '../../services/amount.service';
import { BasicInfoService } from '../../services/basic-info.service';
import { CommentsService } from '../../services/comments.service';
import { FilesService } from '../../services/files.service';
import { AdmAcmService } from '../../services/adm-acm.service';
import { DetailsService } from '../../services/details.service';
import { AlertsService } from '../../../../core/services/alerts.service';

import { AcdmsService } from '../../services/resources/acdms.service';
import { CommentService } from '../../services/resources/comment.service';
import { AgentService } from '../../services/resources/agent.service';
import { CompanyService } from '../../services/resources/company.service';
import { ConfigurationService } from '../../services/resources/configuration.service';
import { CountryService } from '../../services/resources/country.service';
import { CurrencyService } from '../../services/resources/currency.service';
import { FileService } from '../../services/resources/file.service';
import { PeriodService } from '../../services/resources/period.service';
import { ReasonService } from '../../services/resources/reason.service';
import { TocaService } from '../../services/resources/toca.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('AdmAcmComponent', () => {
  let component: AdmAcmComponent;
  let fixture: ComponentFixture<AdmAcmComponent>;

  const _AdmAcmService = jasmine.createSpyObj<AdmAcmService>('AdmAcmService',
    ['issueACDM']);
  const _DetailsService = jasmine.createSpyObj<DetailsService>('DetailsService',
    ['getTicket']);
  const _BasicInfoService = jasmine.createSpyObj<BasicInfoService>('BasicInfoService',
    ['getCountries', 'getBasicInfo']);
  const _AmountService = jasmine.createSpyObj<AmountService>('AmountService',
    ['getValidTaxes']);
  const _CommentsService = jasmine.createSpyObj<CommentsService>('CommentsService',
    ['getComments']);
  const _FilesService = jasmine.createSpyObj<FilesService>('FilesService',
    ['getFiles']);

  const _AcdmsService = jasmine.createSpyObj<AcdmsService>('AcdmsService', ['get']);
  const _AgentService = jasmine.createSpyObj<AgentService>('AgentService', ['get']);
  const _CommentService = jasmine.createSpyObj<CommentService>('CommentService', ['get']);
  const _CompanyService = jasmine.createSpyObj<CompanyService>('CompanyService', ['get']);
  const _ConfigurationService = jasmine.createSpyObj<ConfigurationService>('ConfigurationService', ['get']);
  const _CountryService = jasmine.createSpyObj<CountryService>('CountryService', ['get']);
  const _CurrencyService = jasmine.createSpyObj<CurrencyService>('CurrencyService', ['get']);
  const _FileService = jasmine.createSpyObj<FileService>('FileService', ['get']);
  const _PeriodService = jasmine.createSpyObj<PeriodService>('PeriodService', ['get']);
  const _ReasonService = jasmine.createSpyObj<ReasonService>('ReasonService', ['get']);
  const _TocaService = jasmine.createSpyObj<TocaService>('TocaService', ['get']);

  const _AlertsService = jasmine.createSpyObj<AlertsService>('AlertsService', ['setAlertTranslate']);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ SharedModule, BrowserAnimationsModule ],
      providers: [
        {provide: AdmAcmService, useValue: _AdmAcmService},
        {provide: DetailsService, useValue: _DetailsService},
        {provide: BasicInfoService, useValue: _BasicInfoService},
        {provide: AmountService, useValue: _AmountService},
        {provide: CommentsService, useValue: _CommentsService},
        {provide: FilesService, useValue: _FilesService},

        {provide: AcdmsService, useValue: _AcdmsService},
        {provide: AgentService, useValue: _AgentService},
        {provide: CommentService, useValue: _CommentService},
        {provide: CompanyService, useValue: _CompanyService},
        {provide: ConfigurationService, useValue: _ConfigurationService},
        {provide: CountryService, useValue: _CountryService},
        {provide: CurrencyService, useValue: _CurrencyService},
        {provide: FileService, useValue: _FileService},
        {provide: PeriodService, useValue: _PeriodService},
        {provide: ReasonService, useValue: _ReasonService},
        {provide: TocaService, useValue: _TocaService},

        {provide: AlertsService, useValue: _AlertsService}
      ],
      declarations: [
        ResumeBarComponent,
        BasicInfoComponent,
        AmountComponent,
        DetailsComponent,
        MultitabsComponent,
        HistoryComponent,
        DocumentsComponent,
        CommentsComponent,
        FilesComponent,
        DocumentPopUpComponent,
        AdmAcmComponent
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdmAcmComponent);
    component = fixture.componentInstance;
    component.isAdm = true;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('hide', () => {
    component.hide();
    expect(component.isSticky).toBe(true);
    expect(component.display).toBe(false);
  });

  it('clickCollapse', () => {
    component.clickCollapse();
    expect(component.collapseBasicInfo).toBe(component.collapseAll);
    expect(component.collapseAmount).toBe(component.collapseAll);
    expect(component.collapseDetail).toBe(component.collapseAll);
  });

  it('checkAllCollapsed', () => {
    expect(component.checkAllCollapsed()).toBe(false);
    component.clickCollapse();
    expect(component.checkAllCollapsed()).toBe(true);
  });

  it('checkAllCollapsed', () => {
    component.scrollTo('amount');
    fixture.detectChanges();
    expect(window.scrollY).toBeGreaterThan(0);

    component.scrollTo('details');
    fixture.detectChanges();
    expect(window.scrollY).toBeGreaterThan(0);

    component.scrollTo('basicInfo');
    fixture.detectChanges();
    expect(window.scrollY).toBe(0);
  });



});
