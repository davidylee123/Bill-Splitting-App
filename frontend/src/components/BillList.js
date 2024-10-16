import React, { useState, useEffect } from 'react';
import axios from 'axios';

const BillList = () => {
    const [bills, setBills] = useState([{title: 'bill'}]);
    const [title, setTitle] = useState('');
    const [expenses, setExpenses] = useState([]);
    
    const handleSubmit = async (e) => {
        e.preventDefault();
        setBills([...bills, {id: 1, title: title}]);
        const newBill = { title, expenses };
    
        try {
            const response = await axios.post('http://localhost:8080/api/bills', newBill);
            alert('Bill created successfully!');
            console.log(response.data);
        } catch (error) {
            console.error('There was an error creating the bill!', error);
        }
    };

    useEffect(() => {
        const fetchBills = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/bills');
                setBills(response.data);
            } catch (error) {
                console.error('There was an error fetching the bills!', error);
            }
        };

        fetchBills();
    }, []);

    return (
        <div>
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
            <h1>Bills</h1>
            <ul>
                {bills.map(bill => (
                    <li key={bill.id}>
                        <button onClick={() => {document.location='http://localhost:3000/'+bill.title}}> {bill.title}</button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default BillList;