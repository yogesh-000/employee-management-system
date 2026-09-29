export interface ApiResponse<T> { success: boolean; message: string; data: T; }

export interface Page<T> {
  content: T[];
  number: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface EmployeeUpdateRequest {
  firstName: string; lastName: string; email: string; phone: string;
  gender: string; designation: string; salary: number; joiningDate: string;
  departmentId: number;
}

export interface Department { id: number; name: string; description: string; }

export interface Employee {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  gender: string;
  designation: string;
  salary: number;
  joiningDate: string;
  status: string;
  department: string;
  role: string;
  manager: string | null;
  username: string;
}

export interface EmployeeRequest {
  firstName: string; lastName: string; email: string; phone: string;
  gender: string; designation: string; salary: number; joiningDate: string;
  departmentId: number; roleId: number; managerId: number | null;
  username: string; password: string;
}

export interface LeaveType { id: number; name: string; description: string; maxDays: number; }

export interface DepartmentRequest { name: string; description: string; }

export interface LeaveRequest {
  id: number;
  employeeName: string;
  leaveType: string;
  startDate: string;
  endDate: string;
  reason: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED' | 'CANCELLED';
  appliedAt: string;
  approvedBy: string | null;
  remarks: string | null;
}

export interface LeaveHistory { employeeId: number; employeeName: string; leaveRequests: LeaveRequest[]; }

export interface ApplyLeaveBody { leaveTypeId: number; startDate: string; endDate: string; reason: string; }