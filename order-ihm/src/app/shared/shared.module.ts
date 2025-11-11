import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MaterialModule } from './material/material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { InfoDialogComponent } from './components/info-dialog/info-dialog.component';



@NgModule({
  declarations: [
    InfoDialogComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    MaterialModule,
    FlexLayoutModule
  ],
  exports: [InfoDialogComponent, MatToolbarModule, CommonModule, MaterialModule, ReactiveFormsModule, FormsModule, FlexLayoutModule]
})
export class SharedModule { }
