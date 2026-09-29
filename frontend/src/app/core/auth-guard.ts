import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Auth } from './auth';

export const authGuard: CanActivateFn = (route) => {
  const auth = inject(Auth);
  const router = inject(Router);

  if (!auth.isLoggedIn()) return router.createUrlTree(['/login']);

  const allowed = route.data['roles'] as string[] | undefined;
  if (allowed && !auth.hasRole(...allowed)) return router.createUrlTree(['/dashboard']);

  return true;
};