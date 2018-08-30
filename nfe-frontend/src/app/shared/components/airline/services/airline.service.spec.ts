import { TestBed, inject } from '@angular/core/testing';
import { AirlineService } from './airline.service';
import { HttpClient } from '@angular/common/http';
import { of } from 'rxjs/observable/of';

describe('AirlineService', () => {

  const httpClientStub = jasmine.createSpyObj<HttpClient>('HttpClient', [
    'get'
  ]);
  httpClientStub.get.and.returnValue(of());


  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        { provide: HttpClient, useValue: httpClientStub },
        AirlineService
      ]
    });
  });

  it('should be created', inject([AirlineService], (service: AirlineService) => {
    expect(service).toBeTruthy();
  }));

  it('validateAirlinet', inject([AirlineService], (service: AirlineService) => {
    expect(service.validateAirlinet(false, '123', 'AA')).toBeTruthy();
  }));

});
