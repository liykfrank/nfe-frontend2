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

describe('AdmAcmService', () => {

  const _ConfigurationService =
    jasmine.createSpyObj<ConfigurationService>('ConfigurationService', ['get', 'getWithISO', 'getConfiguration']);
  const _AcdmsService = jasmine.createSpyObj<AcdmsService>('AcdmsService', ['get']);
  const _AmountService = jasmine.createSpyObj<AmountService>('AmountService', ['copyFromAdcm']);
  const _BasicInfoService = jasmine.createSpyObj<BasicInfoService>('BasicInfoService', ['copyFromAdcm']);
  const _DetailsService = jasmine.createSpyObj<DetailsService>('DetailsService', ['copyFromAdcm']);
  const _CommentsService = jasmine.createSpyObj<CommentsService>('CommentsService', ['get', 'getCommentsToUpload']);
  const _CommentService = jasmine.createSpyObj<CommentService>('CommentService', ['get']);
  const _FileService = jasmine.createSpyObj<FileService>('FileService', ['get']);
  const _FilesService = jasmine.createSpyObj<FilesService>('FilesService', ['get', 'getFilesToUpload']);
  const _AlertsService = jasmine.createSpyObj<AlertsService>('AlertsService', ['get']);

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

  /* it('setACDM', inject([AdmAcmService], (service: AdmAcmService) => {
    _BasicInfoService.copyFromAdcm.calls.reset();
    _AmountService.copyFromAdcm.calls.reset();
    _DetailsService.copyFromAdcm.calls.reset();

    _BasicInfoService.copyFromAdcm.and.returnValue(Observable.of({}));
    _AmountService.copyFromAdcm.and.returnValue(Observable.of({}));
    _DetailsService.copyFromAdcm.and.returnValue(Observable.of({}));

    service.setACDM();
    expect(_BasicInfoService.copyFromAdcm.calls.count()).toBe(1);
    expect(_AmountService.copyFromAdcm.calls.count()).toBe(1);
    expect(_DetailsService.copyFromAdcm.calls.count()).toBe(1);
  })); */

});
