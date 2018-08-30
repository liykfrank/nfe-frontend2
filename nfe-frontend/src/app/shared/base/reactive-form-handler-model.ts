export abstract class ReactiveFormHandlerModel {
  constructor() {
    this.createFormControls();
    this.createFormGroups();
    this.createForm();
  }

  abstract createFormControls();

  abstract createFormGroups();

  abstract createForm();
}
