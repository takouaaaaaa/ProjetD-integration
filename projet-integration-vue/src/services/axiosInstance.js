import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8000/api/",
  withCredentials: true, 
  headers: {
    "Content-Type": "application/json",
    "Accept": "application/json", 
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