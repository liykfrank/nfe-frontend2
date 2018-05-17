import { NgModule, ErrorHandler } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MyApp } from './app.component';
import { HomePage } from './pages/home/home';
import { SharedModule } from '../shared/shared.module';
import { IonicApp, IonicErrorHandler, IonicModule } from 'ionic-angular';
import { SplashScreen } from '@ionic-native/splash-screen';
import { StatusBar } from '@ionic-native/status-bar';

@NgModule({
  declarations: [
    MyApp,
    HomePage,
  ],
  imports: [
    SharedModule,
    IonicModule.forRoot(MyApp)
   //BrowserModule

  ],
   entryComponents: [
    MyApp,
    HomePage,
  ],
  exports:[IonicApp],
  providers: [
    StatusBar,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler}
  ]
})
export class IonicAppModule { }
