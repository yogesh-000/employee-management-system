import { Component, OnInit, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HrApi } from '../../core/hr-api';
import { LeaveRequest, LeaveType } from '../../core/models';
import { errMsg } from '../../core/error-message';

@Component({
  selector: 'app-my-leaves',
  imports: [ReactiveFormsModule, DatePipe],
  templateUrl: './my-leaves.html',
  styleUrl: './my-leaves.css',
})
export class MyLeaves implements OnInit {
  private api = inject(HrApi);
  private fb = inject(FormBuilder);

  leaves = signal<LeaveRequest[]>([]);
  leaveTypes = signal<LeaveType[]>([]);
  message = signal('');
  error = signal('');
  today = new Date().toLocaleDateString('en-CA'); // yyyy-MM-dd in local time

  form = this.fb.nonNullable.group({
    leaveTypeId: [0, [Validators.required, Validators.min(1)]],
    startDate: ['', Validators.required],
    endDate: ['', Validators.required],
    reason: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(500)]],
  });

  ngOnInit() {
    this.loadLeaves();
    this.api.getLeaveTypes().subscribe({
      next: (res) => this.leaveTypes.set(res.data.content),
      error: (err) => this.error.set(errMsg(err)),
    });
  }

  loadLeaves() {
    this.api.getMyLeaves().subscribe({
      next: (res) => this.leaves.set(res.leaveRequests),
      error: (err) => this.error.set(errMsg(err)),
    });
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    if (v.endDate < v.startDate) {
      this.message.set('');
      this.error.set('End date cannot be before start date.');
      return;
    }
    this.api.applyLeave(v).subscribe({
      next: () => {
        this.message.set('Leave request submitted.');
        this.error.set('');
        this.form.reset();
        this.loadLeaves();
      },
      error: (err) => {
        this.message.set('');
        this.error.set(errMsg(err));
      },
    });
  }

  cancel(l: LeaveRequest) {
    if (!confirm('Cancel this leave request?')) return;
    this.api.cancelLeave(l.id).subscribe({
      next: () => {
        this.message.set('Leave request cancelled.');
        this.error.set('');
        this.loadLeaves();
      },
      error: (err) => {
        this.message.set('');
        this.error.set(errMsg(err));
      },
    });
  }
}