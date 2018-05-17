import { Log } from "ng2-logger";
import { Logger } from "ng2-logger/src/logger";
import { Language, DefaultLocale, Currency, TranslationService, LocaleService, Localization } from "angular-l10n";
import { Injector } from "@angular/core";

export abstract class NwBaseAbstract extends Localization{

  protected log: Logger<this> = Log.create(this.constructor.name);
  @Language()  lang: string;
  @DefaultLocale() public defaultLocale: string;
  @Currency() public currency: string;
  protected translation: TranslationService;
  protected locale: LocaleService;

 constructor(protected injector: Injector) {
    super();
    this.translation = this.injector.get(TranslationService);
    this.locale = this.injector.get(LocaleService);
    this.log.info("Class " + this.constructor.name + " created");

  }
}
