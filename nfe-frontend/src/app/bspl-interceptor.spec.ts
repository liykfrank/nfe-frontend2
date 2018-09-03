import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { inject, TestBed } from '@angular/core/testing';
import { of } from 'rxjs/observable/of';

import { BsplInterceptor } from './bspl-interceptor';
import { AlertsService } from './core/services/alerts.service';

describe('BsplInterceptor', () => {
  const alertsServiceStub = jasmine.createSpyObj<AlertsService>(
    'AlertsService',
    ['setAlertTranslate']
  );
  alertsServiceStub.setAlertTranslate.and.returnValue(of());

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        { provide: AlertsService, useValue: alertsServiceStub },
        {
          provide: HTTP_INTERCEPTORS,
          useClass: BsplInterceptor,
          multi: true
        }
      ]
    });
  });

  it('should add an Authorization header ', inject(
    [HttpClient, HttpTestingController],
    (http: HttpClient, mock: HttpTestingController) => {
      http.get('/api').subscribe(response => expect(response).toBeTruthy());
      const request = mock.expectOne(req => req.headers.has('Authorization'));
    }
  ));
});
