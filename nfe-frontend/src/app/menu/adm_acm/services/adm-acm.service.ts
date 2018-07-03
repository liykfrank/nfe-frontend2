import { environment } from './../../../../environments/environment';

import { Injectable } from '@angular/core';

import { Configuration } from '../models/configuration.model';
import { CompanyModel } from './../models/company.model';
import { AgentModel } from './../models/agent.model';
import { Country } from './../models/country.model';
import { ScreenType } from './../../../shared/models/screen-type.enum';

import { AgentService } from './resources/agent.service';
import { CompanyService } from './resources/company.service';
import { ConfigurationService } from './resources/configuration.service';
import { CountryService } from './resources/country.service';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';


@Injectable()
export class AdmAcmService {

  private configuration = new BehaviorSubject<Configuration>(this._ConfigurationService.defaultConf());

  //Observables Basic Info
  private decimals = new BehaviorSubject<number>(2);
  private span = new BehaviorSubject<boolean>(false);
  private subtype = new BehaviorSubject<string>('');
  private spdr = new BehaviorSubject<string>('');
  private screenType = new BehaviorSubject<ScreenType>(ScreenType.CREATE);
  //Observables Basic Info

  constructor(private _ConfigurationService: ConfigurationService) { }

  public getConfiguration(): Observable<Configuration> {
    return this.configuration.asObservable();
  }

  public findCountryConfiguration(iso: string): void {
    const urlREAL = this._ConfigurationService.getUrl();
    this._ConfigurationService.configureUrl(this._ConfigurationService.getUrl([iso]));
    this._ConfigurationService.get().subscribe(data => this.configuration.next(data));
    this._ConfigurationService.configureUrl(urlREAL);
  }

  //Observables Basic Info
  public getDecimals(): Observable<number> {
    return this.decimals.asObservable();
  }

  public setDecimals(decimals: number): void {
    this.decimals.next(decimals);
  }

  public getSpan(): Observable<boolean> {
    return this.span.asObservable();
  }

  public setSpan(span: boolean): void {
    this.span.next(span);
  }

  public getSubtype(): Observable<string> {
    return this.subtype.asObservable();
  }

  public setSubtype(subtype: string) : void {
    this.subtype.next(subtype);
  }

  public getSpdr(): Observable<string> {
    return this.spdr.asObservable();
  }

  public setSpdr(spdr: string): void {
    this.spdr.next(spdr);
  }

  public getScreenType(): Observable<ScreenType> {
    return this.screenType.asObservable();
  }

  public setScreenType(screenType: ScreenType): void {
    this.screenType.next(screenType);
  }
  //Observables Basic Info
}
