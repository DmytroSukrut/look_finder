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

    const [expanded, setExpanded] = useState(false);

    const accordionStyleNotExpended = {
        bgcolor: theme.palette.custom.themeBlue_,
        backdropFilter: 'blur(1px)',
        WebkitBackdropFilter: 'blur(1px)',
        transition: "all 0.3s ease-in-out",
    }

    const accordionStyleExpended = {
        bgcolor: theme.palette.custom.themeBlue_,
        backdropFilter: 'blur(10px)',
        WebkitBackdropFilter: 'blur(10px)',
        transition: "all 0.3s ease-in-out",
    }

    const [clothesType, setClothesType] = useState(data.clothesType)
    const [sex, setSex] = useState(data.sex)
    const [bust, setBust] = useState(data.bust)
    const [waist, setWaist] = useState(data.waist)
    const [hip, setHip] = useState(data.hip)

    const renderClothes= () => {
        if (sex === "f"){
            return (
                <RadioGroup defaultValue="jeans" onChange={(e) => setClothesType(e.target.value)} >
                    <FormControlLabel value="jeans" control={<Radio sx={radioStyle}/>} label="JEANS" />
                    <FormControlLabel value="trousers" control={<Radio sx={radioStyle}/>} label="TROUSERS" />
                    <FormControlLabel value="skirts" control={<Radio sx={radioStyle}/>} label="SKIRTS" />
                    <FormControlLabel value="shorts_and_bermudas" control={<Radio sx={radioStyle}/>} label="SHORTS" />
                    <FormControlLabel value="t_shirts" control={<Radio sx={radioStyle}/>} label="T-SHIRTS" />
                    <FormControlLabel value="tops_and_bodysuits" control={<Radio sx={radioStyle}/>} label="TOPS" />
                    <FormControlLabel value="shirts_and_blouses" control={<Radio sx={radioStyle}/>} label="SHIRTS" />
                    <FormControlLabel value="dresses_and_jumpsuits" control={<Radio sx={radioStyle}/>} label="DRESSES" />
                    <FormControlLabel value="jackets+coats" control={<Radio sx={radioStyle}/>} label="JACKETS & COATS2" />
                    <FormControlLabel value="sweaters_and_cardigans+knitwear" control={<Radio sx={radioStyle}/>} label="SWEATERS & CARDIGANS & KNITWEAR" />
                    <FormControlLabel value="sweatshirts_and_hoodies" control={<Radio sx={radioStyle}/>} label="SWEATSHIRTS & HOODIES" />
                    <FormControlLabel value="swimwear" control={<Radio sx={radioStyle}/>} label="SWIMWEAR" />
                </RadioGroup>
            )
        } else {
            return (
                <RadioGroup defaultValue="jeans" onChange={(e) => setClothesType(e.target.value)} >
                    <FormControlLabel value="jeans" control={<Radio sx={radioStyle}/>} label="JEANS" />
                    <FormControlLabel value="jackets" control={<Radio sx={radioStyle}/>} label="JACKETS" />
                </RadioGroup>
            )
        }
    }

    return (
        <Box sx={{
            transform: trigger ? "translateY(-64px)" : "translateY(0)",
            transition: "all 0.3s",
            position: 'fixed',
            top: 64,
            boxShadow: theme.shadows[5],
            backgroundColor: 'transparent',
            zIndex: 10,
            width: {
                xs: "100%",
                sm: '50%',
                md: '30%',
                lg: '20%',
            }
        }}>
            <Accordion
                expanded={expanded}
                onChange={() => setExpanded(!expanded)}
                sx={expanded ? accordionStyleExpended : accordionStyleNotExpended}
            >
                <AccordionSummary expandIcon={<ArrowDropDownIcon sx={{ color: theme.palette.text.primary }}/>} sx={{
                    bgcolor: 'transparent',
                }}>
                    <Typography variant="h6" sx={{
                        textAlign: "left",
                    }}>
                        FILTERS
                    </Typography>
                </AccordionSummary>
                <AccordionDetails sx={{
                    bgcolor: 'transparent',
                    height: trigger ? "calc(100vh - 74px)" : "calc(100vh - 64px - 74px)",
                    transition: "all 0.3s ease-in-out",
                    position: 'relative',
                    display: 'flex',
                    flexDirection: 'column',
                }}>
                    <Box sx={{
                        overflowY: "auto",
                        bgcolor: theme.palette.custom.themeBlueLighter_,
                        backdropFilter: 'blur(10px)',
                        WebkitBackdropFilter: 'blur(10px)',
                        border: `2px solid ${theme.palette.text.secondary}`,
                        borderRadius: 1,
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
                            {renderClothes()}
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

                        {/*/!*SIZE DIGITS*!/*/}
                        {/*<FormControl sx={{*/}
                        {/*    display: "block",*/}
                        {/*    width: "100%",*/}
                        {/*    p: 1,*/}
                        {/*}}>*/}
                        {/*    <FormLabel sx={{*/}
                        {/*        color: theme.palette.text.primary + "!important",*/}
                        {/*        fontSize: 28,*/}
                        {/*        fontWeight: 500,*/}
                        {/*    }}>*/}
                        {/*        Size in digits*/}
                        {/*    </FormLabel>*/}
                        {/*    <Divider variant="fullWidth" sx={{*/}
                        {/*        borderBottomWidth: 3,*/}
                        {/*        borderColor: theme.palette.text.primary,*/}
                        {/*    }}/>*/}
                        {/*    <RadioGroup*/}
                        {/*        defaultValue="36"*/}
                        {/*        onChange={(e) => setSizeD(e.target.value)}*/}
                        {/*    >*/}
                        {/*        <FormControlLabel*/}
                        {/*            value="36"*/}
                        {/*            control={<Radio sx={radioStyle}/>}*/}
                        {/*            label="36"*/}
                        {/*        />*/}
                        {/*        <FormControlLabel*/}
                        {/*            value="38"*/}
                        {/*            control={<Radio sx={radioStyle}/>}*/}
                        {/*            label="38"*/}
                        {/*        />*/}
                        {/*        <FormControlLabel*/}
                        {/*            value="40"*/}
                        {/*            control={<Radio sx={radioStyle}/>}*/}
                        {/*            label="40"*/}
                        {/*        />*/}
                        {/*    </RadioGroup>*/}
                        {/*</FormControl>*/}

                        {/*/!*SIZE STRING*!/*/}
                        {/*<FormControl sx={{*/}
                        {/*    display: "block",*/}
                        {/*    width: "100%",*/}
                        {/*    p: 1,*/}
                        {/*}}>*/}
                        {/*    <FormLabel sx={{*/}
                        {/*        color: theme.palette.text.primary + "!important",*/}
                        {/*        fontSize: 28,*/}
                        {/*        fontWeight: 500,*/}
                        {/*    }}>*/}
                        {/*        Size in letters*/}
                        {/*    </FormLabel>*/}
                        {/*    <Divider variant="fullWidth" sx={{*/}
                        {/*        borderBottomWidth: 3,*/}
                        {/*        borderColor: theme.palette.text.primary,*/}
                        {/*    }}/>*/}
                        {/*    <RadioGroup*/}
                        {/*        defaultValue="XS"*/}
                        {/*        onChange={(e) => setSizeS(e.target.value)}*/}
                        {/*    >*/}
                        {/*        <FormControlLabel*/}
                        {/*            value="XS"*/}
                        {/*            control={<Radio sx={radioStyle}/>}*/}
                        {/*            label="XS"*/}
                        {/*        />*/}
                        {/*        <FormControlLabel*/}
                        {/*            value="S"*/}
                        {/*            control={<Radio sx={radioStyle}/>}*/}
                        {/*            label="S"*/}
                        {/*        />*/}
                        {/*        <FormControlLabel*/}
                        {/*            value="M"*/}
                        {/*            control={<Radio sx={radioStyle}/>}*/}
                        {/*            label="M"*/}
                        {/*        />*/}
                        {/*        <FormControlLabel*/}
                        {/*            value="L"*/}
                        {/*            control={<Radio sx={radioStyle}/>}*/}
                        {/*            label="L"*/}
                        {/*        />*/}
                        {/*        <FormControlLabel*/}
                        {/*            value="XL"*/}
                        {/*            control={<Radio sx={radioStyle}/>}*/}
                        {/*            label="XL"*/}
                        {/*        />*/}
                        {/*    </RadioGroup>*/}
                        {/*</FormControl>*/}
                    </Box>
                    <Box sx={{
                        left: 5,
                        bottom: 5,
                    }}>
                        <Button variant="outlined" sx={{
                            width: "100%",
                            color: theme.palette.text.primary,
                            borderColor: theme.palette.text.primary,
                            borderWidth: 3,
                        }}
                                onClick={() => {
                                    setData({
                                        clothesType: clothesType,
                                        sex: sex,
                                        bust: bust,
                                        waist: waist,
                                        hip: hip,
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