import { Injectable } from '@angular/core';

import { OperationResponse } from '../objects/operationResponse';
import { Cho } from '../objects/cho';

@Injectable()
export class ChoService {

    addCho(cho: Cho) : OperationResponse{
        let requestJson = JSON.stringify(cho);
        console.log(requestJson);
        return new OperationResponse(true, requestJson);
    }
}