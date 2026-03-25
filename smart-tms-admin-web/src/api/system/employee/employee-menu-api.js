import { getRequest, postRequest } from '/@/lib/axios';
export const employeeMenuApi = {

  getEmployeeSelectedMenu: (employeeId) => {
    return getRequest(`/employee/menu/getEmployeeSelectedMenu/${employeeId}`);
  },
  getEmployeeRoleMenu: (employeeId) => {
    return getRequest(`/employee/menu/getEmployeeRoleMenu/${employeeId}`);
  },
  updateEmployeeMenu: (data) => {
    return postRequest('/employee/menu/updateEmployeeMenu', data);
  },
};
