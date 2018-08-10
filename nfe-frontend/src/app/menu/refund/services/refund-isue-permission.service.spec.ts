import { TestBed, inject } from '@angular/core/testing';

import { RefundIsuePermissionService } from './refund-isue-permission.service';

describe('RefundIsuePermissionControllerService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RefundIsuePermissionService]
    });
  });

  it('should be created', inject([RefundIsuePermissionService], (service: RefundIsuePermissionService) => {
    expect(service).toBeTruthy();
  }));
});
