import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Todo } from '../../model/Todo';
import { HttpService } from '../../services/http.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-update',
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.css'] // Change 'styleUrl' to 'styleUrls'
})
export class UpdateComponent implements OnInit {
  todoToBeUpdated!: Todo;
  updateForm!: FormGroup;
  show: boolean = false;
  task: any;

  constructor(private http: HttpService, private router: Router, private fb: FormBuilder, private rout: ActivatedRoute, private toaster: ToastrService) {}

  ngOnInit(): void {
    const idString = this.rout.snapshot.paramMap.get('id');
    const id = String(idString);

    this.http.findTodo(id).subscribe(todo => {
      this.todoToBeUpdated = todo;
      this.updateForm.patchValue(todo);
    });

    this.updateForm = this.fb.group({
      id: [''],
      task: ['', Validators.maxLength(20)],
      description: ['', Validators.maxLength(100)],
      date: [''],
      isDone: [false]
    });

    // Subscribe to value changes in the form fields
    this.updateForm.valueChanges.subscribe(data => {
      this.todoToBeUpdated = data;
    });
  }

  HandleSubmitUpdate() {
    const titleMaxLength = 20;
    const descriptionMaxLength = 100; // Corrected max length value

    if (this.updateForm.value.task && this.updateForm.value.description && this.updateForm.value.date) {
      if(this.updateForm.value.task.length <= titleMaxLength && this.updateForm.value.description.length <= descriptionMaxLength) {
        this.http.updateTodo(this.todoToBeUpdated.id, this.updateForm.value).subscribe(
          () => {
            this.toaster.success("Todo updated successfully");
          },
          () => {
            this.toaster.error("Failed to update todo.");
          }
        );
      } else {
        this.toaster.warning("The task and the description must be in a small text");
      }
    } else {
      this.toaster.error("Please fill in all required fields.");
    }
  }


  deleteTodo(id: string) {
    this.http.deleteTodo(id).subscribe(() => {
      this.router.navigate(["/todo"]);
    });
    this.toaster.info("Todo deleted successfully");
  }
}


