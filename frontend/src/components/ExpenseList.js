import React, { useState, useEffect } from 'react';
import axios from 'axios';

import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';

const columns = [
    { id: 'title', label: 'Title', minWidth: 170 },
    { id: 'amount', label: 'Amount', minWidth: 100 },
    {
      id: 'user',
      label: 'Paid By',
      minWidth: 170,
      align: 'right',
    },
  ];





const ExpenseList = () => {
    const [page, setPage] = React.useState(0);
    const [rowsPerPage, setRowsPerPage] = React.useState(10);

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(+event.target.value);
        setPage(0);
    };




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


{/*testing MUI component*/}


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
            {Expenses
              .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
              .map((Expenses) => {
                return (
                  <TableRow hover role="checkbox" tabIndex={-1} key={Expenses.code}>
                    {columns.map((column) => {
                      const value = Expenses[column.id];
                      return (
                        <TableCell key={column.id} align={column.align}>
                          {column.format && typeof value === 'number'
                            ? column.format(value)
                            : value}
                        </TableCell>
                      );
                    })}
                  </TableRow>
                );
              })}
          </TableBody>
        </Table>
      </TableContainer>
      <TablePagination
        rowsPerPageOptions={[10, 25, 100]}
        component="div"
        count={Expenses.length}
        rowsPerPage={rowsPerPage}
        page={page}
        onPageChange={handleChangePage}
        onRowsPerPageChange={handleChangeRowsPerPage}
      />
    </Paper>




        </div>
    );

};

export default ExpenseList;