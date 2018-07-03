import { TestBed, inject } from '@angular/core/testing';

import { BasicInfoService } from './basic-info.service';

xdescribe('BasicInfoService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [BasicInfoService]
    });
  });

  it('should be created', inject([BasicInfoService], (service: BasicInfoService) => {
    expect(service).toBeTruthy();
  }));
});
