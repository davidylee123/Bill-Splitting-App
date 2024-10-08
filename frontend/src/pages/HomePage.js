import React from 'react';
import BillForm from '../components/BillForm';
import BillList from '../components/BillList';

const HomePage = () => {
    return (
        <div>
            <h1>Bill Splitting App</h1>
            <BillForm />
            <BillList />
        </div>
    );
};

export default HomePage;