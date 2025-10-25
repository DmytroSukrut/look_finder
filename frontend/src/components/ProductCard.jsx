import {Paper, Typography, Button, useTheme, Box, IconButton} from "@mui/material";

import StarIcon from '@mui/icons-material/Star';
import StarBorderOutlinedIcon from '@mui/icons-material/StarBorderOutlined';
import {useState} from "react";

export default function ProductCard ({ name, price, size, img, isFavourite, brandName}){
    const theme = useTheme();
    const [isFav, setIsFav] = useState(isFavourite);

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
                    src={img}
                    alt={name}
                    sx = {{
                        width: "100%",
                        height: "auto",
                        maxWidth: { xs: 200, sm: 250, md: 300 },
                        maxHeight: { xs: 250, sm: 300, md: 350 },
                        objectFit: "contain",
                        margin: "0 auto",
                        borderRadius: 5,
                    }}
                />

                <IconButton sx={{
                    position: "absolute",
                    top: 8,
                    right: 8,
                    backgroundColor: "transparent",
                }}
                            onclick = {() => console.log("Favourite button clicked!")}
                >
                    <StarBorderOutlinedIcon sx={{ color: "gold", fontSize: 30}} />
                </IconButton>
            </Box>
            <Box sx={{
                display: "flex",
                flexDirection: "row",
                mt: 2,
                color: theme.palette.text.primary,
            }}>
                <Typography variant="h6">
                    {name}
                </Typography>
                <Box sx={{ flexGrow: 1 }} />
                <Typography variant="h6">
                    {size}
                </Typography>
            </Box>
            <Box sx={{
                display: "flex",
                flexDirection: "row",
                alignItems: "center",
                mt: 2,
                color: theme.palette.text.primary,
            }}>
                <Typography variant="body2" sx={{ mt: 1 }}>
                    {typeof price === "number" ? `${price} EUR` : price}    {/*If only a number is passed, add EUR*/}
                </Typography>
                <Box sx={{ flexGrow: 1 }} />
                <Box
                    component="img"
                    src={new URL(`../assets/brands/${brandName}.png`, import.meta.url).href}
                    alt={brandName}
                    sx = {{
                        width: "100%",
                        height: "auto",
                        maxWidth: 50,
                        maxHeight: 25,
                        objectFit: "contain",
                        margin: "0 auto",
                    }}
                />

            </Box>
            <Button variant="contained" sx={{color: theme.palette.custom.themeBlue, width: "100%", mt: 2}}>
                See more information
            </Button>
        </Paper>
    )
}
