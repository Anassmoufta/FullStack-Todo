import { Component, ElementRef, HostListener, Renderer2 } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { User } from '../../model/User';
import { HttpService } from '../../services/http.service';

@Component({
  selector: 'app-user-subscription',
  templateUrl: './user-subscription.component.html',
  styleUrl: './user-subscription.component.css'
})
export class UserSubscriptionComponent {
  constructor(private renderer:Renderer2, private rooter: Router,private el:ElementRef, private http:HttpService, private fb: FormBuilder, private toaster:ToastrService) {}

  userForm!: FormGroup;
  loginForm!: FormGroup;
  userData!: User;

  ngOnInit(): void {
    this.userForm = this.fb.group ({
      username: [''],
      email: [''],
      password: ['']
    })

    this.loginForm = this.fb.group({
      username: [''],
      password: ['']
    })
  }

  handleSubmit() {
    this.http.createUser(this.userForm.value).subscribe(
      () => {
        this.toaster.success("User created sucssefuly");
      },
      () => {
        this.toaster.error("server not responded, try again");
      }
    )
  }

  handleLoginSubmit() {
    this.http.loginUser(this.loginForm.value).subscribe(
      () => {
        this.toaster.success("User logged in successfully");
        this.rooter.navigate(["/todo"]);
      },
      () => {
        this.toaster.error("the username or password not correct");
      }
    )
  }

  @HostListener('window:click', ['$event'])
  onClick(event: MouseEvent) {
    const modal = this.el.nativeElement.querySelector('#id01');
    if (event.target === modal) {
      this.renderer.setStyle(modal, 'display', 'none');
    }
  }

}
