import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";



export const confirmEuqalValidator = (main: string, confirm: string): ValidatorFn => {
    
    return (ctrl: AbstractControl) : ValidationErrors | null  => {
    
        if (!ctrl.get(main) || !ctrl.get(confirm)) {
            return {confirmEqual: 'Invalid control Name'}
        }
        const mainValue = ctrl.get(main)?.value;
        const confirmValue = ctrl.get(confirm)?.value;
        return mainValue === confirmValue ? null : {
            confirmEqual: {
                main: mainValue,
                confirm: confirmValue
        }}
    }
}