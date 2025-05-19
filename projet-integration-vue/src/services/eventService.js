import axiosInstance from "./axiosInstance"; 
const fetchEvents = async () => {
  try {
    const response = await axiosInstance.get("events/getAll"); 
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

const getEventById = async (id) => {
  try {
    
    const response = await axiosInstance.get(`events/getById/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching event by ID from Laravel:", error);
    throw error;
  }
};

const acceptEvent = async (id) => {
  try {
   
    const response = await axiosInstance.put(`events/${id}/accept`); 
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
    
    const response = await axiosInstance.put(`events/${id}/reject`); 
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

const getEventEtat = async (id) => {
  try {

    const response = await axiosInstance.get(`events/${id}/etat`); 
    return response.data;
  } catch (error) {
    console.error("Error fetching event status:", error);
    throw error;
  }
};

const updateEvent = async (id, updatedData) => {
  try {
   
    const response = await axiosInstance.put(
      `events/${id}/update`, 
      updatedData
    );
    return response.data;
  } catch (error) {
    console.error("Error updating event:", error);
    throw error;
  }
};

const deleteEvent = async (/*id*/) => {
  try {
  
    console.warn("deleteEvent called, ensure matching Laravel route: DELETE /api/events/{id}");
    
    return true;
  } catch (error) {
    console.error("Error deleting event:", error);
    throw error;
  }
};
const addEvent = async (eventData) => {
  try {
   
    const response = await axiosInstance.post("events/addEvent", eventData, {
      headers: {
        'Content-Type': 'multipart/form-data' 
      }
    });
    return response.data;
  } catch (error) {
    console.error("Error adding event:", error);
    if (error.response) {
      console.error("Error data:", error.response.data);
      console.error("Error status:", error.response.status);
      
    }
    throw error;
  }
};


export default {
  fetchEvents,
  getEventById,
  getEventEtat,
    addEvent, 
  acceptEvent,
  rejectEvent,
  updateEvent,
  deleteEvent, 
};