import { Configuration } from './../models/configuration.model';
import { PeriodService } from './resources/period.service';
import { BasicInfoComponent } from './../components/basic-info/basic-info.component';
import { Observable } from 'rxjs/Observable';
import { BasicInfoModel } from './../models/basic-info.model';
import { environment } from './../../../../environments/environment';
import { CurrencyService } from './resources/currency.service';
import { TocaService } from './resources/toca.service';
import { CompanyService } from './resources/company.service';
import { AgentService } from './resources/agent.service';
import { TranslationService } from 'angular-l10n';
import { Injectable } from '@angular/core';
import { KeyValue } from '../../../shared/models/key.value.model';
import { Currency } from '../models/currency.model';
import { CountryService } from './resources/country.service';
import { Country } from '../models/country.model';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { AgentModel } from '../models/agent.model';
import { CompanyModel } from '../models/company.model';
import { TocaType } from '../models/toca-type.model';
import { Acdm } from './../models/acdm.model';
import { AlertsService } from '../../../core/services/alerts.service';
import { AlertType } from '../../../core/models/alert-type.enum';

@Injectable()
export class BasicInfoService {
  private $countries = new BehaviorSubject<Country[]>([]);
  private $currency = new BehaviorSubject<Currency[]>([]);
  private $toca = new BehaviorSubject<TocaType[]>([]);
  private $period = new BehaviorSubject<any>(null);
  private $basicInfo = new BehaviorSubject<BasicInfoModel>(
    new BasicInfoModel()
  );

  private $validBasicInfo = new BehaviorSubject<boolean>(true);

  constructor(
    private _TranslationService: TranslationService,
    private _CompanyService: CompanyService,
    private _AgentService: AgentService,
    private _CountryService: CountryService,
    private _TocaService: TocaService,
    private _CurrencyService: CurrencyService,
    private _PeriodService: PeriodService,
    private _AlertsService: AlertsService
  ) {
    this._CountryService.get().subscribe(data => this.$countries.next(data));

    this._TocaService.getToca().subscribe(data => this.$toca.next(data));
    this._CurrencyService.getCurrency().subscribe(data => this.$currency.next(data));
    this._PeriodService.getPeriod().subscribe(data => this.$period.next(data));
  }

  public getSubTypeList(adm: boolean): KeyValue[] {
    const list: KeyValue[] = [];

    if (adm) {
      list.push({ code: 'ADMA', description: 'ADMA' });
      list.push({ code: 'SPDR', description: 'SPDR' });
      list.push({ code: 'ADMD', description: 'ADMD' });
    } else {
      list.push({ code: 'ACMA', description: 'ACMA' });
      list.push({ code: 'SPCR', description: 'SPCR' });
      list.push({ code: 'ACMD', description: 'ACMD' });
    }

    return list;
  }

  public checkBasicInfo(conf: Configuration): string[] {
    const bi = this.$basicInfo.getValue();

    let ret = true;
    const errorList = [];

    if (!bi.transactionCode || bi.transactionCode.length == 0) {
      errorList.push('transactionCode');
      ret = false;
    }

    if (!bi.billingPeriod || bi.billingPeriod == 0) {
      errorList.push('billingPeriod');
      ret = false;
    }

    if (!bi.isoCountryCode || bi.isoCountryCode.length == 0) {
      errorList.push('isoCountryCode');
      ret = false;
    }

    if (!bi.concernsIndicator || bi.concernsIndicator.length == 0) {
      errorList.push('concernsIndicator');
      ret = false;
    }

    if (!bi.stat || bi.stat.length == 0) {
      errorList.push('stat');
      ret = false;
    }

    if (conf.airlineVatNumberEnabled && (!bi.airlineVatNumber || bi.airlineVatNumber.length == 0)) {
      errorList.push('airlineVatNumber');
      ret = false;
    }

    if (conf.companyRegistrationNumberEnabled && (!bi.airlineRegistrationNumber || bi.airlineRegistrationNumber.length == 0)) {
      errorList.push('airlineRegistrationNumber');
      ret = false;
    }

    if (!bi.company.airlineCode || bi.company.airlineCode.length == 0) {
      errorList.push('company');
      ret = false;
    }

    if (conf.agentVatNumberEnabled && (!bi.agentVatNumber || bi.agentVatNumber.length == 0)) {
      errorList.push('agentVatNumber');
      ret = false;
    }

    if (conf.companyRegistrationNumberEnabled && (!bi.agentRegistrationNumber || bi.agentRegistrationNumber.length == 0)) {
      errorList.push('agentRegistrationNumber');
      ret = false;
    }

    if (!bi.agent.iataCode || bi.agent.iataCode.length == 0) {
      errorList.push('agent');
      ret = false;
    }


    this.setValidBasicInfo(errorList.length == 0);

    return errorList;
  }

  public copyToAdcm(acdm: Acdm): void {
    const tempBasicInfoModel: BasicInfoModel = this.$basicInfo.getValue();

    acdm.isoCountryCode =             tempBasicInfoModel.isoCountryCode;
    acdm.billingPeriod =              tempBasicInfoModel.billingPeriod;
    acdm.agentCode =                  tempBasicInfoModel.agent.iataCode;
    acdm.agentRegistrationNumber =    tempBasicInfoModel.agentRegistrationNumber;
    acdm.agentVatNumber =             tempBasicInfoModel.agentVatNumber;
    acdm.transactionCode =            tempBasicInfoModel.transactionCode;
    acdm.airlineCode =                tempBasicInfoModel.company.airlineCode;
    acdm.airlineRegistrationNumber =  tempBasicInfoModel.airlineRegistrationNumber;
    acdm.airlineVatNumber =           tempBasicInfoModel.airlineVatNumber;
    acdm.airlineContact =             tempBasicInfoModel.airlineContact;
    acdm.concernsIndicator =          tempBasicInfoModel.concernsIndicator;
    acdm.taxOnCommissionType =        tempBasicInfoModel.tocaType;
    acdm.currency =                   tempBasicInfoModel.currency;
    acdm.netReporting =               tempBasicInfoModel.netReporting;
    acdm.statisticalCode =            tempBasicInfoModel.stat;
  }

  public copyFromAdcm(acdm: Acdm): void {
    const tempBasicInfoModel: BasicInfoModel = new BasicInfoModel();

    this.getTocaAndCurrencies(acdm.isoCountryCode);

    tempBasicInfoModel.id =                   acdm.id;
    tempBasicInfoModel.ticketDocumentNumber = acdm.ticketDocumentNumber;
    tempBasicInfoModel.transactionCode =      acdm.transactionCode;
    tempBasicInfoModel.billingPeriod =        acdm.billingPeriod;
    tempBasicInfoModel.isoCountryCode =       acdm.isoCountryCode;
    tempBasicInfoModel.concernsIndicator =    acdm.concernsIndicator;
    tempBasicInfoModel.tocaType =             acdm.taxOnCommissionType;
    tempBasicInfoModel.currency =             acdm.currency;
    tempBasicInfoModel.stat =                 acdm.statisticalCode;
    tempBasicInfoModel.netReporting =         acdm.netReporting;
    tempBasicInfoModel.airlineContact =       acdm.airlineContact;

    tempBasicInfoModel.agentRegistrationNumber = acdm.agentRegistrationNumber;
    tempBasicInfoModel.agentVatNumber = acdm.agentVatNumber;
    if (acdm.agentCode) {
      this._AgentService.getAgentWithCode(acdm.agentCode);
    }

    tempBasicInfoModel.airlineRegistrationNumber = acdm.airlineRegistrationNumber;
    tempBasicInfoModel.airlineVatNumber = acdm.airlineVatNumber;
    if (acdm.isoCountryCode && acdm.airlineCode) {
      this._CompanyService.getFromServerAirlineCountryAirlineCode(acdm.isoCountryCode, acdm.airlineCode);
    }

    this.$basicInfo.next(tempBasicInfoModel);

  }

  public getSPDRCombo(): KeyValue[] {
    const list: KeyValue[] = [];
    list.push({
      code: 'I',
      description: this._TranslationService.translate(
        'ADM_ACM.BASIC_INFO.SPDRCombo.I'
      )
    });
    list.push({
      code: 'R',
      description: this._TranslationService.translate(
        'ADM_ACM.BASIC_INFO.SPDRCombo.R'
      )
    });
    list.push({
      code: 'X',
      description: this._TranslationService.translate(
        'ADM_ACM.BASIC_INFO.SPDRCombo.X'
      )
    });
    list.push({
      code: 'E',
      description: this._TranslationService.translate(
        'ADM_ACM.BASIC_INFO.SPDRCombo.E'
      )
    });

    return list;
  }

  public getStatList(): KeyValue[] {
    const list: KeyValue[] = [];

    list.push({ code: 'D', description: 'DOM' });
    list.push({ code: 'I', description: 'INT' });

    return list;
  }

  public getTocaAndCurrencies(iso: string) {
    if (!iso || iso.length == 0) {
      this._AlertsService.setAlertTranslate('ADM_ACM.SVCS.CONF.title', 'ADM_ACM.SVCS.CONF.desc_wrong_iso', AlertType.ERROR);
      return;
    }

    this._TocaService.getTocaWithISO(iso);
    this._CurrencyService.getCurrencyWithISO(iso);
    this._PeriodService.getPeriodWithISO(iso);
  }

  public getToca(): Observable<TocaType[]> {
    return this.$toca.asObservable();
  }

  public getCurrency(): Observable<Currency[]> {
    return this.$currency.asObservable();
  }

  public getCountries(): Observable<Country[]> {
    return this.$countries.asObservable();
  }

  public getPeriod(): Observable<any> {
    return this.$period.asObservable();
  }

  public getBasicInfo(): Observable<BasicInfoModel> {
    return this.$basicInfo.asObservable();
  }

  public setBasicInfo(elem: BasicInfoModel): void {
    this.$basicInfo.next(elem);
  }

  public getValidBasicInfo(): Observable<boolean> {
    return this.$validBasicInfo.asObservable();
  }

  public setValidBasicInfo(value: boolean): void {
    this.$validBasicInfo.next(value);
  }

}
