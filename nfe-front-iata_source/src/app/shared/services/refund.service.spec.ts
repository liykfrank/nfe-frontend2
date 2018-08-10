import { TestBed, inject } from '@angular/core/testing';

import { RefundService } from './refund.service';

describe('RefundService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RefundService]
    });
  });

  it('should be created', inject([RefundService], (service: RefundService) => {
    expect(service).toBeTruthy();
  }));
});
