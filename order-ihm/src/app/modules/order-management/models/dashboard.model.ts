export interface DashboardSummary {
  totalOrders: number;
  pendingOrders: number;
  deliveredOrders: number;
  canceledOrders: number;
  revenue: number;
}

export interface Activity {
  id: string;
  title: string;
  subtitle?: string;
  time: string;
}

