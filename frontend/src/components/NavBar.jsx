import { AppBar, Toolbar, Stack, Typography, Button, useTheme,
Divider, Box, Avatar} from "@mui/material";

import { useState } from "react";

import HomeIcon from "@mui/icons-material/Home";
import SearchIcon from "@mui/icons-material/Search";
import StarIcon from '@mui/icons-material/Star';
import FaceIcon from '@mui/icons-material/Face';
import LoginIcon from '@mui/icons-material/Login';
import LogoutIcon from '@mui/icons-material/Logout';

import avatar from "../assets/avatar.jpg";

export const NavBar = () => {
    const theme = useTheme();
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    const handleLogin = () => {
        setIsLoggedIn(!isLoggedIn);
        console.log("Login status changed to: " + isLoggedIn);
    }

    return (
        <AppBar
            position="static"
            sx={{
                backgroundColor: theme.palette.custom.themePink,
            }}
        >
            <Toolbar sx={{ justifyContent: "space-between" }}>
                <Typography
                    variant="h6"
                    component="div"
                    sx={{
                        flexGrow: 1,
                        color: theme.palette.text.primary,
                    }}
                >
                    Look Finder
                </Typography>
                <Stack
                    direction="row"
                    spacing={3}
                    divider={<Divider orientation="vertical" flexItem />}
                    sx = {{
                        justifyContent: "center",
                        alignItems: "center",
                        position: "absolute",
                        left: "50%",
                        transform: "translateX(-50%)",
                        color: theme.palette.text.primary,
                    }}
                >
                    <Button
                        color="inherit"
                        startIcon={
                        <HomeIcon sx = {{
                            transition: "0.5s ease-in-out",
                        }}/>
                        }
                        sx = {{
                            "&:hover .MuiSvgIcon-root": {
                                transform: "scale(1.15)"
                            }
                        }}
                    >
                        Main page
                    </Button>
                    <Button
                        color="inherit"
                        startIcon={
                        <SearchIcon sx = {{
                            transition: "0.5s ease-in-out",
                        }}/>
                        }
                        sx = {{
                            "&:hover .MuiSvgIcon-root": {
                                transform: "rotate(-25deg) scale(1.2)",
                            }
                        }}
                    >
                        Find clothes
                    </Button>
                    <Button
                        color="inherit"
                        startIcon={
                        <Box sx={{ display: "flex", alignItems: "center" }}>
                            <StarIcon sx={{
                                color: "gold",
                                fontSize: 30,
                                transition: "0.5s ease-in-out",
                            }} />
                        </Box>
                        }
                        sx = {{
                            "&:hover .MuiSvgIcon-root": {
                                transform: "rotate(145deg)",
                            }
                        }}
                    >
                        Favourite
                    </Button>
                </Stack>
                <Box sx={{ flexGrow: 1 }} />
                <Box sx={{
                    display: "flex",
                    alignItems: "center",
                    gap: 2,
                    flexDirection: "row"
                }}>
                    <Button
                        color="inherit"
                        onClick={handleLogin}
                        endIcon={
                            isLoggedIn ?
                            <LogoutIcon sx={{transition: "0.5s ease-in-out"}}/> :
                            <LoginIcon sx={{transition: "0.5s ease-in-out"}}/>
                        }
                        sx={{
                            width: "100px",
                            color: theme.palette.text.primary,
                            "&:hover .MuiSvgIcon-root": {
                                transform: "scale(1.1)",
                            }
                        }}
                    >
                        {isLoggedIn ? "Logout" : "Login"} {/*condition ? ifTrue : ifFalse*/}
                    </Button>
                    {isLoggedIn ? (
                        <Avatar alt="UsAvatar" src={avatar}></Avatar>
                    ) : (
                        <Avatar sx={{
                            bgcolor: theme.palette.text.primary,
                        }}>
                            <FaceIcon sx={{
                                color: theme.palette.text.secondary,
                            }}/>
                        </Avatar>
                    )}
                </Box>
            </Toolbar>
        </AppBar>
    )
}