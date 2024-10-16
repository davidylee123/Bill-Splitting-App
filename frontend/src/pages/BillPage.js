import React from 'react';
import ExpenseForm from '../components/ExpenseForm';
import ExpenseList from '../components/ExpenseList';
import { useLocation } from 'react-router-dom';

const BillPage = () => {
    let location = String(useLocation().pathname).substring(1);
    return (
        <div>
            <h1>Bill Page: {location}</h1>
            <ExpenseList />
        </div>
    );
};

export default BillPage;