import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SharedModule } from './../../../shared/shared.module';
import { DashboardService } from '../../services/dashboard.service';
import { Observable } from 'rxjs/Observable';
import { Card, CardIf } from './../../models/card.model';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardComponent } from './dashboard.component';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;

  const IMG_PATH = '../../../../assets/images/dashboard/';
  const STATS1 = IMG_PATH + 'stats1.png';
  const STATS2 = IMG_PATH + 'stats2.png';
  const card1: CardIf = {'title': 'test1', 'imgPath': IMG_PATH + STATS1};
  const card2: CardIf = {'title': 'test2', 'imgPath': IMG_PATH + STATS2};
  const cardsMock: CardIf[] = [card1, card2];
  const dashboardServiceSpy = jasmine.createSpyObj('DashboardService', ['getCards']);

  dashboardServiceSpy.getCards.and.returnValue(Observable.of(cardsMock));
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedModule],
      declarations: [ DashboardComponent ],
      providers: [{provide: DashboardService, useValue: dashboardServiceSpy}]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set options in ngAfterViewInit', () => {
    const widgetObject = component.myDocking.widgetObject;
    expect([
      widgetObject.mode,
      widgetObject.orientation,
      component.CARDS_PER_ROW
    ]).toEqual(['docked', 'horizontal', 4]);
  });

  it('should charge cardsCoords in ngAfterViewInit', async(() => {
      const cardsCords = { test1: { panel: 0, position: 0 }, test2: { panel: 1, position: 0 } };
      expect(component.cardsCoords).toEqual(cardsCords);
    }));

  it('should charge windowOptions in widgetObject', async(() => {
      const windowOptions = component.myDocking.widgetObject._windowOptions;
      const expectedWindowOptions = { test1: { mode: 'docked', resizable: true, size: windowOptions.test1.size }, test2: { mode: 'docked', resizable: true, size: windowOptions.test2.size } };

      expect(windowOptions).toEqual(expectedWindowOptions);
    }));

  it('should update positions on dragging', async(() => {
      const event = { args: { window: 'test1' } };
      const cardsCords = { test1: { panel: 1, position: 0 }, test2: { panel: 0, position: 0 } };

      component.myDocking.widgetObject = { _draggingItem: [{ nextSibling: { id: 'test2' } }] };
      component.ngAfterViewInit();
      component.dragEnd(event);

      expect(component.cardsCoords).toEqual(cardsCords);
    }));

  it('should not update positions on dragging', async(() => {
      const event = { args: { window: 'test1' } };
      const cardsCords = { test1: { panel: 0, position: 0 }, test2: { panel: 1, position: 0 } };

      component.myDocking.widgetObject = { _draggingItem: [{ nextSibling: { id: '' } }] };
      component.ngAfterViewInit();
      component.dragEnd(event);

      expect(component.cardsCoords).toEqual(cardsCords);
    }));

  it('should call relocateCardsInWiew method 2 times on dragging', async(() => {
      const event = { args: { window: 'test1' } };
      const cardsCords = { test1: { panel: 0, position: 0 }, test2: { panel: 1, position: 0 } };
      const spyRelocateCardsInWiew = spyOn<any>(component, 'relocateCardsInWiew');

      component.ngAfterViewInit();

      component.myDocking.widgetObject = { _draggingItem: [{ nextSibling: { id: '' } }] };
      component.dragEnd(event);

      component.myDocking.widgetObject = { _draggingItem: [{ nextSibling: { id: 'test1' } }] };
      component.dragEnd(event);

      expect(spyRelocateCardsInWiew).toHaveBeenCalledTimes(2);



    }));

});
