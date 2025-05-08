<template>
    <nav class="navbar">
      <div class="container">
        <div class="brand-section">
          <svg class="logo-icon" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z" stroke="currentColor" stroke-width="2"/>
            <path d="M12 16V16.01M12 8V12" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
          <span class="brand-name">Admin Dashboard</span>
          <div class="user-section">
        <button class="action-btn" @click="$emit('show-component', 'events')">Events</button>
        <button class="action-btn" @click="$emit('show-component', 'companies')">Companies</button>
      </div>
        </div>
  
        <div class="user-section">
          <div class="user-avatar">
            {{ adminUser.name ? adminUser.name.charAt(0).toUpperCase() : 'A' }}
          </div>
          <div class="user-info">
            <span class="user-name">{{ adminUser.name || 'Admin User' }}</span>
          </div>
          <button class="logout-btn" @click="handleLogout" aria-label="Sign out">
            <svg class="logout-icon" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M15 3H19C20.1046 3 21 3.89543 21 5V19C21 20.1046 20.1046 21 19 21H15M10 17L15 12M15 12L10 7M15 12H3" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </button>
        </div>
      </div>
    </nav>
  </template>
  
  <script>
  export default {
    name: 'AdminNavbar',
    data() {
      return {
        adminUser: {
          name: '',
        },
      };
    },
    methods: {
      handleLogout() {
        localStorage.removeItem('adminAuthToken');
        this.$router.push('/admin/login');
      },
      decodeJwt(token) {
        try {
          const base64Url = token.split('.')[1];
          const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
          const jsonPayload = decodeURIComponent(
            atob(base64)
              .split('')
              .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
              .join('')
          );
          return JSON.parse(jsonPayload);
        } catch {
          return null;
        }
      },
      loadAdminUserData() {
        const token = localStorage.getItem('adminAuthToken');
        if (token) {
          const decoded = this.decodeJwt(token);
          if (decoded) {
            this.adminUser.name = decoded.name || decoded.username || decoded.sub || 'Admin';
          }
        }
      }
    },
    created() {
      this.loadAdminUserData();
    }
  };
  </script>
  
  <style scoped>
  /* Same styles as CompanyNavbar */
  .navbar {
    background-color: #0f172a;
    padding: 0 2rem;
    border-bottom: 1px solid #1e293b;
    font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
    position: relative;
    z-index: 50;
    height: 64px;
    display: flex;
    align-items: center;
  }
  
  .container {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 0 auto;
  }
  
  .brand-section {
    display: flex;
    align-items: center;
    gap: 12px;
  }
  
  .logo-icon {
    width: 24px;
    height: 24px;
    color: #60a5fa;
  }
  
  .brand-name {
    color: #f8fafc;
    font-weight: 600;
    font-size: 1.1rem;
    letter-spacing: 0.5px;
  }
  
  .user-section {
    display: flex;
    align-items: center;
    gap: 16px;
  }
  
  .user-avatar {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    background-color: #1e40af;
    color: #ffffff;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 600;
    font-size: 0.9rem;
  }
  
  .user-info {
    display: flex;
    flex-direction: column;
  }
  
  .user-name {
    color: #f8fafc;
    font-weight: 500;
    font-size: 0.9rem;
    line-height: 1.25;
  }
  
  .logout-btn {
    width: 36px;
    height: 36px;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: transparent;
    color: #94a3b8;
    border: none;
    cursor: pointer;
    transition: all 0.2s ease;
  }
  
  .logout-btn:hover {
    background-color: #1e293b;
    color: #f8fafc;
  }
  
  .logout-btn:active {
    background-color: #334155;
  }
  
  .logout-btn:focus {
    outline: none;
    box-shadow: 0 0 0 3px rgba(96, 165, 250, 0.3);
  }
  
  .logout-icon {
    width: 20px;
    height: 20px;
  }
  .action-btn {
  background-color: #2563eb;
  color: white;
  border: none;
  padding: 0.4rem 0.8rem;
  border-radius: 6px;
  font-size: 0.8rem;
  cursor: pointer;
}
.action-btn:hover {
  background-color: #1d4ed8;
}
  @media (max-width: 768px) {
    .navbar {
      padding: 0 1rem;
    }
  
    .brand-name {
      display: none;
    }
  
    .user-section {
      gap: 12px;
    }
  }
  
  @media (max-width: 480px) {
    .user-name {
      display: none;
    }
  }
  </style>
  