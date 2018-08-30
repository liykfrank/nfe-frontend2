import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslationModule, TranslationService } from 'angular-l10n';

import { Router } from '../../../../../../node_modules/@angular/router';
import { l10nConfig } from '../../../../shared/base/conf/l10n.config';
import { AgentService } from '../../../../shared/components/agent/services/agent.service';
import { GLOBALS } from '../../../../shared/constants/globals';
import { UserViews } from '../../../../shared/enums/users.enum';
import { TemplateService } from '../../services/template.service';
import { SharedModule } from './../../../../shared/shared.module';
import { CountryTerritoryService } from './services/country-territory.service';
import { UserComponent } from './user.component';

describe('UserComponent', () => {
  let comp: UserComponent;
  let fixture: ComponentFixture<UserComponent>;

  beforeEach(() => {
    const routerStub = {
      routerState: {
        snapshot: {
          url: {}
        }
      }
    };
    const translationServiceStub = {
      translate: () => ({})
    };
    const countryTerritoryServiceStub = {
      countryTerritory: {}
    };
    const agentServiceStub = {
      validateAgent: () => ({})
    };
    const templateServiceStub = {
      restoreTemplate: () => ({}),
      removeTemplate: () => ({})
    };

    TestBed.configureTestingModule({
      imports: [SharedModule, TranslationModule.forRoot(l10nConfig)],
      declarations: [UserComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        { provide: Router, useValue: routerStub },
        { provide: TranslationService, useValue: translationServiceStub },
        { provide: TemplateService, useValue: templateServiceStub },
        {
          provide: CountryTerritoryService,
          useValue: countryTerritoryServiceStub
        },
        { provide: AgentService, useValue: agentServiceStub }
      ]
    });

    spyOn(UserComponent.prototype, 'getView');

    fixture = TestBed.createComponent(UserComponent);
    comp = fixture.componentInstance;
  });

  it('can load instance', () => {
    expect(comp).toBeTruthy();
  });

  it('typesOfScreens defaults to: UserViews', () => {
    expect(comp.typesOfScreens).toEqual(UserViews);
  });

  it('screenType defaults to: typesOfScreens.NEW_USER', () => {
    expect(comp.screenType).toEqual(UserViews.NEW_USER);
  });

  it('patterns defaults to: GLOBALS.PATTERNS', () => {
    expect(comp.patterns).toEqual(GLOBALS.PATTERNS);
  });

  describe('constructor', () => {
    it('makes expected calls', () => {
      expect(UserComponent.prototype.getView).toHaveBeenCalled();
    });
  });

  // describe('ngOnInit', () => {
  //   it('makes expected calls', () => {
  //     spyOn(comp, 'isViewUser');
  //     spyOn(comp, 'isViewModUser');
  //     comp.ngOnInit();
  //     expect(comp.isViewUser).toHaveBeenCalled();
  //     expect(comp.isViewModUser).toHaveBeenCalled();
  //   });
  // });

  describe('getTextButton', () => {
    it('makes expected calls', () => {
      const translationServiceStub: TranslationService = fixture.debugElement.injector.get(
        TranslationService
      );
      spyOn(translationServiceStub, 'translate');
      comp.getTextButton();
      expect(translationServiceStub.translate).toHaveBeenCalled();
    });
  });
});
