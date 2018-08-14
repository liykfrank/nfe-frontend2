import { TestBed, inject } from '@angular/core/testing';

import { RefundIndirectService } from './refund-indirect.service';

describe('RefundIndirectService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RefundIndirectService]
    });
  });

  it('should be created', inject([RefundIndirectService], (service: RefundIndirectService) => {
    expect(service).toBeTruthy();
  }));
});
