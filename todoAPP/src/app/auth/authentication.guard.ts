import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authenticationGuard: CanActivateFn = (route, state) => {
const token = localStorage.getItem('token');
const expirationTimestamp = localStorage.getItem('expiration'); // Assuming you store the expiration timestamp
const router = inject(Router)
if (token && expirationTimestamp) {
  const currentTime = new Date().getTime();
  if (currentTime > parseInt(expirationTimestamp, 10)) {
    // Token has expired, remove it from local storage
    localStorage.removeItem('token');
    localStorage.removeItem('expiration');
    router.navigate(['/']); // Redirect to login or home page
    return false;
  }
  return true; // Token is still valid
} else {
  router.navigate(['/']); // Token is missing, redirect to login
  return false;
}

};
