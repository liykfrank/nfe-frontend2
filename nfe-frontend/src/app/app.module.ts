import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { L10nLoader, TranslationModule } from 'angular-l10n';
import { l10nConfig } from './shared/base/conf/l10n.config';
import { BsplInterceptor } from './bspl-interceptor';
import { UserService } from './core/services/user.service';

export function initApp(userService: UserService ) {
  return () => userService.initializeUser();
}

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    TranslationModule.forRoot(l10nConfig),
  ],
  declarations: [
    AppComponent
  ],
  providers: [
    UserService,
    {
      provide: APP_INITIALIZER,
      useFactory: initApp,
      deps: [UserService],
      multi: true
    },

  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule {
  constructor(public l10nLoader: L10nLoader) {
    this.l10nLoader.load();
  }
 }

