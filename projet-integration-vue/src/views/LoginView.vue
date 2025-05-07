<template>
  <div class="admin-login-page">
    <div class="background-overlay"></div>
    <div class="logo-container">
    </div>
    <div class="login-container-wrapper">
      <div class="info-panel">
        <h1 class="welcome-title">Welcome Back!</h1>
        <p class="welcome-subtitle">Manage your events with ease.</p>
        <div class="decorative-elements">
          <span></span><span></span><span></span>
        </div>
      </div>
      <div class="form-panel">
        <LoginPage />
      </div>
    </div>
  </div>
</template>

<script>
import LoginPage from '@/components/LoginPage.vue';

export default {
  name: 'AdminLogin',
  components: {
    LoginPage,
  },
};
</script>

<style scoped>
.admin-login-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-size: cover;
  background-position: center;
  padding: 20px;
  position: relative;
  overflow: hidden; /* Prevents scrollbars from decorative elements if they extend */
}

.background-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* Darker overlay for better text contrast */
  z-index: 1;
}

.logo-container {
  position: absolute;
  top: 30px;
  left: 40px;
  z-index: 3; /* Above overlay and content */
}

.logo {
  max-width: 150px;
  height: auto;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
}

.login-container-wrapper {
  display: flex;
  width: 100%;
  max-width: 1100px; /* Max width for the content card */
  background-color: #ffffff;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  overflow: hidden; /* Important for child panel styling */
  z-index: 2; /* Above overlay */
  min-height: 600px; /* Give it some fixed height */
}

.info-panel {
  flex: 1;
  padding: 60px 40px;
  background-color: #4a5568; /* Darker slate gray */
  color: #ffffff;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  position: relative;
}

.welcome-title {
  font-size: 3.5rem; /* Adjusted for balance */
  font-weight: 700;
  line-height: 1.1;
  margin-bottom: 15px;
  color: #e2e8f0; /* Lighter text color */
}

.welcome-subtitle {
  font-size: 1.5rem;
  line-height: 1.6;
  color: #a0aec0; /* Muted text color */
  max-width: 380px;
}

.decorative-elements {
  position: absolute;
  bottom: 30px;
  left: 40px;
  display: flex;
  gap: 10px;
}

.decorative-elements span {
  display: block;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  opacity: 0.3;
}
.decorative-elements span:nth-child(1) { background-color: #63b3ed; } /* Blue */
.decorative-elements span:nth-child(2) { background-color: #48bb78; } /* Green */
.decorative-elements span:nth-child(3) { background-color: #f56565; } /* Red */


.form-panel {
  flex: 1;
  padding: 40px; /* Padding to contain the LoginPage component */
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f7fafc; /* Light gray background for the form side */
}

/* Responsive adjustments */
@media (max-width: 992px) {
  .login-container-wrapper {
    flex-direction: column;
    max-width: 500px; /* Adjust for stacked layout */
    min-height: auto;
  }

  .info-panel {
    padding: 40px 30px;
    align-items: center;
    text-align: center;
  }

  .welcome-title {
    font-size: 2.5rem;
  }

  .welcome-subtitle {
    font-size: 1.2rem;
  }

  .decorative-elements {
    position: static; /* Reset position */
    margin-top: 20px;
    justify-content: center;
  }

  .form-panel {
    padding: 30px;
  }

  .logo-container {
    top: 20px;
    left: 50%;
    transform: translateX(-50%);
  }
}

@media (max-width: 768px) {
  .logo {
    max-width: 120px;
  }
}
</style>