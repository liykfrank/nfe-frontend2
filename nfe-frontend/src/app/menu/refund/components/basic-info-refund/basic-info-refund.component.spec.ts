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
import { RefundIsuePermissionService } from '../../services/refund-isue-permission.service';
import { BasicInfoRefundFormModel } from './../../models/basic-info-refund-form.model';
import { BasicInfoRefundComponent } from './basic-info-refund.component';

describe('BasicInfoRefundComponent', () => {
  let comp: BasicInfoRefundComponent;
  let fixture: ComponentFixture<BasicInfoRefundComponent>;

  const refundConfigurationServiceStub = jasmine.createSpyObj<RefundConfigurationService>('RefundConfigurationService', ['getConfiguration']);
  refundConfigurationServiceStub.getConfiguration.and.returnValue(
    of(new RefundConfiguration())
  );

  const userServiceStub = jasmine.createSpyObj<UserService>('UserService', ['getUser']);
  userServiceStub.getUser.and.returnValue(of());

  beforeEach(() => {
    const refundIsuePermissionServiceStub = {
      getRefundIssuePermission: () => ({
        subscribe: () => ({})
      })
    };
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        IssueSharedModule
      ],
      declarations: [BasicInfoRefundComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        { provide: UserService, useValue: userServiceStub },
        {
          provide: RefundConfigurationService,
          useValue: refundConfigurationServiceStub
        },
        {
          provide: RefundIsuePermissionService,
          useValue: refundIsuePermissionServiceStub
        }
      ]
    }).compileComponents();
    fixture = TestBed.createComponent(BasicInfoRefundComponent);
    comp = fixture.componentInstance;

    comp.model = new BasicInfoRefundFormModel();
  });

  it('can load instance', () => {
    expect(comp).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('makes expected calls', () => {
      comp.ngOnInit();
      expect(userServiceStub.getUser).toHaveBeenCalled();
      expect(
        refundConfigurationServiceStub.getConfiguration
      ).toHaveBeenCalled();
    });
  });

  // describe('onChangeAirline', () => {
  //   it('makes expected calls', () => {
  //     const refundIsuePermissionServiceStub: RefundIsuePermissionService = fixture.debugElement.injector.get(
  //       RefundIsuePermissionService
  //     );
  //     spyOn(refundIsuePermissionServiceStub, 'getRefundIssuePermission');
  //     comp.onChangeAirline();
  //     expect(
  //       refundIsuePermissionServiceStub.getRefundIssuePermission
  //     ).toHaveBeenCalled();
  //   });
  // });
});
