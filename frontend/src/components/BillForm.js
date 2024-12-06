import React, { useState } from 'react';
import api from '../services/api';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import Drawer from '@mui/material/Drawer';
import Divider from '@mui/material/Divider';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import FormGroup from '@mui/material/FormGroup';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import PersonOutlineOutlinedIcon from '@mui/icons-material/PersonOutlineOutlined';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';
import { drawerWidth } from '../Theme';

const BillForm = ({isOpen, friends, toggler, bills, setBills, currentBill, setCurrentBill, billFriends, setBillFriends, isEditing}) => {
  const [titleErr, setTitleErr] = useState(false);
  const [friendsErr, setFriendsErr] = useState(false);

  const handleDrawerClose = () => {
    toggler();
    setTitleErr(false);
    setFriendsErr(false);
  };

  const handleFriendSelect = (userId) => {
    if (!billFriends || billFriends.length === 0) {
      return; // Do nothing if friends is empty
    }
    const newUsers = billFriends.map(user => ({
      id: user.id,
      name: user.name,
      included: (user.id == userId)? !user.included : user.included
    }))
    setBillFriends(newUsers);
  };

  const checkForm = () => {
    let hasError = false;
    if(currentBill.title.length < 1){
      hasError = true;
      setTitleErr(true);
    }else{
      setTitleErr(false);
    }
    if(billFriends.filter(user => user.included === true).length < 1){
      hasError = true;
      setFriendsErr(true);
    }else{
      setFriendsErr(false);
    }

    return hasError;
  }

  function filterObjectsById(list1, list2) {
    const idsInList2 = list2.filter(item => item.included);
    return list1.filter(item => idsInList2.some(ref => ref.id === item._id));
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if(!checkForm()){
      let bFriends = [];
      friends.map((f) => {
        if(f.included){
          bFriends.push(f);
        }
      });
      
      if(isEditing){
        const newBill = {_id: currentBill._id, title: currentBill.title, expenses: [], users: filterObjectsById(friends, billFriends)};
        handleEdit(currentBill._id, newBill);
      }else{
        const newBill = {title: currentBill.title, expenses: [], users: filterObjectsById(friends, billFriends)};
        handleAdd(newBill);
      }
    }else{
      alert("Please fill out all fields.")
    }
  }

  const handleAdd = async (newBill) => {
    try {
      // create the bill
      const billResponse = await api.post('/api/bills', newBill);
      if (billResponse.status === 200) {
        setBills([...bills, billResponse.data]);
        console.log("Bill added successfully!");
      } else {
        console.error("Error adding bill!", billResponse.error);
      }
    } catch (error) {
      console.error('There was an error adding the bill!', error);
    }
  }

  const handleEdit = async (billId, newBill) => {
    try {
      const response = await api.put('/api/bills/' + billId, newBill);
      // update the state if success
      if (response.status === 200) {
        setBills(bills.map(b => b._id === billId ? response.data : b));
        console.log("Bill edited successfully!");
      } else {
        console.error("Error editing bill!", response.error);
      }
    } catch (error) {
      console.error('There was an error editing the bill!', error);
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
      <form onSubmit={handleSubmit}>
        <Stack spacing={1} direction="column">
          <Stack spacing={1} direction="row">
          <h2 align="center">
              {isEditing ? "Edit Bill" : "Create Bill"}
            </h2>
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
              value={currentBill.title}
              onChange={(e) => setCurrentBill({...currentBill, title: (e.target.value)})}
            />
            :
            <TextField
              variant="outlined"
              placeholder="Bill Title"
              value={currentBill.title}
              onChange={(e) => setCurrentBill({...currentBill, title: (e.target.value)})}
            />}
          {friendsErr ?
            <FormGroup >
              {billFriends.map((friend) => (
                <FormControlLabel
                  key={friend.name}
                  value={friend.id}
                  checked={friend.included}
                  onChange={(e) => handleFriendSelect(friend.id)}
                  control={<Checkbox icon={<PersonOutlineOutlinedIcon color="error" />}
                    checkedIcon={<PersonAddIcon />} />}
                  label={friend.name} />
              ))}
            </FormGroup>
            :
            <FormGroup >
              {billFriends.map((friend) => (
                <FormControlLabel
                  key={friend.name}
                  value={friend.id}
                  checked={friend.included}
                  onChange={(e) => handleFriendSelect(friend.id)}
                  control={<Checkbox icon={<PersonOutlineOutlinedIcon />}
                    checkedIcon={<PersonAddIcon />} />}
                  label={friend.name} />
              ))}
            </FormGroup>
          }
        </Stack>
        <Stack spacing={1} direction="column">
          <Button align="right" variant="contained" type="submit">Save</Button>
        </Stack>
      </form>
    </Drawer>
  );
};

export default BillForm;
