import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8000/api/",
  withCredentials: true, // This is fine to keep, especially for /sanctum/csrf-cookie
  headers: {
    "Content-Type": "application/json",
    "Accept": "application/json", // Good practice to add this for Laravel APIs
  },
});

axiosInstance.interceptors.request.use(
  (config) => {
    const authToken = localStorage.getItem('authToken');
    if (authToken) {
      config.headers.Authorization = `Bearer ${authToken}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default axiosInstance;