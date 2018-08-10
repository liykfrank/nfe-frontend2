import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { FileDataServer } from '../../../shared/components/multitabs/models/file-data-server.model';
import { ScreenType } from '../../../shared/enums/screen-type.enum';

import { AdmAcmConfiguration } from '../models/adm-acm-configuration.model';
import { Acdm } from './../models/acdm.model';
import { CurrencyPost } from '../../../shared/components/currency/models/currency-post.model';

@Injectable()
export class AdmAcmService {
  private $configuration = new BehaviorSubject<AdmAcmConfiguration>(null);

  private $errors = new BehaviorSubject<string[]>([]);

  // Observables Basic Info
  private $currency = new BehaviorSubject<CurrencyPost>(null);
  private $span = new BehaviorSubject<boolean>(false);
  private $subtype = new BehaviorSubject<string>('');
  private $spdr = new BehaviorSubject<string>('I');
  private $screenType = new BehaviorSubject<ScreenType>(ScreenType.CREATE);

  private acdm = new Acdm();
  private filesToUpload: File[] = [];
  private commentsToUpload: any = {};
  // Observables Basic Info

  private files: FileDataServer[] = [];

  constructor(
    /* private _ConfigurationService: AdmAcmConfigurationService, */
   /*  private _AcdmsService: AcdmsService,
    private _AmountService: AmountService,
    private _BasicInfoService: BasicInfoService,
    private _DetailsService: DetailsService,
    private _AlertsService: AlertsService */
    /* , private _MultitabService: MultitabService */
  ) {
   /*  this.acdm.dateOfIssue = new Date().toISOString(); */

    /*
    this.$screenType.subscribe(screen => {
      this._MultitabService.setScreenType(screen);
    });

    this._MultitabService.getFiles().subscribe(data => this.files = data);
 */
// this._ConfigurationService.getConfiguration().subscribe(data => this.$configuration.next(data));
  }
/*
  public isScreenTypeCreate(): boolean {
    return this.$screenType.getValue() === ScreenType.CREATE;
  }

  public getConfiguration(): Observable<AdmAcmConfiguration> {
    return this.$configuration.asObservable();
  }

  public findCountryConfiguration(iso: string): void {
    if (!iso || iso.length == 0) {
      this._AlertsService.setAlertTranslate(
        'ADM_ACM.SVCS.CONF.title',
        'ADM_ACM.SVCS.CONF.desc_wrong_iso',
        AlertType.ERROR
      );
    }
   //  this._ConfigurationService.getWithISO(iso);
  } */

  // Observables Basic Info
 /*  public getCurrency(): Observable<CurrencyServer> {
    return this.$currency.asObservable();
  }

  public setCurrency(currency: CurrencyPost): void {
    this.$currency.next(currency);
  }

  public getSpan(): Observable<boolean> {
    return this.$span.asObservable();
  }

  public setSpan(span: boolean): void {
    this.$span.next(span);
  }

  public getSubtype(): Observable<string> {
    return this.$subtype.asObservable();
  }

  public setSubtype(subtype: string): void {
    this.$subtype.next(subtype);
  }

  public getSpdr(): Observable<string> {
    return this.$spdr.asObservable();
  }

  public setSpdr(spdr: string): void {
    this.$spdr.next(spdr);
  }

  public getScreenType(): Observable<ScreenType> {
    return this.$screenType.asObservable();
  }

  public setScreenType(screenType: ScreenType): void {
    this.$screenType.next(screenType);
  }

  public setDateOfIssue(date: Date): void {
    this.acdm.dateOfIssue = date.toISOString();
  }

  public getDateOfIssue(): string {
    return this.acdm.dateOfIssue;
  }
  // Observables Basic Info

  public issueACDM(regularized?: boolean) {
    const errors = this._BasicInfoService.checkBasicInfo(
      this.$configuration.getValue()
    );

    if (errors.length != 0) {
      this.$errors.next(errors);
      this._AlertsService.setAlertTranslate(
        'error',
        'ADM_ACM.error',
        AlertType.ERROR
      );
      return;
    }

    if (!this._AmountService.checkTaxes()) {
      this._AlertsService.setAlertTranslate(
        'error',
        'ADM_ACM.AMOUNT.tax_error',
        AlertType.ERROR
      );
      return;
    }

    if (!this._AmountService.checkTotal(this.$subtype.getValue())) {
      this._AlertsService.setAlertTranslate(
        'error',
        'ADM_ACM.AMOUNT.total_error',
        AlertType.ERROR
      );
      return;
    }

    // BasicInfo
    this._BasicInfoService.copyToAdcm(this.acdm);

    // Amount
    this._AmountService.copyToAdcm(this.acdm);

    // Details
    this._DetailsService.copyToAdcm(this.acdm);

    // this._FilesService.copyToAdcm(this.acdm);

    if (regularized) {
      this.acdm.regularized = true;
    }

    this._AcdmsService.postAcdm(this.acdm).subscribe(
      response => {
        this.acdm = response;
        // TODO: Alert Guardado
        const alert = new AlertModel(
          'ISSUE Successful',
          'Issue ID: ' + response.id,
          AlertType.INFO
        );
        this._AlertsService.setAlert(alert);
        this.setACDM();
      },
      response => {
        if (this.checkRegularized(response.error)) {
          const alert = new AlertModel(
            'ISSUE Regularized',
            'Regularize the issue?',
            AlertType.CONFIRM
          );
          const subscription = this._AlertsService
            .getAccept()
            .subscribe(acceptance => {
              if (acceptance) {
                this.sendRegularized();
                subscription.unsubscribe();
              }
            });
          this._AlertsService.setAlert(alert);
        } else {
          const alert = new AlertModel(
            'ERROR',
            'Check the wrong fields',
            AlertType.ERROR
          );
          this._AlertsService.setAlert(alert);
        }
      }
    );
  }

  checkRegularized(error) {
    if (error.validationErrors) {
      for (let i = 0; i < error.validationErrors.length; i++) {
        if (error.validationErrors[i].fieldName === 'regularized') {
          return true;
        }
      }
    }

    return false;
  }

  sendRegularized() {
    const regularized = true;
    this.issueACDM(regularized);
  }

  public setACDM() {
    // events
    this.findCountryConfiguration(this.acdm.isoCountryCode);
    this.$currency.next(this.acdm.currency);
    this.$span.next(this.acdm.netReporting);
    this.$subtype.next(this.acdm.transactionCode);
    this.$spdr.next(this.acdm.concernsIndicator);
    this.$screenType.next(ScreenType.DETAIL);

    // BasicInfo
    this._BasicInfoService.copyFromAdcm(this.acdm);

    // Amount
    this._AmountService.copyFromAdcm(this.acdm);

    // Details
    this._DetailsService.copyFromAdcm(this.acdm);
  }

  public getErrors(): Observable<string[]> {
    return this.$errors.asObservable();
  }

  public setErrors(elems: string[]): void {
    return this.$errors.next(elems);
  } */
}
