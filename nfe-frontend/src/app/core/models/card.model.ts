export interface CardIf {
  title: string;
  imgPath: string;
}


export class Card implements CardIf {

    constructor(public title: string, public imgPath) {

    }
}
