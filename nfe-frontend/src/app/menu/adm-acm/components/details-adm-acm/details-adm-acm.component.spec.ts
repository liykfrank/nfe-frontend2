import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs/observable/of';

import { EnvironmentType } from '../../../../shared/enums/environment-type.enum';
import { AcdmDetailsForm } from '../../models/acdm-details-form.model';
import { AdmAcmConfiguration } from '../../models/adm-acm-configuration.model';
import { AcdmConfigurationService } from '../../services/acdm-configuration.service';
import { DetailsAdmAcmComponent } from './details-adm-acm.component';

describe('DetailsAdmAcmComponent', () => {
  let comp: DetailsAdmAcmComponent;
  let fixture: ComponentFixture<DetailsAdmAcmComponent>;

  const acdmConfigurationServiceStub = jasmine.createSpyObj<
    AcdmConfigurationService
  >('AcdmConfigurationService', ['getConfiguration']);
  acdmConfigurationServiceStub.getConfiguration.and.returnValue(
    of(new AdmAcmConfiguration())
  );

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DetailsAdmAcmComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        {
          provide: AcdmConfigurationService,
          useValue: acdmConfigurationServiceStub
        }
      ]
    });
    fixture = TestBed.createComponent(DetailsAdmAcmComponent);
    comp = fixture.componentInstance;
    comp.model = new AcdmDetailsForm();
  });

  it('can load instance', () => {
    expect(comp).toBeTruthy();
  });

  it('type defaults to: EnvironmentType.ACDM', () => {
    expect(comp.type).toEqual(EnvironmentType.ACDM);
  });
});
