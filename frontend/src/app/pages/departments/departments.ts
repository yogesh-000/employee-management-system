import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HrApi } from '../../core/hr-api';
import { Department } from '../../core/models';
import { errMsg } from '../../core/error-message';

@Component({
  selector: 'app-departments',
  imports: [ReactiveFormsModule],
  templateUrl: './departments.html',
  styleUrl: './departments.css',
})
export class Departments implements OnInit {
  private api = inject(HrApi);
  private fb = inject(FormBuilder);

  departments = signal<Department[]>([]);
  page = signal(0);
  totalPages = signal(0);
  editingId = signal<number | null>(null);
  showForm = signal(false);
  message = signal('');
  error = signal('');

  form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    description: ['', Validators.maxLength(255)],
  });

  ngOnInit() {
    this.load(0);
  }

  load(page: number) {
    this.api.getDepartments(page, 10).subscribe({
      next: (res) => {
        this.departments.set(res.data.content);
        this.page.set(res.data.number);
        this.totalPages.set(res.data.totalPages);
      },
      error: (err) => this.error.set(errMsg(err)),
    });
  }

  openAdd() {
    this.editingId.set(null);
    this.form.reset();
    this.showForm.set(true);
  }

  openEdit(d: Department) {
    this.editingId.set(d.id);
    this.form.setValue({ name: d.name, description: d.description ?? '' });
    this.showForm.set(true);
  }

  save() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const body = this.form.getRawValue();
    const id = this.editingId();
    const request = id === null ? this.api.createDepartment(body) : this.api.updateDepartment(id, body);

    request.subscribe({
      next: () => {
        this.message.set(id === null ? 'Department created.' : 'Department updated.');
        this.error.set('');
        this.showForm.set(false);
        this.load(id === null ? 0 : this.page());
      },
      error: (err) => {
        this.message.set('');
        this.error.set(errMsg(err));
      },
    });
  }

  remove(d: Department) {
    if (!confirm(`Delete department "${d.name}"?`)) return;
    this.api.deleteDepartment(d.id).subscribe({
      next: () => {
        this.message.set('Department deleted.');
        this.error.set('');
        this.load(this.departments().length === 1 && this.page() > 0 ? this.page() - 1 : this.page());
      },
      error: (err) => {
        this.message.set('');
        this.error.set(errMsg(err));
      },
    });
  }
}