import { AppBar, Toolbar, Stack, Typography, Button, useTheme,
Divider, Box, Avatar, useScrollTrigger, Slide} from "@mui/material";
import { Link } from "react-router"

import {useEffect, useState} from "react";
import { useNavigate } from "react-router";

import HomeIcon from "@mui/icons-material/Home";
import SearchIcon from "@mui/icons-material/Search";
import StarIcon from '@mui/icons-material/Star';
import FaceIcon from '@mui/icons-material/Face';
import LoginIcon from '@mui/icons-material/Login';
import LogoutIcon from '@mui/icons-material/Logout';

import avatar from "../assets/avatar.jpg";

export const NavBar = () => {
    const theme = useTheme();
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    const handleLogin = () => {
        navigate("/user_operations");
    }

    const backup_search = {
        cat: "jeans",
        sex: "f",
        bust: "91",
        waist: "69",
        hip: "97",
    };

    const build_link_to_find = () => {
        const url_params = new URLSearchParams();

        const savedSearch = localStorage.getItem("last_search");

        console.log("savedSearch:", savedSearch);
        console.log("localStorage:", localStorage.getItem("last_search"));

        const latestSearch =
            savedSearch === null ? backup_search : JSON.parse(savedSearch);

        url_params.set("sex", latestSearch.sex);
        url_params.set("bust", latestSearch.bust);
        url_params.set("waist", latestSearch.waist);
        url_params.set("hip", latestSearch.hip);
        url_params.set("page", String(1));

        return `/find/${encodeURIComponent(latestSearch.cat)}?${url_params.toString()}`;
    };

    const build_link_to_main = (params) => {
        const url_params = new URLSearchParams();

        url_params.set("page", String(params.page));

        return `/main?${url_params.toString()}`;
    }

    const main_params = {
        page: 1
    }

    return (
        <HideOnScroll>
            <AppBar
                position="sticky"
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
                            component={Link}
                            to={build_link_to_main(main_params)}
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
                            component={Link}
                            to = {build_link_to_find()}
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
                            component={Link}
                            to="/favorites"
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
        </HideOnScroll>
    )
}

function HideOnScroll({ children }) {
    const trigger = useScrollTrigger();

    return (
        <Slide appear={false} direction="down" in={!trigger}>
            {children}
        </Slide>
    );
}