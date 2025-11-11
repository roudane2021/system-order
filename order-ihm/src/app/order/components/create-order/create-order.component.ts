import { Component, EventEmitter, inject, OnInit, Output } from '@angular/core';
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import {  map, Observable, startWith, tap } from 'rxjs';
import { confirmEuqalValidator } from '../../../shared/validators/confirm-equal';
import { BaseComponent } from '../../../shared/interfaces/index.interface';
import { Order } from '../../models/order.model';
import { getFormControlErrorText } from 'src/app/shared/utils/form-error.util';
import { LogService } from 'src/app/core/services/log.service';
import { OrderUiFacade } from '../../services/order.ui.facade';

@Component({
  selector: 'app-create-order',
  templateUrl: './create-order.component.html',
  styleUrls: ['./create-order.component.scss'],
})
export class CreateOrderComponent implements BaseComponent, OnInit {
  private formBuilder: FormBuilder = inject(FormBuilder);
  private orderUiFacade: OrderUiFacade = inject(OrderUiFacade);
  private logService: LogService = inject(LogService);

  createOrderForm!: FormGroup;

  personalInfoForm!: FormGroup;

  contactPreferenceCtrl!: FormControl;

  emailForm!: FormGroup;
  confirmEmailCtrl!: FormControl;
  emailCtrl!: FormControl;

  phoneCtrl!: FormControl;

  loginInfoForm!: FormGroup;
  passwordCtrl!: FormControl;
  confirmPasswordCtrl!: FormControl;

  orderItemsForm!: FormArray;

  showEmail$!: Observable<boolean>;
  showPhone$!: Observable<boolean>;

  showEmailError$!: Observable<boolean>;
  showPasswordError$!: Observable<boolean>;

  @Output() eventClose = new EventEmitter<void>();


  ngOnInit(): void {
    this.initFormControls();
    this.initCreateOrderForm();
    this.initObservable();
  }

  private initCreateOrderForm = (): void => {
    this.createOrderForm = this.formBuilder.group({
      personalInfo: this.personalInfoForm,
      contactPreference: this.contactPreferenceCtrl,
      email: this.emailForm,
      phone: this.phoneCtrl,
      loginInfo: this.loginInfoForm,
      items: this.orderItemsForm,
    });
  };

  private initFormControls = (): void => {
    this.personalInfoForm = this.formBuilder.group({
      prenom: this.formBuilder.control('', [Validators.required]),
      nom: this.formBuilder.control('', [Validators.required]),
      ville: this.formBuilder.control('', [Validators.required]),
      nationalite: this.formBuilder.control('', [Validators.required]),
    });

    this.contactPreferenceCtrl = this.formBuilder.control('phone');

    this.emailCtrl = this.formBuilder.control('', [
      Validators.required,
      Validators.email,
    ]);
    this.confirmEmailCtrl = this.formBuilder.control('', [
      Validators.required,
      Validators.email,
    ]);
    this.emailForm = this.formBuilder.group(
      {
        email: this.emailCtrl,
        confirmEmail: this.confirmEmailCtrl,
      },
      {
        validators: [confirmEuqalValidator('email', 'confirmEmail')],
        updateOn: 'blur',
      }
    );

    this.phoneCtrl = this.formBuilder.control('', [Validators.required]);

    this.passwordCtrl = this.formBuilder.control('', [Validators.required]);
    this.confirmPasswordCtrl = this.formBuilder.control('', [
      Validators.required,
    ]);
    this.loginInfoForm = this.formBuilder.group(
      {
        username: this.formBuilder.control('', [Validators.required]),
        password: this.passwordCtrl,
        confirmPassword: this.confirmPasswordCtrl,
      },
      {
        validators: [confirmEuqalValidator('password', 'confirmPassword')],
        updateOn: 'blur',
      }
    );

    this.orderItemsForm = this.formBuilder.array([]);
  };

  private initObservable = () => {
    this.showEmail$ = this.contactPreferenceCtrl.valueChanges.pipe(
      startWith(this.contactPreferenceCtrl.value),
      map((value) => value === 'email'),
      tap((value) => this.setEmailValidators(value))
    );

    this.showPhone$ = this.contactPreferenceCtrl.valueChanges.pipe(
      startWith(this.contactPreferenceCtrl.value),
      map((value) => value === 'phone'),
      tap((value) => this.setPhoneValidators(value))
    );

    this.showEmailError$ = this.emailForm.statusChanges.pipe(
      map(
        () =>
          this.emailForm.hasError('confirmEqual') &&
          !this.emailForm.hasError('email') &&
          this.emailCtrl.value &&
          this.confirmEmailCtrl.value
      )
    );

    this.showPasswordError$ = this.loginInfoForm.statusChanges.pipe(
      map(
        () =>
          this.passwordCtrl.value &&
          this.confirmPasswordCtrl.value &&
          this.loginInfoForm.hasError('confirmEqual')
      )
    );
  };

  private setEmailValidators = (showEmail: boolean): void => {
    if (showEmail) {
      this.emailCtrl.addValidators([Validators.required, Validators.email]);
      this.confirmEmailCtrl.addValidators([
        Validators.required,
        Validators.email,
      ]);
    } else {
      this.emailCtrl.clearValidators();
      this.confirmEmailCtrl.clearValidators();
      this.emailCtrl.patchValue('');
      this.confirmEmailCtrl.patchValue('');
    }
    Object.values(this.emailForm.controls).forEach((ctrl) => {
      if (ctrl instanceof FormControl) {
        ctrl.updateValueAndValidity();
        ctrl.markAsPristine();
        ctrl.markAsUntouched();
      }
    });
  };

  private setPhoneValidators = (showPhone: boolean): void => {
    if (showPhone) {
      this.phoneCtrl.addValidators([Validators.required]);
    } else {
      this.phoneCtrl.clearValidators();
      this.phoneCtrl.patchValue('');
    }
    this.phoneCtrl.updateValueAndValidity();
    this.phoneCtrl.markAsPristine();
    this.phoneCtrl.markAsUntouched();
  };

  public addOrderItem(): void {
    this.orderItemsForm.push(this.createOrderItemGroup() as FormGroup);
  }

  public removeOrderItem(index: number): void {
    this.orderItemsForm.removeAt(index);
  }

  public getOrderItemFormGroup(index: number): FormGroup {
    return this.orderItemsForm.at(index) as FormGroup;
  }

  private createOrderItemGroup(): FormGroup {
    return this.formBuilder.group({
      productId: [null, Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]],
      price: [0, [Validators.required, Validators.min(0)]],
    });
  }

  onSubmitForm() {
    this.createOrderForm.markAllAsTouched();
    if (this.createOrderForm.invalid) {
      return;
    }
    const updateOrder: Order = this.orderUiFacade.mapFormToOrder(this.createOrderForm);
    this.logService.debug('updateOrder :', updateOrder);
    this.orderUiFacade.saveOrder(updateOrder);
    this.createOrderForm.reset();
    this.eventClose.emit();
  }

  getErrorMessage(ctrl: AbstractControl): string {
    return getFormControlErrorText(ctrl);
  }

  onClose() {
    this.eventClose.emit();
  }
}
