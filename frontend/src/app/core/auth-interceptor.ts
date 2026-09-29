import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Auth } from './auth';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = inject(Auth).getToken();
  if (token && !req.url.includes('/api/auth/')) {
    req = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  }
  return next(req);
};