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
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';

import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import Toolbar from '@mui/material/Toolbar';
import Divider from '@mui/material/Divider';

import Fab from '@mui/material/Fab';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import FormGroup from '@mui/material/FormGroup';
import AddIcon from '@mui/icons-material/Add';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import PersonOutlineOutlinedIcon from '@mui/icons-material/PersonOutlineOutlined';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';
import SearchIcon from '@mui/icons-material/Search';
import InputAdornment from '@mui/material/InputAdornment';
import DoneIcon from '@mui/icons-material/Done';
import ErrorIcon from '@mui/icons-material/Error';
import {Link} from 'react-router-dom';
import { Main, AppBar, drawerWidth } from '../Theme';
import BillForm from './BillForm';
import api from '../services/api';

const columns = [
  { id: 'title', label: 'Title', minWidth: 50, align: "left" },
  { id: 'friends', label: 'Friends', minWidth: 50, align: "left" },
  { id: 'id', label: 'Edit', minWidth: 50, align: "right" },
];

const BillList = () => {
  //for form drawer
  const [isOpen, setIsOpen] = React.useState(false);
  const [bills, setBills] = useState([]);
  const [friends, setFriends] = useState([]);
  const [addingFriend, setAddingFriend] = useState(false);
  const [newFriend, setNewFriend] = useState('');
  const [friendAddSuccess, setFriendAddSuccess] = useState(false);
  const [friendAddErr, setFriendAddErr] = useState(false);
  //for table
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(10);

  const [currentBill, setCurrentBill] = useState({
    title: '',
    users: [], 
  });
  const[billUsers, setBillUsers] = useState([]);
  const [isEditingBill, setIsEditingBill] = useState(false);

  const getBills = async () => {
    try {
      const response = await api.get('/api/bills');
      console.log(response.data);
      alert('Bills fetched successfully!');
      setBills(response.data);
    } catch (error) {
      console.error('There was an error fetching the bills!', error);
    }
  }

  const getFriends = async () => {
    let userid = '64c87da267e2a12b3c5d6701';
    //get friends, which is a list of object ids
    //get all users
    // if friend id == user id
    //      setFriends([...friends, { name: friend.name, included: false }]);
    try {
      const thisUser = await api.get('/api/users/' + userid);
      const users = await api.get('/api/users');
      let friendsArray = [];
      users.data.map((u) => {
        thisUser.data.friends.map((f) => {
          if(u._id == f){
            friendsArray.push({ name: u.name, _id: u._id });
          }
        })
      });

      setFriends(friendsArray);
      
    } catch (error) {
      console.error('There was an error fetching the friends!', error);
    }
  }

  useEffect(() => {
    getBills();
    getFriends();
  }, [])

  useEffect(() => {
    setBillUsers(friends.map(user => ({ id: user._id, name: user.name, included: currentBill.users.some(billUser => billUser._id === user._id) })))
  }, [currentBill])

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


  const handleFriendAdd = async () => {
    //alter this to:
    //  try to fetch user with username newFriend
    //  if user exists, add friend.ObjectId to user.friends list
    //  otherwise, throw error
    try {
      const response = await api.get('/api/users');
      let friend = null;
      response.data.map((f) => {if(f.name == newFriend){
          friend = f;
        }}
        );
      if(friend != null){
        alert('Friend found successfully!');
        
        //assume we know our current user id?
        let userid = '64c87da267e2a12b3c5d6701';
        const result = await api.post('/api/users/' + userid + '/friends/' + friend._id);

        setFriends([...friends, { name: newFriend, included: false, _id: friend._id }]);
        setNewFriend('');
        setFriendAddSuccess(true);
      }else{
        alert('Friend could not be found');
        setFriendAddErr(true);
      }
    } catch (error) {
      console.error('There was an error in finding this user!', error);
      setFriendAddErr(true);
    }

  }

  const handleDelete = async (n) => {
    try {
      const response = await api.delete('/api/bills/' + n);
      setBills(bills.filter((f) => f._id !== n));
    } catch (error) {
      console.error('There was an error deleting the bill!', error);
    }

    
  }

  const handleEdit = (bill) => {
    setIsEditingBill(true);
    setCurrentBill(bill);
    toggleBillForm();
  }

  const handleAdd = () => {
    setIsEditingBill(false);
    setCurrentBill({
      title: '',
      users: [], 
    });
    toggleBillForm();
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

  return (
    <div>

      {/* Add Friend Form */}
      <Box sx={{ display: 'flex' }}>
        <AppBar position="fixed" open={isOpen}>
          <Toolbar >
            <h2>Bill Splitting App</h2>
            <Button onClick={() => { setAddingFriend(true) }}
              sx={[
                {
                  bottom: 'auto',
                  right: 20,
                  top: 20,
                  left: 'auto',
                  position: 'fixed',
                },
                isOpen && { display: 'none' },
              ]}
              color="inherit" variant="outlined">Add Friend<AddIcon /></Button>
          </Toolbar>
        </AppBar>
        <Drawer
          anchor='top'
          open={addingFriend}
          onClose={() => { setAddingFriend(false); setNewFriend('') }}
        >
          <Stack spacing={1} direction="row">
            <TextField fullWidth
              placeholder="Enter your friend's username..."
              value={newFriend}
              onChange={(e) => setNewFriend(e.target.value)}
              slotProps={{
                input: {
                  startAdornment: (<InputAdornment position="start"><SearchIcon /></InputAdornment>),
                },
              }}>

            </TextField >
            <IconButton onClick={() => { handleFriendAdd() }}
              color="primary"><AddIcon />
            </IconButton>
            <Divider orientation="vertical" flexItem />
            <IconButton color="error" onClick={() => { setAddingFriend(false); setNewFriend('') }}>
              <CloseIcon />
            </IconButton>
          </Stack>

          {/* Add friend success notif */}
          <Drawer
            anchor='top'
            open={friendAddSuccess}
            onClose={() => { setFriendAddSuccess(false) }}
          >
            <Stack spacing={1} direction="row" sx={{ justifyContent: "center", alignItems: "center", }}>
              <Typography variant="h6">Friend successfully added!</Typography>
              <DoneIcon color="success" />
            </Stack>
          </Drawer>

          {/* Add friend err notif */}
          <Drawer
            anchor='top'
            open={friendAddErr}
            onClose={() => { setFriendAddErr(false) }}
          >
            <Stack spacing={1} direction="row" sx={{ justifyContent: "center", alignItems: "center", }}>
              <Typography variant="h6">Sorry! We couldn't find that user.</Typography>
              <ErrorIcon color="error" />
            </Stack>
          </Drawer>

        </Drawer>

        {/* Create New Bill Form */}
        <BillForm isOpen={isOpen} friends={friends} bills={bills} setBills={setBills} toggler={toggleBillForm} currentBill={currentBill} setCurrentBill={setCurrentBill} isEditing={isEditingBill} billFriends={billUsers} setBillFriends={setBillUsers} />

        {/* Bill List View */}
        <Main open={isOpen}>
          <Paper sx={{ width: '100%', overflow: 'hidden' }}>
            <TableContainer sx={{ maxHeight: 440 }}>
              <Table stickyHeader aria-label="sticky table">
                <TableHead>
                  <TableRow>
                    <TableCell colSpan={3} align="center"><h2>Bills</h2></TableCell>
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
                  {bills.map((bill) => {
                    return (
                      <TableRow hover role="checkbox">
                        <TableCell>
                          <Link to={`/bill/${bill._id}`}>
                            <Button>{bill.title}</Button>
                          </Link>
                        </TableCell>
                        <TableCell>{usersToString(bill.users)}</TableCell>
                        <TableCell
                          align="right"
                        >
                          <IconButton onClick={() => handleEdit(bill)} color="warning"><EditIcon /></IconButton>
                          <IconButton onClick={() => handleDelete(bill._id)} color="error"><DeleteIcon /></IconButton>
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
        onClick={handleAdd}
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
        Create Bill <AddIcon />
      </Fab>

    </div>
  );
};

export default BillList;
