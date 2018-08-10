import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PillsCollapsableComponent } from './pills-collapsable.component';
import { Pill } from '../../models/pill.model';
import { GLOBALS } from '../../constants/globals';

describe('PillsCollapsableComponent', () => {
  let component: PillsCollapsableComponent;
  let fixture: ComponentFixture<PillsCollapsableComponent>;

  const pill1 = new Pill('id1', 'label1');
  const pill2 = new Pill('id2', 'label2');
  const pill3 = new Pill('id3', 'label3');

  const idReference = 'my-id';

  const pills: Pill[] = [
    pill1,
    pill2,
    pill3
  ];

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PillsCollapsableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PillsCollapsableComponent);
    component = fixture.componentInstance;
    component.pills = pills;
    component.idReference = idReference;
    component.collapseAll = true;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('Click collapse', () => {
    component.clickCollapse();
    expect(component.collapseAll).toBeFalsy();
  });

  it('Click pill', () => {
    let emitted = false;
    component.returnRefreshPills.subscribe(() => {
      emitted = true;
    });
    component.clickPill(pill1);
    expect(component.pills[0].active).toBeTruthy();
    expect(component.pills[0].collapsable).toBeTruthy();
    expect(component.pills[1].active).toBeFalsy();
    expect(component.pills[2].active).toBeFalsy();
    expect(component.collapseAll).toBeTruthy();
    expect(emitted).toBeTruthy();
  });

  it('Get Collapse Img', () => {
    expect(component.getCollapseImgSrc()).toBe(GLOBALS.ICONS.COLLAPSE_SRC);
  });
});
