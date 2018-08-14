import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { Card } from '../../core/models/card.model';
import { DashboardService } from '../../core/services/dashboard.service';


@Component({
  selector: 'bspl-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements AfterViewInit, OnInit {

  @ViewChild('myDocking') myDocking;
  jqxDockingComponent;
  dashboard: Card[];
  cardsCoords: any;

  MODE: string;
  ORIENTATION: string;
  WIDTH: number;
  CARDS_PER_ROW: number;


  constructor(
    private dashboardService: DashboardService
  ) {
    this.MODE = 'docked';
    this.ORIENTATION = 'horizontal';
    this.WIDTH = 1000;
    this.CARDS_PER_ROW = 4;

    this.cardsCoords = {};
  }


  ngOnInit(): void {
    this.dashboardService.getCards().subscribe(dash => {
      this.dashboard = dash;
    });
  }

  checkDashboard() {
    return this.dashboard && this.dashboard.length > 0;
  }

  ngAfterViewInit(): void {

    this.myDocking.setOptions({
      cookies: true,
      mode: this.MODE,
      orientation: this.ORIENTATION,
      width: this.WIDTH
    });

    for (
      let i = 0, position = 0, panel = 0;
      i < this.dashboard.length;
      i++, panel = i % this.CARDS_PER_ROW, position = Math.floor(i / this.CARDS_PER_ROW)
    ) {
      this.cardsCoords[this.dashboard[i].title] = {
        panel: panel,
        position: position
      };

      this.myDocking.addWindow(
        this.dashboard[i].title,
        this.MODE,
        panel,
        position
      );
    }

  }


  public dragEnd(event: any): void {

    const sourceWindowId: string = event.args.window;
    const targetWindowId: string = this.myDocking.widgetObject._draggingItem[0]
      .nextSibling.id;

    // targetWindowId != null and targetWindowId != ''
    if (targetWindowId) {
      this.updatePositions(sourceWindowId, targetWindowId);
    }

    this.relocateCardsInWiew();

  }


  private updatePositions (sourceWindowId: string, targetWindowId: string): void {
    let sourceCoords, targetCoords;
    sourceCoords = { panel: this.cardsCoords[sourceWindowId].panel, position: this.cardsCoords[sourceWindowId].position };
    targetCoords = { panel: this.cardsCoords[targetWindowId].panel, position: this.cardsCoords[targetWindowId].position };

    this.cardsCoords[sourceWindowId] = targetCoords;
    this.cardsCoords[targetWindowId] = sourceCoords;

  }


  private relocateCardsInWiew (): void {

    for (const windowId in this.cardsCoords) {
      if (this.cardsCoords.hasOwnProperty(windowId)) {
        this.myDocking.move(
          windowId,
          this.cardsCoords[windowId]['panel'],
          this.cardsCoords[windowId]['position']
        );
      }
    }

  }


}
