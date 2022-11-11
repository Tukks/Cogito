import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() {
  }

  public saveJwt(jwt: string) {
    localStorage.setItem('jwt', jwt);
  }

  public getCookie() {
    return localStorage.getItem('jwt');
  }

  public exist() {
    return localStorage.getItem('jwt') !== null;
  }
}
