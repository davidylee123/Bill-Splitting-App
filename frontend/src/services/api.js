import axios from 'axios';

const API = axios.create({
    baseURL: 'http://localhost:8080/api',
});

// Function to get all bills
export const getBills = () => API.get('/bills');

// Function to create a new bill
export const createBill = (bill) => API.post('/bills', bill);