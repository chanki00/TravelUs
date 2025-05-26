import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080',
  // baseURL: 'http://localhost:8080',
  withCredentials: true, // 세션 쿠키 포함
});

export default api;
