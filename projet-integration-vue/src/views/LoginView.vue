<template>
  <div class="admin-login-page">
    <div class="background-overlay"></div>
    <div class="logo-container">
      <!-- <img src="@/assets/your-logo.png" alt="Logo" class="logo" /> -->
      <!-- Add your logo here if you have one -->
    </div>
    <div class="login-container-wrapper">
      <div class="info-panel">
        <h1 class="welcome-title">
          {{ showLoginForm ? 'Welcome Back!' : 'Join Us!' }}
        </h1>
        <p class="welcome-subtitle">
          {{ showLoginForm ? 'Manage your events with ease.' : 'Create an account to get started.' }}
        </p>
        <div class="decorative-elements">
          <span></span><span></span><span></span>
        </div>
      </div>
      <div class="form-panel">
        <LoginPage v-if="showLoginForm" @switchToRegister="toggleForm" />
        <RegisterCompany v-else @switchToLogin="toggleForm" @registrationSuccess="handleRegistrationSuccess" />
      </div>
    </div>
  </div>
</template>

<script>
import LoginPage from '@/components/LoginPage.vue';
import RegisterCompany from '@/components/RegisterCompany.vue'; // Import the RegisterCompany component
export default {
  name: 'AdminLogin', // Or LoginView
  components: {
    LoginPage,
    RegisterCompany, // Register the component
  },
  data() {
    return {
      showLoginForm: true, // Initially show the login form
    };
  },
  methods: {
    toggleForm() {
      this.showLoginForm = !this.showLoginForm;
    },
    handleRegistrationSuccess(registeredCompany) {
      console.log('Registration successful in parent view:', registeredCompany);
      // Optionally, after a successful registration, you might want to switch to login:
      // setTimeout(() => {
      //   this.showLoginForm = true;
      // }, 3000); // Switch back to login after 3 seconds
    }
  }
};
</script>

<style scoped>
/* Your existing styles for AdminLogin.vue are fine. */
/* No changes needed here if they already style the structure well. */

.admin-login-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  /* background-image: url('YOUR_BACKGROUND_IMAGE_URL'); */ /* Add your background image */
  background-size: cover;
  background-position: center;
  padding: 20px;
  position: relative;
  overflow: hidden;
}

.background-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1;
}

.logo-container {
  position: absolute;
  top: 30px;
  left: 40px;
  z-index: 3;
}

.logo {
  max-width: 150px;
  height: auto;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
}

.login-container-wrapper {
  display: flex;
  width: 100%;
  max-width: 1100px;
  background-color: #ffffff;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  z-index: 2;
  min-height: 600px;
}

.info-panel {
  flex: 1;
  padding: 60px 40px;
  background-color: #4a5568;
  color: #ffffff;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  position: relative;
}

.welcome-title {
  font-size: 3.5rem;
  font-weight: 700;
  line-height: 1.1;
  margin-bottom: 15px;
  color: #e2e8f0;
}

.welcome-subtitle {
  font-size: 1.5rem;
  line-height: 1.6;
  color: #a0aec0;
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
.decorative-elements span:nth-child(1) { background-color: #63b3ed; }
.decorative-elements span:nth-child(2) { background-color: #48bb78; }
.decorative-elements span:nth-child(3) { background-color: #f56565; }

.form-panel {
  flex: 1;
  padding: 40px;
  display: flex;
  flex-direction: column; /* Allow for a message above/below form if needed */
  align-items: center;
  justify-content: center;
  background-color: #f7fafc;
}

@media (max-width: 992px) {
  .login-container-wrapper {
    flex-direction: column;
    max-width: 500px;
    min-height: auto;
  }
  .info-panel {
    padding: 40px 30px;
    align-items: center;
    text-align: center;
  }
  .welcome-title { font-size: 2.5rem; }
  .welcome-subtitle { font-size: 1.2rem; }
  .decorative-elements {
    position: static;
    margin-top: 20px;
    justify-content: center;
  }
  .form-panel { padding: 30px; }
  .logo-container {
    top: 20px;
    left: 50%;
    transform: translateX(-50%);
  }
}
@media (max-width: 768px) {
  .logo { max-width: 120px; }
}
</style>