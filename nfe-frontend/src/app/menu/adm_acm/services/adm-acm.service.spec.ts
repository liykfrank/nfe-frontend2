import { TestBed, inject } from '@angular/core/testing';

import { AdmAcmService } from './adm-acm.service';

xdescribe('AdmAcmService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AdmAcmService]
    });
  });

  it('should be created', inject([AdmAcmService], (service: AdmAcmService) => {
    expect(service).toBeTruthy();
  }));
});
