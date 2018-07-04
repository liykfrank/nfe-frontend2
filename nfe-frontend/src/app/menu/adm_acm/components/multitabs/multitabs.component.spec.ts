import { AcdmsService } from './../../services/resources/acdms.service';
import { AlertsService } from './../../../../core/services/alerts.service';
import { ConfigurationService } from './../../services/resources/configuration.service';
import { AdmAcmService } from './../../services/adm-acm.service';
import { FilesService } from './../../services/files.service';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MultitabsComponent } from './multitabs.component';
import { SharedModule } from '../../../../shared/shared.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DetailsService } from '../../services/details.service';
import { DocumentsComponent } from '../documents/documents.component';
import { FilesComponent } from '../files/files.component';
import { HistoryComponent } from '../history/history.component';
import { CommentsComponent } from '../comments/comments.component';
import { Observable } from 'rxjs';
import { CommentService } from '../../services/resources/comment.service';
import { CommentsService } from '../../services/comments.service';
import { FileService } from '../../services/resources/file.service';
import { AmountService } from '../../services/amount.service';
import { BasicInfoService } from '../../services/basic-info.service';
import { CompanyService } from '../../services/resources/company.service';
import { AgentService } from '../../services/resources/agent.service';
import { CountryService } from '../../services/resources/country.service';
import { TocaService } from '../../services/resources/toca.service';
import { CurrencyService } from '../../services/resources/currency.service';
import { PeriodService } from '../../services/resources/period.service';
import { ScreenType } from '../../../../shared/models/screen-type.enum';

describe('MultitabsComponent', () => {
  let component: MultitabsComponent;
  let fixture: ComponentFixture<MultitabsComponent>;

  const _DetailsService = jasmine.createSpyObj<DetailsService>('DetailsService', ['getRelatedTicketDocuments']);
  _DetailsService.getRelatedTicketDocuments.and.returnValue(Observable.of([{}, {}]));

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ SharedModule, BrowserAnimationsModule ],
      providers: [
        {provide: DetailsService, useValue: _DetailsService},
        FilesService,
        ConfigurationService,
        AdmAcmService,
        AmountService,
        BasicInfoService,
        AcdmsService,
        CommentService,
        CommentsService,
        FileService,
        CompanyService,
        AgentService,
        CountryService,
        TocaService,
        CurrencyService,
        PeriodService,
        AlertsService
      ],
      declarations: [
        HistoryComponent,
        DocumentsComponent,
        FilesComponent,
        CommentsComponent,
        MultitabsComponent
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MultitabsComponent);
    component = fixture.componentInstance;
    component.modelView = ScreenType.CREATE;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('selectTab', () => {
    component.selectTab(3, true);
    expect(3 == component.tab_selected).toBeTruthy();
  });

});
