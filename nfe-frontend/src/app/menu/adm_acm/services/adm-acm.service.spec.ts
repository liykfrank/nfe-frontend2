import { TestBed, inject } from '@angular/core/testing';

import { AdmAcmService } from './adm-acm.service';
import { ConfigurationService } from './resources/configuration.service';
import { AcdmsService } from './resources/acdms.service';
import { AmountService } from './amount.service';
import { BasicInfoService } from './basic-info.service';
import { DetailsService } from './details.service';
import { Observable } from 'rxjs/Observable';
import { ScreenType } from '../../../shared/models/screen-type.enum';
import { CommentsService } from './comments.service';
import { CommentService } from './resources/comment.service';
import { FileService } from './resources/file.service';
import { FilesService } from './files.service';
import { AlertsService } from '../../../core/services/alerts.service';
import { Acdm } from '../models/acdm.model';
import { CurrencyServer } from '../models/currency-server.model';

describe('AdmAcmService', () => {

  const _ConfigurationService =
    jasmine.createSpyObj<ConfigurationService>('ConfigurationService', ['get', 'getWithISO', 'getConfiguration']);

  const _AcdmsService = jasmine.createSpyObj<AcdmsService>('AcdmsService', ['postAcdm']);
  const _BasicInfoService = jasmine.createSpyObj<BasicInfoService>('BasicInfoService', ['copyFromAdcm', 'copyToAdcm', 'checkBasicInfo']);
  const _AmountService = jasmine.createSpyObj<AmountService>('AmountService', ['copyFromAdcm', 'copyToAdcm', 'checkTaxes', 'checkTotal']);
  const _DetailsService = jasmine.createSpyObj<DetailsService>('DetailsService', ['copyFromAdcm', 'copyToAdcm']);

  const _CommentsService = jasmine.createSpyObj<CommentsService>('CommentsService', ['get', 'getCommentsToUpload']);
  const _CommentService = jasmine.createSpyObj<CommentService>('CommentService', ['get']);

  const _FilesService = jasmine.createSpyObj<FilesService>('FilesService', ['copyToAdcm', 'getFilesToUpload']);
  const _FileService = jasmine.createSpyObj<FileService>('FileService', ['copyToAdcm']);

  const _AlertsService = jasmine.createSpyObj<AlertsService>('AlertsService', ['setAlertTranslate', 'setAlert']);

  _FilesService.getFilesToUpload.and.returnValue(Observable.of([]));
  _CommentsService.getCommentsToUpload.and.returnValue(Observable.of([]));
  _ConfigurationService.getConfiguration.and.returnValue(Observable.of({}));


  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AdmAcmService,
        {provide: ConfigurationService, useValue: _ConfigurationService},
        {provide: AcdmsService, useValue: _AcdmsService},
        {provide: AmountService, useValue: _AmountService},
        {provide: BasicInfoService, useValue: _BasicInfoService},
        {provide: DetailsService, useValue: _DetailsService},
        {provide: CommentsService, useValue: _CommentsService},
        {provide: CommentService, useValue: _CommentService},
        {provide: FileService, useValue: _FileService},
        {provide: FilesService, useValue: _FilesService},
        {provide: AlertsService, useValue: _AlertsService}
      ]
    });
  });

  it('should be created', inject([AdmAcmService], (service: AdmAcmService) => {
    expect(service).toBeTruthy();
  }));

  it('getConfiguration', inject([AdmAcmService], (service: AdmAcmService) => {
    expect(service.getConfiguration()).toBeTruthy();
  }));

  it('getDecimals', inject([AdmAcmService], (service: AdmAcmService) => {
    expect(service.getDecimals()).toBeTruthy();
  }));

  it('getSpan', inject([AdmAcmService], (service: AdmAcmService) => {
    expect(service.getSpan()).toBeTruthy();
  }));

  it('getSubtype', inject([AdmAcmService], (service: AdmAcmService) => {
    expect(service.getSubtype()).toBeTruthy();
  }));

  it('getSpdr', inject([AdmAcmService], (service: AdmAcmService) => {
    expect(service.getSpdr()).toBeTruthy();
  }));

  it('getScreenType', inject([AdmAcmService], (service: AdmAcmService) => {
    expect(service.getScreenType()).toBeTruthy();
  }));

  it('getDateOfIssue', inject([AdmAcmService], (service: AdmAcmService) => {
    expect(service.getDateOfIssue()).toBeTruthy();
  }));

  it('setDecimals', inject([AdmAcmService], (service: AdmAcmService) => {
    let observe;
    service.getDecimals().subscribe(data => observe = data);
    const decimal = 1;
    service.setDecimals(decimal);
    expect(observe == decimal).toBe(true);
  }));

  it('setSpan', inject([AdmAcmService], (service: AdmAcmService) => {
    let observe;
    service.getSpan().subscribe(data => observe = data);
    expect(observe).toBe(false);
    service.setSpan(true);
    expect(observe).toBe(true);
  }));

  it('setSubtype', inject([AdmAcmService], (service: AdmAcmService) => {
    let observe;
    service.getSubtype().subscribe(data => observe = data);
    const subtype = 'ABC';
    service.setSubtype(subtype);
    expect(observe == subtype).toBe(true);
  }));

  it('setSpdr', inject([AdmAcmService], (service: AdmAcmService) => {
    let observe;
    service.getSpdr().subscribe(data => observe = data);
    const spdr = 'ABC';
    service.setSpdr(spdr);
    expect(observe == spdr).toBe(true);
  }));

  it('setScreenType', inject([AdmAcmService], (service: AdmAcmService) => {
    let observe;
    service.getScreenType().subscribe(data => observe = data);
    const screenType = ScreenType.CREATE;
    service.setScreenType(screenType);
    expect(observe == screenType).toBe(true);
  }));

  it('setDateOfIssue', inject([AdmAcmService], (service: AdmAcmService) => {
    const date = new Date();
    service.setDateOfIssue(date);
    expect(date.toISOString() == service.getDateOfIssue()).toBe(true);
  }));

  it('findCountryConfiguration', inject([AdmAcmService], (service: AdmAcmService) => {
    _ConfigurationService.getWithISO.and.returnValue(Observable.of({}));

    service.findCountryConfiguration('ABC');
    expect(_ConfigurationService.getWithISO.calls.count()).toBe(1);
  }));

  it('issueACDM error on issue', inject([AdmAcmService], (service: AdmAcmService) => {
    _BasicInfoService.checkBasicInfo.calls.reset();
    _BasicInfoService.checkBasicInfo.and.returnValue(['TEST']);

    _AlertsService.setAlertTranslate.calls.reset();
    _AlertsService.setAlertTranslate.and.returnValue(Observable.of({}));

    service.issueACDM();
    expect(_BasicInfoService.checkBasicInfo.calls.count()).toBe(1);
    expect(_AlertsService.setAlertTranslate.calls.count()).toBe(1);
  }));

  it('issueACDM error on checkTaxes', inject([AdmAcmService], (service: AdmAcmService) => {
    _BasicInfoService.checkBasicInfo.calls.reset();
    _BasicInfoService.checkBasicInfo.and.returnValue([]);

    _AmountService.checkTaxes.calls.reset();
    _AmountService.checkTaxes.and.returnValue(false);

    _AlertsService.setAlertTranslate.calls.reset();
    _AlertsService.setAlertTranslate.and.returnValue(Observable.of({}));

    service.issueACDM();
    expect(_BasicInfoService.checkBasicInfo.calls.count()).toBe(1, 'checkBasicInfo ERROR');
    expect(_AmountService.checkTaxes.calls.count()).toBe(1, 'checkTaxes ERROR');
    expect(_AlertsService.setAlertTranslate.calls.count()).toBe(1, 'setAlertTranslate ERROR');
  }));

  it('issueACDM error on checkTotal', inject([AdmAcmService], (service: AdmAcmService) => {
    _BasicInfoService.checkBasicInfo.calls.reset();
    _BasicInfoService.checkBasicInfo.and.returnValue([]);

    _AmountService.checkTaxes.calls.reset();
    _AmountService.checkTaxes.and.returnValue(true);

    _AmountService.checkTotal.calls.reset();
    _AmountService.checkTotal.and.returnValue(false);

    _AlertsService.setAlertTranslate.calls.reset();
    _AlertsService.setAlertTranslate.and.returnValue(Observable.of({}));

    service.issueACDM();
    expect(_BasicInfoService.checkBasicInfo.calls.count()).toBe(1, 'checkBasicInfo ERROR');
    expect(_AmountService.checkTaxes.calls.count()).toBe(1, 'checkTaxes ERROR');
    expect(_AmountService.checkTotal.calls.count()).toBe(1, 'checkTotal ERROR');
    expect(_AlertsService.setAlertTranslate.calls.count()).toBe(1, 'setAlertTranslate ERROR');
  }));

  it('issueACDM no regularized OK', inject([AdmAcmService], (service: AdmAcmService) => {
    const acdm = new Acdm();
    acdm.isoCountryCode = 'AA';
    acdm.currency = new CurrencyServer();
    acdm.currency.decimals = 2;
    acdm.netReporting = false;
    acdm.transactionCode = 'ABC';
    acdm.concernsIndicator = 'A';

    _BasicInfoService.checkBasicInfo.calls.reset();
    _BasicInfoService.copyToAdcm.calls.reset();
    _BasicInfoService.checkBasicInfo.and.returnValue([]);
    _BasicInfoService.copyToAdcm.and.returnValue(acdm);

    _AmountService.checkTaxes.calls.reset();
    _AmountService.copyToAdcm.calls.reset();
    _AmountService.checkTaxes.and.returnValue(true);
    _AmountService.copyToAdcm.and.returnValue(acdm);

    _AmountService.checkTotal.calls.reset();
    _AmountService.checkTotal.and.returnValue(true);

    _DetailsService.copyToAdcm.calls.reset();
    _DetailsService.copyToAdcm.and.returnValue(acdm);

    _FilesService.copyToAdcm.calls.reset();
    _FilesService.copyToAdcm.and.returnValue(acdm);

    _AcdmsService.postAcdm.calls.reset();
    _AcdmsService.postAcdm.and.returnValue(Observable.of(acdm));

    _AlertsService.setAlert.calls.reset();
    _AlertsService.setAlert.and.returnValue(Observable.of({}));

    service.issueACDM();

    expect(_BasicInfoService.checkBasicInfo.calls.count()).toBe(1, 'checkBasicInfo ERROR');
    expect(_AmountService.checkTaxes.calls.count()).toBe(1, 'checkTaxes ERROR');
    expect(_AmountService.checkTotal.calls.count()).toBe(1, 'checkTotal ERROR');

    expect (_BasicInfoService.copyToAdcm.calls.count()).toBe(1, 'checkTotal ERROR');
    expect (_AmountService.copyToAdcm.calls.count()).toBe(1, 'checkTotal ERROR');
    expect (_DetailsService.copyToAdcm.calls.count()).toBe(1, 'checkTotal ERROR');
    expect (_FilesService.copyToAdcm.calls.count()).toBe(1, 'checkTotal ERROR');

    expect (_AcdmsService.postAcdm.calls.count()).toBe(1, 'checkTotal ERROR');
    expect (_AlertsService.setAlert.calls.count()).toBe(1, 'checkTotal ERROR');
  }));

  it('checkRegularized', inject([AdmAcmService], (service: AdmAcmService) => {
    const error: any = {};
    error.validationErrors = [];

    expect(service.checkRegularized(error)).toBe(false);

    error.validationErrors.push({fieldName : 'regularized'});
    expect(service.checkRegularized(error)).toBe(true);
  }));

  it('sendRegularized', inject([AdmAcmService], (service: AdmAcmService) => {
    const acdm = new Acdm();
    acdm.isoCountryCode = 'AA';
    acdm.currency = new CurrencyServer();
    acdm.currency.decimals = 2;
    acdm.netReporting = false;
    acdm.transactionCode = 'ABC';
    acdm.concernsIndicator = 'A';

    _BasicInfoService.checkBasicInfo.calls.reset();
    _BasicInfoService.copyToAdcm.calls.reset();
    _BasicInfoService.checkBasicInfo.and.returnValue([]);
    _BasicInfoService.copyToAdcm.and.returnValue(acdm);

    _AmountService.checkTaxes.calls.reset();
    _AmountService.copyToAdcm.calls.reset();
    _AmountService.checkTaxes.and.returnValue(true);
    _AmountService.copyToAdcm.and.returnValue(acdm);

    _AmountService.checkTotal.calls.reset();
    _AmountService.checkTotal.and.returnValue(true);

    _DetailsService.copyToAdcm.calls.reset();
    _DetailsService.copyToAdcm.and.returnValue(acdm);

    _FilesService.copyToAdcm.calls.reset();
    _FilesService.copyToAdcm.and.returnValue(acdm);

    _AcdmsService.postAcdm.calls.reset();
    _AcdmsService.postAcdm.and.returnValue(Observable.of(acdm));

    _AlertsService.setAlert.calls.reset();
    _AlertsService.setAlert.and.returnValue(Observable.of({}));

    service.sendRegularized();

    expect(_BasicInfoService.checkBasicInfo.calls.count()).toBe(1, 'checkBasicInfo ERROR');
    expect(_AmountService.checkTaxes.calls.count()).toBe(1, 'checkTaxes ERROR');
    expect(_AmountService.checkTotal.calls.count()).toBe(1, 'checkTotal ERROR');

    expect (_BasicInfoService.copyToAdcm.calls.count()).toBe(1, 'checkTotal ERROR');
    expect (_AmountService.copyToAdcm.calls.count()).toBe(1, 'checkTotal ERROR');
    expect (_DetailsService.copyToAdcm.calls.count()).toBe(1, 'checkTotal ERROR');
    expect (_FilesService.copyToAdcm.calls.count()).toBe(1, 'checkTotal ERROR');

    expect (_AcdmsService.postAcdm.calls.count()).toBe(1, 'checkTotal ERROR');
    expect (_AlertsService.setAlert.calls.count()).toBe(1, 'checkTotal ERROR');
  }));

  it('getErrors', inject([AdmAcmService], (service: AdmAcmService) => {
    expect(service.getErrors()).toBeTruthy();
  }));

  it('setErrors', inject([AdmAcmService], (service: AdmAcmService) => {
    let elems;
    service.getErrors().subscribe(data => elems = data);

    service.setErrors(['TEST']);
    expect(elems[0] == 'TEST').toBe(true);
  }));

});
