import { Location } from '@angular/common';
import {
  async,
  ComponentFixture,
  fakeAsync,
  TestBed,
  tick
} from '@angular/core/testing';
import { ActivatedRoute, Router, Routes } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { jqxDockingComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxdocking';

import { ROUTES } from '../../constants/routes';
import { DashboardComponent } from './../../../menu/dashboard/dashboard.component';
import { DashBoardModule } from './../../../menu/dashboard/dashboard.module';
import { MenuTabsComponent } from './menu-tabs.component';

xdescribe('MenuTabsComponent Empty', () => {
  let component: MenuTabsComponent;
  let fixture: ComponentFixture<MenuTabsComponent>;

  let location: Location;
  let router: Router;

  const fakeActivatedRoute = {
    snapshot: { data: {} },
    root: {
      firstChild: {
        firstChild: {
          firstChild: {
            snapshot: {
              data: {

              }
            }
          }
        }
      }
    }
  } as ActivatedRoute;

  const routes: Routes = [
    {
      path: '',
      component: MenuTabsComponent,
      children: [
        {
          path: '',
          loadChildren: () => DashBoardModule,
          data: { tab: ROUTES.DASHBOARD }
        },
        {
          path: 'dashboard',
          loadChildren: () => DashBoardModule,
          data: { tab: ROUTES.DASHBOARD }
        },
        {
          path: 'module',
          loadChildren: () => DashBoardModule,
          data: { tab: ROUTES.DASHBOARD }
        }
      ]
    }
  ];

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes(routes), DashBoardModule],
      declarations: [MenuTabsComponent],
      providers: [
        Location,
        ActivatedRoute
        /* { provide: ActivatedRoute, useValue: fakeActivatedRoute } */
      ]
    }).compileComponents();

    router = TestBed.get(Router);
    location = TestBed.get(Location);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MenuTabsComponent);
    component = fixture.componentInstance;

    router.initialNavigation();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('add tab', () => {
    console.log(location);
    expect(component.tabList.length).toBe(1);
  });

  xit('navigate to',
    fakeAsync(() => {
      router.navigate(['/monitor']);
      tick(50);
      expect(location.path()).toBe('/monitor');
    })
  );
});

describe('MenuTabsComponent Not Empty', () => {
  let component: MenuTabsComponent;
  let fixture: ComponentFixture<MenuTabsComponent>;

  const fakeActivatedRoute = {
    snapshot: { data: {} }
  } as ActivatedRoute;

  const mockGetItem = {
    getItem: (key: string): string => {
      const ret = [
        {
          label: ROUTES.DASHBOARD.LABEL,
          url: ROUTES.DASHBOARD.URL,
          active: true
        }
      ];

      return JSON.stringify(ret);
    }
  };

  beforeEach(async(() => {
    spyOn(localStorage, 'getItem').and.callFake(mockGetItem.getItem);

    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [
        jqxDockingComponent,
        DashboardComponent,
        MenuTabsComponent
      ],
      providers: [{ provide: ActivatedRoute, useValue: fakeActivatedRoute }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MenuTabsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create with array on localStorage', () => {
    expect(component).toBeTruthy();
  });
});
