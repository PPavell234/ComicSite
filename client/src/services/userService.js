import axios from "axios";

const API_URL = "http://localhost:8080/api/users";

export const getUsers = () => {
  return axios.get(API_URL);
};
