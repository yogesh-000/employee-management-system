import { Component, OnInit, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { HrApi } from '../../core/hr-api';
import { LeaveRequest } from '../../core/models';
import { errMsg } from '../../core/error-message';

@Component({
  selector: 'app-approvals',
  imports: [DatePipe],
  templateUrl: './approvals.html',
  styleUrl: './approvals.css',
})
export class Approvals implements OnInit {
  private api = inject(HrApi);

  requests = signal<LeaveRequest[]>([]);
  status = signal('PENDING');
  page = signal(0);
  totalPages = signal(0);
  totalElements = signal(0);
  message = signal('');
  error = signal('');
  remarks: Record<number, string> = {};
  readonly statuses = ['PENDING', 'APPROVED', 'REJECTED', 'CANCELLED'];

  ngOnInit() {
    this.load(0);
  }

  setStatus(s: string) {
    this.status.set(s);
    this.load(0);
  }

  load(page: number) {
    this.api.getLeavesByStatus(this.status(), page, 10).subscribe({
      next: (p) => {
        this.requests.set(p.content);
        this.page.set(p.number);
        this.totalPages.set(p.totalPages);
        this.totalElements.set(p.totalElements);
      },
      error: (err) => this.error.set(errMsg(err)),
    });
  }

  decide(l: LeaveRequest, decision: 'APPROVED' | 'REJECTED') {
    const note = (this.remarks[l.id] ?? '').trim();
    this.api.decideLeave(l.id, decision, note || null).subscribe({
      next: () => {
        this.message.set(`Leave request ${decision.toLowerCase()}.`);
        this.error.set('');
        delete this.remarks[l.id];
        const onlyOneLeft = this.requests().length === 1 && this.page() > 0;
        this.load(onlyOneLeft ? this.page() - 1 : this.page());
      },
      error: (err) => {
        this.message.set('');
        this.error.set(errMsg(err));
      },
    });
  }
}