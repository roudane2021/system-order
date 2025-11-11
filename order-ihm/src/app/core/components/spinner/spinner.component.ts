import { Component, inject, Input } from '@angular/core';
import { SpinnerService } from '../../services/spinner.service';


@Component({
  selector: 'app-spinner',
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.scss']
})
export class SpinnerComponent {
  @Input() message = 'Chargement...';
  @Input() diameter = 50;
  @Input() color: 'primary' | 'accent' | 'warn' = 'primary';
  public spinnerService: SpinnerService = inject(SpinnerService);
  
}
