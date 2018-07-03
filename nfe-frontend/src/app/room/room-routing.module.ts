
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ShowTableComponent } from './pages/show-table/show-table.component';
import { ShowRoomComponent } from './pages/show-room/show-room.component';
import { ShowFilterComponent } from './pages/show-filter/show-filter.component';
import { ShowTabsComponent } from './pages/show-tabs/show-tabs.component';
import { ShowInternatComponent } from './pages/show-internat/show-internat.component';
import { ShowPersonsComponent } from './pages/show-persons/show-persons.component';
import { ShowPrimesComponent } from './pages/show-primes/show-primes.component';

const routes: Routes = [
  { path: 'show-room', component: ShowRoomComponent,
    children: [
      { path: '', redirectTo: 'table', pathMatch: 'full' },
      { path: 'table', component: ShowTableComponent },
      { path: 'filter', component: ShowFilterComponent },
      { path: 'tabs', component: ShowTabsComponent },
      { path: 'internat', component: ShowInternatComponent },
      { path: 'persons', component: ShowPersonsComponent },
      { path: 'primes-comp', component: ShowPrimesComponent }
    ]
  }
 ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RoomsRoutingModule { }
