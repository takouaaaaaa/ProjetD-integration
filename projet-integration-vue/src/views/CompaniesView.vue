<template>
  <div class="companies-view-layout">
    <CompanyNavbar />
    <div class="main-page-content">
      <!-- You can add a title or other introductory content here if needed -->
      <!-- <h1>My Company Dashboard</h1> -->
      <CompanyEvents />
    </div>
  </div>
</template>

<script>
import CompanyNavbar from '@/components/CompanyNavbar.vue';
import CompanyEvents from '@/components/CompanyEvents.vue'; // Import the new component

export default {
  name: 'CompaniesView', // Or whatever you named this view
  components: {
    CompanyNavbar,
    CompanyEvents, // Register the component
  },
  data() {
    return {
      // companies: [] // This was from your original code, might not be needed here if this view is just for the logged-in company's events.
                    // If you still need a list of 'all' companies here for some other purpose, keep it.
    };
  },
  // You might not need created() or methods to fetch 'all' companies
  // if this page is specifically for the logged-in company's details/events.
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