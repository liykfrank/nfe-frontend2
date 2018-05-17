import { Component, OnInit, ElementRef, Renderer2 } from '@angular/core';
import { jqxGridComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxgrid';

@Component({
  selector: 'jqxGridnew',
  template: '<div><ng-content></ng-content></div>'
 // styleUrls: ['./jqx-nw-grid.component.scss']
})
export class JqxNwGridComponent extends jqxGridComponent implements OnInit  {

  constructor(private el: ElementRef, private rend: Renderer2) {
    super(el);
   /*   this.onRowselect.subscribe(dat => {
      this.rend.setStyle(dat.target, "background", "orange!important");
      //  this.rend.setStyle(dat.target,'height','70px');
      const indes = this.getselectedrowindexes();
      indes.forEach(element => {
         const data = this.getrowdata(element.valueOf());
         console.log(data);
        });
      console.log("ssssssssssssssss");
    }); */
    this.onRowclick.subscribe(dat => {
        console.log(dat.args);
        this.selectrow(dat.args.rowindex);
      });
  }

  ngOnInit() {
    super.ngOnInit();
  }



  public sh() {
    /* const el = (this.el.nativeElement as HTMLElement).querySelector(
      ".jqx-grid-cell"
    );
    this.rend.setStyle(el, "background", "red");
    const items: any = (this.el.nativeElement as HTMLElement).querySelectorAll(
      ".jqx-grid-cell"
    );
    var ref = this;
    var r = this.el.nativeElement.querySelectorAll('[role="row"]')[2];

   items.forEach(element => {
    //ref.rend.setStyle(element, "background", "blue");

   });

    ref.rend.setStyle(r, "background", "green");
    ref.rend.setStyle(r, "height", "70px"); */
  }
}
