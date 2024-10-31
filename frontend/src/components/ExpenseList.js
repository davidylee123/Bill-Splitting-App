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
import IconButton from '@mui/material/IconButton';
import Stack from '@mui/material/Stack';
import { styled, useTheme } from '@mui/material/styles';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import FormLabel from '@mui/material/FormLabel';

import Fab from '@mui/material/Fab';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import FormGroup from '@mui/material/FormGroup';
import InputAdornment from '@mui/material/InputAdornment';

import { useLocation } from 'react-router-dom';
import AddIcon from '@mui/icons-material/Add';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import PersonOutlineOutlinedIcon from '@mui/icons-material/PersonOutlineOutlined';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import CloseIcon from '@mui/icons-material/Close';

import { red } from '@mui/material/colors';

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
    { id: 'title', label: 'Title', minWidth: 50, align: 'left' },
    { id: 'user', label: 'Paid By', minWidth: 50, align: 'center'},
    { id: 'friends', label: 'Shared Between', minWidth: 50, align: 'center'},
    { id: 'amount', label: 'Amount', minWidth: 50, align: 'center'},
    { id: 'id', label: 'Actions', minWidth: 50, align: 'right' }
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
    const [amount, setAmount] = useState(null);
    const [userPaid, setUserPaid] = useState('');
    const [friends, setFriends] = useState([{name: 'Andrew', included: false}, {name: 'Adriana', included: false}]);
    const [Users, setUsers] = useState([{name: 'Andrew'},{name: 'Adriana'}]);
    const [currID, setCurrID] = useState(0);
    const [titleErr, setTitleErr] = useState(false);
    const [friendsErr, setFriendsErr] = useState(false);
    const [paidErr, setPaidErr] = useState(false);
    const [amountErr, setAmountErr] = useState(false);
    
    const handleFriendSelect = (event, n) => {
      friends.map(f => {
        if(f.name === n){
          f.included = !f.included
        }   
      });

    };

    const handleDelete = (n) => {
      setExpenses(Expenses.filter((f) => f.id !== n));
    }

    const handleEdit = (n) => {
      //setTitle(col.title);
      //setAmount(col.amount);

      setExpenses(Expenses.filter((f) => f.id !== n));
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        let expenseFriends = [];
        friends.map(f => {
          if(f.included){
            expenseFriends.push(f.name);
          }
        })

        if(title.length < 1){
          setTitleErr(true);
        }else{
          setTitleErr(false);
        }
        if(expenseFriends.length < 1){
          setFriendsErr(true);
        }else{
          setFriendsErr(false);
        }
        if(userPaid.length < 1){
          setPaidErr(true);
        }else{
          setPaidErr(false);
        }

        let n = amount.toFixed(2);
        if(amount == '' || amount == NaN || amount < 0 || amount != n){
          setAmountErr(true);
        }else{
          setAmountErr(false);
        }

        if(!titleErr && !friendsErr && !paidErr && !amountErr){
          
          setExpenses([...Expenses, {id: currID, title: title, amount: amount, user: userPaid, friends: expenseFriends}]);
          setCurrID(currID+1);
          setAmount(null);
          setTitle('');
        }
        
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
          <Stack spacing={1} direction="column">
          <Stack spacing={1} direction="row">
            <h2 align="center">Create Expense</h2>
            <IconButton size="small" color="error" variant="outlined" onClick={handleDrawerClose}>
              <CloseIcon />
              </IconButton>
          </Stack>
            <Divider />
            {titleErr ?
              <TextField
                error
                helperText="Please enter a title for the bill."
                value={title}
                label="Title of Expense"
                onChange={(e) => setTitle(e.target.value)}
                margin="normal"
                fullWidth
              />
            :
            <TextField
                value={title}
                label="Title of Expense"
                onChange={(e) => setTitle(e.target.value)}
                margin="normal"
                fullWidth
            />
            }
                
                <br/>
              {amountErr ?
              <TextField
              error
              helperText="Please enter valid amount"
              slotProps={{
                input: {
                  startAdornment: (<InputAdornment position="start">$</InputAdornment>),
                },
              }}
              type="number"
              value={amount}
              label="Expense Amount"
              onChange={(e) => setAmount(e.target.valueAsNumber)}
              margin="normal"
              fullWidth
          />
              :
              <TextField
                    slotProps={{
                      input: {
                        startAdornment: (<InputAdornment position="start">$</InputAdornment>),
                      },
                    }}
                    type="number"
                    value={amount}
                    label="Expense Amount"
                    onChange={(e) => setAmount(e.target.valueAsNumber)}
                    margin="normal"
                    fullWidth
                />
              }
                
                </Stack>
                <br/> 
                <Divider />
                <br /> 
                <FormLabel id="radio-group-label">Paid By:</FormLabel>
                {paidErr ?
                  <RadioGroup
                    aria-labelledby="radio-group-label"
                    style={{marginBottom: 10, marginLeft: 10}}
                  >
                    {Users.map(user => (
                      <FormControlLabel onChange={(e) => setUserPaid(e.target.value)} value={user.name} control={<Radio sx={{
                        color: red[800],
                      }}/>} label={user.name} />
                    ))}
                  </RadioGroup>
                :
                  <RadioGroup
                    aria-labelledby="radio-group-label"
                    style={{marginBottom: 10, marginLeft: 10}}
                  >
                    {Users.map(user => (
                      <FormControlLabel onChange={(e) => setUserPaid(e.target.value)} value={user.name} control={<Radio />} label={user.name} />
                    ))}
                  </RadioGroup>
                }
                    
                <FormLabel id="checkbox-group-label"> Shared Between: </FormLabel>

                {friendsErr ?
                <FormGroup aria-labelledby='checkbox-group-label'>
                {friends.map((friend) => (
                  <FormControlLabel 
                      value={friend.name}
                      onChange={(e) => handleFriendSelect(e.target, friend.name)}
                      control={<Checkbox icon={<PersonOutlineOutlinedIcon color="error"/>}
                      checkedIcon={<PersonAddIcon />}/>} 
                      label={friend.name} />
                  ))}
              </FormGroup>
                :
                <FormGroup aria-labelledby='checkbox-group-label'>
                  {friends.map((friend) => (
                    <FormControlLabel 
                        value={friend.name}
                        onChange={(e) => handleFriendSelect(e.target, friend.name)}
                        control={<Checkbox icon={<PersonOutlineOutlinedIcon />}
                        checkedIcon={<PersonAddIcon />}/>} 
                        label={friend.name} />
                    ))}
                </FormGroup>
                }
                
                {/* Add fields to enter expenses here */}

                <Stack spacing={1} direction="row">
                
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
                            <TableCell colSpan={5} align="center"><h2>Expenses</h2></TableCell>
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
                                  {column.id === 'friends' 
                                    ? value.map((friend, i) => (
                                      i == 0 ?
                                      friend :
                                      ", " + friend
                                    ))
                                    : column.id === 'id' 
                                    ? <>
                                    <IconButton onClick={() => handleEdit(value)} color="warning"><EditIcon /></IconButton>
                                    <IconButton onClick={() => handleDelete(value)} color="error"><DeleteIcon /></IconButton>
                                    </>
                                    :value}
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
            Create Expense &nbsp;<AddCircleOutlineIcon />
          </Fab>


        </div>
    );

};

export default ExpenseList;