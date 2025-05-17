import axiosInstance from "./axiosInstance"; // Your configured Axios instance

// ... fetchEvents (looks good) ...
const fetchEvents = async () => {
  try {
    const response = await axiosInstance.get("events/getAll"); // Correct: baseURL + "events/getAll"
    return response.data;
  } catch (error) {
    console.error("Error fetching events from Laravel:", error);
    if (error.response) {
      console.error("Error data:", error.response.data);
      console.error("Error status:", error.response.status);
    }
    throw error;
  }
};

// ... getEventById (adjust path if needed, current one seems to have "api/" prefix again) ...
const getEventById = async (id) => {
  try {
    // If baseURL is "http://localhost:8000/api/", then path should be "events/getById/${id}"
    const response = await axiosInstance.get(`events/getById/${id}`); // REMOVED "api/" prefix
    return response.data;
  } catch (error) {
    console.error("Error fetching event by ID from Laravel:", error);
    throw error;
  }
};


// --- CORRECTED METHODS ---
const acceptEvent = async (id) => {
  try {
    // Laravel route: PUT /api/events/{id}/accept
    // Axios baseURL: http://localhost:8000/api/
    // Relative path: events/{id}/accept
    const response = await axiosInstance.put(`events/${id}/accept`); // CORRECTED PATH
    return response.data;
  } catch (error) {
    console.error("Error accepting event via Laravel:", error);
    if (error.response) {
      console.error("Error data:", error.response.data);
      console.error("Error status:", error.response.status);
    }
    throw error;
  }
};

const rejectEvent = async (id) => {
  try {
    // Laravel route: PUT /api/events/{id}/reject
    // Axios baseURL: http://localhost:8000/api/
    // Relative path: events/{id}/reject
    const response = await axiosInstance.put(`events/${id}/reject`); // CORRECTED PATH
    return response.data;
  } catch (error) {
    console.error("Error rejecting event via Laravel:", error);
    if (error.response) {
      console.error("Error data:", error.response.data);
      console.error("Error status:", error.response.status);
    }
    throw error;
  }
};
// --- END CORRECTED METHODS ---

// Review other methods for the same potential "api/" prefix issue if baseURL already has it
const getEventEtat = async (id) => {
  try {
    // Check your Laravel route for this. Assuming it's /api/events/{id}/etat
    const response = await axiosInstance.get(`events/${id}/etat`); // REMOVED "api/" prefix
    return response.data;
  } catch (error) {
    console.error("Error fetching event status:", error);
    throw error;
  }
};

const updateEvent = async (id, updatedData) => {
  try {
    // Laravel route: PUT /api/events/{id}/update
    const response = await axiosInstance.put(
      `events/${id}/update`, // CORRECTED PATH from "api/events/updateEvent/${id}"
      updatedData
    );
    return response.data;
  } catch (error) {
    console.error("Error updating event:", error);
    throw error;
  }
};

// src/services/eventService.js
// ... other functions ...

const deleteEvent = async (/*id*/) => { // << 'id' is defined here
  try {
    // Check your Laravel route for delete. Assuming something like /api/events/{id} with DELETE method
    // If Laravel route is DELETE /api/events/{id}
    // const response = await axiosInstance.delete(`events/${id}`); // << 'id' would be used here
    // For now, commenting out until we confirm the Laravel delete route
    console.warn("deleteEvent called, ensure matching Laravel route: DELETE /api/events/{id}");
    // await axiosInstance.delete(`events/delete/${id}`); // This path doesn't match standard REST
    return true;
  } catch (error) {
    console.error("Error deleting event:", error);
    throw error;
  }
};


export default {
  fetchEvents,
  getEventById,
  getEventEtat, // Make sure you have a Laravel route for this if you use it
  acceptEvent,
  rejectEvent,
  updateEvent,
  deleteEvent, // Make sure you have a Laravel route for this if you use it
};