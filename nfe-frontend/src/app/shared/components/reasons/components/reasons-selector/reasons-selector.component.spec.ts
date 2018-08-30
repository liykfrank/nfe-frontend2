import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs/observable/of';

import { ReasonsService } from '../../services/reasons.service';
import { ReasonsSelectorComponent } from './reasons-selector.component';

describe('ReasonsSelectorComponent', () => {
  let component: ReasonsSelectorComponent;
  let fixture: ComponentFixture<ReasonsSelectorComponent>;

  const reasonsServiceStub = jasmine.createSpyObj<ReasonsService>(
    'ReasonsService',
    ['getReasonsByAdcms', 'getReasonsByIndirectRefund']
  );
  reasonsServiceStub.getReasonsByAdcms.and.returnValue(of());
  reasonsServiceStub.getReasonsByIndirectRefund.and.returnValue(of());

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      providers: [
        {
          provide: ReasonsService,
          useValue: reasonsServiceStub
        }
      ],
      declarations: [ReasonsSelectorComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReasonsSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
