import { Component, EventEmitter, inject, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Criteria, Operator } from '../../models/inventory.model';

@Component({
  selector: 'app-table-filter',
  templateUrl: './table-filter.component.html',
  styleUrls: ['./table-filter.component.scss']
})
export class TableFilterComponent implements OnInit{

  private formBuildre: FormBuilder = inject(FormBuilder);

  operatorOptions = ['=', '!=', '>', '<', 'contains'];
  fields = ['itemNumber', 'itemDate', 'customerId'];
  filtreForm!: FormGroup;

  @Output() criteriaChange = new EventEmitter<Criteria[]>();

  ngOnInit(): void {
    this.initFiltreForm();
  }

  private initFiltreForm(): void {
    const filtres = this.fields.reduce((acc, field) => {
      acc[`${field}Value`] = this.formBuildre.control('');
      acc[`${field}Operator`] = this.formBuildre.control('=');
      return acc;
    }, {} as Record<string, unknown>);

    this.filtreForm = this.formBuildre.group(filtres);
  }

  chercher() {
    const formValue = this.filtreForm?.value;
    const criteriaList: Criteria[] = [];
    this.fields.forEach(field => {
      const value = formValue[`${field}Value`];
      const operator = formValue[`${field}Operator`];
      if (value) {
        criteriaList.push({
          key: field,
          value: value,
        } as Criteria);
      }
    });
    this.criteriaChange.emit(criteriaList);
  }

  reinitialiser() {
    this.filtreForm?.reset();
    this.fields.forEach(field => {
      this.filtreForm.get(`${field}Operator`)?.patchValue('=');
    });
    this.criteriaChange.emit([]);
  }
}
