import { Component, EventEmitter, inject, OnDestroy, OnInit, Output } from '@angular/core';
import { Observable, Subject, takeUntil } from 'rxjs';
import { Page } from '../../models/order.model';
import { PageEvent } from '@angular/material/paginator';
import { OrderUiFacade } from '../../services/order.ui.facade';

@Component({
  selector: 'app-table-pagination',
  templateUrl: './table-pagination.component.html',
  styleUrls: ['./table-pagination.component.scss']
})
export class TablePaginationComponent implements OnInit, OnDestroy {


  private orderUiFacade: OrderUiFacade = inject(OrderUiFacade);

  private destroy$ = new Subject<void>();
  page$!: Observable<Page>;

  @Output() pageChange = new EventEmitter<number>();

  ngOnInit(): void {
    this.initializeOrders();

  }
  private initializeOrders(): void {
    this.page$ = this.orderUiFacade.page$.pipe(
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
