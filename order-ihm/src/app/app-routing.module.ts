import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: 'order', loadChildren: () => import('./order/order.module').then(m => m.OrderModule) },
  { path: '**', redirectTo: 'order'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
