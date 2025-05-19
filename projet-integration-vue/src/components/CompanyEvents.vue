<template>
  <div class="company-events-container">
    <h2 class="events-title">My Company's Events</h2>
    <div v-if="loading" class="loading-indicator">
      <div class="spinner"></div>
      <p>Loading events...</p>
    </div>
    <div v-else-if="error" class="error-message">
      <i class="bi bi-exclamation-triangle-fill"></i> {{ error }}
    </div>
    <div v-else-if="events.length === 0" class="no-events-message">
      <i class="bi bi-calendar-x"></i>
      <p>You haven't created any events yet.</p>
    </div>
    <div v-else class="events-list">
      <div v-for="event in events" :key="event.id" class="event-card">
        <div class="event-details">
          <h3 class="event-name">{{ event.nom || event.name }}</h3>

          <img :src="getImageUrl(event.image)" alt="" />
          <p class="event-description">
            {{ event.description }}
          </p>

          <div class="event-info">
            <span
              ><i class="bi bi-calendar-event"></i>
              {{ formatDate(event.date || event.eventDate) }}</span
            >
            <span
              ><i class="bi bi-clock"></i>
              {{ formatTime(event.time || event.eventTime) }}</span
            >
          </div>
          <div class="event-info">
            <span
              ><i class="bi bi-geo-alt-fill"></i> {{ event.localisation }}</span
            >
            <span :class="['event-status', getStatusClass(event.etat)]">{{
              formatStatus(event.etat)
            }}</span>
          </div>
          <div class="event-actions">
            <button
              class="btn btn-sm btn-outline-secondary"
              @click="navigateToUpdate(event.id)"
            >
              Edit
            </button>
            <button
              class="btn btn-sm btn-danger"
              @click="deleteEvent(event.id)"
            >
              Delete
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import companyService from "@/services/companyService";
import eventService from "@/services/eventService";

export default {
  name: "CompanyEvents",
  data() {
    return {
      events: [],
      loading: true,
      error: "",
    };
  },
  async created() {
    await this.fetchMyEvents();
  },
  methods: {
    getImageUrl(filename) {
      try {
        return require(`@/assets/eventImages/${filename}`);
      } catch {
        return require("@/assets/No_Image_Available.jpg");
      }
    },
    
async fetchMyEvents() {
  this.loading = true;
  this.error = "";
  try {
    
    const companyIdToTest = 1; 
    this.events = await companyService.fetchCompanyEvents(companyIdToTest);
    console.log("Fetched events:", this.events); 

  } catch (err) {
   
    console.error("Full error object in fetchMyEvents:", err);
    if (err.response) {
        console.error("Error response data:", err.response.data);
        console.error("Error response status:", err.response.status);
        console.error("Error response headers:", err.response.headers);
    } else if (err.request) {
        console.error("Error request data:", err.request);
    } else {
        console.error('Error message:', err.message);
    }

    if (err.response && err.response.status === 401) {
      this.error =
        "You are not authorized to view these events. Please log in as a company.";
    } else if (err.response && err.response.status === 404) {
      this.error =
        "No events found for this company, or the company ID is invalid.";
    } else {
      this.error =
        "Failed to load your company events. Please try again later.";
    }
    
  } finally {
    this.loading = false;
  }
},
    formatDate(dateString) {
      if (!dateString) return "N/A";
      const options = { year: "numeric", month: "long", day: "numeric" };
      return new Date(dateString).toLocaleDateString(undefined, options);
    },
    formatTime(timeString) {
      if (!timeString) return "N/A";
      try {
        if (
          typeof timeString === "string" &&
          timeString.includes(":") &&
          !timeString.includes("T")
        ) {
          const [hours, minutes] = timeString.split(":");
          const date = new Date();
          date.setHours(parseInt(hours, 10), parseInt(minutes, 10), 0);
          return date.toLocaleTimeString([], {
            hour: "2-digit",
            minute: "2-digit",
            hour12: true,
          });
        }
        return new Date(timeString).toLocaleTimeString([], {
          hour: "2-digit",
          minute: "2-digit",
          hour12: true,
        });
      } catch (e) {
        return timeString;
      }
    },
    formatStatus(status) {
      if (!status) return "Unknown";
      return status
        .replace("_", " ")
        .toLowerCase()
        .replace(/\b\w/g, (l) => l.toUpperCase());
    },
    getStatusClass(status) {
      if (!status) return "status-unknown";
      return `status-${status.toLowerCase().replace("_", "-")}`;
    },
    truncateText(text, length) {
      if (!text) return "";
      if (text.length <= length) {
        return text;
      }
      return text.substring(0, length) + "...";
    },
    navigateToUpdate(eventId) {
      this.$router.push({ name: "UpdateEvent", params: { id: eventId } });
    },
    async deleteEvent(id) {
      if (confirm("Are you sure you want to delete this event?")) {
        this.loading = true;
        this.actionId = id;
        try {
          await eventService.deleteEvent(id);
          location.reload();
        } catch (error) {
          console.error("Error deleting event:", error);
          alert("Failed to delete event");
        } finally {
          this.loading = false;
          this.actionId = null;
        }
      }
    },
  },
};
</script>

<style scoped>
.company-events-container {
  padding: 20px;
  max-width: 1200px;
  margin: 20px auto;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.events-title {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
  font-size: 2rem;
  font-weight: 600;
}

.loading-indicator,
.no-events-message {
  text-align: center;
  padding: 40px;
  color: #555;
}
.loading-indicator .spinner {
  width: 40px;
  height: 40px;
  border: 4px solid rgba(0, 0, 0, 0.1);
  border-left-color: #007bff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 15px auto;
}
@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
.no-events-message i {
  font-size: 3rem;
  color: #007bff;
  margin-bottom: 15px;
}

.events-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.event-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  transition: transform 0.2s ease-in-out;
}
.event-card:hover {
  transform: translateY(-5px);
}

.event-details {
  padding: 15px;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
}

.event-name {
  font-size: 1.4rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.event-description {
  font-size: 0.9rem;
  color: #666;
  line-height: 1.5;
  margin-bottom: 12px;
  flex-grow: 1;
}

.event-info {
  display: flex;
  justify-content: space-between;
  font-size: 0.85rem;
  color: #555;
  margin-bottom: 8px;
}
.event-info span {
  display: flex;
  align-items: center;
}
.event-info i {
  margin-right: 6px;
  color: #007bff;
}

.event-status {
  padding: 3px 8px;
  border-radius: 4px;
  font-weight: 500;
  font-size: 0.8rem;
  text-transform: capitalize;
}
.status-en-attente {
  background-color: #ffc107;
  color: #333;
}
.status-accepte {
  background-color: #28a745;
  color: white;
}
.status-rejete {
  background-color: #dc3545;
  color: white;
}
.status-termine {
  background-color: #17a2b8;
  color: white;
}
.status-annule {
  background-color: #6c757d;
  color: white;
}
.status-unknown {
  background-color: #6c757d;
  color: white;
}

.event-actions {
  margin-top: auto;
  padding-top: 10px;
  border-top: 1px solid #eee;
  display: flex;
  gap: 10px;
}
.btn-sm {
  font-size: 0.8rem;
  padding: 0.25rem 0.6rem;
}

.error-message {
  color: #dc3545;
  background-color: #f8d7da;
  border: 1px solid #f5c6cb;
  padding: 15px;
  border-radius: 5px;
  text-align: center;
}
.error-message i {
  margin-right: 8px;
}
</style>
