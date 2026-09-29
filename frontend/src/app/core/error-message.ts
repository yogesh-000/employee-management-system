export function errMsg(err: any): string {
  if (err.status === 0) return 'Cannot reach the server.';
  if (err.status === 403) return 'You do not have permission to do that.';
  return err.error?.message || 'Something went wrong.';
}