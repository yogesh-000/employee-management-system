import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse, Department, Employee, EmployeeRequest, Page, ApplyLeaveBody, LeaveHistory, LeaveRequest, LeaveType, DepartmentRequest, EmployeeUpdateRequest } from './models';

@Injectable({ providedIn: 'root' })
export class HrApi {

    getLeavesByStatus(status: string, page: number, size: number) {
  const params = new HttpParams().set('page', page).set('size', size);
  return this.http.get<Page<LeaveRequest>>(`${this.base}/leave-requests/status/${status}`, { params });
}

decideLeave(id: number, status: 'APPROVED' | 'REJECTED', remarks: string | null) {
  return this.http.put<LeaveRequest>(`${this.base}/leave-requests/${id}/approve`, { status, remarks });
}

    getLeaveTypes() {
  const params = new HttpParams().set('size', 100);
  return this.http.get<ApiResponse<Page<LeaveType>>>(`${this.base}/leave-types`, { params });
}

applyLeave(body: ApplyLeaveBody) {
  return this.http.post<LeaveRequest>(`${this.base}/leave-requests`, body);
}

getMyLeaves() {
  return this.http.get<LeaveHistory>(`${this.base}/leave-requests/my`);
}

cancelLeave(id: number) {
  return this.http.put<LeaveRequest>(`${this.base}/leave-requests/${id}/cancel`, {});
}

  private http = inject(HttpClient);
  private base = 'http://localhost:8080/api';

  getEmployees(page: number, size: number, keyword: string): Observable<ApiResponse<Page<Employee>>> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (keyword) {
      params = params.set('keyword', keyword);
      return this.http.get<ApiResponse<Page<Employee>>>(`${this.base}/employees/search`, { params });
    }
    return this.http.get<ApiResponse<Page<Employee>>>(`${this.base}/employees`, { params });
  }

  createEmployee(body: EmployeeRequest) {
    return this.http.post<ApiResponse<Employee>>(`${this.base}/employees`, body);
  }

  updateEmployee(id: number, body: EmployeeUpdateRequest) {
  return this.http.put<ApiResponse<Employee>>(`${this.base}/employees/${id}`, body);
}

  deleteEmployee(id: number) {
    return this.http.delete<void>(`${this.base}/employees/${id}`);
  }

    getDepartments(page = 0, size = 100) {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<ApiResponse<Page<Department>>>(`${this.base}/departments`, { params });
    }

    createDepartment(body: DepartmentRequest) {
    return this.http.post<Department>(`${this.base}/departments`, body);
    }

    updateDepartment(id: number, body: DepartmentRequest) {
    return this.http.put<Department>(`${this.base}/departments/${id}`, body);
    }

    deleteDepartment(id: number) {
    return this.http.delete<void>(`${this.base}/departments/${id}`);
    }

}