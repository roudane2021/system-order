import { EventEmitter, Type } from "@angular/core";


export interface BaseComponent {
    data?: unknown;
    eventClose?: EventEmitter<void>;
    
}

export type DynamicComponentType = Type<BaseComponent>;