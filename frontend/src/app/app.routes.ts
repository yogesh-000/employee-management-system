import { Routes } from '@angular/router';
import { authGuard } from './core/auth-guard';
import { Login } from './pages/login/login';
import { Dashboard } from './pages/dashboard/dashboard';
import { Home } from './pages/home/home';
import { Employees } from './pages/employees/employees';
import { MyLeaves } from './pages/my-leaves/my-leaves';
import { Approvals } from './pages/approvals/approvals';
import { Departments } from './pages/departments/departments';


export const routes: Routes = [
  { path: 'login', component: Login },
  {
    path: '',
    component: Dashboard,
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', component: Home },
      { path: 'employees', component: Employees, canActivate: [authGuard], data: { roles: ['ADMIN'] } },
      { path: 'approvals', component: Approvals, canActivate: [authGuard], data: { roles: ['ADMIN', 'MANAGER'] } },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'leaves', component: MyLeaves },
      { path: 'departments', component: Departments, canActivate: [authGuard], data: { roles: ['ADMIN'] } },
    ],
  },
  { path: '**', redirectTo: 'dashboard' },
];