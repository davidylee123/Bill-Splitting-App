import React, { useState, useEffect } from 'react';
import axios from 'axios';

const BillList = () => {
    const [bills, setBills] = useState([]);

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
            <h1>Bills</h1>
            <ul>
                {bills.map(bill => (
                    <li key={bill.id}>
                        {bill.title}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default BillList;