import { TimeLocation } from './time-location';
import { UserInfo } from './userinfo';
import { Shape } from './shape';
import { ShapeType } from '../data/shape-types';
import { CircleShape } from './circle-shape';
import { Geocode } from './geocode';

export class Cho {
    title: string;
    description: string;
    category: string;
    timeLocations: TimeLocation[];
    userId: number;
    username: string;
    imageUrl: string;

    requestJson(): string {
        return JSON.stringify({ 'title': this.title, 'description': this.description, 'category': this.category, 'userId': this.userId, 'username': this.username, 'imageUrl': this.imageUrl, 'timeLocations': this.timeLocations.map(tl => new String(JSON.stringify(tl))) });
    }

    static createFromJson(js: any): Cho {
        let cho = new Cho();
        cho.title = js.title;
        cho.description = js.description;
        cho.category = js.category;
        cho.userId = js.userId;
        cho.username = js.username;
        cho.imageUrl = js.imageUrl;
        if (js.timeLocations) {
            cho.timeLocations = [];
            for (let i = 0; i < js.timeLocations.length; i++) {
                let tl = JSON.parse(js.timeLocations[i]);
                let timeLocation = new TimeLocation();
                timeLocation.name = tl.name;
                timeLocation.time = tl.time;
                timeLocation.timeType = tl.timeType;
                let shape = new Shape();
                shape.identifier = tl.shape.identifier;
                shape.type = <ShapeType>tl.shape.type;
                if(shape.type === ShapeType.CIRCLE)
                {
                    let circle = new CircleShape();
                    circle.radius = tl.shape.shape.radius;
                    let center = new Geocode();
                    center.lat = Number(tl.shape.shape.center.lat);
                    center.lng = Number(tl.shape.shape.center.lng);
                    circle.center = center;
                    shape.shape = circle;
                }
                timeLocation.shape = shape;
                cho.timeLocations[i] = timeLocation;
            }
        }
        return cho;
    }
}