import { CurrencyServer } from './../models/currency-server.model';
import { Observable } from 'rxjs/Observable';
import { environment } from './../../../../environments/environment';

import { Injectable, EventEmitter } from '@angular/core';

import { Configuration } from '../models/configuration.model';
import { CompanyModel } from './../models/company.model';
import { AgentModel } from './../models/agent.model';
import { Country } from './../models/country.model';
import { ScreenType } from './../../../shared/models/screen-type.enum';
import { Acdm } from './../models/acdm.model';
import { CommentAcdm } from './../models/comment-acdm.model';

import { AgentService } from './resources/agent.service';
import { AcdmsService } from './resources/acdms.service';
import { CommentService } from './resources/comment.service';
import { FileService } from './resources/file.service';
import { AmountService } from './amount.service';
import { BasicInfoService } from './basic-info.service';
import { DetailsService } from './details.service';
import { CompanyService } from './resources/company.service';
import { ConfigurationService } from './resources/configuration.service';
import { CountryService } from './resources/country.service';
import { Subject } from 'rxjs/Subject';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { FilesService } from './files.service';
import { CommentsService } from './comments.service';
import { AlertsService } from '../../../core/services/alerts.service';
import { AlertModel } from '../../../core/models/alert.model';
import { AlertType } from '../../../core/models/alert-type.enum';


@Injectable()
export class AdmAcmService {

  private $configuration = new BehaviorSubject<Configuration>(null);

  private $errors = new BehaviorSubject<string[]>([]);

  //Observables Basic Info
  private $currency = new BehaviorSubject<CurrencyServer>(null);
  private $span = new BehaviorSubject<boolean>(false);
  private $subtype = new BehaviorSubject<string>('');
  private $spdr = new BehaviorSubject<string>('I');
  private $screenType = new BehaviorSubject<ScreenType>(ScreenType.CREATE);

  private acdm = new Acdm();
  private filesToUpload: File[] = [];
  private commentsToUpload: any = {};
  // Observables Basic Info

  constructor(
    private _ConfigurationService: ConfigurationService,
    private _AcdmsService: AcdmsService,
    private _CommentService: CommentService,
    private _CommentsService: CommentsService,
    private _FileService: FileService,
    private _FilesService: FilesService,
    private _AmountService: AmountService,
    private _BasicInfoService: BasicInfoService,
    private _DetailsService: DetailsService,
    private _AlertsService: AlertsService
  ) {
    this.acdm.dateOfIssue = new Date().toISOString();

    this.$screenType.subscribe(screen => {
      if (screen === ScreenType.DETAIL) {
        this.saveComment();
        this.saveFiles();
      }
    });

    this._FilesService.getFilesToUpload().subscribe(files => {
      this.filesToUpload = files;
      if (!this.isScreenTypeCreate()) {
        this.saveFiles();
      }
    });

    this._CommentsService.getCommentsToUpload().subscribe(comment => {
      this.commentsToUpload = comment;
      if (!this.isScreenTypeCreate()) {
        this.saveComment();
      }
    });

    this._ConfigurationService.getConfiguration().subscribe(data => this.$configuration.next(data));
  }

  public isScreenTypeCreate (): boolean {
    return this.$screenType.getValue() === ScreenType.CREATE;
  }

  public getConfiguration(): Observable<Configuration> {
    return this.$configuration.asObservable();
  }

  public findCountryConfiguration(iso: string): void {
    if (!iso || iso.length == 0) {
      this._AlertsService.setAlertTranslate('ADM_ACM.SVCS.CONF.title', 'ADM_ACM.SVCS.CONF.desc_wrong_iso', AlertType.ERROR);
    }
    this._ConfigurationService.getWithISO(iso);
  }

  //Observables Basic Info
  public getCurrency(): Observable<CurrencyServer> {
    return this.$currency.asObservable();
  }

  public setCurrency(currency: CurrencyServer): void {
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
    const errors = this._BasicInfoService.checkBasicInfo(this.$configuration.getValue());

    if (errors.length != 0) {
      this.$errors.next(errors);
      this._AlertsService.setAlertTranslate('error', 'ADM_ACM.error', AlertType.ERROR);
      return;
    }

    if (!this._AmountService.checkTaxes()) {
      this._AlertsService.setAlertTranslate('error', 'ADM_ACM.AMOUNT.tax_error', AlertType.ERROR);
      return;
    }

    if (!this._AmountService.checkTotal(this.$subtype.getValue())) {
      this._AlertsService.setAlertTranslate('error', 'ADM_ACM.AMOUNT.total_error', AlertType.ERROR);
      return;
    }

    // BasicInfo
    this._BasicInfoService.copyToAdcm(this.acdm);

    // Amount
    this._AmountService.copyToAdcm(this.acdm);

    // Details
    this._DetailsService.copyToAdcm(this.acdm);

    this._FilesService.copyToAdcm(this.acdm);

    if (regularized) {
      this.acdm.regularized = true;
    }

    this._AcdmsService.postAcdm(this.acdm).subscribe(
      response => {
        this.acdm = response;
        // TODO: Alert Guardado
        let alert = new AlertModel('ISSUE Successful', 'Issue ID: ' + response.id, AlertType.INFO);
        this._AlertsService.setAlert(alert);
        this.setACDM();
      },
      response => {
        if (this.checkRegularized(response.error)) {
          let alert = new AlertModel('ISSUE Regularized', 'Regularize the issue?', AlertType.CONFIRM);
          let subscription = this._AlertsService.getAccept().subscribe(acceptance => {
            if (acceptance) {
              this.sendRegularized();
              subscription.unsubscribe();
            }
          });
          this._AlertsService.setAlert(alert);
        } else {
          let alert = new AlertModel('ERROR', 'Check the wrong fields', AlertType.ERROR);
          this._AlertsService.setAlert(alert);
        }
      });
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
    let regularized = true;
    this.issueACDM(regularized);
  }

  public saveComment() {
    if (this.commentsToUpload.text && this.commentsToUpload.text !== '') {
      this._CommentService.postComment(this.acdm.id, this.commentsToUpload.text);
    }
  }

  public saveFiles() {
    if (this.filesToUpload.length > 0) {
      this._FileService.postFile(this.acdm.id, this.filesToUpload);
    }
  }

  public setACDM() {
    //events
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
  }


}
