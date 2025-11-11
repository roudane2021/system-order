import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { getFormControlErrorText } from 'src/app/shared/utils/form-error.util';
import { LogService } from 'src/app/core/services/log.service';
import { InventoryUiFacade } from '../../services/inventory.ui.facade';
import { Inventory } from '../../models/inventory.model';
import { BaseComponent } from '../../../shared/interfaces/index.interface';

@Component({
  selector: 'app-update-item',
  templateUrl: './update-item.component.html',
  styleUrls: ['./update-item.component.scss'],
})
export class UpdateItemComponent implements BaseComponent, OnInit {
  private formBuilder: FormBuilder = inject(FormBuilder);
  private inventoryUiFacade: InventoryUiFacade = inject(InventoryUiFacade);
  private logService: LogService = inject(LogService);

  updateItemForm!: FormGroup;
  personalInfoForm!: FormGroup;
  loginInfoForm!: FormGroup;
  inventoryItemsForm!: FormArray;

  @Output() eventClose = new EventEmitter<void>();
  @Input() data!: unknown;

  ngOnInit(): void {
    this.initFormControls();
    this.initUpdateItemForm();
    this.populateForm(this.data as Inventory);
  }

  private initUpdateItemForm = (): void => {
    this.updateItemForm = this.formBuilder.group({
      personalInfo: this.personalInfoForm,
      loginInfo: this.loginInfoForm,
      items: this.inventoryItemsForm,
    });
  };

  private initFormControls = (): void => {
    this.personalInfoForm = this.formBuilder.group({
      prenom: this.formBuilder.control('', [Validators.required]),
      nom: this.formBuilder.control('', [Validators.required]),
      ville: this.formBuilder.control('', [Validators.required]),
      nationalite: this.formBuilder.control('', [Validators.required]),
      email: this.formBuilder.control('', [Validators.required, Validators.email]),
      phone: this.formBuilder.control('', [Validators.required]),
    });

    this.loginInfoForm = this.formBuilder.group({
      username: this.formBuilder.control('', [Validators.required]),
      password: this.formBuilder.control('', [Validators.required]),
    });

    this.inventoryItemsForm = this.formBuilder.array([]);
  };

  public addInventoryItem(): void {
    this.inventoryItemsForm.push(this.createInventoryItemGroup() as FormGroup);
  }

  public removeInventoryItem(index: number): void {
    this.inventoryItemsForm.removeAt(index);
  }

  public getInventoryItemFormGroup(index: number): FormGroup {
    return this.inventoryItemsForm.at(index) as FormGroup;
  }

  private createInventoryItemGroup(): FormGroup {
    return this.formBuilder.group({
      productId: [null, Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]],
      price: [0, [Validators.required, Validators.min(0)]],
    });
  }

  onSubmitForm() {
    this.updateItemForm.markAllAsTouched();
    if (this.updateItemForm.invalid) {
      return;
    }
    const updatedItem: Inventory = this.mapFormToInventory(this.updateItemForm, this.data as Inventory);
    this.logService.debug('updatedItem :', updatedItem);
    this.inventoryUiFacade.saveItem(updatedItem);
    this.updateItemForm.reset();
    this.eventClose.emit();
  }

  getErrorMessage(ctrl: AbstractControl): string {
    return getFormControlErrorText(ctrl);
  }

  onClose() {
    this.eventClose.emit();
  }

  private populateForm(item: Inventory): void {
    if (!item) return;
    this.personalInfoForm.patchValue({
      prenom: item.firstName,
      nom: item.lastName,
      ville: item.city,
      nationalite: item.nationality,
      email: item.email,
      phone: item.phone,
    });

    this.loginInfoForm.patchValue({
      username: item.userName,
      password: item.password,
    });

    this.inventoryItemsForm.clear();
    item.items.forEach(inventoryItem => {
      this.inventoryItemsForm.push(this.formBuilder.group({
        productId: [inventoryItem.productId, Validators.required],
        quantity: [inventoryItem.quantity, [Validators.required, Validators.min(1)]],
        price: [inventoryItem.price, [Validators.required, Validators.min(0)]],
      }));
    });
  }

  private mapFormToInventory = (form: FormGroup, existingItem: Inventory): Inventory => {
    const formValue = form.value;
    return {
      ...existingItem,
      firstName: formValue.personalInfo.prenom,
      lastName: formValue.personalInfo.nom,
      city: formValue.personalInfo.ville,
      nationality: formValue.personalInfo.nationalite,
      email: formValue.personalInfo.email,
      phone: formValue.personalInfo.phone,
      userName: formValue.loginInfo.username,
      password: formValue.loginInfo.password,
      items: formValue.items,
    };
  }
}
