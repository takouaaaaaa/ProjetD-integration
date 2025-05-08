<template>
  <div class="event-view">
    <h1>Gestion des événements</h1>

    <AddEvent :companies="companies" @event-added="fetchEvents" />

    <EventsList :events="events" />
  </div>
</template>

<script>
import AddEvent from "@/components/addEvent.vue";
import EventsList from "@/components/eventList.vue";
import eventService from "@/services/eventService";
import companyService from "@/services/companyService";

export default {
  components: { AddEvent, EventsList },
  data() {
    return {
      companies: [],
      events: [],
    };
  },
  async created() {
    await Promise.all([this.fetchCompanies(), this.fetchEvents()]);
  },
  methods: {
    async fetchCompanies() {
      try {
        this.companies = await companyService.fetchCompanies();
      } catch (err) {
        console.error("Error loading companies:", err);
        alert("Impossible de charger les sociétés.");
      }
    },
    async fetchEvents() {
      try {
        this.events = await eventService.fetchEvents();
      } catch (err) {
        console.error("Error loading events:", err);
        alert("Impossible de charger les événements.");
      }
    },
  },
};
</script>

<style scoped>
.event-view {
  padding: 2rem;
}
</style>
