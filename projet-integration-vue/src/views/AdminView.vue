<template>
  <div class="event-view">
    <EventNavBar @show-component="currentView = $event" />
    
    <div v-if="currentView === 'events'">
      <EventsList :events="events" />
    </div>
    <div v-else-if="currentView === 'companies'">
<companiesList
  :companies="companies"
  :key="companiesListKey"
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
import CompaniesList from "@/components/companiesList.vue";
import EventNavBar from "@/components/EventNavBar.vue";
import eventService from "@/services/eventService";
import companyService from "@/services/companyService";

export default {
  components: { EventsList, CompaniesList, EventNavBar },
  data() {
    return {
      companies: [],
      events: [],
      currentView: 'events',
    };
  },
  computed: { 
    companiesListKey() {
      if (!this.companies || this.companies.length === 0) {
        console.log("PARENT KEY GEN: Companies array is empty or undefined");
        return 'no-companies';
      }
      const keyParts = this.companies.map(c => {
        console.log(`PARENT KEY GEN: Company ID: ${c.id}, Name: ${c.name}, Confirmed: ${c.confirmed} (Type: ${typeof c.confirmed})`);
        return `${c.id}-${String(c.confirmed)}-${c.name}`; 
      });
      const key = keyParts.join('|');
      console.log("PARENT KEY GEN: Final key:", key);
      return key;
    }
  },
  methods: { 
     async fetchCompanies() {
    
    try {
      const result = await companyService.fetchCompanies();
    
      const targetCompanyInResult = result.find(c => c.id === 1);
      if (targetCompanyInResult) {
        // console.log(`PARENT FETCH: Tech Corp (ID 1) in *result* - Is_Confirmed: ${targetCompanyInResult.is_confirmed} (Type: ${typeof targetCompanyInResult.is_confirmed})`); // CHANGE HERE
      } else {
        // console.log("PARENT FETCH: Tech Corp (ID 1) not found in *result*.");
      }

      this.companies = [...result];

      const targetCompanyInThisDotCompanies = this.companies.find(c => c.id === 1);
      if (targetCompanyInThisDotCompanies) {
        // console.log(`PARENT FETCH: Tech Corp (ID 1) in *this.companies* - Is_Confirmed: ${targetCompanyInThisDotCompanies.is_confirmed} (Type: ${typeof targetCompanyInThisDotCompanies.is_confirmed})`); // CHANGE HERE
      } else {
        // console.log("PARENT FETCH: Tech Corp (ID 1) not found in *this.companies* after assignment.");
      }
    } catch (err) {
      console.error("Error loading companies:", err);
      alert("Impossible de charger les sociétés.");
    }
    
  },
    async fetchEvents() { 
      try {
        this.events = await eventService.fetchEvents();
        console.log("PARENT FETCH: Events fetched", this.events); 
      } catch (err) {
        console.error("Error loading events:", err);
        alert("Impossible de charger les événements.");
      }
    } 
  }, 
  async created() { 
    console.log("PARENT CREATED: Fetching initial data");
    try {
      await Promise.all([this.fetchCompanies(), this.fetchEvents()]);
    } catch (error) {
      console.error("PARENT CREATED: Error during initial data fetch", error);
    }
  } 
};
</script>
<style scoped>

</style>
