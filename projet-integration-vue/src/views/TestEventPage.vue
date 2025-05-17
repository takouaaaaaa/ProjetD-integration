<template>
  <div class="test-events-page">
    <h1>Test Event List (Data from Laravel)</h1>

    <div v-if="loading" class="loading-indicator">
      <p>Loading events from Laravel API...</p>
    </div>

    <div v-else-if="error" class="error-message">
      <p>Failed to load events:</p>
      <p><strong>{{ error.message || 'Unknown error' }}</strong></p>
      <details v-if="error.response">
        <summary>Error Details (from server)</summary>
        <pre>{{ JSON.stringify(error.response.data, null, 2) }}</pre>
        <p>Status: {{ error.response.status }}</p>
      </details>
      <details v-else-if="error.request">
        <summary>Error Details (no response from server)</summary>
        <pre>The request was made but no response was received. Check if the Laravel server is running and accessible at the configured baseURL.</pre>
      </details>
      <p><i>Check browser console (Network tab & Console tab) for more info.</i></p>
    </div>

    <div v-else>
      <EventsList :events="fetchedEvents" @event-updated="loadEventsFromApi" />
      <div v-if="fetchedEvents.length === 0" class="no-events-message">
        <p>No events were found or returned from the API.</p>
      </div>
    </div>

    <hr style="margin: 30px 0;">
    <button @click="loadEventsFromApi" :disabled="loading">
      {{ loading ? 'Reloading...' : 'Reload Events Manually' }}
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import EventsList from '@/components/eventList.vue'; // Adjust path if your component is elsewhere
import eventService from '@/services/eventService';   // Your updated eventService.js

const fetchedEvents = ref([]);
const loading = ref(true);
const error = ref(null);

const loadEventsFromApi = async () => {
  loading.value = true;
  error.value = null; // Clear previous error
  console.log("Attempting to fetch events from Laravel API...");
  try {
    const data = await eventService.fetchEvents();
    fetchedEvents.value = data; // Assuming your API returns an array directly
    console.log("Events fetched successfully:", data);
  } catch (err) {
    console.error("Error in TestEventsPage while loading events:", err);
    error.value = err; // Store the full error object
  } finally {
    loading.value = false;
  }
};

// Fetch events when the component is mounted
onMounted(() => {
  loadEventsFromApi();
});
</script>

<style scoped>
.test-events-page {
  padding: 20px;
  font-family: sans-serif;
}
.loading-indicator, .error-message, .no-events-message {
  padding: 20px;
  margin: 15px 0;
  border: 1px solid #ddd;
  border-radius: 5px;
  text-align: center;
}
.error-message {
  background-color: #ffebee;
  color: #c62828;
  border-color: #ef9a9a;
  text-align: left;
}
.error-message p {
  margin-bottom: 10px;
}
.error-message pre {
  background-color: #f5f5f5;
  padding: 10px;
  border-radius: 3px;
  max-height: 200px;
  overflow-y: auto;
  font-size: 0.9em;
  color: #333;
  white-space: pre-wrap; /* Ensures long lines wrap */
  word-break: break-all; /* Ensures long words/strings break */
}
.error-message details {
  margin-top: 15px;
}
.error-message summary {
  cursor: pointer;
  font-weight: bold;
  margin-bottom: 5px;
}
.no-events-message {
  background-color: #e3f2fd;
  color: #0d47a1;
  border-color: #90caf9;
}
hr {
  border: none;
  border-top: 1px solid #eee;
}
button {
  padding: 10px 15px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1em;
}
button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
button:hover:not(:disabled) {
  background-color: #45a049;
}
</style>