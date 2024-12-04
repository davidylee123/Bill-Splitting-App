import React, { useState, useEffect } from 'react';
import axios from 'axios';
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

const BillForm = ({isOpen, friends, toggler, bills, setBills, currentBill, isEditing}) => {
  const [title, setTitle] = useState(currentBill.title);
  const [billFriends, setBillFriends] = useState([]);
  const [titleErr, setTitleErr] = useState(false);
  const [friendsErr, setFriendsErr] = useState(false);

  useEffect(() => {
    setBillFriends(friends);
  }, [friends])

  const handleDrawerClose = () => {
    toggler();
    setTitle('');
    setTitleErr(false);
    setFriendsErr(false);
  };

  const handleFriendSelect = (userId) => {
    let newUsers = billFriends.map(user => ({
      _id: user._id,
      name: user.name,
      included: (user._id == userId)? !user.included : user.included
    }))
    setBillFriends(newUsers);
  };

  const checkForm = () => {
    if(title.length < 1){
      setTitleErr(true);
    }else{
      setTitleErr(false);
    }
    if(!billFriends && billFriends.length < 1){
      setFriendsErr(true);
    }else{
      setFriendsErr(false);
    }
  }

  const addBill = async (e) => {
    e.preventDefault();
    checkForm();
    if(!titleErr && !friendsErr){
      let bFriends = [];
      billFriends.map((f) => {
        if(f.included){
          bFriends.push(f);
        }
      });
      const newBill = {title: title, expenses: [], users: bFriends};
      try {
        const response = await axios.post('http://localhost:8080/api/bills', newBill);
        alert('Bill created successfully!');
        console.log(response.data);
        setBills([...bills, response.data]);
      } catch (error) {
        console.error('There was an error creating the bill!', error);
      }
      setTitle('');
      setBillFriends([]);
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
      <form onSubmit={addBill}>
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
              {billFriends.map((friend) => (
                <FormControlLabel
                  value={friend.included}
                  onChange={(e) => handleFriendSelect(e.target, friend._id)}
                  control={<Checkbox icon={<PersonOutlineOutlinedIcon color="error" />}
                    checkedIcon={<PersonAddIcon />} />}
                  label={friend.name} />
              ))}
            </FormGroup>
            :
            <FormGroup >
              {billFriends.map((friend) => (
                <FormControlLabel
                  value={friend.included}
                  onChange={(e) => handleFriendSelect(e.target, friend._id)}
                  control={<Checkbox icon={<PersonOutlineOutlinedIcon />}
                    checkedIcon={<PersonAddIcon />} />}
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
  );
};

export default BillForm;
