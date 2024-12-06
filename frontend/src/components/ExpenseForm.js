import React, { useState, useEffect } from 'react';
import api from '../services/api';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import Drawer from '@mui/material/Drawer';
import Divider from '@mui/material/Divider';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormControl from '@mui/material/FormControl';
import FormLabel from '@mui/material/FormLabel';
import Checkbox from '@mui/material/Checkbox';
import FormGroup from '@mui/material/FormGroup';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import PersonOutlineOutlinedIcon from '@mui/icons-material/PersonOutlineOutlined';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';
import { drawerWidth } from '../Theme';
import { FormHelperText } from '@mui/material';

const ExpenseForm = ({ isOpen, toggler, bill_id, billUsers, isEditing, currentExpense, setCurrentExpense, users, setUsers, setExpenses }) => {

  const [titleErr, setTitleErr] = useState(false);
  const [amountErr, setAmountErr] = useState(false);
  const [usersErr, setUsersErr] = useState(false);
  const [paidByErr, setPaidByErr] = useState(false);

  const handleDrawerClose = () => {
    toggler();
    setTitleErr(false);
    setUsersErr(false);
    setAmountErr(false);
    setPaidByErr(false);
  };

  const handleUserSelect = (userId) => {
    if (!users || users.length === 0) {
      return; // Do nothing if users is empty
    }
    const newUsers = users.map(user => ({
      id: user.id,
      name: user.name,
      included: user.included = (user.id === userId)? !user.included : user.included
    }))
    setUsers(newUsers);
  }

  const checkForm = () => {
    let hasError = false;
    if (currentExpense.title.length < 1) {
      setTitleErr(true);
      hasError = true;
    } else {
      setTitleErr(false);
    }
    if (currentExpense.amount < 0) {
      setAmountErr(true);
      hasError = true;
    } else {
      setAmountErr(false);
    }
    if (currentExpense.paidBy === undefined) {
      setPaidByErr(true);
      hasError = true;
    } else {
      setPaidByErr(false);
    }
    if (users.filter(user => user.included === true).length < 1) {
      setUsersErr(true);
      hasError = true;
    } else {
      setUsersErr(false);
    }
    return hasError;
  }

  function filterObjectsById(list1, list2) {
    const idsInList2 = list2.filter(item => item.included);
    return list1.filter(item => idsInList2.some(ref => ref.id === item._id));
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    if (checkForm()) {
      alert('Please fill out all fields.');
    } else {
      if (isEditing) {
        const newExpense = {
          _id: currentExpense._id,
          title: currentExpense.title,
          amount: currentExpense.amount,
          paidBy: currentExpense.paidBy,
          users: filterObjectsById(billUsers, users)
        }
        handleEdit(currentExpense._id, newExpense);
      } else {
        const newExpense = {
          title: currentExpense.title,
          amount: currentExpense.amount,
          paidBy: currentExpense.paidBy,
          users: filterObjectsById(billUsers, users)
        }
        handleAdd(newExpense);
      }
    }
  }

  const handleAdd = async (newExpense) => {
    try {
      // create the expense
      const expenseResponse = await api.post('/api/expenses', newExpense);
      console.log('new expense:', expenseResponse.data);
      // get the current bill
      const currentBill = await api.get('api/bills/' + bill_id);
      const newBill = currentBill.data;
      // add new expense to the bill
      newBill.expenses = [...newBill.expenses, expenseResponse.data];
      const billResponse = await api.put(`/api/bills/${bill_id}`, newBill);
      // update the state if success
      if (billResponse.status === 200) {
        setExpenses(billResponse.data.expenses);
        console.log("Expense added successfully!");
      } else {
        console.error("Error adding expense!", billResponse.error);
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
          console.log("Expense edited successfully!");
        } else {
          console.error("Error editing expense!", response.error);
        }
      } else {
        console.error('Expense not found!');
      }
    } catch (error) {
      console.error('There was an error editing the expense!', error);
    }
  }

  return (
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
      open={isOpen}
    >
      <Divider />
      <form onSubmit={(e) => handleSubmit(e)}>
        <Stack spacing={1} direction="column">
          <Stack spacing={1} direction="row">
            <h2 align="center">
              {isEditing ? "Edit Expense" : "Create Expense"}
            </h2>
            <IconButton size="small" color="error" onClick={handleDrawerClose}>
              <CloseIcon />
            </IconButton>
          </Stack>
          <Divider />
          <FormLabel>Title</FormLabel>
          {titleErr ?
            <TextField
              error
              helperText="Please enter a title."
              variant="outlined"
              placeholder="Title"
              value={currentExpense.title}
              onChange={(e) => setCurrentExpense({...currentExpense, title: (e.target.value)})}
            />
            :
            <TextField
              variant="outlined"
              placeholder="Title"
              value={currentExpense.title}
              onChange={(e) => setCurrentExpense({...currentExpense, title: (e.target.value)})}
            />}
          <FormLabel>Amount</FormLabel>
          {amountErr ?
            <TextField
              error
              helperText="Please enter a cost."
              variant="outlined"
              placeholder="Cost"
              value={currentExpense.amount}
              onChange={(e) => setCurrentExpense({...currentExpense, amount: (e.target.value)})}
            />
            :
            <TextField
              variant="outlined"
              placeholder="Cost"
              value={currentExpense.amount}
              onChange={(e) => setCurrentExpense({...currentExpense, amount: (e.target.value)})}
            />}
          {paidByErr ?
            <FormControl>
              <FormLabel id="select_paidby_label">Paid by:</FormLabel>
              <FormHelperText error>Please select a user.</FormHelperText>
              <RadioGroup
                aria-labelledby="select_paidby_label"
                defaultValue={currentExpense.paidBy?._id}
              >
                {billUsers.map((billUser) => (
                  <FormControlLabel
                    key={billUser._id}
                    value={billUser._id}
                    control={<Radio color="error" />}
                    checked={billUser._id === currentExpense.paidBy?._id}
                    onChange={(e) => setCurrentExpense({...currentExpense, paidBy: (billUsers.find(u => u._id === e.target.value))})}
                    label={billUser.name} />
                ))}
              </RadioGroup >
            </FormControl>
            :
            <FormControl>
              <FormLabel id="select_paidby_label">Paid by:</FormLabel>
              <RadioGroup
                aria-labelledby="select_paidby_label"
                defaultValue={currentExpense.paidBy?._id}
              >
                {billUsers.map((billUser) => (
                  <FormControlLabel
                    key={billUser._id}
                    value={billUser._id}
                    control={<Radio />}
                    checked={billUser._id === currentExpense.paidBy?._id}
                    onChange={(e) => setCurrentExpense({...currentExpense, paidBy: (billUsers.find(u => u._id === e.target.value))})}
                    label={billUser.name} />
                ))}
              </RadioGroup>
            </FormControl>
          }
          {usersErr ?
            <FormControl>
              <FormLabel id="select_user_label">Split to:</FormLabel>
              <FormHelperText error>Please select a user.</FormHelperText>
              <FormGroup
                aria-labelledby="select_user_label"
                >
                {users.map((user) => (
                  <FormControlLabel
                    key={user.name}
                    value={user.id}
                    checked={user.included}
                    control={<Checkbox icon={<PersonOutlineOutlinedIcon color="error" />}
                      checkedIcon={<PersonAddIcon />} />}
                    label={user.name}
                    onChange={() => handleUserSelect(user.id)} />
                ))}
              </FormGroup>
            </FormControl>
            :
            <FormControl>
              <FormLabel id="select_user_label">Split to:</FormLabel>
              <FormGroup >
                {users.map((user) => (
                  <FormControlLabel
                    key={user.name}
                    value={user.id}
                    checked={user.included}
                    control={<Checkbox icon={<PersonOutlineOutlinedIcon />}
                      checkedIcon={<PersonAddIcon />} />}
                    label={user.name}
                    onChange={() => handleUserSelect(user.id)} />
                ))}
              </FormGroup>
            </FormControl>
          }
        </Stack>
        <Stack spacing={1} direction="column">
          <Button align="right" variant="contained" type="submit">Save</Button>
        </Stack>
      </form>
    </Drawer>
  );

};

export default ExpenseForm;