<template>
  <div class="companies-view-layout">
    <CompanyNavbar />
    <div class="main-page-content">
      <AddEvent :companies="companies" @event-added="fetchEvents" />

      <CompanyEvents />
    </div>
  </div>
</template>
<script>
import AddEvent from "@/components/addEvent.vue";
import CompanyNavbar from "@/components/CompanyNavbar.vue";
import CompanyEvents from "@/components/CompanyEvents.vue";

import eventService from "@/services/eventService";
import companyService from "@/services/companyService";

export default {
  name: "CompaniesView",
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
       
      }
    },
    async fetchEvents() {
      try {
        this.events = await eventService.fetchEvents();
      } catch (err) {
        console.error("Error loading events:", err);
        
      }
    },
  },
};
</script>

<style scoped>
.companies-view-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #eef1f5;
}

.main-page-content {
  flex-grow: 1;
  padding: 20px;
}
</style>
