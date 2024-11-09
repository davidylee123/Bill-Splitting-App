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
import {
  GridRowModes,
  DataGrid,
  GridToolbarContainer,
  GridActionsCellItem,
  GridRowEditStopReasons,
} from '@mui/x-data-grid';

import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import MuiAppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Divider from '@mui/material/Divider';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import Stack from '@mui/material/Stack';
import { styled, useTheme } from '@mui/material/styles';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import FormLabel from '@mui/material/FormLabel';

import Fab from '@mui/material/Fab';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import FormGroup from '@mui/material/FormGroup';
import InputAdornment from '@mui/material/InputAdornment';
import Typography from '@mui/material/Typography';

import { useLocation } from 'react-router-dom';
import AddIcon from '@mui/icons-material/Add';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import PersonOutlineOutlinedIcon from '@mui/icons-material/PersonOutlineOutlined';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import CloseIcon from '@mui/icons-material/Close';
import SaveIcon from '@mui/icons-material/Save';
import CancelIcon from '@mui/icons-material/Close';
import ErrorIcon from '@mui/icons-material/Error';

import { red } from '@mui/material/colors';

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

// const columns = [
//     { id: 'title', label: 'Title', minWidth: 50, align: 'left' },
//     { id: 'user', label: 'Paid By', minWidth: 50, align: 'center'},
//     { id: 'friends', label: 'Shared Between', minWidth: 50, align: 'center'},
//     { id: 'amount', label: 'Amount', minWidth: 50, align: 'center'},
//     { id: 'id', label: 'Actions', minWidth: 50, align: 'right' }
//   ];





  
  
  
  
  const initialRows = [
    {
      title: 'milk',
      user: 'Andrew',
      friends: [' Andrew', ' Adriana'],
      split: '50/50',
      amount: 2.55,
      id: -1,
    },
  ];




const ExpenseList = () => {
  let location = String(useLocation().pathname).substring(1);

  //for form drawer
  const [open, setOpen] = React.useState(false);
  const handleDrawerOpen = () => {
    setOpen(true);
  };

  const handleDrawerClose = () => {
    setOpen(false);
    setTitleErr(false);
    setFriendsErr(false);
    setPaidErr(false);
    setAmountErr(false);
  };


    const [Expenses, setExpenses] = useState(initialRows);
    const [title, setTitle] = useState('');
    const [amount, setAmount] = useState(null);
    const [userPaid, setUserPaid] = useState('');
    const [friends, setFriends] = useState([{name: 'Andrew', included: false, split: 50}, {name: 'Adriana', included: false, split: 50}]);
    const [Users, setUsers] = useState(['Andrew','Adriana']);
    const [currID, setCurrID] = useState(0);
    const [titleErr, setTitleErr] = useState(false);
    const [friendsErr, setFriendsErr] = useState(false);
    const [paidErr, setPaidErr] = useState(false);
    const [amountErr, setAmountErr] = useState(false);
    const [editErr, setEditErr] = useState(false);


    const [rowModesModel, setRowModesModel] = React.useState({});
  
    const handleRowEditStop = (params, event) => {
      if (params.reason === GridRowEditStopReasons.rowFocusOut) {
        event.defaultMuiPrevented = true;
      }
    };
  
    const handleEditClick = (id) => () => {
      setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.Edit } });
    };
  
    const handleSaveClick = (id) => () => {
      setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.View } });
    };
  
    const handleDeleteClick = (id) => () => {
      setExpenses(Expenses.filter((row) => row.id !== id));
    };
  
    const handleCancelClick = (id) => () => {
      setRowModesModel({
        ...rowModesModel,
        [id]: { mode: GridRowModes.View, ignoreModifications: true },
      });
    };
  
    const processRowUpdate = (newRow) => {
      const updatedRow = { ...newRow, isNew: false };

      if(updatedRow.title.length < 1){
        setEditErr(true);
        handleCancelClick(newRow.id);
        return NaN;
      }

      setExpenses(Expenses.map((row) => (row.id === newRow.id ? updatedRow : row)));
      return updatedRow;
    };
  
    const handleRowModesModelChange = (newRowModesModel) => {
      setRowModesModel(newRowModesModel);
    };

    const columns = [
      { field: 'title', 
        headerName: 'Title', 
        flex: 0.8, 
        align: 'left', 
        headerAlign: 'left',
        editable: true },
      {
        field: 'user',
        headerName: 'Paid By',
        flex: 1,
        type: "singleSelect",
        valueOptions: Users,
        align: 'center',
        headerAlign: 'center',
        editable: true,
      },
      { field: 'friends', 
        headerName: 'Shared Between', 
        flex: 1, 
        align: 'center',
        headerAlign: 'center', 
        editable: false,
      },  
      {
        field: 'split',
        headerName: 'Split',
        flex: 1,
        align: 'center',
        headerAlign: 'center',
        editable: true,
      },
      {
        field: 'amount',
        headerName: 'Amount',
        type: 'number',
        flex: 1,
        align: 'center',
        headerAlign: 'center',
        editable: true,
      },
      {
        field: 'id',
        type: 'actions',
        headerName: 'Actions',
        flex: 0.8,
        align: 'right',
        headerAlign: 'right',
        cellClassName: 'actions',
        getActions: ({ id }) => {
          const isInEditMode = rowModesModel[id]?.mode === GridRowModes.Edit;
  
          if (isInEditMode) {
            return [
              <GridActionsCellItem
                icon={<SaveIcon />}
                label="Save"
                sx={{
                  color: 'primary.main',
                }}
                onClick={handleSaveClick(id)}
              />,
              <GridActionsCellItem
                icon={<CancelIcon />}
                label="Cancel"
                className="textPrimary"
                onClick={handleCancelClick(id)}
                color="inherit"
              />,
            ];
          }
  
          return [
            <GridActionsCellItem
              icon={<EditIcon />}
              label="Edit"
              className="textPrimary"
              onClick={handleEditClick(id)}
              color="inherit"
            />,
            <GridActionsCellItem
              icon={<DeleteIcon />}
              label="Delete"
              onClick={handleDeleteClick(id)}
              color="inherit"
            />,
          ];
        },
      },
    ];
    
    const handleFriendSelect = (n) => {
      friends.map(f => {
        if(f.name === n){
          f.included = !f.included;
        }   
      });

    };

    const handleSplit = (n, s) => {
      friends.map(f => {
        if(f.name === n){
          f.split = s;
        }   
      })
      setFriends(friends);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        let expenseFriends = [];
        let split = "";
        friends.map((f,i) => {
          if(f.included){
            expenseFriends.push(f.name);
            if(i == 0){
              split += f.split;
            }else{
              split += "/" + f.split;
            }
          }

        })

        if(title.length < 1){
          setTitleErr(true);
        }else{
          setTitleErr(false);
        }
        if(expenseFriends.length < 1){
          setFriendsErr(true);
        }else{
          setFriendsErr(false);
        }
        if(userPaid.length < 1){
          setPaidErr(true);
        }else{
          setPaidErr(false);
        }

        //THIS ISN'T WORKING FOR SOME REASON,
        // it detects valid input as being invalid
        let amountFormatted;
        if(!isNaN(amount)){
          amountFormatted = amount.toFixed(2);
        }else{
          amountFormatted = 0.00;
        }
        
        //if(isNaN(amount) || amount < 0 || amount !== amountFormatted){
        //  setAmountErr(true);
        //}else{
          setAmountErr(false);
        //}

        if(!titleErr && !friendsErr && !paidErr && !amountErr){
          setExpenses([...Expenses, {id: currID, title: title, split: split, amount: amountFormatted, user: userPaid, friends: expenseFriends}]);
          setCurrID(currID+1);
          //setAmount(NaN);
          setTitle('');
        }
        
    };

    useEffect(() => {
        const fetchExpenses = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/Expenses');
                setExpenses(response.data);
            } catch (error) {
                console.error('There was an error fetching the Expenses!', error);
            }
        };

        fetchExpenses();
    }, []);

    return (
        <div>

{/* form Drawer */}
<Box sx={{ display: 'flex' }}>
<AppBar position="fixed" open={open}>
        <Toolbar >
            <h2>Bill Splitting App - {location}</h2>
        </Toolbar>
      </AppBar> 

      <Drawer
              anchor='top'
              open={editErr}
              onClose={() => {setEditErr(false)}}
            >
            <Stack spacing={1} direction="row" sx={{ justifyContent: "center", alignItems: "center",}}>
            <Typography variant="h6">Could not save values, please enter a valid title.</Typography> 
            <ErrorIcon color="error"/>
            </Stack>
            </Drawer>

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
            <h2 align="center">Create Expense</h2>
            <IconButton size="small" color="error" variant="outlined" onClick={handleDrawerClose}>
              <CloseIcon />
              </IconButton>
          </Stack>
            <Divider />
            {titleErr ?
              <TextField
                error
                helperText="Please enter a title for the bill."
                value={title}
                label="Title of Expense"
                onChange={(e) => setTitle(e.target.value)}
                margin="normal"
                fullWidth
              />
            :
            <TextField
                value={title}
                label="Title of Expense"
                onChange={(e) => setTitle(e.target.value)}
                margin="normal"
                fullWidth
            />
            }
                
                <br/>
              {amountErr ?
              <TextField
              error
              helperText="Please enter valid amount"
              slotProps={{
                input: {
                  startAdornment: (<InputAdornment position="start">$</InputAdornment>),
                },
              }}
              type="number"
              value={amount}
              label="Expense Amount"
              onChange={(e) => setAmount(e.target.valueAsNumber)}
              margin="normal"
              fullWidth
          />
              :
              <TextField
                    slotProps={{
                      input: {
                        startAdornment: (<InputAdornment position="start">$</InputAdornment>),
                      },
                    }}
                    type="number"
                    value={amount}
                    label="Expense Amount"
                    onChange={(e) => setAmount(e.target.valueAsNumber)}
                    margin="normal"
                    fullWidth
                />
              }
                
                </Stack>
                <br/> 
                <Divider />
                <br /> 
                <FormLabel id="radio-group-label">Paid By:</FormLabel>
                {paidErr ?
                  <RadioGroup
                    aria-labelledby="radio-group-label"
                    style={{marginBottom: 10, marginLeft: 10}}
                  >
                    {Users.map(user => (
                      <FormControlLabel onChange={(e) => setUserPaid(e.target.value)} value={user} control={<Radio sx={{
                        color: red[800],
                      }}/>} label={user} />
                    ))}
                  </RadioGroup>
                :
                  <RadioGroup
                    aria-labelledby="radio-group-label"
                    style={{marginBottom: 10, marginLeft: 10}}
                  >
                    {Users.map(user => (
                      <FormControlLabel onChange={(e) => setUserPaid(e.target.value)} value={user} control={<Radio />} label={user} />
                    ))}
                  </RadioGroup>
                }
                    
                <FormLabel id="checkbox-group-label"> Shared Between: </FormLabel>

                {friendsErr ?
                <FormGroup aria-labelledby='checkbox-group-label'>
                {friends.map((friend) => (
                  <FormControlLabel 
                      value={friend.name}
                      onChange={(e) => handleFriendSelect(friend.name)}
                      control={<Checkbox icon={<PersonOutlineOutlinedIcon color="error"/>}
                      checkedIcon={<PersonAddIcon />}/>} 
                      label={friend.name} />
                  ))}
              </FormGroup>
                :
                <FormGroup aria-labelledby='checkbox-group-label'>
                  {friends.map((friend) => (
                    <Stack direction="row">
                      <FormControlLabel 
                        value={friend.name}
                        onChange={(e) => handleFriendSelect(friend.name)}
                        control={<Checkbox icon={<PersonOutlineOutlinedIcon />}
                        checkedIcon={<PersonAddIcon />}/>} 
                        label={friend.name} />
                    <TextField 
                        type="number"
                        //value={friend.split}
                        onChange={(e) => handleSplit(friend.name, e.target.value)}
                        placeholder="split">
                    </TextField>
                    </Stack>
                    ))}
                </FormGroup>
                }
                
                {/* Add fields to enter expenses here */}

                <Stack spacing={1} direction="row">
                
                <Button fullWidth variant="contained" type="submit">Add Expense <AddIcon /></Button>
                </Stack>
            </form>
      </Drawer>
      <Main open={open}>

{/* Data Grid Table */}
<Box
        sx={{
          width: '100%',
          '& .actions': {
            color: 'text.secondary',
          },
          '& .textPrimary': {
            color: 'text.primary',
          },
        }}
      >
        <div style={{ display: 'flex', flexDirection: 'column' }}>
        <Paper elevation="0" align="center"><h2>Expenses</h2></Paper>
        <DataGrid
          rows={Expenses}
          columns={columns}
          editMode="row"
          rowModesModel={rowModesModel}
          onRowModesModelChange={handleRowModesModelChange}
          onRowEditStop={handleRowEditStop}
          processRowUpdate={processRowUpdate}
        />
        </div>
      </Box>

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
            Create Expense &nbsp;<AddCircleOutlineIcon />
          </Fab>


        </div>
    );

};

export default ExpenseList;