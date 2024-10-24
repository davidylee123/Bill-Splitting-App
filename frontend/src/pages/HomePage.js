import React from 'react';
import BillForm from '../components/BillForm';
import BillList from '../components/BillList';

const HomePage = () => {
    return (
        <div>
            <h2>Bill Splitting App</h2>
            <BillList />
        </div>
    );
};

export default HomePage;