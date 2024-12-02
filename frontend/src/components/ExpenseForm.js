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

const ExpenseForm = ({ isOpen, toggler, bill_id, billUsers, isEditing, currentExpense, expenseSplitUsers, setExpenses }) => {

  const [title, setTitle] = useState(currentExpense.title);
  const [amount, setAmount] = useState(currentExpense.amount);
  const [users, setUsers] = useState(expenseSplitUsers);
  const [paidBy, setPaidBy] = useState(currentExpense.paidBy);
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
    console.log(JSON.stringify(users));
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
    if (title.length < 1) {
      setTitleErr(true);
    } else {
      setTitleErr(false);
    }
    if (!users && users.length < 1) {
      setUsersErr(true);
    } else {
      setUsersErr(false);
    }
  }

  function filterObjectsById(list1, list2) {
    const idsInList2 = new Set(list2.map(item => item.id));
    return list1.filter(item => idsInList2.has(item._id));
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    checkForm();
    if (isEditing) {
      const newExpense = {
        _id: currentExpense._id,
        title: title,
        amount: amount,
        paidBy: paidBy,
        users: filterObjectsById(billUsers, users)
      }
      console.log(JSON.stringify(newExpense));
      handleEdit(currentExpense._id, newExpense);
    } else {
      const newExpense = {
        title: title,
        amount: amount,
        paidBy: paidBy,
        users: filterObjectsById(billUsers, users)
      }
      handleAdd(newExpense);
    }
  }

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
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />
            :
            <TextField
              variant="outlined"
              placeholder="Title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />}
          <FormLabel>Amount</FormLabel>
          {amountErr ?
            <TextField
              error
              helperText="Please enter a cost."
              variant="outlined"
              placeholder="Cost"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
            />
            :
            <TextField
              variant="outlined"
              placeholder="Cost"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
            />}
          {paidByErr ?
            <FormControl>
              <FormLabel id="select_paidby_label">Paid by:</FormLabel>
              <RadioGroup
                aria-labelledby="select_paidby_label"
                defaultValue={paidBy ? paidBy._id : undefined}
              >
                {billUsers.map((billUser) => (
                  <FormControlLabel
                    value={billUser._id}
                    control={<Radio color="error" />}
                    onChange={(e) => setPaidBy(billUsers.find(u => u._id === e.target.value))}
                    label={billUser.name} />
                ))}
              </RadioGroup >
            </FormControl>
            :
            <FormControl>
              <FormLabel id="select_paidby_label">Paid by:</FormLabel>
              <RadioGroup
                aria-labelledby="select_paidby_label"
                defaultValue={paidBy ? paidBy._id : undefined}
              >
                {billUsers.map((billUser) => (
                  <FormControlLabel
                    value={billUser._id}
                    control={<Radio />}
                    onChange={(e) => setPaidBy(billUsers.find(u => u._id === e.target.value))}
                    label={billUser.name} />
                ))}
              </RadioGroup>
            </FormControl>
          }
          {usersErr ?
            <FormControl>
              <FormLabel id="select_user_label">Split to:</FormLabel>
              <FormGroup
                aria-labelledby="select_user_label">
                {users.map((user) => (
                  <FormControlLabel
                    value={user.included}
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
                    value={user.included}
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
