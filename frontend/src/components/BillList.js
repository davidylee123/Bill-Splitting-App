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
import { Main, AppBar, drawerWidth } from '../Theme';
import BillForm from './BillForm';

const columns = [
  { id: 'title', label: 'Title', minWidth: 50, align: "left" },
  { id: 'friends', label: 'Friends', minWidth: 50, align: "left" },
  { id: 'id', label: 'Edit', minWidth: 50, align: "right" },
];

const BillList = () => {
  //for form drawer
  const [isOpen, setIsOpen] = React.useState(false);
  const [bills, setBills] = useState([]);
  const [friends, setFriends] = useState([{ name: 'Andrew', included: false }, { name: 'Adriana', included: false }]);
  const [addingFriend, setAddingFriend] = useState(false);
  const [newFriend, setNewFriend] = useState('');
  const [friendAddSuccess, setFriendAddSuccess] = useState(false);
  const [friendAddErr, setFriendAddErr] = useState(false);
  //for table
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(10);

  const getBills = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/bills');
      console.log(response.data);
      alert('Bills fetched successfully!');
      setBills(response.data);
    } catch (error) {
      console.error('There was an error fetching the bills!', error);
    }
  }

  useEffect(() => {
    getBills();
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


  const handleFriendAdd = () => {
    if (newFriend.length >= 2) {
      setFriends([...friends, { name: newFriend, included: false }]);
      setNewFriend('');
      setFriendAddSuccess(true);
    } else {
      setFriendAddErr(true);
    }

  }

  const handleDelete = (n) => {
    setBills(bills.filter((f) => f.id !== n));
  }

  const handleEdit = (n) => {
    //setTitle(col.title);
    //setAmount(col.amount);

    setBills(bills.filter((f) => f.id !== n));
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
          isOpen={addingFriend}
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
            isOpen={friendAddSuccess}
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
            isOpen={friendAddErr}
            onClose={() => { setFriendAddErr(false) }}
          >
            <Stack spacing={1} direction="row" sx={{ justifyContent: "center", alignItems: "center", }}>
              <Typography variant="h6">Sorry! We couldn't find that user.</Typography>
              <ErrorIcon color="error" />
            </Stack>
          </Drawer>

        </Drawer>

        {/* Create New Bill Form */}
        <BillForm isOpen={isOpen} friends={friends} bills={bills} setBills={setBills} toggler={toggleBillForm} />

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
                          <Button onClick={() => { document.location = 'http://localhost:3000/' + bill.title }}> {bill.title}</Button>
                        </TableCell>
                        <TableCell>{usersToString(bill.users)}</TableCell>
                        <TableCell
                          align="right"
                        >
                          <IconButton onClick={() => handleEdit(bill.id)} color="warning"><EditIcon /></IconButton>
                          <IconButton onClick={() => handleDelete(bill.id)} color="error"><DeleteIcon /></IconButton>
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
        Create Bill <AddIcon />
      </Fab>

    </div>
  );
};

export default BillList;
