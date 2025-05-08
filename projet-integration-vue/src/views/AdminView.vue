<template>
  <div class="event-view">
    <EventNavBar @show-component="currentView = $event" />
    
    <div v-if="currentView === 'events'">
      <EventsList :events="events" />
    </div>
    <div v-else-if="currentView === 'companies'">
      <companiesList 
    :companies="companies" 
    @refresh-companies="fetchCompanies"
  />
    </div>
    <div v-else>
      <p style="padding: 1rem;">Sélectionnez une vue.</p>
    </div>
  </div>
</template>

<script>
import EventsList from "@/components/eventList.vue";
import CompaniesList from "@/components/companiesList.vue"; // make sure this exists
import EventNavBar from "@/components/EventNavBar.vue";
import eventService from "@/services/eventService";
import companyService from "@/services/companyService";

export default {
  components: { EventsList, CompaniesList, EventNavBar },
  data() {
    return {
      companies: [],
      events: [],
      currentView: 'events' // default to event list
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
/* Add padding or styling if needed */
</style>
