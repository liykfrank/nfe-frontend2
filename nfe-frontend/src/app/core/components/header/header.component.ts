import { TabsStateService } from './../../services/tabs-state.service';
import { Component, OnInit } from '@angular/core';
import { ActionsEnum } from '../../../shared/models/actions-enum.enum';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls:['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(private tabsService:TabsStateService) { }

  ngOnInit() {
  }

}
