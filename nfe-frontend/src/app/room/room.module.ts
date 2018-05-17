import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";

import { ShowRoomComponent } from "./pages/show-room/show-room.component";
import { RoomsRoutingModule } from "./room-routing.module";
import { ShowTableComponent } from "./pages/show-table/show-table.component";
import { ShowFilterComponent } from "./pages/show-filter/show-filter.component";
import { SharedModule } from "../shared/shared.module";
import { ShowTabsComponent } from "./pages/show-tabs/show-tabs.component";
import { ShowInternatComponent } from "./pages/show-internat/show-internat.component";
import { TranslationModule } from "angular-l10n";
import { ShowPersonsComponent } from "./pages/show-persons/show-persons.component";
import { PersonsService } from "./resources/persons.service";
import { ShowNewGridComponent } from './pages/show-new-grid/show-new-grid.component';
import { ShowPageTableComponent } from './pages/show-page-table/show-page-table.component';
import { ShowPrimesComponent } from './pages/show-primes/show-primes.component';
import { FormsModule } from "@angular/forms";

@NgModule({
  imports: [CommonModule, RoomsRoutingModule, SharedModule],
  declarations: [
    ShowRoomComponent,
    ShowTableComponent,
    ShowFilterComponent,
    ShowTabsComponent,
    ShowInternatComponent,
    ShowPersonsComponent,
    ShowNewGridComponent,
    ShowPageTableComponent,
    ShowPrimesComponent
  ],
  providers: [PersonsService]
})
export class RoomModule {}
