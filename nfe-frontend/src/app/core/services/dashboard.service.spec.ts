import { asyncError } from '../../../testing/async-observable-helpers';
import { HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { inject, TestBed, async } from '@angular/core/testing';
import { LocalizationModule } from 'angular-l10n';
import { TranslationModule } from 'angular-l10n';
import { Observable } from 'rxjs/Observable';

import { l10nConfig } from './../../shared/base/conf/l10n.config';
import { CardIf } from './../models/card.model';
import { DashboardService } from './dashboard.service';

describe('DashboardService', () => {
  const IMG_PATH = '../../../../assets/images/dashboard/';
  const STATS1 = IMG_PATH + 'stats1.png';
  const STATS2 = IMG_PATH + 'stats2.png';
  const card1: CardIf = { title: 'test1', imgPath: IMG_PATH + STATS1 };
  const card2: CardIf = { title: 'test2', imgPath: IMG_PATH + STATS2 };
  const expectedCards: CardIf[] = [card1, card2];
  const error404: HttpErrorResponse = new HttpErrorResponse({error: 'test 404 error', status: 404, statusText: 'Not Found'});
  const dashboardServiceSpy = jasmine.createSpyObj('DashboardService', [
    'getCards'
  ]);
  const dashboardServiceErrorSpy = jasmine.createSpyObj('DashboardService', [
    'getCards'
  ]);

  dashboardServiceSpy.getCards.and.returnValue(Observable.of(expectedCards));
  dashboardServiceErrorSpy.getCards.and.returnValue(asyncError(error404));

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [
        { provide: DashboardService, useValue: dashboardServiceSpy },
        { provide: DashboardService, useValue: dashboardServiceErrorSpy }
      ]
    });
  });

  it(
    'should be created',
    inject([DashboardService], (service: DashboardService) => {
      expect(service).toBeTruthy();
    })
  );

  it('should return the expected cards', () => {
    dashboardServiceSpy
      .getCards()
      .subscribe(
        dashboard => expect(dashboard).toEqual(expectedCards, 'expected cards'),
        fail
      );
  });

  it('should return an error when the server returns a 404', () => {
    dashboardServiceErrorSpy
      .getCards()
      .subscribe(
        dashboard => fail('expected an error, not dashboard'),
        error => expect(error.message).toContain('404 Not Found')
      );
  });
});
