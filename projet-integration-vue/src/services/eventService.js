import axiosInstance from "./axiosInstance";

const fetchEvents = async () => {
  try {
    const response = await axiosInstance.get("api/events/getAll");
    return response.data;
  } catch (error) {
    console.error("Error fetching events:", error);
    throw error;
  }
};

const registerEvents = async (companyData) => {
  try {
    const response = await axiosInstance.post(
      "api/events/addEvent",
      companyData
    );
    return response.data;
  } catch (error) {
    console.error("Error adding event:", error);
    throw error;
  }
};

const getEventsById = async (id) => {
  try {
    const response = await axiosInstance.get(`api/events/getById/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching event by ID:", error);
    throw error;
  }
};

export default {
  fetchCompanies: fetchEvents,
  registerCompany: registerEvents,
  getCompanyById: getEventsById,
};
