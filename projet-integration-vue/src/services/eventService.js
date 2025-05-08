import axiosInstance from "./axiosInstance";

// 1. Add a new event (multipart/form-data)
const registerEvent = async (formData) => {
  try {
    const response = await axiosInstance.post("api/events/addEvent", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error adding event:", error);
    throw error;
  }
};

// 2. Get all events
const fetchEvents = async () => {
  try {
    const response = await axiosInstance.get("api/events/getAll");
    return response.data;
  } catch (error) {
    console.error("Error fetching events:", error);
    throw error;
  }
};

// 3. Get event by ID
const getEventById = async (id) => {
  try {
    const response = await axiosInstance.get(`api/events/getById/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching event by ID:", error);
    throw error;
  }
};

// 4. Get events by company ID
const getEventsByCompany = async (companyId) => {
  try {
    const response = await axiosInstance.get(`api/events/companies/${companyId}/events`);
    return response.data;
  } catch (error) {
    console.error("Error fetching events by company:", error);
    throw error;
  }
};

// 5. Get event status (Etat) by ID
const getEventEtat = async (id) => {
  try {
    const response = await axiosInstance.get(`api/events/${id}/etat`);
    return response.data;
  } catch (error) {
    console.error("Error fetching event status:", error);
    throw error;
  }
};

// 6. Accept an event
const acceptEvent = async (id) => {
  try {
    const response = await axiosInstance.put(`api/events/${id}/accepter`);
    return response.data;
  } catch (error) {
    console.error("Error accepting event:", error);
    throw error;
  }
};

// 7. Reject an event
const rejectEvent = async (id) => {
  try {
    const response = await axiosInstance.put(`api/events/${id}/rejeter`);
    return response.data;
  } catch (error) {
    console.error("Error rejecting event:", error);
    throw error;
  }
};

// 8. Update an event
const updateEvent = async (id, updatedData) => {
  try {
    const response = await axiosInstance.put(`api/events/updateEvent/${id}`, updatedData);
    return response.data;
  } catch (error) {
    console.error("Error updating event:", error);
    throw error;
  }
};

// Export all
export default {
  registerEvent,
  fetchEvents,
  getEventById,
  getEventsByCompany,
  getEventEtat,
  acceptEvent,
  rejectEvent,
  updateEvent,
};
