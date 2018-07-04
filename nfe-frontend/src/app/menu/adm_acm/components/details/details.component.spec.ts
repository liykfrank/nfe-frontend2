import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailsComponent } from './details.component';
import { DetailsService } from '../../services/details.service';
import { AdmAcmService } from '../../services/adm-acm.service';
import { AlertsService } from '../../../../core/services/alerts.service';
import { SharedModule } from '../../../../shared/shared.module';
import { Configuration } from '../../models/configuration.model';
import { Observable } from 'rxjs';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Reason } from '../../models/reason.model';

describe('DetailsComponent', () => {
  let component: DetailsComponent;
  let fixture: ComponentFixture<DetailsComponent>;

  const conf = new Configuration();
  conf.maxNumberOfRelatedDocuments = 1;

  const _DetailsService = jasmine.createSpyObj<DetailsService>('DetailsService',
    [
      'getReasonsOnISO',
      'getReasons',
      'getRelatedTicketDocuments',
      'setDateOfIssueRelatedDocument',
      'setPassenger',
      'setReasonForMemoIssuanceCode',
      'setReasonForMemo',
      'setReasonForMemoIssuanceCode',
      'pushRelatedTicketDocument'
  ]);
  const _AdmAcmService = jasmine.createSpyObj<AdmAcmService>('AdmAcmService', ['getConfiguration']);
  const _AlertsService = jasmine.createSpyObj<AlertsService>('AlertsService', ['setAlertTranslate']);

  _AdmAcmService.getConfiguration.and.returnValue(Observable.of(conf));
  _DetailsService.getReasons.and.returnValue(Observable.of({}));
  _DetailsService.getReasonsOnISO.and.returnValue(Observable.of([]));
  _DetailsService.getRelatedTicketDocuments.and.returnValue(Observable.of([]));

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ SharedModule, BrowserAnimationsModule ],
      providers: [
        {provide: AlertsService, useValue: _AlertsService},
        {provide: DetailsService, useValue: _DetailsService},
        {provide: AdmAcmService, useValue: _AdmAcmService}
      ],
      declarations: [ DetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('onRelatedDocumentChange', () => {
    _DetailsService.setDateOfIssueRelatedDocument.calls.reset();
    _DetailsService.setDateOfIssueRelatedDocument.and.returnValue(Observable.of({}));

    component.relatedDocument = new Date('01/01/2018');
    component.onRelatedDocumentChange();
    expect(_DetailsService.setDateOfIssueRelatedDocument.calls.count()).toBe(1, 'setDateOfIssueRelatedDocument.calls');
  });

  it('onPasengerChange', () => {
    _DetailsService.setPassenger.calls.reset();
    _DetailsService.setPassenger.and.returnValue(Observable.of({}));

    component.passenger = 'AAA';
    component.onPasengerChange();
    expect(_DetailsService.setPassenger.calls.count()).toBe(1, 'setPassenger.calls');
  });

  it('onReasonForMemoIssuanceCodeChange', () => {
    _DetailsService.setReasonForMemoIssuanceCode.calls.reset();
    _DetailsService.setReasonForMemoIssuanceCode.and.returnValue(Observable.of({}));

    component.reasonForMemoIssuanceCode = 'AAA';
    component.onReasonForMemoIssuanceCodeChange();
    expect(_DetailsService.setReasonForMemoIssuanceCode.calls.count()).toBe(1, 'setReasonForMemoIssuanceCode.calls');
  });

  it('onReasonForMemoChange', () => {
    _DetailsService.setReasonForMemo.calls.reset();
    _DetailsService.setReasonForMemo.and.returnValue(Observable.of({}));

    component.reasonForMemo = 'AAA';
    component.onReasonForMemoChange();
    expect(_DetailsService.setReasonForMemo.calls.count()).toBe(1, 'setReasonForMemo.calls');
  });

  it('checkNumbers', () => {
    component.documentNumber = '1a2A';
    component.checkNumbers();
    expect(component.documentNumber).toBe('12');
  });

  it('topTenSelect', () => {
    _DetailsService.setReasonForMemo.calls.reset();
    _DetailsService.setReasonForMemo.and.returnValue(Observable.of({}));

    const reason = new Reason();
    reason.detail = 'Prueba1';
    reason.id = 0;
    reason.isoCountryCode = 'AAA';
    reason.title = 'Prueba1 Tit';
    component.elemSelect = reason;
    component.topTenSelect();
    expect(_DetailsService.setReasonForMemo.calls.count()).toBe(1, 'setReasonForMemo.calls');
    expect(component.reasonForMemo == reason.detail).toBe(true);
  });

  it('addDocument, add 1', () => {
    _DetailsService.pushRelatedTicketDocument.calls.reset();
    _DetailsService.pushRelatedTicketDocument.and.returnValue(Observable.of({}));

    component.documentNumber = '1111111111111';
    component.addDocument();
    expect(_DetailsService.pushRelatedTicketDocument.calls.count()).toBe(1, 'pushRelatedTicketDocument.calls');
  });

});
