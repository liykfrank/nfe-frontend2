import { DetailsRefundFormModel } from './../../models/details-refund-form.model';
import { HttpClientModule } from '@angular/common/http';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslationModule } from 'angular-l10n';
import { of } from 'rxjs/observable/of';

import { UserService } from '../../../../core/services/user.service';
import { l10nConfig } from '../../../../shared/base/conf/l10n.config';
import { IssueSharedModule } from '../../../../shared/issue-shared.module';
import { RefundConfiguration } from '../../models/refund-configuration.model';
import { RefundConfigurationService } from '../../services/refund-configuration.service';
import { DetailsRefundComponent } from './details-refund.component';
import { EnvironmentType } from '../../../../shared/enums/environment-type.enum';

describe('DetailsRefundComponent', () => {
  let comp: DetailsRefundComponent;
  let fixture: ComponentFixture<DetailsRefundComponent>;

  const refundConfigurationServiceStub = jasmine.createSpyObj<RefundConfigurationService>('RefundConfigurationService'
                                                              , [
                                                                'getConfiguration',
                                                                'getCountCuponsState',
                                                                'setCountCuponsState'
                                                              ]);
  refundConfigurationServiceStub.getConfiguration.and.returnValue(of(new RefundConfiguration()));
  refundConfigurationServiceStub.getCountCuponsState.and.returnValue(of(0));
  refundConfigurationServiceStub.setCountCuponsState.and.returnValue(of());

  const userServiceStub = jasmine.createSpyObj<UserService>('UserService', ['getUser']);
  userServiceStub.getUser.and.returnValue(
    of({
      id: '2d9429f0-fb99-4237-b2ce-bbfbb3e00ae9',
      name: 'Accelya User',
      lastname: 'Accelya Group',
      email: 'airline@accelya.com',
      isoc: 'AA',
      role: 'AGENT',
      iataCode: '78200021'
    })
  );

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        IssueSharedModule
      ],
      declarations: [DetailsRefundComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        { provide: UserService, useValue: userServiceStub },
        {
          provide: RefundConfigurationService,
          useValue: refundConfigurationServiceStub
        }
      ]
    });
    fixture = TestBed.createComponent(DetailsRefundComponent);
    comp = fixture.componentInstance;

    comp.model = new DetailsRefundFormModel();
  });

  it('can load instance', () => {
    expect(comp).toBeTruthy();
  });

  it('type defaults to: EnvironmentType.REFUND_INDIRECT', () => {
    expect(comp.type).toEqual(EnvironmentType.REFUND_INDIRECT);
  });

  describe('ngOnInit', () => {
    it('makes expected calls', () => {
      comp.ngOnInit();
      expect(userServiceStub.getUser).toHaveBeenCalled();
      expect(refundConfigurationServiceStub.getConfiguration).toHaveBeenCalled();
      expect(refundConfigurationServiceStub.getCountCuponsState).toHaveBeenCalled();
    });
  });
});
