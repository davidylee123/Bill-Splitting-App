import React, { useState, useEffect } from 'react';

import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
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
import BillForm from './BillForm';
import api from '../services/api';

const ExpenseList = ({ bill_id }) => {

  //for form drawer
  const [isFormOpen, setIsFormOpen] = React.useState(false);
  const [expenses, setExpenses] = useState([]);
  const [friends, setFriends] = useState([{ name: 'Andrew', included: false }, { name: 'Adriana', included: false }]);

  const getExpenses = async () => {
    try {
      const response = await api.get('/api/bills/' + bill_id);
      console.log('displaying expenses:', response.data.expenses);
      alert('Expenses fetched successfully!');
      setExpenses(response.data.expenses)
    } catch (error) {
      console.error('There was an error fetching the expenses!', error);
    }
  }

  useEffect(() => {
    getExpenses();
  }, [])

  const toggleForm = () => {
    setIsFormOpen(!isFormOpen);
  };

  const handleAdd = async (newExpense) => {
    try {
      // get the current bill
      const currentBill = await api.get('api/bills/' + bill_id);
      const newBill = currentBill.data;
      // add new expense to the bill
      newBill.expenses = [...newBill.expenses, newExpense];
      const response = await api.put(`/api/bills/${bill_id}`, newBill);
      // update the state if success
      if (response.status === 200) {
        setExpenses(response.data.expenses);
        console.log("Expense added successfully!");
      } else {
        console.error("Error adding expense!", response.error);
      }
    } catch (error) {
      console.error('There was an error adding the expense!', error);
    }
  }

  const handleEdit = async (expenseId, newExpense) => {
    try {
      // get the current bill
      const currentBill = await api.get('api/bills/' + bill_id);
      const newBill = currentBill.data;
      // edit the expense on the bill
      const expenseIndex = newBill.expenses.findIndex((expense) => expense._id.toString() === expenseId);
      if (expenseIndex !== -1) {
        newBill.expenses[expenseIndex] = newExpense;
        const response = await api.put(`/api/bills/${bill_id}`, newBill);
        // update the state if success
        if (response.status === 200) {
          setExpenses(response.data.expenses);
          console.log("Expense added successfully!");
        } else {
          console.error("Error adding expense!", response.error);
        }
      } else {
        console.error('Expense not found!');
      }
    } catch (error) {
      console.error('There was an error adding the expense!', error);
    }
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

    const columns = [
      { id: 'title', label: 'Title', minWidth: 50, align: "left" },
      { id: 'amount', label: 'Cost', minWidth: 50, align: "left" },
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
          <BillForm isOpen={isFormOpen} friends={friends} expenses={expenses} setExpenses={setExpenses} toggler={toggleForm} />

          {/*Expense List View */}
          <Main>
            <Paper sx={{ width: '100%', overflow: 'hidden' }}>
              <TableContainer sx={{ maxHeight: 440 }}>
                <Table stickyHeader aria-label="sticky table">
                  <TableHead>
                    <TableRow>
                      <TableCell colSpan={3} align="center"><h2>Expenses</h2></TableCell>
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
                          <TableCell>{usersToString(expense.users)}</TableCell>
                          <TableCell
                            align="right"
                          >
                            <IconButton onClick={() => handleEdit(expense._id)} color="warning"><EditIcon /></IconButton>
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
          onClick={toggleForm}
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
