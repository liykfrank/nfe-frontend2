import { UserMaintenanceService } from './services/user-maintenance.service';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import { TranslationService } from 'angular-l10n';
import { GLOBALS } from '../../../../shared/constants/globals';
import { UserViews } from '../../../../shared/enums/users.enum';
import { TemplateService } from '../../services/template.service';
import { AgentService } from './../../../../shared/components/agent/services/agent.service';
import { CountryTerritoryService } from './services/country-territory.service';
import { ModSubUserView } from './views/mod-sub-user.view';
import { ModUserView } from './views/mod-user.view';
import { NewSubUserView } from './views/new-sub-user.view';
import { NewUserView } from './views/new-user.view';
import { UtilsService } from '../../../../shared/services/utils.service';
import { AlertsService } from '../../../../core/services/alerts.service';


@Component({
  selector: 'bspl-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class UserComponent implements OnInit {

  screenType;

  view;

  typesOfScreens = UserViews;

  patterns = GLOBALS.HTML_PATTERN;

  constructor(
    public router: Router,
    private _translationService: TranslationService,
    public templateService: TemplateService,
    public countryTerritoryService: CountryTerritoryService,
    private _agentService: AgentService,
    private _userMaintenanceService: UserMaintenanceService,
    private _utilsService: UtilsService,
    private _alertService: AlertsService
  ) {}

  ngOnInit() {
    this.loadView();
  }

  loadView() {
    this.setScreenType(this.router.routerState.snapshot.url);
    switch (this.screenType) {
      case this.typesOfScreens.NEW_USER:
        this.view = new NewUserView(this._translationService,
        this.templateService,
        this.countryTerritoryService,
        this._agentService,
        this._userMaintenanceService,
        this._utilsService,
        this._alertService);
        break;

      case this.typesOfScreens.MOD_USER:
        this.view = new ModUserView(this._translationService,
        this.templateService,
        this.countryTerritoryService,
        this._userMaintenanceService);
        break;

      case this.typesOfScreens.NEW_SUB_USER:
        this.view = new NewSubUserView(this._translationService,
        this.templateService,
        this.countryTerritoryService);
        break;

      case this.typesOfScreens.MOD_SUB_USER:
        this.view = new ModSubUserView(this._translationService,
        this.templateService,
        this.countryTerritoryService,
        this._userMaintenanceService);
        break;
    }

  }

  isView(view) {
    return this.screenType == view;
  }

  setScreenType(url): void {
    const arr_url = url.split('/');
    if (arr_url.length > 1) {
      this.screenType = arr_url[2];
    }
  }

}
