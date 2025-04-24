<template>
  <companiesList 
    :companies="companies" 
    @refresh-companies="fetchCompanies"
  />
</template>

<script>
import companiesList from '@/components/companiesList.vue';
import companyService from '@/services/companyService';

export default {
  components: { companiesList },
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