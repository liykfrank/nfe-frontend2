import { ActionsEnum } from '../../../../shared/models/actions-enum.enum';
import {  ComponentFactoryResolver, ViewContainerRef, ComponentRef, Injector, ComponentFactory } from '@angular/core';

export interface ItabAction<T> {
  action: ActionsEnum;
  title?: string;
  getComp( cfResolver: ComponentFactoryResolver,
     vcRef: ViewContainerRef): ComponentRef<T>;
}
