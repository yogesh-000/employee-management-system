import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { HrApi } from '../../core/hr-api';
import { Department, Employee } from '../../core/models';

@Component({
  selector: 'app-employees',
  imports: [ReactiveFormsModule],
  templateUrl: './employees.html',
  styleUrl: './employees.css',
})
export class Employees implements OnInit {
  private api = inject(HrApi);
  private fb = inject(FormBuilder);

  employees = signal<Employee[]>([]);
  departments = signal<Department[]>([]);
  page = signal(0);
  totalPages = signal(0);
  totalElements = signal(0);
  loading = signal(false);
  showForm = signal(false);
  message = signal('');
  error = signal('');

  me = localStorage.getItem('username');
  searchCtrl = new FormControl('', { nonNullable: true });

  // Verify these IDs with: select * from roles;
  readonly roles = [
    { id: 1, name: 'ADMIN' },
    { id: 2, name: 'MANAGER' },
    { id: 3, name: 'EMPLOYEE' },
  ];
  readonly genders = ['MALE', 'FEMALE', 'OTHER'];

  form = this.fb.nonNullable.group({
    firstName: ['', [Validators.required, Validators.minLength(2)]],
    lastName: ['', [Validators.required, Validators.minLength(2)]],
    email: ['', [Validators.required, Validators.email]],
    phone: ['', [Validators.required, Validators.pattern(/^[6-9]\d{9}$/)]],
    gender: ['MALE', Validators.required],
    designation: ['', [Validators.required, Validators.minLength(2)]],
    salary: [0, [Validators.required, Validators.min(1)]],
    joiningDate: ['', Validators.required],
    departmentId: [0, [Validators.required, Validators.min(1)]],
    roleId: [3, Validators.required],
    managerId: [null as number | null],
    username: ['', [Validators.required, Validators.minLength(4)]],
    password: ['', [Validators.required, Validators.minLength(6)]],
  });

editingId = signal<number | null>(null);

editForm = this.fb.nonNullable.group({
  firstName: ['', [Validators.required, Validators.minLength(2)]],
  lastName: ['', [Validators.required, Validators.minLength(2)]],
  email: ['', [Validators.required, Validators.email]],
  phone: ['', [Validators.required, Validators.pattern(/^[6-9]\d{9}$/)]],
  gender: ['MALE', Validators.required],
  designation: ['', [Validators.required, Validators.minLength(2)]],
  salary: [0, [Validators.required, Validators.min(1)]],
  joiningDate: ['', Validators.required],
  departmentId: [0, [Validators.required, Validators.min(1)]],
  managerId: [null as number | null],
});

  ngOnInit() {
    this.load(0);
    this.api.getDepartments().subscribe({
      next: (res) => this.departments.set(res.data.content),
      error: (err) => this.error.set(this.errMsg(err)),
    });
  }

  load(page: number) {
    this.loading.set(true);
    this.api.getEmployees(page, 10, this.searchCtrl.value.trim()).subscribe({
      next: (res) => {
        const p = res.data;
        this.employees.set(p.content);
        this.page.set(p.number);
        this.totalPages.set(p.totalPages);
        this.totalElements.set(p.totalElements);
        this.loading.set(false);
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(this.errMsg(err));
      },
    });
  }

  clearSearch() {
    this.searchCtrl.setValue('');
    this.load(0);
  }

  save() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    this.api.createEmployee({ ...v, managerId: v.managerId || null }).subscribe({
      next: () => {
        this.message.set('Employee created.');
        this.error.set('');
        this.showForm.set(false);
        this.form.reset();
        this.load(0);
      },
      error: (err) => {
        this.message.set('');
        this.error.set(this.errMsg(err));
      },
    });
  }

  remove(e: Employee) {
    if (!confirm(`Delete ${e.firstName} ${e.lastName}?`)) return;
    this.api.deleteEmployee(e.id).subscribe({
      next: () => {
        this.message.set('Employee deleted.');
        this.error.set('');
        this.load(this.page());
      },
      error: (err) => this.error.set(this.errMsg(err)),
    });
  }

openEdit(e: Employee) {
  this.showForm.set(false);
  this.editingId.set(e.id);

  const dept = this.departments().find((d) => d.name === e.department);
  const mgr = this.employees().find(
    (x) => e.manager === `${x.firstName} ${x.lastName}` && x.id !== e.id
  );

  this.editForm.setValue({
    firstName: e.firstName,
    lastName: e.lastName,
    email: e.email,
    phone: e.phone,
    gender: e.gender,
    designation: e.designation,
    salary: e.salary,
    joiningDate: e.joiningDate,
    departmentId: dept ? dept.id : 0,
    managerId: mgr ? mgr.id : null,
  });
}

cancelEdit() {
  this.editingId.set(null);
  this.editForm.reset();
}

saveEdit() {
  if (this.editForm.invalid) {
    this.editForm.markAllAsTouched();
    return;
  }
  const id = this.editingId();
  if (id === null) return;

  this.api.updateEmployee(id, this.editForm.getRawValue()).subscribe({
    next: () => {
      this.message.set('Employee updated.');
      this.error.set('');
      this.editingId.set(null);
      this.load(this.page());
    },
    error: (err) => {
      this.message.set('');
      this.error.set(this.errMsg(err));
    },
  });
}

  private errMsg(err: any): string {
    if (err.status === 0) return 'Cannot reach the server.';
    if (err.status === 403) return 'You do not have permission to do that.';
    return err.error?.message || 'Something went wrong.';
  }
}