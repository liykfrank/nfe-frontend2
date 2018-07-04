import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContactComponent } from './contact.component';
import { SharedModule } from '../../shared.module';

describe('ContactComponent', () => {

  let component: ContactComponent;
  let fixture: ComponentFixture<ContactComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedModule],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContactComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  it('change', () => {
    let data;
    component.onChange.subscribe(val => data = val);
    component.name = 'TEXT';
    component.change();
    expect(data).toBeTruthy();
    expect(data.contactName).toBe('TEXT');
  });

});
