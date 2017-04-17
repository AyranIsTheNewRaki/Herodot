import { Shape } from './shape';

export class TimeLocation{
    shape: Shape;
    name: string;
    time: string;
    timeType: string;

    constructor(){
        this.name = "";
        this.time = "";
        this.timeType = "";
        this.shape = new Shape();
    }
}