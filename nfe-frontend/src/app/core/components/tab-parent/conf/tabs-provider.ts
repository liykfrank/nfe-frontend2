import { DashboardComponent } from "./../../dashboard/dashboard.component";
import { MonitorUrlComponent } from "../../../../monitor/monitor-url/monitor-url.component";
import { FilesListComponent } from "../../../../files/components/files-list/files-list.component";
import { ItabAction } from "./itab-action.model";
import { ActionsEnum } from "../../../../shared/models/actions-enum.enum";
import { ComponentFactoryResolver, ViewContainerRef } from "@angular/core";
import { FilesUploadComponent } from "../../../../files/components/files-upload/files-upload.component";
import { FilesDownloadComponent } from "../../../../files/components/files-download/files-download.component";
import { SftpAccountsQueryComponent } from "../../../../sftp-accounts/sftp-accounts-query/sftp-accounts-query.component";
import { SftpAccountMaintenanceComponent } from "../../../../sftp-accounts/sftp-account-maintenance/sftp-account-maintenance.component";

import { EmptyComponent } from "../../empty/empty.component";

export class TabsProvider {
  tabDashboard: ItabAction<DashboardComponent> = {
    action: ActionsEnum.DASHBOARD,
    title: "Dashboard",
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
    title: "ADM/ACM",
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
    title: "Refunds",
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
    title: "Reports",
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
    title: "Documents",
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
    title: "Ticketing",
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
    title: "General Info",
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
    title: "Self Service",
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
    title: "Admin",
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
    title: "Query Files",
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
    title: "Query Files Read Only",
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
    title: "Upload Files",
    getComp: (
      cfResolver: ComponentFactoryResolver,
      vcRef: ViewContainerRef
    ) => {
      const f = cfResolver.resolveComponentFactory(FilesUploadComponent);
      return vcRef.createComponent(f);
    }
  };

  tabSftpAccounts: ItabAction<SftpAccountsQueryComponent> = {
    action: ActionsEnum.SFTP_ACCOUNTS,
    title: "SFTP Accounts",
    getComp: (
      cfResolver: ComponentFactoryResolver,
      vcRef: ViewContainerRef
    ) => {
      const f = cfResolver.resolveComponentFactory(SftpAccountsQueryComponent);
      return vcRef.createComponent(f);
    }
  };

  tabQuerySftpAccounts: ItabAction<SftpAccountsQueryComponent> = {
    action: ActionsEnum.QUERY_SFTP_ACCOUNTS,
    title: "Query SFTP Accounts",
    getComp: (
      cfResolver: ComponentFactoryResolver,
      vcRef: ViewContainerRef
    ) => {
      const f = cfResolver.resolveComponentFactory(SftpAccountsQueryComponent);
      return vcRef.createComponent(f);
    }
  };

  tabCreateSftpAccounts: ItabAction<SftpAccountMaintenanceComponent> = {
    action: ActionsEnum.CREATE_SFTP_ACCOUNT,
    title: "Create SFTP Account",
    getComp: (
      cfResolver: ComponentFactoryResolver,
      vcRef: ViewContainerRef
    ) => {
      const f = cfResolver.resolveComponentFactory(
        SftpAccountMaintenanceComponent
      );
      return vcRef.createComponent(f);
    }
  };

  tabMonitor: ItabAction<MonitorUrlComponent> = {
    action: ActionsEnum.MONITOR,
    title: "Monitor",
    getComp: (
      cfResolver: ComponentFactoryResolver,
      vcRef: ViewContainerRef
    ) => {
      const f = cfResolver.resolveComponentFactory(MonitorUrlComponent);
      return vcRef.createComponent(f);
    }
  };

  confTabs: Array<ItabAction<any>> = [
    this.tabDashboard,
    this.tabADM_ACM,
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
    this.tabQuerySftpAccounts,
    this.tabCreateSftpAccounts
  ];

  public getTabProv(action: ActionsEnum): ItabAction<any> {
    return this.confTabs.find(conf => conf.action == action);
  }
}
