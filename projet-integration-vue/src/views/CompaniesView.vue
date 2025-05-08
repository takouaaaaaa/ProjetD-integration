<template>
  <div class="companies-view-layout">
    <CompanyNavbar />
    <div class="main-page-content">
      
    <AddEvent :companies="companies" @event-added="fetchEvents" />

      <!-- You can add a title or other introductory content here if needed -->
      <!-- <h1>My Company Dashboard</h1> -->
      <CompanyEvents />
    </div>
  </div>
</template>
<script>
import AddEvent from "@/components/addEvent.vue";
import CompanyNavbar from '@/components/CompanyNavbar.vue';
import CompanyEvents from '@/components/CompanyEvents.vue';

import eventService from "@/services/eventService";
import companyService from "@/services/companyService";

export default {
  name: 'CompaniesView',
  components: {
    CompanyNavbar,
    CompanyEvents,
    AddEvent,
  },
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
  }
};
</script>

<style scoped>
.companies-view-layout {
  display: flex;
  flex-direction: column; /* Navbar on top, content below */
  min-height: 100vh;
  background-color: #eef1f5; /* A light background for the page */
}

.main-page-content {
  flex-grow: 1;
  padding: 20px; /* Add some padding around the content area */
  /* max-width: 1200px; /* Optional: constrain width for very wide screens */
  /* margin: 0 auto; /* Center content if max-width is set */
}

/* Example: Add a title style if you uncomment the H1 */
/*
.main-page-content h1 {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}
*/
</style>