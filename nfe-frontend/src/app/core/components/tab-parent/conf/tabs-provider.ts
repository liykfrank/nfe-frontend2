import { ComponentFactoryResolver, ViewContainerRef } from '@angular/core';

import { FilesDownloadComponent } from '../../../../menu/files/components/files-download/files-download.component';
import { FilesUploadComponent } from '../../../../menu/files/components/files-upload/files-upload.component';
import { MonitorUrlComponent } from '../../../../menu/monitor/monitor-url/monitor-url.component';
import { SftpAccountComponent } from '../../../../menu/sftp-accounts/components/sftp-account/sftp-account.component';
import { ActionsEnum } from '../../../../shared/models/actions-enum.enum';
import { EmptyComponent } from '../../empty/empty.component';
import { DashboardComponent } from './../../dashboard/dashboard.component';
import { ItabAction } from './itab-action.model';
import { AdmAcmComponent } from '../../../../menu/adm_acm/components/adm-acm/adm-acm.component';
import { ScreenType } from '../../../../shared/models/screen-type.enum';

export class TabsProvider {
  tabDashboard: ItabAction<DashboardComponent> = {
    action: ActionsEnum.DASHBOARD,
    title: 'Dashboard',
    getComp: (
      cfResolver: ComponentFactoryResolver,
      vcRef: ViewContainerRef
    ) => {
      const f = cfResolver.resolveComponentFactory(DashboardComponent);
      return vcRef.createComponent(f);
    }
  };

  tabADM_ACM: ItabAction<EmptyComponent> = {
    action: ActionsEnum.ADM_ACM,
    title: 'ADM/ACM',
    getComp: (
      cfResolver: ComponentFactoryResolver,
      vcRef: ViewContainerRef
    ) => {
      const f = cfResolver.resolveComponentFactory(EmptyComponent);
      return vcRef.createComponent(f);
    }
  };

  tabRefunds: ItabAction<EmptyComponent> = {
    action: ActionsEnum.REFUNDS,
    title: 'Refunds',
    getComp: (
      cfResolver: ComponentFactoryResolver,
      vcRef: ViewContainerRef
    ) => {
      const f = cfResolver.resolveComponentFactory(EmptyComponent);
      return vcRef.createComponent(f);
    }
  };

  tabReports: ItabAction<EmptyComponent> = {
    action: ActionsEnum.REPORTS,
    title: 'Reports',
    getComp: (
      cfResolver: ComponentFactoryResolver,
      vcRef: ViewContainerRef
    ) => {
      const f = cfResolver.resolveComponentFactory(EmptyComponent);
      return vcRef.createComponent(f);
    }
  };

  tabDocuments: ItabAction<EmptyComponent> = {
    action: ActionsEnum.DOCUMENTS,
    title: 'Documents',
    getComp: (
      cfResolver: ComponentFactoryResolver,
      vcRef: ViewContainerRef
    ) => {
      const f = cfResolver.resolveComponentFactory(EmptyComponent);
      return vcRef.createComponent(f);
    }
  };

  tabTicketing: ItabAction<EmptyComponent> = {
    action: ActionsEnum.TICKETING,
    title: 'Ticketing',
    getComp: (
      cfResolver: ComponentFactoryResolver,
      vcRef: ViewContainerRef
    ) => {
      const f = cfResolver.resolveComponentFactory(EmptyComponent);
      return vcRef.createComponent(f);
    }
  };

  tabGeneralInfo: ItabAction<EmptyComponent> = {
    action: ActionsEnum.GENERAL_INFO,
    title: 'General Info',
    getComp: (
      cfResolver: ComponentFactoryResolver,
      vcRef: ViewContainerRef
    ) => {
      const f = cfResolver.resolveComponentFactory(EmptyComponent);
      return vcRef.createComponent(f);
    }
  };

  tabSelfService: ItabAction<EmptyComponent> = {
    action: ActionsEnum.SELF_SERVICE,
    title: 'Self Service',
    getComp: (
      cfResolver: ComponentFactoryResolver,
      vcRef: ViewContainerRef
    ) => {
      const f = cfResolver.resolveComponentFactory(EmptyComponent);
      return vcRef.createComponent(f);
    }
  };

  tabAdmin: ItabAction<EmptyComponent> = {
    action: ActionsEnum.ADMIN,
    title: 'Admin',
    getComp: (
      cfResolver: ComponentFactoryResolver,
      vcRef: ViewContainerRef
    ) => {
      const f = cfResolver.resolveComponentFactory(EmptyComponent);
      return vcRef.createComponent(f);
    }
  };

  tabQueryFiles: ItabAction<FilesDownloadComponent> = {
    action: ActionsEnum.QUERY_FILES,
    title: 'Query Files',
    getComp: (
      cfResolver: ComponentFactoryResolver,
      vcRef: ViewContainerRef
    ) => {
      const f = cfResolver.resolveComponentFactory(FilesDownloadComponent);
      return vcRef.createComponent(f);
    }
  };

  tabQueryFilesReadOnly: ItabAction<FilesDownloadComponent> = {
    action: ActionsEnum.QUERY_FILES_READ_ONLY,
    title: 'Query Files Read Only',
    getComp: (
      cfResolver: ComponentFactoryResolver,
      vcRef: ViewContainerRef
    ) => {
      const f = cfResolver.resolveComponentFactory(FilesDownloadComponent);
      let s = vcRef.createComponent(f);
      s.instance.readOnly = true;

      return s;
    }
  };

  tabUploadFiles: ItabAction<FilesUploadComponent> = {
    action: ActionsEnum.UPLOAD_FILES,
    title: 'Upload Files',
    getComp: (
      cfResolver: ComponentFactoryResolver,
      vcRef: ViewContainerRef
    ) => {
      const f = cfResolver.resolveComponentFactory(FilesUploadComponent);
      return vcRef.createComponent(f);
    }
  };

  tabSftpAccounts: ItabAction<SftpAccountComponent> = {
    action: ActionsEnum.SFTP_ACCOUNTS,
    title: 'SFTP Accounts',
    getComp: (
      cfResolver: ComponentFactoryResolver,
      vcRef: ViewContainerRef
    ) => {
      const f = cfResolver.resolveComponentFactory(SftpAccountComponent);
      return vcRef.createComponent(f);
    }
  };

  tabMonitor: ItabAction<MonitorUrlComponent> = {
    action: ActionsEnum.MONITOR,
    title: 'Monitor',
    getComp: (
      cfResolver: ComponentFactoryResolver,
      vcRef: ViewContainerRef
    ) => {
      const f = cfResolver.resolveComponentFactory(MonitorUrlComponent);
      return vcRef.createComponent(f);
    }
  };

  tabDisputeADM: ItabAction<AdmAcmComponent> = {
    action: ActionsEnum.DISPUTE_ADM,
    title: 'ADM Issue',
    getComp: (
      cfResolver: ComponentFactoryResolver,
      vcRef: ViewContainerRef
    ) => {
      const f = cfResolver.resolveComponentFactory(AdmAcmComponent);
      let screen = vcRef.createComponent(f);
      screen.instance.isAdm = true;
      screen.instance.modelView = ScreenType.CREATE;
      return screen;
    }
  };

  tabDisputeACM: ItabAction<AdmAcmComponent> = {
    action: ActionsEnum.DISPUTE_ACM,
    title: 'ACM Issue',
    getComp: (
      cfResolver: ComponentFactoryResolver,
      vcRef: ViewContainerRef
    ) => {
      const f = cfResolver.resolveComponentFactory(AdmAcmComponent);
      let screen = vcRef.createComponent(f);
      screen.instance.isAdm = false;
      screen.instance.modelView = ScreenType.CREATE;
      return screen;
    }
  };

  confTabs: Array<ItabAction<any>> = [
    this.tabDashboard,
    /* this.tabADM_ACM, */
    this.tabRefunds,
    this.tabReports,
    this.tabDocuments,
    this.tabTicketing,
    this.tabGeneralInfo,
    this.tabSelfService,
    this.tabAdmin,
    this.tabQueryFiles,
    this.tabQueryFilesReadOnly,
    this.tabUploadFiles,
    this.tabMonitor,
    this.tabSftpAccounts,
    this.tabDisputeADM,
    this.tabDisputeACM
  ];

  public getTabProv(action: ActionsEnum): ItabAction<any> {
    return this.confTabs.find(conf => conf.action == action);
  }
}
