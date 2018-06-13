
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

@Injectable()
export class BasicInfoService {

  private countries = new BehaviorSubject<Country[]>([]);

  private currency = new BehaviorSubject<Currency[]>([]);
  private toca = new BehaviorSubject<TocaType[]>([]);
  private period = new BehaviorSubject<number[]>([]);

  private agent = new BehaviorSubject<AgentModel>(null);
  private company = new BehaviorSubject<CompanyModel>(null);

  private basicInfo = new BehaviorSubject<BasicInfoModel>(new BasicInfoModel());

  constructor(
        private _TranslationService: TranslationService,
        private _CompanyService: CompanyService,
        private _CountryService: CountryService,
        private _TocaService: TocaService,
        private _CurrencyService: CurrencyService,
        private _PeriodService: PeriodService,
        private _AgentService: AgentService
      ) {
    this._AgentService.get().subscribe(data => this.agent.next(data));
    this._CompanyService.get().subscribe(data => this.company.next(data));
    this._CountryService.get().subscribe(data => {
      console.log('on country');
      console.log(data);
      this.countries.next(data);
    });
  }

  public getSubTypeList(adm: boolean): KeyValue[] {
    const list: KeyValue[] = [];

    if (adm) {
      list.push({'code': 'ADMA', 'description': 'ADMA'});
      list.push({'code': 'SPDR', 'description': 'SPDR'});
      list.push({'code': 'ADMD', 'description': 'ADMD'});
    } else {
      list.push({'code': 'ACMA', 'description': 'ACMA'});
      list.push({'code': 'SPCR', 'description': 'SPCR'});
      list.push({'code': 'ACMD', 'description': 'ACMD'});
    }

    return list;
  }

  public getSPDRCombo(): KeyValue[] {
    const list: KeyValue[] = [];
    list.push({'code': 'I', 'description': this._TranslationService.translate('ADM_ACM.BASIC_INFO.SPDRCombo.I')});
    list.push({'code': 'R', 'description': this._TranslationService.translate('ADM_ACM.BASIC_INFO.SPDRCombo.R')});
    list.push({'code': 'X', 'description': this._TranslationService.translate('ADM_ACM.BASIC_INFO.SPDRCombo.X')});
    list.push({'code': 'E', 'description': this._TranslationService.translate('ADM_ACM.BASIC_INFO.SPDRCombo.E')});

    return list;
  }

  public getStatList(): KeyValue[] {
    const list: KeyValue[] = [];

    list.push({'code': 'DOM', 'description': 'DOM'});
    list.push({'code': 'INT', 'description': 'INT'});

    return list;
  }

  public getTocaAndCurrencies(iso: string) {
    let urlREAL, urlISO;

    urlREAL = this._TocaService.getUrl();
    this._TocaService.configureUrl(this._TocaService.getUrl([iso]));
    this._TocaService.get().subscribe((data) => this.toca.next(data));
    this._TocaService.configureUrl(urlREAL);

    urlREAL = this._CurrencyService.getUrl();
    this._CurrencyService.configureUrl(this._TocaService.getUrl([iso]));
    this._CurrencyService.get().subscribe(data => {
      for (let x of data) {
        x.description = x.code;
      }
      this.currency.next(data);
    });
    this._CurrencyService.configureUrl(urlREAL);

    urlREAL = this._PeriodService.getUrl();
    this._PeriodService.configureUrl(this._PeriodService.getUrl([iso]));
    this._PeriodService.get().subscribe(data => this.period.next(data));
    this._PeriodService.configureUrl(urlREAL);
  }

  public getBasicInfo(): Observable<BasicInfoModel> {
    return this.basicInfo.asObservable();
  }

  public setBasicInfo(elem: BasicInfoModel): void {
    this.basicInfo.next(elem);
  }

  public getToca(): Observable<TocaType[]> {
    return this.toca.asObservable();
  }

  public getCurrency(): Observable<Currency[]> {
    return this.currency.asObservable();
  }

  public getPeriod(): Observable<number[]> {
    return this.period.asObservable();
  }

  public getCountries(): Observable<Country[]> {
    return this.countries.asObservable();
  }
  public getAgent(): Observable<AgentModel> {
    return this.agent.asObservable();
  }
  public getCompany(): Observable<CompanyModel> {
    return this.company.asObservable();
  }

}
