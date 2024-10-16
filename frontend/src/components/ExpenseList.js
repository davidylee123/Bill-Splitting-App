import React, { useState, useEffect } from 'react';
import axios from 'axios';


const ExpenseList = () => {
    const [Expenses, setExpenses] = useState([]);
    const [title, setTitle] = useState('');
    const [amount, setAmount] = useState('');
    const [Users, setUsers] = useState([{name: 'jenny'},{name: 'alex'}]);
    const [userPaid, setUserPaid] = useState('');
    
    const handleSubmit = async (e) => {
        e.preventDefault();
        setExpenses([...Expenses, {id: 1, title: title, amount: amount, user: userPaid}]);
    };

    useEffect(() => {
        const fetchExpenses = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/Expenses');
                setExpenses(response.data);
            } catch (error) {
                console.error('There was an error fetching the Expenses!', error);
            }
        };

        fetchExpenses();
    }, []);

    return (
        <div>

            {/* FORM ELEMENTS */}
            <form onSubmit={handleSubmit} style={{marginLeft: 10, backgroundColor: "lightBlue"}}>
                Title of Expense 
                <input
                    type="text"
                    placeholder="Expense Title"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    style={{float: "right", marginRight: 10}}
                />
                <br/>
                Expense Amount
                <input
                    type="text"
                    placeholder="Expense Amount"
                    value={amount}
                    onChange={(e) => setAmount(e.target.value)}
                    style={{float: "right", marginRight: 10}}
                />
                <br/>
                User That Paid
                {Users.map(user => (<>
                    <label for={user.name} style={{float: "right", marginRight: 10}}>{user.name}</label>
                    <input type="radio" id={user.name} name="users" value={user.name} onChange={(e) => setUserPaid(e.target.value)} style={{float: "right", marginRight: 10}}/>
                    </>))}
                
                {/* Add fields to enter expenses here */}
                <br/>
                <button type="submit" style={{float: "right", marginRight: 10, backgroundColor: "cornflowerblue"}}>Create Expense</button>
            </form>


            <h1>Expenses</h1>
            <ul>
                {Expenses.map(Expense => (
                    <li key={Expense.id}>
                        {Expense.title}
                        ___
                        {Expense.amount}
                        ___
                        {Expense.user}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ExpenseList;