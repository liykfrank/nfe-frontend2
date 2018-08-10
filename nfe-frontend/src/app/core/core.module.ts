import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CoreRoutingModule } from './core-routing.module';
import { CoreComponent } from './core.component';
import { HeaderComponent } from './components/header/header.component';
import { MenuComponent } from './components/menu/menu.component';
import { MenuTabsComponent } from '../shared/components/menu-tabs/menu-tabs.component';
import { FooterComponent } from './components/footer/footer.component';
import { NotificationsComponent } from './components/notifications/notifications.component';
import { AlertsComponent } from './components/alerts/alerts.component';
import { AlertsService } from './services/alerts.service';
import { MessageService } from 'primeng/components/common/messageservice';
import { GrowlModule } from 'primeng/growl';
import { DialogModule } from 'primeng/dialog';
import { HttpClientModule } from '@angular/common/http';
import { TranslationModule } from 'angular-l10n';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  imports: [
    HttpClientModule,
    CoreRoutingModule,
    GrowlModule,
    DialogModule,
    SharedModule
  ],
  declarations: [
    CoreComponent,
    HeaderComponent,
    MenuComponent,
    MenuTabsComponent,
    FooterComponent,
    NotificationsComponent,
    AlertsComponent,
  ],
  providers: [
    AlertsService,
    MessageService
  ],
})

export class CoreModule {
}
