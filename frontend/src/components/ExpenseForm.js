import React, { useState } from 'react';
import axios from 'axios';

// this code is not in use, the functionality of the expense form has been
// moved to ExpenseList

const ExpenseForm = () => {
    const [title, setTitle] = useState('');
    const [expenses, setExpenses] = useState([]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const newExpense = { title, expenses };

        try {
            const response = await axios.post('http://localhost:8080/api/Expenses', newExpense);
            alert('Expense created successfully!');
            console.log(response.data);
        } catch (error) {
            console.error('There was an error creating the Expense!', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                placeholder="Expense Title"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
            />
            {/* Add fields to enter expenses here */}
            <button type="submit">Create Expense</button>
        </form>
    );
};

export default ExpenseForm;