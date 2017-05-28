import { Component, OnInit, NgZone } from '@angular/core';
import { Router } from '@angular/router';
import { CloudinaryOptions, CloudinaryUploader } from 'ng2-cloudinary';

import { CATEGORIES } from '../../data/categories';
import { UserService } from '../../services/user.service';
import { AlertService } from '../../services/alert.service';
import { MapsService } from '../../services/maps.service';
import { ChoService } from '../../services/cho.service';
import { Cho } from '../../objects/cho';
import { TimeLocation } from '../../objects/time-location';
import { Shape } from '../../objects/shape';

@Component({
    moduleId: module.id,
    selector: 'addupdate',
    templateUrl: 'addupdate.component.html'
})

export class AddUpdateComponent implements OnInit {
    categories = CATEGORIES;
    model = new Cho();
    loading = false;
    imageId: string;

    uploader: CloudinaryUploader = new CloudinaryUploader(
        new CloudinaryOptions({ cloudName: 'dw5fiolsb', uploadPreset: 'herodot' })
    );

    constructor(private userService: UserService, private mapsService: MapsService, private zone: NgZone, private alertService: AlertService, private choService: ChoService, private router: Router) {
        mapsService.shapeAdded().subscribe(shape => this.addShape(shape));
        this.model.timeLocations = [];
        this.uploader.onSuccessItem = (item: any, response: string, status: number, headers: any): any => {
            let res: any = JSON.parse(response);
            this.imageId = res.public_id;
            this.model.imageUrl = res.url;
            return { item, response, status, headers };
        };
    }

    ngOnInit() {
        this.model.username = this.userService.getUser().userName;
        this.model.userId = this.userService.getUser().id;
        this.mapsService.initMap(document.getElementById('map'), false, 51.508742, -0.120850, document.getElementById('mapSearch'));
    }

    deleteShape(id: number): void {
        for (let i = 0; i < this.model.timeLocations.length; i++) {
            if (this.model.timeLocations[i] && this.model.timeLocations[i].shape.id == id) {
                this.model.timeLocations.splice(i, 1);
                this.mapsService.deleteShape(id);
                break;
            }
        }
        console.log(JSON.stringify(this.model));
    }

    private addShape(shape: Shape): void {
        let timeLocation = new TimeLocation();
        timeLocation.shape = shape;
        this.zone.run(() => this.model.timeLocations.push(timeLocation));
        console.log(JSON.stringify(this.model));
    }

    addCho(): void {
        console.log(this.model.userId);
        this.loading = true;
        if (this.model.userId === 0) {
            this.alertService.error("Please select a provider!");
            this.loading = false;
            return;
        }
        this.choService.addCho(this.model).subscribe(data => {
            this.alertService.success("Cho added successfully.", true);
            this.router.navigate(['categories']);
        }, error => {
            this.alertService.error(JSON.stringify(error));
            this.loading = false;
        });
    }

    upload() {
        this.uploader.uploadAll();
    }
}