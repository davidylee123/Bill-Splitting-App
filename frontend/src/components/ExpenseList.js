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

const columns = [
  { id: 'title', label: 'Title', minWidth: 50, align: "left" },
  { id: 'friends', label: 'Friends', minWidth: 50, align: "left" },
  { id: 'id', label: 'Edit', minWidth: 50, align: "right" },
];

const ExpenseList = ({bill_id}) => {

  //for form drawer
  const [isOpen, setIsOpen] = React.useState(false);
  const [expenses, setExpenses] = useState([]);
  const [friends, setFriends] = useState([{ name: 'Andrew', included: false }, { name: 'Adriana', included: false }]);
  //for table
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(10);

  const getExpenses = async () => {
    try {
      const response = await api.get('/api/bills/' + bill_id);
      console.log('displaying expenses:',response.data[0].expenses);
      alert('Expenses fetched successfully!');
      setExpenses(response.data[0].expenses)
    } catch (error) {
      console.error('There was an error fetching the expenses!', error);
    }
  }

  useEffect(() => {
    getExpenses();
  }, [])

  const toggleBillForm = () => {
    setIsOpen(!isOpen);
  };


  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  };

  const handleDelete = (n) => {
    setExpenses(expenses.filter((f) => f.id !== n));
  }

  const handleEdit = (n) => {
    //setTitle(col.title);
    //setAmount(col.amount);

    setExpenses(expenses.filter((f) => f.id !== n));
  }

  const usersToString = (users) => {
    if (users) {
      let userNames = [];
      userNames = users.map((user) => {
        if (user) {
          return user.userName;
        } else {
          return 'null';
        }
      })
      return userNames.toString();
    }
    return '';
  }

  return (
    <div>
      <Box sx={{ display: 'flex' }}>
        <AppBar position="fixed" open={isOpen}>
          <Toolbar >
            <h2>Bill Splitting App</h2>
          </Toolbar>
        </AppBar>
        {/* Create New Expense Form */}
        <BillForm isOpen={isOpen} friends={friends} expenses={expenses} setExpenses={setExpenses} toggler={toggleBillForm} />

        {/*Expense List View */}
        <Main open={isOpen}>
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
                      <TableRow hover role="checkbox">
                        <TableCell>
                          <Button> {expense.name}</Button>
                        </TableCell>
                        <TableCell>{usersToString(expense.splitBetween)}</TableCell>
                        <TableCell
                          align="right"
                        >
                          <IconButton onClick={() => handleEdit(expense.id)} color="warning"><EditIcon /></IconButton>
                          <IconButton onClick={() => handleDelete(expense.id)} color="error"><DeleteIcon /></IconButton>
                        </TableCell>
                      </TableRow>
                    );
                  })}
                </TableBody>
              </Table>
            </TableContainer>
            <TablePagination
              rowsPerPageOptions={[10, 25, 100]}
              component="div"
              count={expenses.length}
              rowsPerPage={rowsPerPage}
              page={page}
              onPageChange={handleChangePage}
              onRowsPerPageChange={handleChangeRowsPerPage}
            />
          </Paper>
        </Main>
      </Box>

      {/* Floating Action Button */}
      <Fab
        variant="extended"
        color="primary"
        aria-label="open drawer"
        onClick={toggleBillForm}
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
          isOpen && { display: 'none' },
        ]}
      >
        Create Expense<AddIcon />
      </Fab>

    </div >
  );
};

export default ExpenseList;
