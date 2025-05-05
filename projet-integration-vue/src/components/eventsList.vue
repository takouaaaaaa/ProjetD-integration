<template>
  <div class="event-container">
    <table v-if="events.length > 0" class="event-table">
      <thead>
        <tr>
          <th>Name</th>
          <th>Responsable</th>
          <th>Category</th>
          <th>Email</th>
          <th>Phone</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="event in events" :key="event.id">
          <td>{{ event.name }}</td>
          <td>{{ event.responsable }}</td>
          <td>{{ event.category }}</td>
          <td>{{ event.email }}</td>
          <td>{{ event.numTel }}</td>
          <td :class="event.confirmed ? 'confirmed' : 'pending'">
            {{ event.confirmed ? "Confirmed" : "Pending" }}
          </td>
          <td class="actions">
            <button
              v-if="!event.confirmed"
              :disabled="loading"
              class="confirm-btn"
              @click="confirmCompany(event.id)"
            >
              <span v-if="loading && actionId === event.id">Processing...</span>
              <span v-else>Confirm</span>
            </button>
            <button
              v-else
              :disabled="loading"
              class="unconfirm-btn"
              @click="unconfirmCompany(event.id)"
            >
              <span v-if="loading && actionId === event.id">Processing...</span>
              <span v-else>Unconfirm</span>
            </button>
            <button
              :disabled="loading"
              class="delete-btn"
              @click="deleteCompany(event.id)"
            >
              <span v-if="loading && actionId === event.id">Processing...</span>
              <span v-else>Delete</span>
            </button>
          </td>
        </tr>
      </tbody>
    </table>
    <div v-else class="no-events">
      <p>No events to show.</p>
    </div>
  </div>
</template>

<script>
import companyService from "@/services/companyService";

export default {
  name: "companiesList",
  props: {
    events: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      loading: false,
      actionId: null,
    };
  },
  methods: {
    async refreshCompanies() {
      // Emit event to parent to refresh the events list
      this.$emit("refresh-events");
    },
    async confirmCompany(id) {
      this.loading = true;
      this.actionId = id;
      try {
        await companyService.confirmCompany(id);
        await this.refreshCompanies();
      } catch (error) {
        console.error("Error confirming event:", error);
        alert("Failed to confirm event");
      } finally {
        this.loading = false;
        this.actionId = null;
      }
    },
    async unconfirmCompany(id) {
      this.loading = true;
      this.actionId = id;
      try {
        await companyService.unconfirmCompany(id);
        await this.refreshCompanies();
      } catch (error) {
        console.error("Error unconfirming event:", error);
        alert("Failed to unconfirm event");
      } finally {
        this.loading = false;
        this.actionId = null;
      }
    },
    async deleteCompany(id) {
      if (confirm("Are you sure you want to delete this event?")) {
        this.loading = true;
        this.actionId = id;
        try {
          await companyService.deleteCompany(id);
          await this.refreshCompanies();
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
.event-container {
  padding: 20px;
  width: 100%;
  overflow-x: auto;
}

.event-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.event-table th,
.event-table td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.event-table th {
  background-color: #f2f2f2;
  font-weight: bold;
}

.event-table tr:hover {
  background-color: #f5f5f5;
}

.confirmed {
  color: #4caf50;
  font-weight: bold;
}

.pending {
  color: #ff9800;
  font-weight: bold;
}

.actions {
  display: flex;
  gap: 8px;
}

button {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.confirm-btn {
  background-color: #4caf50;
  color: white;
}

.confirm-btn:hover {
  background-color: #45a049;
}

.unconfirm-btn {
  background-color: #ff9800;
  color: white;
}

.unconfirm-btn:hover {
  background-color: #e68a00;
}

.delete-btn {
  background-color: #f44336;
  color: white;
}

.delete-btn:hover {
  background-color: #d32f2f;
}

.no-events {
  text-align: center;
  padding: 20px;
  color: #666;
}
</style>
