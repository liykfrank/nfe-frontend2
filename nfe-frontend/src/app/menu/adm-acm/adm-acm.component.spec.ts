import { CommonModule } from '@angular/common';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslationModule } from 'angular-l10n';
import { MessageService } from 'primeng/components/common/messageservice';
import { Observable } from 'rxjs/Observable';

import { AlertsService } from '../../core/services/alerts.service';
import { DocumentPopUpComponent } from '../../shared/components/multitabs/components/document-pop-up/document-pop-up.component';
import { ResumeBarComponent } from '../../shared/components/resume-bar/resume-bar.component';
import { AgentService } from '../../shared/services/resources/agent.service';
import { CurrencyService } from '../../shared/services/resources/currency.service';
import { ReasonService } from '../../shared/services/resources/reason.service';
import { AdmAcmRoutingModule } from './adm-acm-routing.module';
import { AdmAcmComponent } from './adm-acm.component';
import { AmountComponent } from './components/amount/amount.component';
import { BasicInfoComponent } from './components/basic-info/basic-info.component';
import { DetailsComponent } from './components/details/details.component';
import { AdmAcmService } from './services/adm-acm.service';
import { AmountService } from './services/amount.service';
import { BasicInfoService } from './services/basic-info.service';
import { DetailsService } from './services/details.service';
import { AcdmsService } from './services/resources/acdms.service';
import { ConfigurationService } from './services/resources/configuration.service';
import { CountryService } from './services/resources/country.service';
import { PeriodService } from './services/resources/period.service';
import { TocaService } from './services/resources/toca.service';

xdescribe('AdmAcmComponent', () => {
  let component: AdmAcmComponent;
  let fixture: ComponentFixture<AdmAcmComponent>;

  const _AdmAcmService = jasmine.createSpyObj<AdmAcmService>('AdmAcmService', [
    'issueACDM',
    'getConfiguration'
  ]);
  _AdmAcmService.getConfiguration.and.returnValue(Observable.of({}));

  const _BasicInfoService = jasmine.createSpyObj<BasicInfoService>(
    'BasicInfoService',
    ['getCountries', 'getBasicInfo']
  );
  const _AmountService = jasmine.createSpyObj<AmountService>('AmountService', [
    'getValidTaxes'
  ]);

  const _DetailsService = jasmine.createSpyObj<DetailsService>(
    'DetailsService',
    ['getTicket', 'getReasonsOnISO', 'getReasons', 'setRelatedTicketDocuments']
  );
  _DetailsService.getReasonsOnISO.and.returnValue(Observable.of([]));
  _DetailsService.getReasons.and.returnValue(Observable.of([]));
  _DetailsService.setRelatedTicketDocuments.and.returnValue(Observable.of([]));
  /*
  const _MultitabService = jasmine.createSpyObj<MultitabService>('MultitabService',
    [
      'getShowThisTicket',
      'getTickets',
      'setUrl',
      'getScreenType',
      'setScreenType',
      'setId',
      'pushTicket',
      'getFiles',
      'setFiles'
    ]);
  _MultitabService.getShowThisTicket.and.returnValue(Observable.of({}));
  _MultitabService.setScreenType.and.returnValue(Observable.of({}));
  _MultitabService.getScreenType.and.returnValue(Observable.of(ScreenType.CREATE));
  _MultitabService.getTickets.and.returnValue(Observable.of([]));
  _MultitabService.getFiles.and.returnValue(Observable.of([]));

  const _CommentsService = jasmine.createSpyObj<CommentsService>('CommentsService', ['getComments']);
  _CommentsService.getComments.and.returnValue(Observable.of([]));
*/
  const _AcdmsService = jasmine.createSpyObj<AcdmsService>('AcdmsService', [
    'get'
  ]);
  const _AgentService = jasmine.createSpyObj<AgentService>('AgentService', [
    'get'
  ]);
  const _ConfigurationService = jasmine.createSpyObj<ConfigurationService>(
    'ConfigurationService',
    ['get']
  );
  const _CountryService = jasmine.createSpyObj<CountryService>(
    'CountryService',
    ['get']
  );
  const _CurrencyService = jasmine.createSpyObj<CurrencyService>(
    'CurrencyService',
    ['get']
  );
  const _PeriodService = jasmine.createSpyObj<PeriodService>('PeriodService', [
    'get'
  ]);
  const _ReasonService = jasmine.createSpyObj<ReasonService>('ReasonService', [
    'get'
  ]);
  const _TocaService = jasmine.createSpyObj<TocaService>('TocaService', [
    'get'
  ]);

  const _AlertsService = jasmine.createSpyObj<AlertsService>('AlertsService', [
    'setAlert',
    'setAlertTranslate'
  ]);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        CommonModule,
        AdmAcmRoutingModule,
        TranslationModule
        /* BrowserAnimationsModule */
      ],
      providers: [
        { provide: AdmAcmService, useValue: _AdmAcmService },
        { provide: DetailsService, useValue: _DetailsService },
        { provide: BasicInfoService, useValue: _BasicInfoService },
        { provide: AmountService, useValue: _AmountService },

        { provide: AcdmsService, useValue: _AcdmsService },
        { provide: AgentService, useValue: _AgentService },
        { provide: ConfigurationService, useValue: _ConfigurationService },
        { provide: CountryService, useValue: _CountryService },
        { provide: CurrencyService, useValue: _CurrencyService },
        { provide: PeriodService, useValue: _PeriodService },
        { provide: ReasonService, useValue: _ReasonService },
        { provide: TocaService, useValue: _TocaService },

        { provide: AlertsService, useValue: _AlertsService },
        MessageService
      ],
      declarations: [
        ResumeBarComponent,
        BasicInfoComponent,
        AmountComponent,
        DetailsComponent,
        DocumentPopUpComponent,
        AdmAcmComponent
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdmAcmComponent);
    component = fixture.componentInstance;
    //component.isAdm = true;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
