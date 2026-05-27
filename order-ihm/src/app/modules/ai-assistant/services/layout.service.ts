/**
 * Layout Service
 * Manages layout state (sidebar open/close, responsive behavior)
 */

import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class LayoutService {
  private sidebarOpenStream$ = new BehaviorSubject<boolean>(true);
  public sidebarOpen$ = this.sidebarOpenStream$.asObservable();

  public isMobile$: Observable<boolean>;

  constructor(private breakpointObserver: BreakpointObserver) {
    // Detect mobile devices
    this.isMobile$ = this.breakpointObserver.observe([Breakpoints.Handset]).pipe(
      map(result => result.matches)
    );

    // Close sidebar on mobile by default
    this.isMobile$.subscribe(isMobile => {
      if (isMobile) {
        this.closeSidebar();
      } else {
        this.openSidebar();
      }
    });
  }

  /**
   * Toggle sidebar visibility
   */
  toggleSidebar(): void {
    this.sidebarOpenStream$.next(!this.sidebarOpenStream$.value);
  }

  /**
   * Open sidebar
   */
  openSidebar(): void {
    this.sidebarOpenStream$.next(true);
  }

  /**
   * Close sidebar
   */
  closeSidebar(): void {
    this.sidebarOpenStream$.next(false);
  }

  /**
   * Get current sidebar state
   */
  isSidebarOpen(): boolean {
    return this.sidebarOpenStream$.value;
  }
}

