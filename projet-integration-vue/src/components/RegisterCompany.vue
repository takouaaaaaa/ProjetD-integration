<template>
  <div class="register-company-container">
    <div class="register-card">
      <h2 class="register-title">Register Your Company</h2>
      <p class="register-subtitle">Join our platform to manage your events.</p>
      <hr class="title-divider">
      <form @submit.prevent="handleRegister" class="register-form">
        
        <div class="form-row">
          <div class="form-group half-width">
            <label for="companyName" class="form-label">
              <i class="bi bi-building form-icon"></i> Company Name
            </label>
            <input type="text" id="companyName" v-model.trim="company.name" class="form-input" placeholder="Enter company name" required />
          </div>
          <div class="form-group half-width">
            <label for="responsable" class="form-label">
              <i class="bi bi-person-badge form-icon"></i> Responsible Person
            </label>
            <input type="text" id="responsable" v-model.trim="company.responsable" class="form-input" placeholder="Name of responsible person" required />
          </div>
        </div>
        <div class="form-group">
          <label for="description" class="form-label">
            <i class="bi bi-file-text form-icon"></i> Description
          </label>
          <textarea id="description" v-model.trim="company.description" class="form-input" placeholder="Brief description of your company" rows="3" required></textarea>
        </div>
        <div class="form-row">
          <div class="form-group half-width">
            <label for="category" class="form-label">
              <i class="bi bi-tag form-icon"></i> Category
            </label>
            <input type="text" id="category" v-model.trim="company.category" class="form-input" placeholder="e.g., Technology, Healthcare" required />
          </div>
          <div class="form-group half-width">
            <label for="numTel" class="form-label">
              <i class="bi bi-telephone form-icon"></i> Phone Number
            </label>
            <input type="tel" id="numTel" v-model.trim="company.numTel" @input="validatePhoneNumber" class="form-input" placeholder="Enter phone number (digits only)" required />
            <small v-if="formErrors.numTel" class="form-error-text">{{ formErrors.numTel }}</small>
          </div>
        </div>
        <div class="form-group">
          <label for="email" class="form-label">
            <i class="bi bi-envelope form-icon"></i> Email Address
          </label>
          <input type="email" id="email" v-model.trim="company.email" @blur="validateEmail" class="form-input" placeholder="Enter company email" required />
          <small v-if="formErrors.email" class="form-error-text">{{ formErrors.email }}</small>
          <small v-if="emailChecking" class="form-info-text">Checking email availability...</small>
          <small v-if="emailError" class="form-error-text">{{ emailError }}</small>
        </div>
        <div class="form-group">
          <label for="password" class="form-label">
            <i class="bi bi-lock-fill form-icon"></i> Password
          </label>
          <input type="password" id="password" v-model="company.password" class="form-input" placeholder="Choose a strong password" required />
           <small v-if="formErrors.password" class="form-error-text">{{ formErrors.password }}</small>
        </div>

        <button type="submit" class="register-button" :disabled="loading || !isFormValid">
          <span v-if="loading" class="spinner"></span>
          {{ loading ? 'Registering...' : 'Register Company' }}
        </button>

        <div v-if="error" class="error-message">
          <i class="bi bi-exclamation-triangle-fill"></i> {{ error }}
        </div>
        <div v-if="successMessage" class="success-message">
          <i class="bi bi-check-circle-fill"></i> {{ successMessage }}
        </div>

        <p class="switch-form-link">
          Already have an account?
          <a href="#" @click.prevent="$emit('switchToLogin')">Login here</a>
        </p>
      </form>
    </div>
  </div>
</template>

<script>
import companyService from '@/services/companyService';

export default {
  name: 'RegisterCompany',
  emits: ['switchToLogin', 'registrationSuccess'],
  data() {
    return {
      company: {
        name: '',
        description: '',
        responsable: '',
        category: '',
        email: '',
        numTel: '',
        password: '',
      },
      formErrors: { 
        email: '',
        numTel: '',
        password: '',
      },
      emailChecking: false, 
      emailError: '', 
      loading: false,
      error: '', 
      successMessage: '',
    };
  },
  computed: {
    isFormValid() {
      return Object.values(this.company).every(value => typeof value === 'string' ? value.trim() !== '' : value !== null) &&
             !this.formErrors.email &&
             !this.formErrors.numTel &&
             !this.formErrors.password &&
             !this.emailError; 
    }
  },
  methods: {
    validateEmail() {
      this.formErrors.email = '';
      this.emailError = ''; 
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!this.company.email) {
        this.formErrors.email = 'Email is required.';
      } else if (!emailRegex.test(this.company.email)) {
        this.formErrors.email = 'Please enter a valid email address (e.g., user@example.com).';
      } else {
        console.log("Client-side email format is valid. Backend will verify uniqueness.");
      }
    },

    validatePhoneNumber() {
      this.formErrors.numTel = '';
      const phoneRegex = /^[0-9]+$/; 
      if (!this.company.numTel) {
        this.formErrors.numTel = 'Phone number is required.';
      } else if (!phoneRegex.test(this.company.numTel)) {
        this.formErrors.numTel = 'Phone number must contain only digits.';
      }
    },

    validatePassword() {
        this.formErrors.password = '';
        if (!this.company.password) {
            this.formErrors.password = 'Password is required.';
        } else if (this.company.password.length < 6) { 
            this.formErrors.password = 'Password must be at least 6 characters long.';
        }
    },


    performValidations() {
        this.validateEmail();
        this.validatePhoneNumber();
        this.validatePassword();
        return !this.formErrors.email && !this.formErrors.numTel && !this.formErrors.password && !this.emailError;
    },

    async handleRegister() {
      this.error = ''; 
      this.successMessage = '';

      if (!this.performValidations()) {
        this.error = 'Please correct the errors in the form.';
        return;
      }

      this.loading = true;
      try {
        const registeredCompany = await companyService.registerCompany(this.company);
        this.successMessage = `Company "${registeredCompany.name}" registered successfully! Please wait for admin confirmation.`;
        this.$emit('registrationSuccess', registeredCompany);
        // Reset form:
        this.company = { name: '', description: '', responsable: '', category: '', email: '', numTel: '', password: '' };
        this.formErrors = { email: '', numTel: '', password: '' }; 
                this.emailError = '';
      } catch (err) {
        if (err.response && err.response.data) {
            if (typeof err.response.data === 'string' && err.response.data.toLowerCase().includes('email already exists')) {
                this.emailError = 'This email address is already registered.';
                this.formErrors.email = 'This email address is already registered.'; 
            } else {
                 this.error = err.response.data.message || err.response.data || 'Failed to register company.';
            }
        } else {
            this.error = err.message || 'Failed to register company. Please try again.';
        }
        console.error('Registration error:', err.response || err);
      } finally {
        this.loading = false;
      }
    },
  },
  
};
</script>

<style scoped>
.register-company-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  max-width: 520px;
  margin: 0 auto;
}

.register-card {
  background: transparent;
  width: 100%;
  text-align: left;
}

.register-title {
  font-size: 1.8rem;
  font-weight: 600;
  color: #2d3748;
  margin-bottom: 5px;
  text-align: center;
}

.register-subtitle {
  font-size: 0.95rem;
  color: #718096;
  margin-bottom: 20px;
  text-align: center;
}

.title-divider {
  border: 0;
  height: 1px;
  background-image: linear-gradient(to right, rgba(0,0,0,0), rgba(0,0,0,0.15), rgba(0,0,0,0));
  margin-bottom: 30px;
}

.register-form {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.form-row {
  display: flex;
  gap: 15px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 4px; 
  width: 100%;
}

.form-group.half-width {
  flex: 1;
}


.form-label {
  font-size: 0.9rem;
  font-weight: 500;
  color: #4a5568;
  display: flex;
  align-items: center;
  margin-bottom: 4px; 
}

.form-icon {
  margin-right: 8px;
  color: #a0aec0;
}

.form-input {
  padding: 10px 15px;
  border: 1px solid #cbd5e0;
  border-radius: 8px;
  font-size: 0.95rem;
  color: #2d3748;
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
  background-color: #fff;
}
.form-input::placeholder {
  color: #a0aec0;
}
.form-input:focus {
  border-color: #4299e1;
  box-shadow: 0 0 0 3px rgba(66, 153, 225, 0.3);
  outline: none;
}

textarea.form-input {
  resize: vertical;
  min-height: 80px;
}

.register-button {
  padding: 12px 15px;
  background-color: #38a169;
  color: #ffffff;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.2s ease, transform 0.1s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-top: 10px;
}
.register-button:hover:not(:disabled) {
  background-color: #2f855a;
  transform: translateY(-1px);
}
.register-button:active:not(:disabled) {
  transform: translateY(0px);
}
.register-button:disabled {
  background-color: #a0aec0;
  cursor: not-allowed;
}

.spinner {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}
@keyframes spin {
  to { transform: rotate(360deg); }
}

.error-message, .success-message {
  border-radius: 6px;
  font-size: 0.9rem;
  margin-top: 15px;
  padding: 10px 15px;
  text-align: left;
  display: flex;
  align-items: center;
}
.error-message i, .success-message i {
  margin-right: 8px;
}
.error-message {
  color: #e53e3e;
  background-color: #fef2f2;
  border: 1px solid #fbcbcb;
}
.success-message {
  color: #2f855a;
  background-color: #f0fff4;
  border: 1px solid #9ae6b4;
}

.switch-form-link {
  text-align: center;
  margin-top: 20px;
  font-size: 0.9rem;
  color: #4a5568;
}
.switch-form-link a {
  color: #4299e1;
  font-weight: 500;
  text-decoration: none;
}
.switch-form-link a:hover {
  text-decoration: underline;
}

.form-error-text {
  font-size: 0.8rem;
  color: #e53e3e; 
  margin-top: 2px;
}
.form-info-text {
  font-size: 0.8rem;
  color: #718096; 
  margin-top: 2px;
}
</style>