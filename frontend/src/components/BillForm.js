import React, { useState } from 'react';
import axios from 'axios';

const BillForm = () => {
    const [title, setTitle] = useState('');
    const [expenses, setExpenses] = useState([]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const newBill = { title, expenses };

        try {
            const response = await axios.post('http://localhost:8080/api/bills', newBill);
            alert('Bill created successfully!');
            console.log(response.data);
        } catch (error) {
            console.error('There was an error creating the bill!', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                placeholder="Bill Title"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
            />
            {/* Add fields to enter expenses here */}
            <button type="submit">Create Bill</button>
        </form>
    );
};

export default BillForm;