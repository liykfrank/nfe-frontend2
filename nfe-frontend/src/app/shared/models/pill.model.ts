export class Pill {

    public id: string;
    public label: string;
    public active: boolean;
    public collapsable;

    constructor(id: string, label: string) {
      this.id = id;
      this.label = label;
      this.active = false;
      this.collapsable = true;
    }

}
