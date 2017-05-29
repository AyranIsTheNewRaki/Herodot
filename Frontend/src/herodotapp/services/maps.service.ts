import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Rx';

import { Shape } from '../objects/shape';
import { ShapeType } from '../data/shape-types';
import { CircleShape } from '../objects/circle-shape';
import { Geocode } from '../objects/geocode';

declare var google: any;
const labels = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

@Injectable()
export class MapsService {

    private map: any;
    private drawingManager: any;
    private labelIndex = 0;
    private googleShapes: [any, any][];
    private shapeSource = new Subject<Shape>();

    constructor() {
        this.googleShapes = [];
    }

    initMap(mapBox: Element, isReadonly: boolean, lat: number, lng: number, searchBox: Element = null): void {
        console.log(mapBox);
        var mapProp = {
            center: new google.maps.LatLng(lat, lng),
            zoom: 5,
            mapTypeId: google.maps.MapTypeId.ROADMAP,
            streetViewControl: false,
            mapTypeControl: false
        };
        this.map = new google.maps.Map(mapBox, mapProp);
        this.drawingManager = new google.maps.drawing.DrawingManager();
        this.drawingManager.setOptions({
            drawingControl: !isReadonly,
            drawingControlOptions: { drawingModes: ['circle'] }
        });
        this.drawingManager.setMap(this.map);

        if (searchBox) {
            let input = new google.maps.places.SearchBox(searchBox);
            input.addListener('places_changed', () => this.changeMapBoundsFromPlaces(input.getPlaces()));
        }
        if (!isReadonly) {
            google.maps.event.addListener(this.drawingManager, 'overlaycomplete', (shape: any) => this.addGoogleShape(shape));
        }
    }

    private changeMapBoundsFromPlaces(places: any): void {
        if (places.length == 0) {
            return;
        }
        var bounds = new google.maps.LatLngBounds();
        places.forEach(function (place: any) {
            if (!place.geometry) {
                console.log("Returned place contains no geometry");
                return;
            }
            if (place.geometry.viewport) {
                bounds.union(place.geometry.viewport);
            } else {
                bounds.extend(place.geometry.location);
            }
        });
        this.map.fitBounds(bounds);
    }

    private addGoogleShape(googleShape: any): void {
        let markerPosition: any;
        let shape = new Shape();
        shape.id = this.labelIndex++;
        shape.identifier = labels[shape.id % labels.length];
        if (googleShape.type == 'circle') {
            shape.type = ShapeType.CIRCLE;
            let circle = new CircleShape();
            let center = new Geocode();
            center.lat = googleShape.overlay.getCenter().lat();
            center.lng = googleShape.overlay.getCenter().lng();
            circle.center = center;
            circle.radius = googleShape.overlay.getRadius();
            shape.shape = circle;
            markerPosition = googleShape.overlay.getCenter();
        }
        else {
            this.labelIndex--;//revert
            console.log("unsupported shape => " + googleShape.type);
            return;
        }
        this.shapeSource.next(shape);
        this.googleShapes[shape.id] = [googleShape.overlay, this.addMarker(markerPosition, shape.identifier)];
    }

    private addMarker(location: any, label: string): any {
        return new google.maps.Marker({
            position: location,
            label: label,
            map: this.map
        });
    }

    shapeAdded(): Observable<Shape> {
        return this.shapeSource.asObservable();
    }

    deleteShape(id: number): any {
        this.googleShapes[id][0].setMap(null);
        this.googleShapes[id][1].setMap(null);
        this.googleShapes[id] = null;
    }

    addShape(shape: Shape, center: boolean): void {
        if (shape.type === ShapeType.CIRCLE) {
            let markerPosition = new Geocode();
            markerPosition.lat = shape.shape.center.lat;
            markerPosition.lng = shape.shape.center.lng;
            this.addMarker(markerPosition, shape.identifier);
            new google.maps.Circle({
                map: this.map,
                center: shape.shape.center,
                radius: shape.shape.radius
            });
            if (center) {
                this.map.setCenter(markerPosition);
                this.map.setZoom(15);
            }
        }
    }
}
