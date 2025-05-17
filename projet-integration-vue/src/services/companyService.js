// src/services/companyService.js
import axiosInstance from "./axiosInstance";

const fetchCompanies = async () => { // <<<< DEFINITION
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
  } catch (error)       {
    console.error("Error fetching company by ID:", error.response?.data || error.message);
    throw error;
  }
};

// ... other functions like getUnconfirmedCompanies, getConfirmedCompanies ...

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

const fetchMyCompanyEvents = async () => {
  try {
    const response = await axiosInstance.get("events/companies/2/events");
    return response.data;
  } catch (error) {
    console.error("Error fetching my company events:", error.response?.data || error.message);
    throw error;
  }
};

// Make sure all functions you want to export are defined above this block
export default {
  registerCompany,
  fetchCompanies,         // <<<< ENSURE THIS IS DEFINED ABOVE
  getCompanyById,
  // getUnconfirmedCompanies,
  // getConfirmedCompanies,
  confirmCompany,
  unconfirmCompany,
  deleteCompany,
  // registerEvent,
  fetchMyCompanyEvents,
};