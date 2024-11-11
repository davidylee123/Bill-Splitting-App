import React, { useState } from 'react';
import axios from 'axios';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid2';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import Stack from '@mui/material/Stack';


const CreateAccountForm = () => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [rPass, setRPass] = useState('');
    const [usernameErr, setUsernameErr] = useState(false);
    const [emailErr, setEmailErr] = useState(false);
    const [passwordErr, setPasswordErr] = useState(false);
    const [passMatchErr, setPassMatchErr] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        let err = false;
        //do some client-side validation
        if(username.length<2){
            setUsernameErr(true);
            err = true;
        }else{
            setUsernameErr(false);
        }
        //add email formatting checking in the future
        if(email.length<2){
            setEmailErr(true);
            err = true;
        }else{
            setEmailErr(false);
        }
        if(password.length < 6 || password.length > 32){
            setPasswordErr(true);
            err = true;
        }else{
            setPasswordErr(false);
        }
        if(password.localeCompare(rPass) != 0){
            setPassMatchErr(true);
            err = true;
        }else{
            setPassMatchErr(false);
        }

        if(!err){
          
          setUsername('');
          setEmail('');
          setPassword('');
          setRPass('');
          const newAccount = { username, password};
    
          try {
              const response = await axios.post('http://localhost:8080/api/bills', newAccount);
              alert('Account created successfully!');
              console.log(response.data);
          } catch (error) {
              console.error('There was an error creating the account!', error);
          }
        }
        
    };
    
    return (
        <>
        <Grid container justifyContent="center">
        <Box  sx={{minWidth: 300}}>
        <form onSubmit={handleSubmit}>
        <Card variant="outlined">
            <CardContent>
            <Stack spacing={2}>
            {usernameErr ?
                <TextField error 
                    helperText="Invalid username" 
                    onChange={(e) => setUsername(e.target.value)}
                    value={username} label="Username"/>
            :
                <TextField value={username} label="Username" onChange={(e) => setUsername(e.target.value)}/>
            }
            {emailErr ?
                <TextField error 
                    helperText="Invalid email" 
                    onChange={(e) => setEmail(e.target.value)}
                    value={email} label="Email"/>
            :
                <TextField value={email} label="Email" onChange={(e) => setEmail(e.target.value)}/>
            }
            {passwordErr ?
            <TextField error 
                helperText="Passwords must be between 6 and 32 characters" 
                onChange={(e) => setPassword(e.target.value)}
                value={password} type="password" label="Password"/>
            :
            <TextField value={password} type="password" label="Password" onChange={(e) => setPassword(e.target.value)}/>
            }
            {passMatchErr ?
            <TextField error 
                helperText="Passwords do not match" 
                onChange={(e) => setRPass(e.target.value)}
                value={rPass} type="password" label="Repeat password"/>
            :
            <TextField value={rPass} type="password" label="Repeat password" onChange={(e) => setRPass(e.target.value)}/>
            }
            
            </Stack>
            </CardContent>
            <CardActions>
                <Button type="submit" variant="contained" fullWidth size="small">Create Account</Button>
            </CardActions>
        </Card>
        </form>
        </Box>
        </Grid>
        </>
    );
};

export default CreateAccountForm