import { TestBed, inject } from '@angular/core/testing';

import { PersonsService } from './persons.service';

xdescribe('PersonsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PersonsService]
    });
  });

  it('should be created', inject([PersonsService], (service: PersonsService) => {
    expect(service).toBeTruthy();
  }));
});
