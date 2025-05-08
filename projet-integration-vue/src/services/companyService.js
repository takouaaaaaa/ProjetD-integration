import axiosInstance from './axiosInstance';

// 1. Register a new company
const registerCompany = async (companyData) => {
  try {
    const response = await axiosInstance.post('api/companies/register', companyData);
    return response.data;
  } catch (error) {
    console.error('Error registering company:', error);
    throw error;
  }
};

// 2. Fetch all companies
const fetchCompanies = async () => {
  try {
    const response = await axiosInstance.get('api/companies/getAll');
    return response.data;
  } catch (error) {
    console.error('Error fetching companies:', error);
    throw error;
  }
};

// 3. Get company by ID
const getCompanyById = async (id) => {
  try {
    const response = await axiosInstance.get(`api/companies/getById/${id}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching company by ID:', error);
    throw error;
  }
};

// 4. Get confirmed and unconfirmed companies
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

// 5. Confirm or unconfirm a company
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

// 6. Delete a company
const deleteCompany = async (id) => {
  try {
    await axiosInstance.delete(`api/companies/deleteComapny/${id}`);
    return true;
  } catch (error) {
    console.error('Error deleting company:', error);
    throw error;
  }
};

// 7. Create event for authenticated company
const registerEvent = async (formData) => {
  try {
    const response = await axiosInstance.post('api/companies/addEvent', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  } catch (error) {
    console.error('Error adding event:', error);
    throw error;
  }
};

// 8. Fetch authenticated company's events
const fetchMyCompanyEvents = async () => {
  try {
    const response = await axiosInstance.get('api/companies/myEvents');
    return response.data;
  } catch (error) {
    console.error('Error fetching my company events:', error);
    throw error;
  }
};

export default {
  registerCompany,
  fetchCompanies,
  getCompanyById,
  getUnconfirmedCompanies,
  getConfirmedCompanies,
  confirmCompany,
  unconfirmCompany,
  deleteCompany,
  registerEvent,
  fetchMyCompanyEvents,
};
