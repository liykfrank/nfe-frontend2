import { TestBed, inject } from '@angular/core/testing';

import { TabsStateService } from './tabs-state.service';

describe('TabsStateService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TabsStateService]
    });
  });

  it('should be created', inject([TabsStateService], (service: TabsStateService) => {
    expect(service).toBeTruthy();
  }));
});
