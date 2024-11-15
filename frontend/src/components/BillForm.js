import React, { useState } from 'react';
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

const BillForm = ({isOpen, friends, toggler, bills, setBills}) => {
  const [title, setTitle] = useState('');
  const [billFriends, setBillFriends] = useState([]);
  const [titleErr, setTitleErr] = useState(false);
  const [friendsErr, setFriendsErr] = useState(false);

  const handleDrawerClose = () => {
    toggler();
    setTitle('');
    setTitleErr(false);
    setFriendsErr(false);
  };

  const handleFriendSelect = (name) => {
    setBillFriends(setBillFriends(friends.map(f => {
      if(f.name === name){
        f.included = !f.included
      }   
  })))};

  const checkForm = () => {
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
  }

  const addBill = async (e) => {
    e.preventDefault();
    if(!titleErr && !friendsErr){
      const newBill = {title: title, expenses: [], users: billFriends};
      try {
        const response = await axios.post('http://localhost:8080/api/bills', newBill);
        alert('Bill created successfully!');
        console.log(response.data);
      } catch (error) {
        console.error('There was an error creating the bill!', error);
      }
      setBills([...bills, newBill]);
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
              {friends.map((friend) => (
                <FormControlLabel
                  value={friend.included}
                  onChange={(e) => handleFriendSelect(e.target, friend.name)}
                  control={<Checkbox icon={<PersonOutlineOutlinedIcon color="error" />}
                    checkedIcon={<PersonAddIcon />} />}
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
