import { HttpClientModule } from '@angular/common/http';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { TranslationModule } from 'angular-l10n';
import { ScrollPanelModule } from 'primeng/primeng';

import { l10nConfig } from '../../../../base/conf/l10n.config';
import { HistoryComponent } from './history.component';

describe('HistoryComponent', () => {
  let component: HistoryComponent;
  let fixture: ComponentFixture<HistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        ScrollPanelModule,
        TranslationModule.forRoot(l10nConfig),
        HttpClientModule,
        FormsModule
      ],
      declarations: [HistoryComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('filter 1', () => {
    component.history.push({action: 'REFUND_FILE', insertDateTime: new Date(), fileName: '', id: 0});
    component.history.push({action: 'REFUND_ISSUE', insertDateTime: new Date(), fileName: '', id: 0});
    component.history.push({action: 'MODIFY', insertDateTime: new Date(), fileName: '', id: 0});
    component.history.push({action: 'ATTACH_FILE', insertDateTime: new Date(), fileName: '', id: 0});
    component.history.push({action: 'ADD_COMMENT', insertDateTime: new Date(), fileName: '', id: 0});
    component.history.push({action: 'MASSLOAD_UPDATE', insertDateTime: new Date(), fileName: '', id: 0});

    const list = component.history;

    expect(component.checkFilter).toBe(true);
    component.filter();
    expect(component.history.length).toBe(list.length);
  });

  it('filter 2', () => {
    component.history.push({action: 'REFUND_FILE', insertDateTime: new Date(), fileName: '', id: 0});
    component.history.push({action: 'REFUND_ISSUE', insertDateTime: new Date(), fileName: '', id: 0});
    component.history.push({action: 'MODIFY', insertDateTime: new Date(), fileName: '', id: 0});
    component.history.push({action: 'ATTACH_FILE', insertDateTime: new Date(), fileName: '', id: 0});
    component.history.push({action: 'ADD_COMMENT', insertDateTime: new Date(), fileName: '', id: 0});
    component.history.push({action: 'MASSLOAD_UPDATE', insertDateTime: new Date(), fileName: '', id: 0});

    const list = component.history;

    component.listExcludes = ['ATTACH_FILE']

    expect(component.checkFilter).toBe(true);
    component.checkFilter = false;
    component.filter();
    expect(component.history.length).toBeLessThan(list.length);
  });

});
