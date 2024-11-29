import React from 'react';
import ExpenseForm from '../components/ExpenseForm';
import ExpenseList from '../components/ExpenseList';
import { useLocation } from 'react-router-dom';
import {useParams} from 'react-router-dom';

const BillPage = () => {
    let bill_id = useParams().bill_id;
    console.log('in BillPage; bill_id:' + bill_id)
    let location = String(useLocation().pathname).substring(1);
    return (
        <div >
            <h2>Bill Page: {location}</h2>
            <ExpenseList bill_id={bill_id}/>
        </div>
    );
};

export default BillPage;
