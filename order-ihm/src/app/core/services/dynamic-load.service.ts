import { ComponentRef, Injectable, Type, ViewContainerRef} from '@angular/core';
import { BaseComponent } from 'src/app/shared/interfaces/base-component.interface';



@Injectable({ providedIn: 'root' })
export class DynamicLoaderService {

    public createComponent(
        container: ViewContainerRef,
        componentType: Type<BaseComponent>,
        inputs?: Partial<BaseComponent>,
        onClose?: () => void
    ): ComponentRef<BaseComponent> {
        // Détruire l’ancien composant
        container.clear();

        const componentRef = container.createComponent(componentType);
        
        if (inputs) { Object.assign(componentRef.instance, inputs); }

        // Vérifier dynamiquement si la propriété `close` existe et est un EventEmitter
        const instance = componentRef.instance;
        if (instance.eventClose?.subscribe && onClose) {
            instance.eventClose.subscribe(onClose);
        }

        // Retourner la référence au parent
        return componentRef;
    }


}