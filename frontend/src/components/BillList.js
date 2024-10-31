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
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';

import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import MuiAppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Divider from '@mui/material/Divider';
import { styled, useTheme } from '@mui/material/styles';

import Fab from '@mui/material/Fab';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import FormGroup from '@mui/material/FormGroup';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import PersonOutlineOutlinedIcon from '@mui/icons-material/PersonOutlineOutlined';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';


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
    { id: 'title', label: 'Title', minWidth: 50 },
    { id: 'friends', label: 'Friends', minWidth: 50 },
    { id: 'amount', label: 'Amount', minWidth: 50 },
    { id: 'id', label: 'Edit', minWidth: 50 },
  ];

const BillList = () => {
    //for form drawer
      const [open, setOpen] = React.useState(false);
      const handleDrawerOpen = () => {
        setOpen(true);
      };
    
      const handleDrawerClose = () => {
        setOpen(false);
        setTitle('');
        setTitleErr(false);
        setFriendsErr(false);
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


    const [bills, setBills] = useState([{title: 'bill', friends: ['Adriana', 'Alan'], amount: '15.00', id: -2}, {title: 'different bill', friends: ['Andrew', 'Adriana'], amount: '2.00', id: -1}]);
    const [title, setTitle] = useState('');
    const [expenses, setExpenses] = useState([]);
    const [friends, setFriends] = useState([{name: 'Andrew', included: false}, {name: 'Adriana', included: false}]);
    const [currID, setCurrID] = useState(0);
    const [titleErr, setTitleErr] = useState(false);
    const [friendsErr, setFriendsErr] = useState(false);

    const handleFriendSelect = (event, n) => {
      friends.map(f => {
        if(f.name === n){
          f.included = !f.included
        }   
      });

    };

    const handleDelete = (n) => {
      setBills(bills.filter((f) => f.id !== n));
    }

    const handleEdit = (n) => {
      //setTitle(col.title);
      //setAmount(col.amount);

      setBills(bills.filter((f) => f.id !== n));
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        let billFriends = [];
        friends.map(f => {
          if(f.included){
            billFriends.push(f.name);
          }
        });
        
        if(title.length < 1){
          setTitleErr(true);
        }else{
          setTitleErr(false);
        }
        if(billFriends.length < 1){
          setFriendsErr(true);
        }else{
          setFriendsErr(false);
        }

        if(!titleErr && !friendsErr){
          setBills([...bills, {id: currID, title: title, friends: billFriends, amount: "0.00"}]);
          setTitle('');
          setCurrID(currID+1);
          const newBill = { title, expenses, friends};
    
          try {
              const response = await axios.post('http://localhost:8080/api/bills', newBill);
              alert('Bill created successfully!');
              console.log(response.data);
          } catch (error) {
              console.error('There was an error creating the bill!', error);
          }
        }
        
    };

    useEffect(() => {
        const fetchBills = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/bills');
                setBills(response.data);
            } catch (error) {
                console.error('There was an error fetching the bills!', error);
            }
        };

        fetchBills();
    }, []);

    return (
        <div>

{/* form Drawer */}
<Box sx={{display: 'flex'}}>
<AppBar position="fixed" open={open}>
        <Toolbar >
            <h2>Bill Splitting App</h2>
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
            <h2 align="center">Create New Bill</h2>
            <IconButton size="small" color="error" onClick={handleDrawerClose}>
                <CloseIcon />
            </IconButton>
            </Stack>
            <Divider />
            {titleErr ? 
            <TextField
                    error
                    helperText="Please enter a title for the bill."
                    variant="outlined"
                    placeholder="Bill Title"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                /> 
                :
                <TextField
                variant="outlined"
                placeholder="Bill Title"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
            />}

            {friendsErr ?
            <FormGroup >
              {friends.map((friend) => (
              <FormControlLabel 
                
                value={friend.included}
                onChange={(e) => handleFriendSelect(e.target, friend.name)}
                control={<Checkbox  icon={<PersonOutlineOutlinedIcon color="error"/>} 
                checkedIcon={<PersonAddIcon />}/>}
                label={friend.name} />
              ))}
            </FormGroup>
            :
            <FormGroup >
              {friends.map((friend) => (
              <FormControlLabel 
                value={friend.included}
                onChange={(e) => handleFriendSelect(e.target, friend.name)}
                control={<Checkbox icon={<PersonOutlineOutlinedIcon />} 
                checkedIcon={<PersonAddIcon />}/>}
                label={friend.name} />
              ))}
            </FormGroup>
            }
                
                </Stack>
                <Stack spacing={1} direction="column">
                
                <Button align="right" variant="contained" type="submit">Create Bill</Button>
                </Stack>

            </form>
      </Drawer>
      <Main open={open}>
{/* MUI Table Component*/}

            <Paper sx={{ width: '100%', overflow: 'hidden' }}>
              <TableContainer sx={{ maxHeight: 440 }}>
                <Table stickyHeader aria-label="sticky table">
                  <TableHead>
                    <TableRow>
                            <TableCell colSpan={4} align="center"><h2>Bills</h2></TableCell>
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
                    {bills
                      .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                      .map((Bills) => {
                        return (
                          <TableRow hover role="checkbox" tabIndex={-1} key={Bills.code}>
                            {columns.map((column) => {
                              const value = Bills[column.id];
                              return (
                                <TableCell key={column.id} align={column.align}>
                                    {column.id === 'title' 
                                        ? <Button onClick={() => {document.location='http://localhost:3000/'+value}}> {value}</Button> 
                                        :column.id === 'friends'
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
                count={bills.length}
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
            Create Bill <AddCircleIcon />
          </Fab>

        </div>
    );
};

export default BillList;