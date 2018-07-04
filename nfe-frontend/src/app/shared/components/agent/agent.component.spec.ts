import { By } from '@angular/platform-browser';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { AgentComponent } from './agent.component';
import { ContactComponent } from '../contact/contact.component';
import { SharedModule } from '../../shared.module';
import { AlertsService } from '../../../core/services/alerts.service';
import { Contact } from '../../models/contact.model';

describe('AgentComponent', () => {
  let component: AgentComponent;
  let fixture: ComponentFixture<AgentComponent>;

  const _AlertsService = jasmine.createSpyObj<AlertsService>(
    'AlertsService',
    ['setAlertTranslate']
  );

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      providers: [
        {provide: AlertsService, useValue: _AlertsService}
      ],
      imports: [SharedModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AgentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('onValueChanged', () => {
    _AlertsService.setAlertTranslate.and.returnValue(Observable.of({}));
    let data;
    component.onChangeAgentCode.subscribe(val => data = val);
    component.agentCode = '1111111';
    component.agentCheckDigit = '2';
    component.onValueChanged();

    expect(_AlertsService.setAlertTranslate.calls.count()).toBe(1);

    component.agentCheckDigit = '1';
    component.onValueChanged();
    expect(data).toBeTruthy();
  });

  it('changeVatNumber', () => {
    let data;
    component.onChangeVatNumber.subscribe(val => data = val);
    component.vatNumber = 'TEXT';
    component.changeVatNumber();
    expect(data).toBeTruthy();
  });

  it('changeCompanyReg', () => {
    let data;
    component.onChangeCompanyReg.subscribe(val => data = val);
    component.companyReg = 'TEXT';
    component.changeCompanyReg();
    expect(data).toBeTruthy();
  });

  it('changeContact', () => {
    let data;
    component.onChangeContact.subscribe(val => data = val);
    const contact: Contact = new Contact();
    contact.contactName = 'TEXT';
    contact.email = 'TEXT';
    contact.phoneFaxNumber = 'TEXT';
    component.changeContact(contact);
    expect(data).toBeTruthy();
  });

  it('clickMoreDetails', () => {
    let data = false;
    component.onClickMoreDetails.subscribe(val => data = true);

    component.clickMoreDetails();
    expect(data).toBe(true);
  });




});
