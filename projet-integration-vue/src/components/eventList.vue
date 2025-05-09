<template>
  <div class="events-container">
    <div v-if="events.length" class="table-responsive">
      <table class="events-table">
        <thead>
          <tr>
            <th class="image-col">Image</th>
            <th class="name-col">Nom</th>
            <th class="desc-col">Description</th>
            <th class="date-col">Date</th>
            <th class="time-col">Heure</th>
            <th class="location-col">Localisation</th>
            <th class="host-col">Animateur</th>
            <th class="status-col">État</th>
            <th class="company-col">Société</th>
            <th class="actions-col">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="evt in events" :key="evt.id">
            <td>
              <div class="event-image-container">
                <img
                  v-if="evt.image"
                  :alt="`Image for ${evt.nom}`"
                  :src="getImageUrl(evt.image)"
                  class="event-thumbnail"
                />
                <img
                  v-else
                  :src="getImageUrl(null)"
                  alt="No Image"
                  class="event-thumbnail"
                />
              </div>
            </td>
            <td class="event-name">{{ evt.nom }}</td>
            <td class="event-desc">
              {{ truncateDescription(evt.description) }}
            </td>
            <td class="event-date">{{ formatDate(evt.date) }}</td>
            <td class="event-time">
              {{ evt.time ? formatTime(evt.time) : "—" }}
            </td>
            <td class="event-location">{{ evt.localisation || "—" }}</td>
            <td class="event-host">{{ evt.animateur || "—" }}</td>
            <td>
              <span :class="['event-status-badge', getStatusClass(evt.etat)]">
                {{ formatStatus(evt.etat) }}
              </span>
            </td>
            <td class="event-company">{{ evt.company?.name || "—" }}</td>
            <td class="actions-cell">
              <template v-if="isActionable(evt.etat)">
                <button
                  class="action-btn accept-btn"
                  @click="handleAccept(evt.id)"
                >
                  <svg class="icon" height="16" viewBox="0 0 24 24" width="16">
                    <path
                      d="M5 12l5 5L20 7"
                      stroke="currentColor"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                    />
                  </svg>
                  <span>Accepter</span>
                </button>
                <button
                  class="action-btn reject-btn"
                  @click="handleReject(evt.id)"
                >
                  <svg class="icon" height="16" viewBox="0 0 24 24" width="16">
                    <path
                      d="M18 6L6 18M6 6l12 12"
                      stroke="currentColor"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                    />
                  </svg>
                  <span>Rejeter</span>
                </button>
              </template>
              <span v-else class="no-actions">—</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-else class="no-events">
      <svg class="empty-icon" height="48" viewBox="0 0 24 24" width="48">
        <path
          d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0118 0z"
          stroke="currentColor"
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="2"
        />
        <path
          d="M12 13a3 3 0 100-6 3 3 0 000 6z"
          stroke="currentColor"
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="2"
        />
      </svg>
      <h3>Aucun événement à afficher</h3>
      <p>Lorsque des événements seront disponibles, ils apparaîtront ici.</p>
    </div>
  </div>
</template>

<script>
import eventService from "@/services/eventService";

export default {
  name: "EventsList",
  props: {
    events: {
      type: Array,
      required: true,
    },
  },
  methods: {
    getImageUrl(filename) {
      if (!filename) {
        try {
          return require("@/assets/No_Image_Available.jpg");
        } catch (e) {
          console.warn("Default fallback image not found.", e);
          return "";
        }
      }
      try {
        return require(`@/assets/eventImages/${filename}`);
      } catch (e) {
        console.warn(`Event image "${filename}" not found, using fallback.`, e);
        try {
          return require("@/assets/No_Image_Available.jpg");
        } catch (fe) {
          console.warn("Fallback image also not found.", fe);
          return "";
        }
      }
    },
    truncateDescription(desc) {
      if (!desc) return "—";
      return desc.length > 50 ? `${desc.substring(0, 50)}...` : desc;
    },
    formatDate(dateString) {
      if (!dateString) return "—";
      const options = { year: "numeric", month: "short", day: "numeric" };
      return new Date(dateString).toLocaleDateString(undefined, options);
    },
    formatTime(timeString) {
      if (!timeString) return "—";
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
          hour12: false,
        });
      }
      try {
        return new Date(timeString).toLocaleTimeString([], {
          hour: "2-digit",
          minute: "2-digit",
          hour12: false,
        });
      } catch (e) {
        return timeString.slice(0, 5);
      }
    },
    formatStatus(status) {
      if (!status) return "Inconnu";
      return status
        .replace("_", " ")
        .toLowerCase()
        .replace(/\b\w/g, (l) => l.toUpperCase());
    },
    getStatusClass(status) {
      if (!status) return "status-unknown";
      return `status-${status.toLowerCase().replace("_", "-")}`;
    },
    isActionable(status) {
      return status === "EN_ATTENTE";
    },
    async handleAccept(eventId) {
      if (!confirm("Êtes-vous sûr de vouloir accepter cet événement ?")) return;
      try {
        await eventService.acceptEvent(eventId);
        this.$emit("eventUpdated");
        this.showToast("Événement accepté avec succès", "success");
        location.reload();
      } catch (error) {
        console.error("Erreur lors de l'acceptation de l'événement:", error);
        this.showToast(
          "Erreur lors de l'acceptation: " +
            (error.response?.data?.message || error.message),
          "error"
        );
      }
    },
    async handleReject(eventId) {
      if (!confirm("Êtes-vous sûr de vouloir rejeter cet événement ?")) return;
      try {
        await eventService.rejectEvent(eventId);
        this.$emit("eventUpdated");
        this.showToast("Événement rejeté avec succès", "success");
        location.reload();
      } catch (error) {
        console.error("Erreur lors du rejet de l'événement:", error);
        this.showToast(
          "Erreur lors du rejet: " +
            (error.response?.data?.message || error.message),
          "error"
        );
      }
    },
    showToast(message, type) {
      alert(`${type === "success" ? "✓" : "✗"} ${message}`);
    },
  },
};
</script>

<style scoped>
.events-container {
  width: 100%;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.table-responsive {
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
}

.events-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
  font-size: 0.9rem;
}

.events-table th {
  position: sticky;
  top: 0;
  background-color: #f8fafc;
  color: #64748b;
  font-weight: 600;
  text-transform: uppercase;
  font-size: 0.75rem;
  letter-spacing: 0.5px;
  padding: 12px 16px;
  border-bottom: 1px solid #e2e8f0;
  z-index: 10;
}

.events-table td {
  padding: 16px;
  border-bottom: 1px solid #f1f5f9;
  vertical-align: middle;
}

.events-table tr:last-child td {
  border-bottom: none;
}

.events-table tr:hover td {
  background-color: #f8fafc;
}

.event-image-container {
  width: 80px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f8fafc;
  border-radius: 6px;
  overflow: hidden;
}

.event-thumbnail {
  max-width: 100%;
  max-height: 100%;
  object-fit: cover;
}

.event-name {
  font-weight: 600;
  color: #1e293b;
}

.event-desc {
  color: #64748b;
  max-width: 200px;
}

.event-date,
.event-time {
  white-space: nowrap;
  color: #334155;
}

.event-location,
.event-host,
.event-company {
  color: #475569;
}

.event-status-badge {
  display: inline-block;
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 600;
  letter-spacing: 0.3px;
  text-align: center;
  min-width: 80px;
}

.status-en-attente {
  background-color: #ffedd5;
  color: #9a3412;
}

.status-accepte {
  background-color: #dcfce7;
  color: #166534;
}

.status-rejete {
  background-color: #fee2e2;
  color: #991b1b;
}

.status-termine {
  background-color: #e0f2fe;
  color: #075985;
}

.status-annule {
  background-color: #f3f4f6;
  color: #4b5563;
}

.status-unknown {
  background-color: #f3f4f6;
  color: #4b5563;
}

.actions-cell {
  white-space: nowrap;
}

.action-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 0.8rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  border: 1px solid transparent;
  margin-right: 8px;
}

.action-btn:last-child {
  margin-right: 0;
}

.action-btn .icon {
  flex-shrink: 0;
}

.accept-btn {
  background-color: #dcfce7;
  color: #166534;
  border-color: #bbf7d0;
}

.accept-btn:hover {
  background-color: #bbf7d0;
}

.reject-btn {
  background-color: #fee2e2;
  color: #991b1b;
  border-color: #fecaca;
}

.reject-btn:hover {
  background-color: #fecaca;
}

.no-actions {
  color: #94a3b8;
  font-style: italic;
}

.no-events {
  padding: 48px 24px;
  text-align: center;
  color: #64748b;
}

.empty-icon {
  color: #cbd5e1;
  margin-bottom: 16px;
}

.no-events h3 {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 8px;
  color: #334155;
}

.no-events p {
  font-size: 0.9rem;
  margin: 0;
}
@media (max-width: 1200px) {
  .events-table {
    font-size: 0.85rem;
  }

  .events-table th,
  .events-table td {
    padding: 12px;
  }

  .event-image-container {
    width: 60px;
    height: 45px;
  }
}

@media (max-width: 768px) {
  .desc-col,
  .company-col {
    display: none;
  }

  .action-btn span {
    display: none;
  }

  .action-btn {
    padding: 6px;
  }
}

@media (max-width: 576px) {
  .host-col,
  .location-col {
    display: none;
  }

  .event-status-badge {
    min-width: auto;
    padding: 4px 8px;
  }
}
</style>
