import {
    Accordion, AccordionSummary, AccordionDetails, Checkbox, Button, Typography, Box, Divider,
    useTheme, useScrollTrigger, Slide, Radio, RadioGroup, FormControl, FormLabel, FormControlLabel,

} from "@mui/material";

import ArrowDropDownIcon from '@mui/icons-material/ArrowDropDown';
import {useState} from "react";

export const FindAccordion = ({data, setData}) => {
    const theme = useTheme()
    const trigger = useScrollTrigger();

    const radioStyle = {
        color: theme.palette.text.primary,
        "&.Mui-checked": {
            color: theme.palette.custom.themePink,
        }
    }

    const [clothesType, setClothesType] = useState("jeans")
    const [sex, setSex] = useState("f")
    const [sizeD, setSizeD] = useState("38")
    const [sizeS, setSizeS] = useState("L")

    return (
        <Box sx={{
            transform: trigger ? "translateY(-64px)" : "translateY(0)",
            transition: "all 0.3s",
            width: '20%',
            position: 'fixed',
            top: 64,
            boxShadow: theme.shadows[5],
            zIndex: 10
        }}>
            <Accordion>
                <AccordionSummary expandIcon={<ArrowDropDownIcon sx={{ color: theme.palette.text.primary }}/>} sx={{
                    bgcolor: theme.palette.custom.themeBlue,
                }}>
                    <Typography variant="h6" sx={{
                        textAlign: "left",
                    }}>
                        FILTERS
                    </Typography>
                </AccordionSummary>
                <AccordionDetails sx={{
                    bgcolor: theme.palette.custom.themeBlue,
                    height: trigger ? "calc(100vh - 74px)" : "calc(100vh - 64px - 74px)",
                    transition: "all 0.3s ease-in-out",
                    position: 'relative',
                    display: 'flex',
                    flexDirection: 'column',
                }}>
                    <Box sx={{
                        overflowY: "auto",
                        bgcolor: theme.palette.custom.themeBlueLighter,
                        borderRadius: 1,
                        border: `1px solid ${theme.palette.text.secondary}`,
                        mb: 2,
                        flexGrow: 1,
                    }}>
                        {/*CLOTHES TYPE*/}
                        <FormControl sx={{
                            display: "block",
                            width: "100%",
                            p: 1,
                        }}>
                            <FormLabel sx={{
                                color: theme.palette.text.primary + "!important",
                                fontSize: 28,
                                fontWeight: 500,
                            }}>
                                Clothes type
                            </FormLabel>
                            <Divider variant="fullWidth" sx={{
                                borderBottomWidth: 3,
                                borderColor: theme.palette.text.primary,
                            }}/>
                            <RadioGroup
                                defaultValue="jeans"
                                onChange={(e) => setClothesType(e.target.value)}
                            >
                                <FormControlLabel
                                    value="jeans"
                                    control={<Radio sx={radioStyle}/>}
                                    label="JEANS"
                                />
                                <FormControlLabel
                                    value="jackets"
                                    control={<Radio sx={radioStyle}/>}
                                    label="JACKETS"
                                />
                            </RadioGroup>
                        </FormControl>

                        {/*MALE OR FEMALE*/}
                        <FormControl sx={{
                            display: "block",
                            width: "100%",
                            p: 1,
                        }}>
                            <FormLabel sx={{
                                color: theme.palette.text.primary + "!important",
                                fontSize: 28,
                                fontWeight: 500,
                            }}>
                                Sex
                            </FormLabel>
                            <Divider variant="fullWidth" sx={{
                                borderBottomWidth: 3,
                                borderColor: theme.palette.text.primary,
                            }}/>
                            <RadioGroup
                                defaultValue="f"
                                onChange={(e) => setSex(e.target.value)}
                            >
                                <FormControlLabel
                                    value="f"
                                    control={<Radio sx={radioStyle}/>}
                                    label="FEMALE"
                                />
                                <FormControlLabel
                                    value="m"
                                    control={<Radio sx={radioStyle}/>}
                                    label="MALE"
                                />
                            </RadioGroup>
                        </FormControl>

                        {/*SIZE DIGITS*/}
                        <FormControl sx={{
                            display: "block",
                            width: "100%",
                            p: 1,
                        }}>
                            <FormLabel sx={{
                                color: theme.palette.text.primary + "!important",
                                fontSize: 28,
                                fontWeight: 500,
                            }}>
                                Size in digits
                            </FormLabel>
                            <Divider variant="fullWidth" sx={{
                                borderBottomWidth: 3,
                                borderColor: theme.palette.text.primary,
                            }}/>
                            <RadioGroup
                                defaultValue="36"
                                onChange={(e) => setSizeD(e.target.value)}
                            >
                                <FormControlLabel
                                    value="36"
                                    control={<Radio sx={radioStyle}/>}
                                    label="36"
                                />
                                <FormControlLabel
                                    value="38"
                                    control={<Radio sx={radioStyle}/>}
                                    label="38"
                                />
                                <FormControlLabel
                                    value="40"
                                    control={<Radio sx={radioStyle}/>}
                                    label="40"
                                />
                            </RadioGroup>
                        </FormControl>

                        {/*SIZE STRING*/}
                        <FormControl sx={{
                            display: "block",
                            width: "100%",
                            p: 1,
                        }}>
                            <FormLabel sx={{
                                color: theme.palette.text.primary + "!important",
                                fontSize: 28,
                                fontWeight: 500,
                            }}>
                                Size in letters
                            </FormLabel>
                            <Divider variant="fullWidth" sx={{
                                borderBottomWidth: 3,
                                borderColor: theme.palette.text.primary,
                            }}/>
                            <RadioGroup
                                defaultValue="XS"
                                onChange={(e) => setSizeS(e.target.value)}
                            >
                                <FormControlLabel
                                    value="XS"
                                    control={<Radio sx={radioStyle}/>}
                                    label="XS"
                                />
                                <FormControlLabel
                                    value="S"
                                    control={<Radio sx={radioStyle}/>}
                                    label="S"
                                />
                                <FormControlLabel
                                    value="M"
                                    control={<Radio sx={radioStyle}/>}
                                    label="M"
                                />
                                <FormControlLabel
                                    value="L"
                                    control={<Radio sx={radioStyle}/>}
                                    label="L"
                                />
                                <FormControlLabel
                                    value="XL"
                                    control={<Radio sx={radioStyle}/>}
                                    label="XL"
                                />
                            </RadioGroup>
                        </FormControl>
                    </Box>
                    <Box sx={{
                        left: 5,
                        bottom: 5,
                    }}>
                        <Button variant="contained"
                                onClick={() => {
                                    setData({
                                        clothesType: clothesType,
                                        sizeD: sizeD,
                                        sizeS: sizeS,
                                        sex: sex,
                                    })
                                }}
                        >
                            FIND
                        </Button>
                    </Box>
                </AccordionDetails>
            </Accordion>
        </Box>
    );
}