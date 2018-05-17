import { Log } from "ng2-logger";
import { Logger } from "ng2-logger/src/logger";
import { NwBaseAbstract } from "./nw-base-abstract";
import { Injector } from "@angular/core";
import { Language } from "angular-l10n";

export abstract class NwAbstractComponent extends NwBaseAbstract{

  constructor(protected injector: Injector) {
    super(injector);
  }
}
