import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { AuthUser } from '../model/AuthUser';
import { Done } from '../model/Done';
import { Todo } from '../model/Todo';
import { User } from '../model/User';
import { Username } from '../model/Username';

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  url: string = 'http://localhost:8082/';

  constructor(private http:HttpClient) { }

  public fetchAll(): Observable<Todo[]> {
    const token = localStorage.getItem('token');
    const headObject = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json', // Other headers as needed
    });

    return this.http.get<Todo[]>(this.url + "todo/getall", {headers:headObject});
  }

  public addTodo(todo: Todo) {
    const token = localStorage.getItem('token');
    const headObject = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json', // Other headers as needed
    });
    return this.http.post<Todo>(this.url + "todo/add", todo, {headers:headObject});
  }

  public deleteTodo(id:string) {
    const token = localStorage.getItem('token');
    const headObject = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json', // Other headers as needed
    });
    return this.http.delete<Todo>(this.url + "todo/delete/" + id, {headers:headObject})
  }

  public findTodo(id:string) {
    const token = localStorage.getItem('token');
    const headObject = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json', // Other headers as needed
    });
    return this.http.get<Todo>(this.url + "todo/findelement/" + id, {headers:headObject});
  }

  public updateTodo(id:string, todo:Todo) {
    const token = localStorage.getItem('token');
    const headObject = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json', // Other headers as needed
    });
    return this.http.put<Todo>(this.url + "todo/update/" + id, todo, {headers:headObject});
  }

  public done(id:string, done:Done) {
    const token = localStorage.getItem('token');
    const headObject = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json', // Other headers as needed
    });
    return this.http.put<Done>(this.url + "todo/done/" + id, done, {headers:headObject});
  }

  public createUser(user: User) {
    return this.http.post<User>(this.url + "user/add", user);
  }

  public loginUser(user: AuthUser) {
    return this.http.post<AuthUser>(this.url + "user/login", user).pipe(
      tap((response) => {
        if(response && response.token) {
          localStorage.setItem("token", response.token);
          const expirationDate = new Date().getTime() + 3600 * 1000;
          localStorage.setItem("expiration", expirationDate.toString());
        }
      })
    );
  }

  public getUser(): Observable<Username> {
    const token = localStorage.getItem('token');
    const headObject = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json', // Other headers as needed
    });
    return this.http.get<Username>(this.url + "user/get", {headers:headObject});
  }




}
