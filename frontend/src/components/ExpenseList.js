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

import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import MuiAppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Divider from '@mui/material/Divider';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import { styled, useTheme } from '@mui/material/styles';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import FormLabel from '@mui/material/FormLabel';

import Fab from '@mui/material/Fab';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';

import { useLocation } from 'react-router-dom';
import AddIcon from '@mui/icons-material/Add';

const drawerWidth = 240;

const Main = styled('main', { shouldForwardProp: (prop) => prop !== 'open' })(
    ({ theme }) => ({
      flexGrow: 1,
      padding: theme.spacing(3),
      transition: theme.transitions.create('margin', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
      }),
      marginLeft: `-${drawerWidth}px`,
      variants: [
        {
          props: ({ open }) => open,
          style: {
            transition: theme.transitions.create('margin', {
              easing: theme.transitions.easing.easeOut,
              duration: theme.transitions.duration.enteringScreen,
            }),
            marginLeft: 0,
          },
        },
      ],
    }),
  );

  const AppBar = styled(MuiAppBar, {
    shouldForwardProp: (prop) => prop !== 'open',
  })(({ theme }) => ({
    transition: theme.transitions.create(['margin', 'width'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    variants: [
      {
        props: ({ open }) => open,
        style: {
          width: `calc(100% - ${drawerWidth}px)`,
          marginLeft: `${drawerWidth}px`,
          transition: theme.transitions.create(['margin', 'width'], {
            easing: theme.transitions.easing.easeOut,
            duration: theme.transitions.duration.enteringScreen,
          }),
        },
      },
    ],
  }));

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
  let location = String(useLocation().pathname).substring(1);

  //for form drawer
  const [open, setOpen] = React.useState(false);
  const handleDrawerOpen = () => {
    setOpen(true);
  };

  const handleDrawerClose = () => {
    setOpen(false);
    setTitle('');
  };

  //for table
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

{/* form Drawer */}
<Box sx={{ display: 'flex' }}>
<AppBar position="fixed" open={open}>
        <Toolbar >
            <h2>Bill Splitting App - {location}</h2>
        </Toolbar>
      </AppBar> 
<Drawer
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          '& .MuiDrawer-paper': {
            width: drawerWidth,
            boxSizing: 'border-box',
          },
        }}
        variant="persistent"
        anchor="left"
        open={open}
      >
        
          
        <Divider />
        <form onSubmit={handleSubmit}>
                <TextField
                    placeholder="Expense Title"
                    value={title}
                    label="Title of Expense"
                    onChange={(e) => setTitle(e.target.value)}
                    style={{float: "right", marginTop: 10, marginRight: 10}}
                />
                <br/>
                <TextField
                    placeholder="Expense Amount"
                    value={amount}
                    label="Expense Amount"
                    onChange={(e) => setAmount(e.target.value)}
                    style={{float: "right", marginTop: 10, marginRight: 10}}
                />
                <br/> 
                <Divider />
                <br /> 
                <FormLabel id="radio-group-label">Paid By</FormLabel>
                    <RadioGroup
                      aria-labelledby="radio-group-label"
                      defaultValue="female"
                      name="radio-buttons-group"
                      style={{marginBottom: 10, marginLeft: 10}}
                    >
                      {Users.map(user => (
                        <FormControlLabel onChange={(e) => setUserPaid(e.target.value)} value={user.name} control={<Radio />} label={user.name} />
                      ))}
                    </RadioGroup>
                {/* Add fields to enter expenses here */}

                <Stack spacing={1} direction="row">
                <Button color="error" variant="outlined" onClick={handleDrawerClose}>
                Close
                </Button>
                <Button variant="contained" type="submit">Add <AddIcon /></Button>
                </Stack>
            </form>
      </Drawer>
      <Main open={open}>

{/*MUI table component*/}

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
</Main>
</Box>

{/* Floating Action Button */}
<Fab
            variant="extended"
            color="primary"
            aria-label="open drawer"
            onClick={handleDrawerOpen}
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
              open && { display: 'none' },
            ]}
          >
            Create Expense
          </Fab>


        </div>
    );

};

export default ExpenseList;