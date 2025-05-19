
import axiosInstance from "./axiosInstance";


const fetchCompanies = async () => {
  try {
    const response = await axiosInstance.get("companies/getAll");
    return response.data;
  } catch (error) {
    console.error("Error fetching companies:", error.response?.data || error.message);
    throw error;
  }
};

const registerCompany = async (companyData) => {
  try {
    const response = await axiosInstance.post("companies/register", companyData);
    return response.data;
  } catch (error) {
    console.error("Error registering company:", error.response?.data || error.message);
    throw error;
  }
};

const getCompanyById = async (id) => {
  try {
    const response = await axiosInstance.get(`companies/getById/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching company by ID:", error.response?.data || error.message);
    throw error;
  }
};

const getUnconfirmedCompanies = async () => {
  try {
    const response = await axiosInstance.get("companies/unconfirmed");
    return response.data;
  } catch (error) {
    console.error("Error fetching unconfirmed companies:", error.response?.data || error.message);
    throw error;
  }
};

const getConfirmedCompanies = async () => {
  try {
    const response = await axiosInstance.get("companies/confirmed");
    return response.data;
  } catch (error) {
    console.error("Error fetching confirmed companies:", error.response?.data || error.message);
    throw error;
  }
};

const confirmCompany = async (id) => {
  try {
    const response = await axiosInstance.put(`companies/${id}/confirm`);
    return response.data;
  } catch (error) {
    console.error(`Error confirming company ${id}:`, error.response?.data || error.message);
    throw error;
  }
};

const unconfirmCompany = async (id) => {
  try {
    const response = await axiosInstance.put(`companies/${id}/unconfirm`);
    return response.data;
  } catch (error) {
    console.error(`Error unconfirming company ${id}:`, error.response?.data || error.message);
    throw error;
  }
};

const deleteCompany = async (id) => {
  try {
    await axiosInstance.delete(`companies/${id}/delete`);
    return true;
  } catch (error) {
    console.error(`Error deleting company ${id}:`, error.response?.data || error.message);
    throw error;
  }
};


const fetchCompanyEvents = async (companyId) => {
  try {
    const response = await axiosInstance.get(`events/companies/${companyId}/events`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching events for company ${companyId}:`, error.response?.data || error.message);
    throw error;
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
  fetchCompanyEvents,
};
