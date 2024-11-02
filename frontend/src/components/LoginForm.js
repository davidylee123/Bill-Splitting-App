import React, { useState } from 'react';
import axios from 'axios';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid2';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Stack from '@mui/material/Stack';


const LoginForm = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [usernameErr, setUsernameErr] = useState(false);
    const [passwordErr, setPasswordErr] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        //do some client-side validation
        if(username.length<2){
            setUsernameErr(true);
        }else{
            setUsernameErr(false);
        }
        if(password.length < 6 || password.length > 32){
            setPasswordErr(true);
        }else{
            setPasswordErr(false);
        }

        if(!usernameErr && !passwordErr){
          
          setUsername('');
          setPassword('');

          // TODO: make sure this checks and redirects if valid credentials
          const loginAccount = { username, password};
    
          try {
              const response = await axios.post('http://localhost:8080/api/bills', loginAccount);
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
            {passwordErr ?
            <TextField error 
                helperText="Passwords must be between 6 and 32 characters" 
                onChange={(e) => setPassword(e.target.value)}
                value={password} type="password" label="Password"/>
            :
            <TextField value={password} type="password" label="Password" onChange={(e) => setPassword(e.target.value)}/>
            }
            </Stack>
            </CardContent>
            <CardActions>
                <Button type="submit" variant="contained" fullWidth size="small">submit</Button>
            </CardActions>
        </Card>
        </form>
        </Box>
        </Grid>
        </>
    );
};

export default LoginForm