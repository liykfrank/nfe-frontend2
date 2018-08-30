import { TestBed, inject } from '@angular/core/testing';

import { AgentService } from './agent.service';
import { HttpClient } from '@angular/common/http';
import { of } from 'rxjs/observable/of';
import { EnvironmentType } from '../../../enums/environment-type.enum';

describe('AgentService', () => {
  const httpClientStub = jasmine.createSpyObj<HttpClient>('HttpClient', [
    'get'
  ]);
  httpClientStub.get.and.returnValue(of());

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        { provide: HttpClient, useValue: httpClientStub },
        AgentService
      ]
    });
  });

  it('should be created', inject([AgentService], (service: AgentService) => {
    expect(service).toBeTruthy();
  }));

  it('validateAgent', inject([AgentService], (service: AgentService) => {
    expect(service.validateAgent(EnvironmentType.REFUND_INDIRECT, '11111111')).toBeTruthy();
  }));

});
