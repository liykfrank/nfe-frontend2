import { TabsStateService } from './services/tabs-state.service';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WebComponent } from './components/web/web.component';
import { HeaderComponent } from './components/header/header.component';
import { TabParentComponent } from './components/tab-parent/tab-parent.component';
import { SharedModule } from '../shared/shared.module';
import { CoreRoutingModule } from './core-routing.module';
import { RouterModule } from '@angular/router';
import { MenuComponent } from './components/menu/menu.component';
import { FooterComponent } from './components/footer/footer.component';
import { TranslationModule, L10nLoader } from 'angular-l10n';
import { l10nConfig } from '../shared/base/conf/l10n.config';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { HTTPMock } from './interceptors/http-mock.interceptor';
import { errorsInterceptorProvider } from './interceptors/errors.interceptor';
import { communInterceptorProvider } from './interceptors/commun.interceptor';
import { UtilsService } from '../shared/services/utils.service';
import { EmptyComponent } from './components/empty/empty.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { SftpModifyPasswordService } from './services/sftp-modify-password.service';
import { NotificationsComponent } from './components/notifications/notifications.component';

import { DashboardService } from './services/dashboard.service';

@NgModule({
  imports: [HttpClientModule, CoreRoutingModule, SharedModule],
  declarations: [
    WebComponent,
    HeaderComponent,
    TabParentComponent,
    MenuComponent,
    FooterComponent,
    EmptyComponent,
    DashboardComponent,
    NotificationsComponent
  ],
  exports: [
    HeaderComponent,
    RouterModule,
    MenuComponent,
    FooterComponent,
    NotificationsComponent
  ],
  entryComponents: [EmptyComponent, DashboardComponent],
  providers: [
    TabsStateService,
    DashboardService,
    {
      multi: true,
      provide: HTTP_INTERCEPTORS,
      useClass: HTTPMock
    },
    communInterceptorProvider,
    SftpModifyPasswordService
  ]
})
export class CoreModule {
  constructor(public l10nLoader: L10nLoader) {
    this.l10nLoader.load();
  }
}
