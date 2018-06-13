import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';

import { Card } from '../models/card.model';

const IMG_PATH = '../../../../assets/images/dashboard/';
const STATS1 = IMG_PATH + 'stats1.png';
const STATS2 = IMG_PATH + 'stats2.png';

@Injectable()
export class DashboardService {
  dashboard: Card[];

  constructor() {
    this.dashboard = [
      new Card('ADM', STATS1),
      new Card('Files', STATS1),
      new Card('Refunds', STATS2),
      new Card('Alerts', STATS2)//,
      // new Card('Alerts2', STATS2),
      // new Card('Alerts3', STATS2),
      // new Card('Alerts4', STATS2)
    ];
  }

  getCards(): Observable<Card[]> {
    return Observable.of(this.dashboard);
  }
}
