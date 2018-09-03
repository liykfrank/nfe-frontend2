import { inject, TestBed } from '@angular/core/testing';
import { FormArray, FormControl, FormGroup } from '@angular/forms';

import { UtilsService } from './utils.service';

describe('UtilsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UtilsService]
    });
  });

  it('should be created', inject([UtilsService], (service: UtilsService) => {
    expect(service).toBeTruthy();
  }));

  it('execFn', inject([UtilsService], (service: UtilsService) => {
    let aux = 'TEST';

    service.execFn(aux, () => (aux += 'BB'));
    expect(aux).toBe('TESTBB');

    service.execFn(null, () => (aux += 'BB'));
    expect(aux).toBe('TESTBB');
  }));

  it('cloneObj', inject([UtilsService], (service: UtilsService) => {
    const aux = { a: 0, b: { c: 0 } };
    const aux2 = service.cloneObj(aux);
    expect(JSON.stringify(aux2)).toBe(JSON.stringify(aux));
  }));

  describe('Forms Functions', () => {
    it('touchAllForms', inject([UtilsService], (service: UtilsService) => {
      const form = new FormGroup({
        form2: new FormGroup({
          elem: new FormControl()
        })
      });

      service.touchAllForms([form]);
      expect(form.get('form2').get('elem').touched).toBe(true);
    }));

    it('setBackErrorsOnForms', inject(
      [UtilsService],
      (service: UtilsService) => {
        const form = new FormGroup({
          form2: new FormGroup({
            elem2: new FormControl(),
            array: new FormArray([
              new FormGroup({
                elem_array: new FormControl()
              })
            ])
          }),
          form3: new FormGroup({
            elem3: new FormControl(),
            elem3_1: new FormControl()
          })
        });

        const errors = [];
        errors.push({ fieldName: 'array.0.elem_array', message: 'TEST' });
        errors.push({ fieldName: 'elem3_1', message: 'TEST2' });
        errors.push({ fieldName: 'NO_ELEM', message: 'TEST3' });
        errors.push({ fieldName: null, message: 'TEST4' });

        const notFound = service.setBackErrorsOnForms([form], errors);

        expect(form.get('form3').get('elem3_1').errors).toBeTruthy();
        expect(form.get('form3').get('elem3_1').touched).toBe(true);

        expect(form.get('form2').get('array').get('0').get('elem_array').errors).toBeTruthy();
        expect(form.get('form2').get('array').get('0').get('elem_array').touched).toBe(true);

        expect(notFound.length).toBeGreaterThan(0);
      }
    ));
  });
});
