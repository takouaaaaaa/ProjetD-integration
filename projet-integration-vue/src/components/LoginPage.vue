<template>
  <div class="login-form-container">
    <div class="login-card">
      <h2 class="login-title">Event Platform Login</h2>
      <p class="login-subtitle">Access your dashboard</p>
      <hr class="title-divider">
      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label for="username" class="form-label">
            <i class="bi bi-person-fill form-icon"></i> Username
          </label>
          <input
            type="text"
            id="username"
            v-model="username"
            class="form-input"
            placeholder="Enter your username"
            required
          />
        </div>
        
        <div class="form-group">
          <label for="password" class="form-label">
            <i class="bi bi-lock-fill form-icon"></i> Password
          </label>
          <input
            type="password"
            id="password"
            v-model="password"
            class="form-input"
            placeholder="Enter your password"
            required
          />
        </div>
        
        <button type="submit" class="login-button" :disabled="loading">
          <span v-if="loading" class="spinner"></span>
          {{ loading ? 'Logging in...' : 'Login' }}
        </button>

<p class="switch-form-link">
  Don't have an account?
  <a href="#" @click.prevent="$emit('switchToRegister')">Register here</a>
</p>
        <div v-if="error" class="error-message">
          <i class="bi bi-exclamation-triangle-fill"></i> {{ error }}
        </div>
      </form>
    </div>
  </div>
</template>
<script>
import axiosInstance from '@/services/axiosInstance';
import { useRouter } from 'vue-router';

export default {
  name: 'LoginPage',
  emits: ['switchToRegister'],
  data() {
    return {
      username: '',
      password: '',
      loading: false,
      error: '',
    };
  },
  setup() {
    const router = useRouter();
    return { router };
  },
  methods: {
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
      } catch (error) {
        console.error('Failed to decode JWT:', error);
        return null;
      }
    },

    async handleLogin() {
      this.loading = true;
      this.error = '';

      try {
        const response = await axiosInstance.post('/api/auth/signin', {
          username: this.username,
          password: this.password,
        });

        const token = response.data; 
        const decodedToken = this.decodeJwt(token);

        if (decodedToken && decodedToken.role === 'ADMIN') {
          localStorage.setItem('authToken', token);
          this.router.push('/admin');
        } else if (decodedToken && decodedToken.role === 'ORGANIZATION') {
          localStorage.setItem('authToken', token);
          this.router.push('/companies');
        } else {
          this.error = 'Access denied. Insufficient privileges.';
        }

      } catch (err) {
        if (err.response && err.response.status === 401) {
          this.error = 'Invalid username or password.';
        } else {
          this.error = 'An error occurred. Please try again.';
        }
        console.error('Login error:', err.response || err.message || err);
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>

<style scoped>
.login-form-container {
  display: flex;
  justify-content: center;
  align-items: center; 
  width: 100%;
  max-width: 420px; 
  margin: 0 auto; 
}

.login-card {
  background: transparent; 
  width: 100%;
  text-align: left; 
}

.login-title {
  font-size: 1.8rem; 
  font-weight: 600;
  color: #2d3748; 
  margin-bottom: 5px;
  text-align: center;
}
.login-subtitle {
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


.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px; 
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-label {
  font-size: 0.9rem;
  font-weight: 500;
  color: #4a5568; 
  display: flex;
  align-items: center;
}

.form-icon {
  margin-right: 8px;
  color: #a0aec0; 
}

.form-input {
  padding: 12px 15px;
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

.login-button {
  padding: 12px 15px;
  background-color: #4299e1;
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

.login-button:hover:not(:disabled) {
  background-color: #3182ce; 
  transform: translateY(-1px);
}

.login-button:active:not(:disabled) {
  transform: translateY(0px);
}

.login-button:disabled {
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

.error-message {
  color: #e53e3e; 
  background-color: #fef2f2;
  border: 1px solid #fbcbcb;
  border-radius: 6px;
  font-size: 0.9rem;
  margin-top: 15px;
  padding: 10px 15px;
  text-align: left;
  display: flex;
  align-items: center;
}
.error-message i {
  margin-right: 8px;
}
</style>