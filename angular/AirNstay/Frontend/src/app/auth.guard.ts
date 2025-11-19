import { inject, Inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthenticationService } from './authentication.service';


export const authGuard: CanActivateFn = (route, state) => {

  const auth = inject(AuthenticationService);
  const router = inject(Router);

  const expectedRole = route.data['role'];
  const currentRole = auth.getRole();

  if(currentRole === expectedRole){
    return true;
  }

  router.navigate(['/unauthorized']);
  return false;
};
