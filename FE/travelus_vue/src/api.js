import axios from 'axios';

const api = axios.create({
  baseURL: 'http://INTERNAL_IP_REDACTED:8080',
  // baseURL: 'http://localhost:8080',
  withCredentials: true, // 세션 쿠키 포함
});

export default api;
