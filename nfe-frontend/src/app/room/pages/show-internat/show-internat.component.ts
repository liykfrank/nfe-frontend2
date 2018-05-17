import { Component, OnInit, Injector } from '@angular/core';
import { LocaleService, TranslationService, Language, DefaultLocale, Currency } from 'angular-l10n';
import { NwAbstractComponent } from '../../../shared/base/abstract-component';

@Component({
  selector: 'app-show-internat',
  templateUrl: './show-internat.component.html',
  styleUrls: ['./show-internat.component.scss']
})
export class ShowInternatComponent extends NwAbstractComponent  implements OnInit {

  title: string;
  //@Language() public lang: string;
  constructor(
   protected injector: Injector) {super(injector); }

  ngOnInit(): void {
    this.translation.translationChanged().subscribe(
        () => { this.title = this.translation.translate('Title'); }
    );
}
  selectLocale(language: string, country: string, currency: string): void {
    this.locale.setDefaultLocale(language, country);
    this.locale.setCurrentCurrency(currency);
  }

}
