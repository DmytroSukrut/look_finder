import {Paper, Typography, Button, useTheme, Box, IconButton, Popover, Fade, Grow, Grid, Avatar} from "@mui/material";

import 'swiper/css';
import 'swiper/css/pagination';
import 'swiper/css/navigation';
import { Swiper, SwiperSlide } from 'swiper/react';
import {Pagination, Navigation} from 'swiper/modules'

import StarIcon from '@mui/icons-material/Star';
import StarBorderOutlinedIcon from '@mui/icons-material/StarBorderOutlined';
import {useState} from "react";
import * as React from "react";

export default function ProductCard ({ id, name, price, size, img_display, img_all,  isFavourite, brandName}){
    const theme = useTheme();
    const [isFav, setIsFav] = useState(isFavourite);

    const [anchorEl, setAnchorEl] = React.useState(null);

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    const open = Boolean(anchorEl);
    console.log(img_display);

    return (
        <Paper
            elevation={4}
            sx={{
                p: 2,
                textAlign: "center",
                display: "flex",
                flexDirection: "column",
                borderRadius: 5,
                transition: "0.5s ease-in-out",
                height: "100%",
                backgroundColor: theme.palette.primary.main,
                "&:hover": {
                    transform: "scale(1.01)",
                    boxShadow: 8,
                },
            }}
        >
            <Box sx={{position: "relative"}}>
                <Box
                    component="img"
                    src={img_display}
                    alt={name}
                    sx={{
                        width: "auto",
                        height: "auto",
                        maxWidth: "calc(100% - 32px)",
                        maxHeight: {xs: 250, sm: 300, md: 350},
                        objectFit: "contain",
                        margin: "0 auto",
                        borderRadius: 2,
                    }}
                />

                <IconButton sx={{
                    position: "absolute",
                    top: 8,
                    right: 8,
                    backgroundColor: "transparent",
                }}
                            onClick={() => console.log("Favourite button clicked!")}
                >
                    <StarBorderOutlinedIcon sx={{color: "gold", fontSize: 30}}/>
                </IconButton>
            </Box>

            <Box sx={{ flexGrow: 1 }} />

            <Box sx={{
                display: "flex",
                flexDirection: "row",
                mt: 2,
                color: theme.palette.text.primary,
            }}>
                <Typography variant="h6" sx={{
                    maxWidth: 200,
                    textAlign: "left",
                }}>
                    {name}
                </Typography>
                <Box sx={{flexGrow: 1}}/>
                <Typography variant="h6">
                    {size}
                </Typography>
            </Box>
            <Box sx={{ flexGrow: 1 }} />
            <Box sx={{
                display: "flex",
                flexDirection: "row",
                alignItems: "center",
                mt: 2,
                color: theme.palette.text.primary,
            }}>
                <Typography variant="body2" sx={{mt: 1}}>
                    {typeof price === "number" ? `${price} EUR` : price} {/*If only a number is passed, add EUR*/}
                </Typography>
                <Box sx={{flexGrow: 1}}/>
                <Box
                    component="img"
                    src={new URL(`../assets/brands/${brandName}.png`, import.meta.url).href}
                    alt={brandName}
                    sx={{
                        width: "100%",
                        height: "auto",
                        maxWidth: 50,
                        maxHeight: 25,
                        objectFit: "contain",
                        margin: "0 auto",
                    }}
                />
            </Box>
            <Box sx={{ flexGrow: 1 }} />
            <Button variant="contained"
                    onClick={handleClick}
                    sx={{
                        color: theme.palette.custom.themeBlue,
                        width: "100%",
                        mt: 2
                    }}>
                See more information
            </Button>



            <Popover
                keepMounted
                open={open}
                onClose={handleClose}
                anchorReference="anchorPosition"
                anchorPosition={{
                    top: window.innerHeight / 2,
                    left: window.innerWidth / 2,
                }}
                anchorOrigin={{vertical: "center", horizontal: "center"}}
                transformOrigin={{vertical: "center", horizontal: "center"}}
                slots={{transition: Grow}}
                transitionDuration={200}
                slotProps={{
                    paper: {
                        sx: {
                            backgroundColor: "transparent",
                            boxShadow: "none",
                            overflow: "visible",
                            maxHeight: "none",
                            maxWidth: "none",
                        },
                    },
                }}
            >
                <Paper elevation={8} sx={{
                    p: 2,
                    m: 2,
                    height: window.innerHeight * 0.8,
                    width: window.innerWidth * 0.7,
                    overflowY: "auto",
                    textAlign: "center",
                    display: "flex",
                    flexDirection: "column",
                    zIndex: 200,
                    borderRadius: 2,
                    backgroundColor: theme.palette.primary.main,
                }}>
                    <Grid container spacing={2} sx={{height: "100%"}}>
                        <Grid size={{xs: 12, md: 6}} sx={{
                            height: "100%",
                            overflow: "hidden",
                            borderRadius: 2,
                            // border: "1px solid",
                            // borderColor: theme.palette.custom.themeBlue_,
                            // backgroundColor: theme.palette.custom.themeBlue_,
                            backdropFilter: 'blur(1px)',
                            WebkitBackdropFilter: 'blur(1px)',
                            "& .swiper-pagination-fraction": {
                                color: theme.palette.custom.themePink,
                            },
                            "& .swiper-button-next, & .swiper-button-prev": {
                                color: theme.palette.custom.themePink,
                            }
                        }}>
                            <Swiper
                                loop={true}
                                key={open ? "open" : "closed"}
                                pagination={{type: 'fraction'}}
                                navigation
                                modules={[Pagination, Navigation]}
                                className="swiper-container"
                                style={{width: "100%", height: "100%"}}
                            >
                                {img_all.map((url, index) => (
                                    <SwiperSlide key={index} style={{borderRadius: 2}}>
                                        <Box
                                            component="img"
                                            src={url}
                                            alt={`slide-${index}`}
                                            sx={{
                                                width: "auto",
                                                height: "auto",
                                                maxHeight: "100%",
                                                maxWidth: "100%",
                                                objectFit: "contain",
                                                margin: "0 auto",
                                                borderRadius: 2,
                                                display: "block",
                                            }}
                                        />
                                    </SwiperSlide>
                                ))}
                            </Swiper>
                        </Grid>
                        <Grid size={{xs: 12, md: 6}} direction={"column"} sx={{height: "100%"}}>
                            <Box sx={{
                                display: "flex",
                                flexDirection: "row",
                                alignItems: "center",
                                color: theme.palette.text.primary,
                                borderRadius: 2,
                                border: "1px solid",
                                borderColor: theme.palette.custom.themeBlue_,
                                backgroundColor: theme.palette.custom.themeBlue_,
                                backdropFilter: 'blur(1px)',
                                WebkitBackdropFilter: 'blur(1px)',
                                transition: "all 0.3s ease-in-out",
                            }}>
                                <Typography variant="h5" sx={{
                                    maxWidth: 350,
                                    textAlign: "left",
                                    fontWeight: 500,
                                    px: 1,
                                }}>
                                    {name}
                                </Typography>
                                <Box sx={{flexGrow: 1}}/>
                                <Box
                                    component="img"
                                    src={new URL(`../assets/brands/${brandName}.png`, import.meta.url).href}
                                    alt={brandName}
                                    sx={{
                                        width: "100%",
                                        height: "auto",
                                        maxWidth: 100,
                                        maxHeight: 50,
                                        objectFit: "contain",
                                        mx: 5,
                                    }}
                                />
                            </Box>
                            <Box sx={{
                                display: "flex",
                                flexDirection: "row",
                                alignItems: "center",
                                my: 2,
                                color: theme.palette.custom.themePink,
                            }}>
                                <Typography variant="h5" sx={{mt: 1, fontWeight: 500, px: 1}}>
                                    {typeof price === "number" ? `${price} EUR` : price} {/*If only a number is passed, add EUR*/}
                                </Typography>
                                <Box sx={{flexGrow: 1}}/>
                            </Box>
                            <Box sx={{
                                display: "flex",
                                flexDirection: "row",
                                alignItems: "center",
                                mt: 2,
                                borderRadius: 2,
                                border: "1px solid",
                                borderColor: theme.palette.custom.themeBlue_,
                                backgroundColor: theme.palette.custom.themeBlue_,
                                backdropFilter: 'blur(1px)',
                                WebkitBackdropFilter: 'blur(1px)',
                                transition: "all 0.3s ease-in-out",
                            }}>
                                <Box sx={{
                                    display: "flex",
                                    flexDirection: "column",
                                    alignItems: "flex-start",
                                    justifyContent: "flex-start",
                                    px: 1,
                                }}>
                                    <Typography variant="h5" sx={{mt: 1, fontWeight: 400}}>
                                        Search parameters:
                                    </Typography>
                                    <Typography variant="h6" sx={{ mt: 1, fontWeight: 300 }}>
                                        Bust:{" "}
                                        <Box component="span" sx={{ fontWeight: 400 }}>
                                            91
                                        </Box>
                                    </Typography>
                                    <Typography variant="h6" sx={{ mt: 1, fontWeight: 300 }}>
                                        Waist: 69
                                    </Typography>
                                    <Typography variant="h6" sx={{ mt: 1, fontWeight: 300 }}>
                                        Hip: 97
                                    </Typography>
                                </Box>
                                <Box sx={{flexGrow: 1}}/>
                                <Avatar sx={{
                                    width: "60px",
                                    height: "60px",
                                    alignItems: "center",
                                    justifyContent: "center",
                                    mx: 7.5,
                                    fontWeight: 500,
                                    fontSize: 24,
                                    backgroundColor: "transparent",
                                    color: theme.palette.text.primary,
                                    border: "1px solid",
                                }}>
                                    {size}
                                </Avatar>
                            </Box>
                        </Grid>
                    </Grid>
                </Paper>
            </Popover>

        </Paper>
    );
}
