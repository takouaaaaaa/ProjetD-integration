<template>
    <div class="update-event-container">
      <h2>Update Event</h2>
  
      <div v-if="loading" class="loading-indicator">
        <div class="spinner"></div>
        <p>Loading event details...</p>
      </div>
  
      <div v-else-if="fetchError" class="error-message">
        <i class="bi bi-exclamation-triangle-fill"></i> {{ fetchError }}
      </div>
  
      <form v-else @submit.prevent="handleUpdate" class="update-form">
        <div class="event-info-display">
          <h4>Current Event: {{ eventData.nom }}</h4>
          <p>Current Date: {{ formatDateDisplay(eventData.date) }}</p>
          <p>Current Time: {{ formatTimeDisplay(eventData.time) }}</p>
          <p>Current Location: {{ eventData.localisation }}</p>
        </div>
  
        <hr />
        <h4>Edit Details:</h4>
  
        <div class="form-group">
          <label for="eventDate">New Event Date:</label>
          <input type="date" id="eventDate" v-model="formData.date" class="form-control" />
        </div>
  
        <div class="form-group">
          <label for="eventTime">New Event Time:</label>
          <input type="time" id="eventTime" v-model="formData.time" class="form-control" />
        </div>
  
        <div class="form-group">
          <label for="eventLocation">New Event Location:</label>
          <input
            type="text"
            id="eventLocation"
            v-model="formData.localisation"
            class="form-control"
            placeholder="Enter new location"
          />
        </div>
  
        <div v-if="successMessage" class="success-message">
          <i class="bi bi-check-circle-fill"></i> {{ successMessage }}
        </div>
  
        <div v-if="updateError" class="error-message">
          <i class="bi bi-exclamation-triangle-fill"></i> {{ updateError }}
        </div>
  
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="isSubmitting">
            {{ isSubmitting ? "Updating..." : "Save Changes" }}
          </button>
          <router-link :to="`/events/details/${route.params.id}`" class="btn btn-secondary">Cancel</router-link>
        </div>
      </form>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from "vue";
  import { useRoute, useRouter } from "vue-router";
  import eventService from "@/services/eventService";
  
  const route = useRoute();
  const router = useRouter();
  
  const eventData = ref(null);
  const formData = ref({
    date: "",
    time: "",
    localisation: "",
  });
  
  const loading = ref(true);
  const fetchError = ref("");
  const updateError = ref("");
  const successMessage = ref("");
  const isSubmitting = ref(false);
  
  const formatDateForInput = (dateString) => {
    try {
      const date = new Date(dateString);
      return date.toISOString().split("T")[0];
    } catch {
      return "";
    }
  };
  
  const formatTimeForInput = (timeString) => {
    try {
      const date = new Date(`1970-01-01T${timeString}`);
      return date.toTimeString().slice(0, 5);
    } catch {
      return "";
    }
  };
  
  const formatDateDisplay = (dateString) => {
    try {
      return new Date(dateString).toLocaleDateString(undefined, {
        year: "numeric",
        month: "long",
        day: "numeric",
      });
    } catch {
      return "N/A";
    }
  };
  
  const formatTimeDisplay = (timeString) => {
    try {
      const [hour, minute] = timeString.split(":");
      const date = new Date();
      date.setHours(parseInt(hour), parseInt(minute));
      return date.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit", hour12: true });
    } catch {
      return "N/A";
    }
  };
  
  const fetchEventDetails = async () => {
    try {
      const eventId = route.params.id;
      const event = await eventService.getEventById(eventId);
  
      // Normalize event fields
      eventData.value = {
        ...event,
        date: event.date || event.eventDate,
        time: event.time || event.eventTime,
        localisation: event.localisation || event.lieu || event.location,
      };
  
      // Pre-fill form inputs
      formData.value.date = formatDateForInput(eventData.value.date);
      formData.value.time = formatTimeForInput(eventData.value.time);
      formData.value.localisation = eventData.value.localisation || "";
    } catch (err) {
      fetchError.value = err.response?.status === 404 ? "Event not found." : "Failed to load event details.";
    } finally {
      loading.value = false;
    }
  };
  
  const handleUpdate = async () => {
    isSubmitting.value = true;
    updateError.value = "";
    successMessage.value = "";
  
    const payload = {
      date: formData.value.date || eventData.value.date,
      time: formData.value.time || eventData.value.time,
      localisation: formData.value.localisation || eventData.value.localisation,
    };
  
    try {
      await eventService.updateEvent(route.params.id, payload);
      successMessage.value = "Event updated successfully!";
      setTimeout(() => {
        router.push("/companies");
      }, 2000);
    } catch (err) {
      updateError.value = "Failed to update event. ";
      if (err.response?.data) {
        updateError.value += typeof err.response.data === "string"
          ? err.response.data
          : err.response.data.message || JSON.stringify(err.response.data);
      } else {
        updateError.value += "Please try again later.";
      }
    } finally {
      isSubmitting.value = false;
    }
  };
  
  onMounted(fetchEventDetails);
  </script>
  
  
  <style scoped>
  .update-event-container {
    max-width: 700px;
    margin: 30px auto;
    padding: 25px;
    background-color: #ffffff;
    border-radius: 8px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  }
  
  h2 {
    text-align: center;
    color: #333;
    margin-bottom: 25px;
    font-weight: 600;
  }
  
  .update-form .form-group {
    margin-bottom: 20px;
  }
  
  .update-form label {
    display: block;
    margin-bottom: 8px;
    font-weight: 500;
    color: #555;
  }
  
  .form-control {
    width: 100%;
    padding: 10px 12px;
    border: 1px solid #ced4da;
    border-radius: 4px;
    box-sizing: border-box;
    font-size: 1rem;
    transition: border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
  }
  .form-control:focus {
    border-color: #80bdff;
    outline: 0;
    box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
  }
  
  .form-actions {
    margin-top: 30px;
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
  
  .btn {
    padding: 10px 20px;
    border-radius: 4px;
    text-decoration: none;
    font-size: 0.95rem;
    font-weight: 500;
    cursor: pointer;
    transition: background-color 0.2s ease, border-color 0.2s ease;
  }
  
  .btn-primary {
    background-color: #007bff;
    color: white;
    border: 1px solid #007bff;
  }
  .btn-primary:hover {
    background-color: #0056b3;
    border-color: #0056b3;
  }
  .btn-primary:disabled {
    background-color: #6c757d;
    border-color: #6c757d;
    cursor: not-allowed;
  }
  
  .btn-secondary {
    background-color: #6c757d;
    color: white;
    border: 1px solid #6c757d;
  }
  .btn-secondary:hover {
    background-color: #545b62;
    border-color: #545b62;
  }
  
  .loading-indicator,
  .error-message,
  .success-message {
    text-align: center;
    padding: 15px;
    margin-bottom: 20px;
    border-radius: 5px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .loading-indicator .spinner {
    width: 30px;
    height: 30px;
    border: 3px solid rgba(0, 0, 0, 0.1);
    border-left-color: #007bff;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    margin-right: 10px;
  }
  @keyframes spin {
    to {
      transform: rotate(360deg);
    }
  }
  .error-message i,
  .success-message i {
    margin-right: 8px;
    font-size: 1.2rem;
  }
  
  .error-message {
    color: #721c24;
    background-color: #f8d7da;
    border: 1px solid #f5c6cb;
  }
  
  .success-message {
    color: #155724;
    background-color: #d4edda;
    border: 1px solid #c3e6cb;
  }
  
  .event-info-display {
    background-color: #f8f9fa;
    padding: 15px;
    border-radius: 5px;
    margin-bottom: 20px;
    border: 1px solid #e9ecef;
  }
  .event-info-display h4 {
    margin-top: 0;
    margin-bottom: 10px;
    color: #007bff;
  }
  .event-info-display p {
    margin-bottom: 5px;
    font-size: 0.95rem;
    color: #495057;
  }
  hr {
      margin-top: 20px;
      margin-bottom: 20px;
      border: 0;
      border-top: 1px solid #dee2e6;
  }
  </style>