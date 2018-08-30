import { HttpClient } from '@angular/common/http';
import { inject, TestBed } from '@angular/core/testing';
import { of } from 'rxjs/observable/of';

import { TemplateService } from './template.service';

describe('TemplateService', () => {

  const httpClientStub = jasmine.createSpyObj<HttpClient>('HttpClient', [
    'get'
  ]);
  httpClientStub.get.and.returnValue(of());

    beforeEach(() => {
      TestBed.configureTestingModule({
        providers: [
        { provide: HttpClient, useValue: httpClientStub }, TemplateService]
      });

    });

    it('works', inject(
      [TemplateService],
      (service: TemplateService) => {
        expect(service).toBeTruthy();
      }
    ));

});
