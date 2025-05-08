import axiosInstance from './axiosInstance';

const fetchCompanies = async () => {
  try {
    const response = await axiosInstance.get('api/companies/getAll');
    return response.data;
  } catch (error) {
    console.error('Error fetching companies:', error);
    throw error;
  }
};

const registerCompany = async (companyData) => {
  try {
    const response = await axiosInstance.post('api/companies/register', companyData);
    return response.data;
  } catch (error) {
    console.error('Error registering company:', error);
    throw error;
  }
};

const getCompanyById = async (id) => {
  try {
    const response = await axiosInstance.get(`api/companies/getById/${id}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching company by ID:', error);
    throw error;
  }
};

const getUnconfirmedCompanies = async () => {
  try {
    const response = await axiosInstance.get('api/companies/getUnconfirmedCompanies');
    return response.data;
  } catch (error) {
    console.error('Error fetching unconfirmed companies:', error);
    throw error;
  }
};

const getConfirmedCompanies = async () => {
  try {
    const response = await axiosInstance.get('api/companies/getConfirmedCompanies');
    return response.data;
  } catch (error) {
    console.error('Error fetching confirmed companies:', error);
    throw error;
  }
};

const confirmCompany = async (id) => {
  try {
    const response = await axiosInstance.put(`api/companies/confirmCompany/${id}`);
    return response.data;
  } catch (error) {
    console.error('Error confirming company:', error);
    throw error;
  }
};

const unconfirmCompany = async (id) => {
  try {
    const response = await axiosInstance.put(`api/companies/unconfirmCompany/${id}`);
    return response.data;
  } catch (error) {
    console.error('Error unconfirming company:', error);
    throw error;
  }
};

const deleteCompany = async (id) => {
  try {
    await axiosInstance.delete(`api/companies/deleteComapny/${id}`);
    return true;
  } catch (error) {
    console.error('Error deleting company:', error);
    throw error;
  }
};
const fetchMyCompanyEvents = async () => {
  try {
    // The token will be automatically included by axiosInstance if it's configured
    // to do so (e.g., via an interceptor that adds the Authorization header).
    const response = await axiosInstance.get('api/companies/myEvents');
    return response.data; // This should be the list of event objects
  } catch (error) {
    console.error('Error fetching my company events:', error);
    // It's good to check for specific error responses, e.g., 401 for unauthorized
    if (error.response && error.response.status === 401) {
      // Handle unauthorized access, e.g., redirect to login
      console.error("Unauthorized access fetching company events. Redirecting to login or showing error.");
    }
    throw error; // Re-throw to be caught by the component
  }
};
export default {
  fetchCompanies,
  registerCompany,
  getCompanyById,
  getUnconfirmedCompanies,
  getConfirmedCompanies,
  confirmCompany,
  unconfirmCompany,
  deleteCompany,
  fetchMyCompanyEvents
};