import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() {
  }

  public removeCookie() {
    document.cookie = "accessToken= ; expires = Thu, 01 Jan 1970 00:00:00 GMT"
  }

}
