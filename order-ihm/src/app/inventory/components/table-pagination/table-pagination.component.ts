import { Component, EventEmitter, inject, OnDestroy, OnInit, Output } from '@angular/core';
import { Observable, Subject, takeUntil } from 'rxjs';
import { Page } from '../../models/inventory.model';
import { PageEvent } from '@angular/material/paginator';
import { InventoryUiFacade } from '../../services/inventory.ui.facade';

@Component({
  selector: 'app-table-pagination',
  templateUrl: './table-pagination.component.html',
  styleUrls: ['./table-pagination.component.scss']
})
export class TablePaginationComponent implements OnInit, OnDestroy {

  private inventoryUiFacade: InventoryUiFacade = inject(InventoryUiFacade);

  private destroy$ = new Subject<void>();
  page$!: Observable<Page>;

  @Output() pageChange = new EventEmitter<number>();

  ngOnInit(): void {
    this.initializeItems();
  }

  private initializeItems(): void {
    this.page$ = this.inventoryUiFacade.page$.pipe(
      takeUntil(this.destroy$)
    );
  }

  onPageChange(event: PageEvent) {
    this.pageChange.emit(event?.pageIndex || 0);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
