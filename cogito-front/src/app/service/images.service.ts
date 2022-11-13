import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ImagesService {

  constructor(private httpClient: HttpClient) {
  }

  public saveImage(file: File): Observable<{ id: number }> {
    const data: FormData = new FormData();
    data.append('image', file);

    return this.httpClient.post<{ id: number }>("/api/image", data);
  }

  public buildImageUrl(id: number) {
    return `http://localhost:9191/api/image/${id}`;
    //  return this.httpClient.get(`/api/image/${id}`);

  }
}
