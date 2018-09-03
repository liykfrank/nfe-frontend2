import { Agent } from './models/agent.model';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { SimpleChanges } from '@angular/core';
import { AgentService } from './services/agent.service';
import { AgentComponent } from './agent.component';
import { of } from 'rxjs/observable/of';
import { AgentFormModel } from './models/agent-form-model';
import { ResponseOptions } from '@angular/http';

describe('AgentComponent', () => {
  let comp: AgentComponent;
  let fixture: ComponentFixture<AgentComponent>;

  const agentServiceStub = jasmine.createSpyObj<AgentService>('AgentService', [
    'validateAgent'
  ]);

  const agent: Agent = {
    billingCity: 'CITY_78200021',
    billingCountry: 'COUNTRY_78200021',
    billingPostalCode: 'PC_78200021',
    billingStreet: 'STREET_78200021',
    defaultDate: null,
    formOfPayment: [
      { status: 'Non-Active', type: 'CC' },
      { status: 'Active', type: 'EP' }
    ],
    iataCode: '78200021',
    isoCountryCode: 'AA',
    name: 'AGENT_78200021',
    vatNumber: '00000078200021'
  };

  beforeEach(() => {
    // const simpleChangesStub = {
    //     agentCode: {}
    // };

    TestBed.configureTestingModule({
      declarations: [AgentComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [{ provide: AgentService, useValue: agentServiceStub }]
    });
    fixture = TestBed.createComponent(AgentComponent);
    comp = fixture.componentInstance;

    comp.model = new AgentFormModel();
    comp = fixture.componentInstance;
  });

  it('can load instance', () => {
    expect(comp).toBeTruthy();
  });

  it('showMoreDetails defaults to: true', () => {
    expect(comp.showMoreDetails).toEqual(true);
  });

  it('disabledMoreDetails defaults to: true', () => {
    expect(comp.disabledMoreDetails).toEqual(true);
  });

  it('display defaults to: false', () => {
    expect(comp.display).toEqual(false);
  });

  it('onClickMoreDetails/onClose', () => {
    expect(comp.display).toEqual(false);
    comp.onClickMoreDetails();
    expect(comp.display).toEqual(true);
    comp.onClose();
    expect(comp.display).toEqual(false);
  });

  describe('ngOnInit', () => {
    it('all OK', () => {
      agentServiceStub.validateAgent.and.returnValue(of(agent));

      comp.ngOnInit();
      comp.model.agentCode.setValue('1111111');
      comp.model.agentControlDigit.setValue('1');
      expect(agentServiceStub.validateAgent.calls.count()).toBe(1);
      expect(comp.disabledMoreDetails).toBe(false);

      agentServiceStub.validateAgent.calls.reset();
      comp.model.agentCode.setValue('1');
      comp.model.agentControlDigit.setValue('');
      expect(agentServiceStub.validateAgent.calls.count()).toBe(0);

      agentServiceStub.validateAgent.calls.reset();
      comp.agentVatNumberEnabled = false;
      comp.model.agentCode.setValue('1111111');
      comp.model.agentControlDigit.setValue('1');
      expect(agentServiceStub.validateAgent.calls.count()).toBe(1);
      expect(comp.disabledMoreDetails).toBe(false);
    });

    // it('error', () => {
    //   agentServiceStub.validateAgent.calls.reset();
    //   agentServiceStub.validateAgent.and.returnValue(of({body: {}, status: 404}));

    //   comp.ngOnInit();
    //   comp.model.agentCode.setValue('1111111');
    //   comp.model.agentControlDigit.setValue('1');
    //   expect(agentServiceStub.validateAgent.calls.count()).toBe(1);
    //   // expect(comp.disabledMoreDetails).toBe(true);
    //   expect(comp.model.agentVatNumber.errors.length).toBeGreaterThan(0);
    // });

  });
});
