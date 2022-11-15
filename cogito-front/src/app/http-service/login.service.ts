import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private httpClient: HttpClient) {
  }


  public register(user: { email: string | null, password: string | null }): Observable<any> {
    return this.httpClient.post("/api/register", user);
  }

  public login(user: { email: string | null, password: string | null }): Observable<any> {
    return this.httpClient.post("/api/login", user);
  }

}
