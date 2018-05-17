import { UtilsService } from './../../../shared/services/utils.service';
import { ActionsEnum } from '../../../shared/models/actions-enum.enum';
import { Component } from '@angular/core';
import { TabsStateService } from '../../services/tabs-state.service';

@Component({
    selector: 'menu',
    templateUrl: './menu.component.html'
})

export class MenuComponent {

  actions = ActionsEnum;

    constructor(private tabsService: TabsStateService, public _utilsService: UtilsService) { }

    openTab(selectedTab) {
        switch (selectedTab) {
            case 'dashboard':
                this.tabsService.addTabAction(ActionsEnum.DASHBOARD);
                break;
            case 'adm_acm':
                this.tabsService.addTabAction(ActionsEnum.ADM_ACM);
                break;
            case 'query_adm':
                this.tabsService.addTabAction(ActionsEnum.ADM_ACM);
                break;
            case 'query_acm':
                this.tabsService.addTabAction(ActionsEnum.ADM_ACM);
                break;
            case 'refunds':
                this.tabsService.addTabAction(ActionsEnum.REFUNDS);
                break;
            case 'reports':
                this.tabsService.addTabAction(ActionsEnum.REPORTS);
                break;
            case 'query_files':
                this.tabsService.addTabAction(ActionsEnum.QUERY_FILES);
                break;
            case 'query_files/readOnly':
                this.tabsService.addTabAction(ActionsEnum.QUERY_FILES_READ_ONLY);
                break;
            case 'upload_files':
                this.tabsService.addTabAction(ActionsEnum.UPLOAD_FILES);
                break;
            case 'documents':
                this.tabsService.addTabAction(ActionsEnum.DOCUMENTS);
                break;
            case 'sftp_accounts':
                this.tabsService.addTabAction(ActionsEnum.SFTP_ACCOUNTS);
                break;
            case 'query_sftp_accounts':
                this.tabsService.addTabAction(ActionsEnum.QUERY_SFTP_ACCOUNTS);
                break;
            case 'create_sftp_account':
                this.tabsService.addTabAction(ActionsEnum.CREATE_SFTP_ACCOUNT);
                break;
            case 'ticketing':
                this.tabsService.addTabAction(ActionsEnum.TICKETING);
                break;
            case 'general_info':
                this.tabsService.addTabAction(ActionsEnum.GENERAL_INFO);
                break;
            case 'self_service':
                this.tabsService.addTabAction(ActionsEnum.SELF_SERVICE);
                break;
            case 'admin':
                this.tabsService.addTabAction(ActionsEnum.ADMIN);
                break;
            case 'monitor':
                this.tabsService.addTabAction(ActionsEnum.MONITOR);
                break;
            default:
                break;

        }
    }

}

