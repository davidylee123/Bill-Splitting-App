import React, { useState, useEffect } from 'react';

import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Button from '@mui/material/Button';

import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';

import Fab from '@mui/material/Fab';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import IconButton from '@mui/material/IconButton';
import { Main, AppBar, drawerWidth } from '../Theme';
import ExpenseForm from './ExpenseForm';
import api from '../services/api';

const ExpenseList = ({ bill_id }) => {

  const [isFormOpen, setIsFormOpen] = React.useState(false);
  const [expenses, setExpenses] = useState([]);
  const [users, setUsers] = useState([]);
  const [currentExpense, setCurrentExpense] = useState({
      title: '',
      paidBy: undefined,
      amount: 0,
      users: [], 
    });
  const [expenseSplitUsers, setExpenseSplitUsers] = useState([]);
  const [isEditingExpense, setIsEditingExpense] = useState(false);

  const getBillData = async () => {
    try {
      const response = await api.get('/api/bills/' + bill_id);
      console.log('displaying expenses:', response.data.expenses);
      alert('Expenses fetched successfully!');
      setExpenses(response.data.expenses)
      setUsers(response.data.users)
    } catch (error) {
      console.error('There was an error fetching the expenses!', error);
    }
  }

  useEffect(() => {
    getBillData();
  }, [])

  const toggleForm = () => {
    setIsFormOpen(!isFormOpen);
  };

  const activateEditForm = (expense) => {
    setIsEditingExpense(true);
    setCurrentExpense(expense);
    setExpenseSplitUsers(users.map(user => ({ id: user._id, name: user.name, included: currentExpense.users.some(expenseUser => expenseUser._id === user._id) })))
    toggleForm();
    console.log('ExpenseSplitUsers: ', expenseSplitUsers)
  }

  const activateAddForm = () => {
    setIsEditingExpense(false);
    setCurrentExpense({
      title: '',
      paidBy: undefined,
      amount: 0,
      users: [], 
    });
    setExpenseSplitUsers(users.map(user => ({ id: user._id, name: user.name, included: false })))
    console.log('ExpenseSplitUsers: ', expenseSplitUsers)
    toggleForm();
  }

  const handleDelete = async (expenseId) => {
      try {
        // get the current bill
        const currentBill = await api.get('api/bills/' + bill_id);
        const newBill = currentBill.data;
        // remove the expense from the bill
        newBill.expenses = newBill.expenses.filter((expense) => expense._id !== expenseId);
        const response = await api.put(`/api/bills/${bill_id}`, newBill);
        // update the state
        if (response.status === 200) {
          setExpenses(response.data.expenses);
          console.log("Expense deleted successfully!");
        } else {
          console.error("Error deleting expense!", response.error);
        }
      } catch (error) {
        console.error('There was an error deleting the expense!', error);
      }
    }

    const usersToString = (users) => {
      if (users) {
        let userNames = [];
        userNames = users.map((user) => {
          if (user) {
            return user.name;
          } else {
            return 'null';
          }
        })
        return userNames.join(', ');
      }
      return '';
    }

    const calculateTotal = () => {
      let total = 0;
      expenses.forEach((expense) => {
        total += expense.amount;
      });
      return total;
    }

    const columns = [
      { id: 'title', label: 'Title', minWidth: 50, align: "left" },
      { id: 'amount', label: 'Cost', minWidth: 50, align: "left" },
      { id: 'paidBy', label: 'Paid by', minWidth: 50, align: "left" },
      { id: 'splitBetween', label: 'Split To', minWidth: 50, align: "left" },
      { id: 'id', label: 'Edit', minWidth: 50, align: "right" },
    ];

    return (
      <div>
        <Box sx={{ display: 'flex' }}>
          <AppBar position="fixed">
            <Toolbar >
              <h2>Bill Splitting App</h2>
            </Toolbar>
          </AppBar>
          {/* Create New Expense Form */}
          <ExpenseForm isOpen={isFormOpen} currentExpense={currentExpense} setCurrentExpense={setCurrentExpense} setExpenses={setExpenses} toggler={toggleForm} bill_id={bill_id} billUsers={users} isEditing={isEditingExpense} users={expenseSplitUsers} setUsers={setExpenseSplitUsers}/>

          {/*Expense List View */}
          <Main>
            <Paper sx={{ width: '100%', overflow: 'hidden' }}>
              <TableContainer sx={{ maxHeight: 440 }}>
                <Table stickyHeader aria-label="sticky table">
                  <TableHead>
                    <TableRow>
                      <TableCell align="left"><h2>Expenses</h2></TableCell>
                      <TableCell align="center"><h2>Total: $ {calculateTotal()}</h2></TableCell>
                      <TableCell align="right"><h2>Your Share: $ {Math.round(calculateTotal() / (users.length + 1) * 100)/100}</h2></TableCell>
                    </TableRow>
                    <TableRow>
                      {columns.map((column) => (
                        <TableCell
                          key={column.id}
                          align={column.align}
                          style={{ minWidth: column.minWidth }}
                        >
                          {column.label}
                        </TableCell>
                      ))}
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {expenses.map((expense) => {
                      return (
                        <TableRow
                          hover role="checkbox"
                          key={expense._id}
                        >
                          <TableCell>
                            <Button> {expense.title}</Button>
                          </TableCell>
                          <TableCell>
                            $ {expense.amount}
                          </TableCell>
                          <TableCell>
                            {expense.paidBy.name}
                          </TableCell>
                          <TableCell>{usersToString(expense.users)}</TableCell>
                          <TableCell
                            align="right"
                          >
                            <IconButton onClick={() => activateEditForm(expense)} color="warning"><EditIcon /></IconButton>
                            <IconButton onClick={() => handleDelete(expense._id)} color="error"><DeleteIcon /></IconButton>
                          </TableCell>
                        </TableRow>
                      );
                    })}
                  </TableBody>
                </Table>
              </TableContainer>
            </Paper>
          </Main>
        </Box>

        {/* Floating Action Button */}
        <Fab
          variant="extended"
          color="primary"
          aria-label="open drawer"
          onClick={() => activateAddForm()}
          sx={[
            {
              mr: 2,
              margin: 0,
              top: 'auto',
              right: 20,
              bottom: 20,
              left: 'auto',
              position: 'fixed',
            },
            isFormOpen && { display: 'none' },
          ]}
        >
          Create Expense<AddIcon />
        </Fab>

      </div >
    );
  };

  export default ExpenseList;
