import { SftpModifyPasswordService } from './../../services/sftp-modify-password.service';
import { UtilsService } from './../../../shared/services/utils.service';
import { ActionsEnum } from '../../../shared/models/actions-enum.enum';
import { Component, Injector } from '@angular/core';
import { TabsStateService } from '../../services/tabs-state.service';
import { NwAbstractComponent } from '../../../shared/base/abstract-component';
import { MessageService } from 'primeng/components/common/messageservice';
import { Message } from 'primeng/components/common/api';
import { HttpParams } from '@angular/common/http';

@Component({
    selector: 'app-menu',
    templateUrl: './menu.component.html'
})
export class MenuComponent extends NwAbstractComponent {

  actions = ActionsEnum;

  constructor(
    private _Injector: Injector,
    private _MessageService: MessageService,
    private tabsService: TabsStateService,
    public _utilsService: UtilsService,
    private _SftpModifyPasswordService: SftpModifyPasswordService
  ) {
    super(_Injector);
  }

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
      case 'dispute_adm':
        this.tabsService.addTabAction(ActionsEnum.DISPUTE_ADM);
        break;
      case 'dispute_acm':
        this.tabsService.addTabAction(ActionsEnum.DISPUTE_ACM);
        break;
      default:
        break;
    }
  }

  sftpModifyPass() {
    let url = this._SftpModifyPasswordService.getUrl();
    url = url.replace('\{1\}', 'ruben.acebron@accelya.com');

    this._SftpModifyPasswordService.configureUrl(url);

    const body = new HttpParams();
    body.set('responseType', 'text');

    const svc = this._SftpModifyPasswordService.getXML();

    const summaryText = this.translation.translate('SFTP_ACCOUNT.modify.title');

    svc.subscribe( data => {
      console.log('ok');
      console.log(data);

      const detail = this.translation.translate('SFTP_ACCOUNT.modify.ok');

      const init = data.indexOf('<response>');
      const end = data.indexOf('</response>');

      let msg = data.substr(init + '<response>'.length, end - (init + '<response>'.length));
      msg = msg.replace(/%20/g, ' ');
      msg = msg.replace(/%27/g, '\'');
      msg = msg.replace(/%2C/g, ',');

      this._MessageService.add({
        severity: 'success',
        summary: summaryText ,
        detail: msg
      });
      console.log('fin ok');
    }, err => {
      console.log('err');
      console.log(err);

      this._MessageService.add({
        severity: 'error',
        summary: summaryText,
        detail: this.translation.translate('SFTP_ACCOUNT.modify.err')
      } );

      console.log('fin err');
    });
  }

}

