import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Done } from '../../model/Done';
import { Todo } from '../../model/Todo';
import { Username } from '../../model/Username';
import { HttpService } from '../../services/http.service';
import { Route, Router } from '@angular/router';

@Component({
  selector: 'app-show-all',
  templateUrl: './show-all.component.html',
  styleUrl: './show-all.component.css'
})
export class ShowAllComponent implements OnInit {
  HttpService: any;
update: any;
todo: any;
  constructor(private http: HttpService, private router:Router, private fb: FormBuilder, private toaster: ToastrService) { }
  ListOfTodos: Todo[] = [];
  todoForm!: FormGroup;
  done!: Done;
  doneForm!: FormGroup;
  username!:Username;


  ngOnInit(): void {
    this.http.fetchAll().subscribe((todo: Todo[]) => this.ListOfTodos = todo);
    this.getUsername();
    this.todoForm = this.fb.group({
      task: ['', Validators.maxLength(20)],
      description: ['', Validators.maxLength(100)],
      date: [''],
      isDone: [false]
    });
  }

  ngOnInitDone(): void {
    this.doneForm = this.fb.group({
      id: [''],
      isDone:['']
    });
  }
  show = false;

  addTodoForm() {
    this.show = true;
  }
  closeTodoForm() {
    this.show = false;
  }

  handleSubmit() {
    const titleMaxLength = 20;
    const descriptionMaxLength = 80;

  if (this.todoForm.value.task && this.todoForm.value.description && this.todoForm.value.date) {
    if(this.todoForm.value.task.length <= titleMaxLength && this.todoForm.value.description.length <= descriptionMaxLength) {
      this.http.addTodo(this.todoForm.value).subscribe(
        () => {
          this.ngOnInit();
          this.toaster.success("Todo Added successfully");
        },
        () => {
          this.toaster.error("Error add todo. Server not responded");
        }
      );
    }
    else {
      this.toaster.warning("The task and the description must be in a @SMALL text");
    }

  } else {
    this.toaster.error("Please fill in all required fields.");
  }
}





// show-all.component.ts
updateTaskStatus(id: string, event: Event) {
  const checkbox = event.target as HTMLInputElement;
  const isDone = checkbox.checked; // Get the checked status

  const done: Done = { isDone }; // Create a Done object

  // Call the service method to update the isDone status on the server
  this.http.done(id, done).subscribe(
    () => {
      this.toaster.success("Task status updated successfully!");
    },
    error => {
      this.toaster.error("Error updating task status: " + error.message);
    }
  );
}

getUsername() {
  this.http.getUser().subscribe(
    (username: Username) => {
      this.username = username;
    }
  );
}

logOut() {
  localStorage.removeItem("token");
  localStorage.removeItem("expiration");
  this.toaster.info("You're logged in successfully")
  this.router.navigate(["/"]);
}

}
