<template>
   <CompanyNavbar />
  <companiesList 
    :companies="companies" 
    @refresh-companies="fetchCompanies"
  />
</template>

<script>
import companiesList from '@/components/companiesList.vue';
import companyService from '@/services/companyService';
import CompanyNavbar from '@/components/CompanyNavbar.vue';
export default {
  components: {
     companiesList,
    CompanyNavbar,
   },
  data() {
    return {
      companies: []
    }
  },
  async created() {
    await this.fetchCompanies();
  },
  methods: {
    async fetchCompanies() {
      try {
        this.companies = await companyService.fetchCompanies();
      } catch (error) {
        console.error('Error fetching companies:', error);
        alert('Failed to load companies');
      }
    }
  }
}
</script>