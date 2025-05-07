<template>
    <nav class="navbar company-navbar">
      <div class="container-fluid d-flex justify-content-between align-items-center">
        <span class="navbar-text">
          Hello, {{ currentUser.name || 'Company' }}
        </span>
        <button class="btn btn-outline-danger btn-sm" @click="handleLogout">
          Logout
        </button>
      </div>
    </nav>
  </template>
  
  <script>
  export default {
    name: 'CompanyNavbar',
    data() {
      return {
        currentUser: {
          name: '',
        },
      };
    },
    methods: {
      handleLogout() {
        localStorage.removeItem('authToken');
        this.$router.push('/');
      },
      decodeJwt(token) {
        try {
          const base64Url = token.split('.')[1];
          const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
          const jsonPayload = decodeURIComponent(
            atob(base64)
              .split('')
              .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
              .join('')
          );
          return JSON.parse(jsonPayload);
        } catch {
          return null;
        }
      },
      loadCurrentUserData() {
        const token = localStorage.getItem('authToken');
        if (token) {
          const decoded = this.decodeJwt(token);
          if (decoded) {
            this.currentUser.name = decoded.username || decoded.name || decoded.sub || 'Company';
          }
        }
      }
    },
    created() {
      this.loadCurrentUserData();
    }
  };
  </script>
  
  <style scoped>
  .company-navbar {
    background-color: #e0999978;
    padding: 0.5rem 1rem;
  }
  .navbar-text {
    font-weight: 500;
    font-size: 1rem;
  }
  </style>
  