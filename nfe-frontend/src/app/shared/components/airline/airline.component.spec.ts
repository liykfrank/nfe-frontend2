import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AirlineComponent } from './airline.component';
import { SharedModule } from '../../shared.module';
import { Contact } from '../../models/contact.model';

describe('AirlineComponent', () => {
  let component: AirlineComponent;
  let fixture: ComponentFixture<AirlineComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AirlineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('changeAirline', () => {
    let data;
    component.onChangeAirlineCode.subscribe(val => data = val);
    component.airlineCode = 'TEX';
    component.changeAirline();
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
