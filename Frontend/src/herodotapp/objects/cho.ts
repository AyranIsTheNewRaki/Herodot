import { TimeLocation } from './time-location';
import { UserInfo } from './userinfo';

export class Cho {
    title: string;
    description: string;
    category: string;
    timeLocations: TimeLocation[];
    userId: number;
    username: string;
    imageUrl: string;

    requestJson(): string {
        return JSON.stringify({ 'title': this.title, 'description': this.description, 'category': this.category, 'userId': this.userId, 'username': this.username, 'imageUrl':this.imageUrl, 'timeLocations': this.timeLocations.map(tl => new String(JSON.stringify(tl)))});
    }

    static createFromJson(js:string) : Cho{
        return new Cho();
    }
}