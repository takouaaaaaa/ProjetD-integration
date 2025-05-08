import axiosInstance from './axiosInstance';

const fetchEvents = async () => {
  try {
    const response = await axiosInstance.get('api/events/getAll');
    return response.data;
  } catch (error) {
    console.error('Error fetching events:', error);
    throw error;
  }
};

const getEventById = async (id) => {
  try {
    const response = await axiosInstance.get(`api/events/getById/${id}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching event by ID:', error);
    throw error;
  }
};

const getEventEtat = async (id) => {
  try {
    const response = await axiosInstance.get(`api/events/${id}/etat`);
    return response.data;
  } catch (error) {
    console.error('Error fetching event status:', error);
    throw error;
  }
};

const acceptEvent = async (id) => {
  try {
    const response = await axiosInstance.put(`api/events/${id}/accepter`);
    return response.data;
  } catch (error) {
    console.error('Error accepting event:', error);
    throw error;
  }
};

const rejectEvent = async (id) => {
  try {
    const response = await axiosInstance.put(`api/events/${id}/rejeter`);
    return response.data;
  } catch (error) {
    console.error('Error rejecting event:', error);
    throw error;
  }
};

const updateEvent = async (id, updatedData) => {
  try {
    const response = await axiosInstance.put(`api/events/updateEvent/${id}`, updatedData);
    return response.data;
  } catch (error) {
    console.error('Error updating event:', error);
    throw error;
  }
};

export default {
  fetchEvents,
  getEventById,
  getEventEtat,
  acceptEvent,
  rejectEvent,
  updateEvent,
};
