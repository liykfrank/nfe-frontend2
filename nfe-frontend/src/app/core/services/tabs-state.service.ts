import { Injectable, } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { ActionsEnum } from '../../shared/models/actions-enum.enum';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class TabsStateService {

  private subjectTabs: BehaviorSubject<ActionsEnum>  = new BehaviorSubject(null);
  constructor() { }

 addTabAction(action: ActionsEnum){
    this.subjectTabs.next(action)
 }

 getObserTabs() : Observable<ActionsEnum>{
   return this.subjectTabs.asObservable();
 }

}
