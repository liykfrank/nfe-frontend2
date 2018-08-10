import { Component, OnInit } from '@angular/core';
import {
  ActivatedRoute,
  NavigationEnd,
  NavigationStart,
  Router
} from '@angular/router';

import { ROUTES } from '../../constants/routes';

interface MenuTab {
  label: string;
  url: string;
}

@Component({
  selector: 'bspl-menu-tabs',
  templateUrl: './menu-tabs.component.html',
  styleUrls: ['./menu-tabs.component.scss']
})
export class MenuTabsComponent implements OnInit {

  private tabList: MenuTab[] = [];

  private activeUrl: string;


  constructor(private activatedRoute: ActivatedRoute, protected router: Router) {

    const tabListStringify: string = localStorage.getItem('tabList');
    this.tabList = tabListStringify ? JSON.parse(tabListStringify)  : [{ label: ROUTES.DASHBOARD.LABEL, url: ROUTES.DASHBOARD.URL }];
    this.activeUrl = this.router.routerState.snapshot.url;

  }

  ngOnInit() {
    this.router.events
      .filter(event => event instanceof NavigationStart)
      .subscribe(() => {
        this.activeUrl = this.router.routerState.snapshot.url;
      });

    this.router.events
      .filter(event => event instanceof NavigationEnd)
      .subscribe(() => {

        this.activeUrl = this.router.routerState.snapshot.url;
        const tabSelected = this._getTabUrl(this.activatedRoute.root);

        if (!this._existTab(tabSelected)) {
          this._addTab(tabSelected);
        }

        this.tabList.map(tab => {
        });

        localStorage.setItem('tabList', JSON.stringify(this.tabList));
      });
  }

  _addTab(nextRouteUrl) {
    this.tabList.push({
      label: nextRouteUrl.LABEL,
      url: nextRouteUrl.URL,
    });
  }

  _existTab(nextRouteUrl) {
    return this.tabList.find(tab => tab.label === nextRouteUrl.LABEL) ||
      nextRouteUrl.URL == this.tabList[0].url;
  }

  _getTabUrl(root: ActivatedRoute) {
    if (!root.firstChild.firstChild.firstChild.firstChild.snapshot.data.tab) {
      return root.firstChild.firstChild.firstChild.snapshot.data.tab;
    } else {
      return root.firstChild.firstChild.firstChild.firstChild.snapshot.data.tab;
    }
  }

  checkTabClass(tab: MenuTab) {
    return tab.url === this.activeUrl ? 'selected-tab' : '';
  }

  closeTab(tab, index) {
    if (tab.url === this.activeUrl) {
      this.tabList.splice(index, 1);
      this.router.navigate([this.tabList[index - 1].url]);
    } else {
      this.tabList.splice(index, 1);
    }
  }


}
