import { AbstractControl } from '@angular/forms';

export function getFormControlErrorText(ctrl: AbstractControl): string {
  const errors = ctrl.errors;
  if (!errors) return '';

  if (ctrl.hasError('required')) {
    return 'Ce champ est requis';
  } else if (ctrl.hasError('email')) {
    return "Merci d'entrer une adresse mail valide";
  } else if (ctrl.hasError('minlength')) {
    return 'Ce numéro de téléphone ne contient pas assez de chiffres';
  } else if (ctrl.hasError('maxlength')) {
    return 'Ce numéro de téléphone contient trop de chiffres';
  } else if (ctrl.hasError('validValidator')) {
    return `Ce texte ${errors?.['validValidator']} ne contient pas le mot VALID`;
  } else {
    return 'Ce champ contient une erreur';
  }
}
