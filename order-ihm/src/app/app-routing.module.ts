import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: 'order', loadChildren: () => import('./order/order.module').then(m => m.OrderModule) },
  { path: 'inventory', loadChildren: () => import('./inventory/inventory.module').then(m => m.InventoryModule) },
  { path: 'ai-assistant', loadChildren: () => import('./modules/ai-assistant/ai-assistant.module').then(m => m.AiAssistantModule), data: { title: 'AI Assistant' } },
  { path: 'commande', loadChildren: () => import('./modules/order-management/order-management.module').then(m => m.OrderManagementModule), data: { title: 'AI Assistant' } },
  { path: '**', redirectTo: 'order'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
