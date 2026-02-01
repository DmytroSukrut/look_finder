import { createTheme } from '@mui/material/styles';

export const lightTheme = createTheme({
    typography: {
        fontFamily: " 'Ubuntu', sans-serif",
    },
    palette: {
        mode: 'light',
        primary: {
            main: '#fffdf8',
        },
        custom: {
            themePink: '#ff97ca',
            themePinkLighter: '#fbafde',
            themeBlue: '#97dfff',
            themeBlueDarker: '#59d2ff',
            themeBlue_: 'rgba(150,221,253, 0.3)',
            themeBlueLighter: '#98e9f4',
            themeBlueLighter_: 'rgb(151,231,242, 0.6)',
        },
        text: {
            primary: '#000000',
            secondary: '#ffffff',
        },
        background: {
            default: '#fffdf8',
        }
    },
    breakpoints: {
        values: {
            xs: 0,
            sm: 490,
            md: 910,
            lg: 1200,
            xl: 1600,
            xxl: 2000,
        }
    },
    components: {
        MuiCssBaseline: {
            styleOverrides: {
                "&::-webkit-scrollbar": {
                    width: 8,
                    borderRadius: 0,
                },
                "&::-webkit-scrollbar-track": {
                    borderRadius: 10,
                },
                "&::-webkit-scrollbar-thumb": {
                    backgroundColor: 'rgba(253,150,201, 0.8)',
                    border: `2px solid rgba(253,150,201, 0.9)`,
                    borderRadius: 10,
                },
            }
        },
        MuiTextField: {
            defaultProps: {
                variant: "outlined",
            },
        },
        MuiOutlinedInput: {
            styleOverrides: {
                root: ({ theme }) => ({
                    color: theme.palette.text.primary,

                    "& .MuiOutlinedInput-notchedOutline": {
                        borderColor: theme.palette.text.primary,
                    },

                    "&:hover .MuiOutlinedInput-notchedOutline": {
                        borderColor: theme.palette.text.primary,
                    },

                    "&.Mui-focused .MuiOutlinedInput-notchedOutline": {
                        borderColor: theme.palette.text.primary,
                    },
                }),
            },
        },
        MuiInputLabel: {
            styleOverrides: {
                root: ({ theme }) => ({
                    color: theme.palette.text.primary,
                    "&.Mui-focused": {
                        color: theme.palette.text.primary,
                    },
                }),
            },
        },
    }
});

export const darkTheme = createTheme({
    typography: {
        fontFamily: " 'Ubuntu', sans-serif",
    },
    palette: {
        mode: 'dark',
        primary: {
            main: '#232323',
        },
        custom: {
            themePink: '#63001b',
            themePinkLight: '#450013',
            themeBlue: '#01017c',
            themeBlueLighter: '#000061',
        },
        text: {
            primary: '#ffffff',
            secondary: '#000000',
        },
        background: {
            default: '#232323',
        }
    },
    breakpoints: {
        values: {
            xs: 0,
            sm: 490,
            md: 910,
            lg: 1200,
            xl: 1600,
            xxl: 2000,
        }
    },
    components: {
        MuiCssBaseline: {
            styleOverrides: {
                "&::-webkit-scrollbar": {
                    width: 10,
                    borderRadius: 10,
                    backgroundColor: '#63001b',
                },
                "&::-webkit-scrollbar-track": {
                    borderRadius: 10,
                },
                "&::-webkit-scrollbar-thumb": {
                    backgroundColor: '#ff487c',
                    borderRadius: 10,
                    border: "2px solid #f86894",
                },
            }
        },
        MuiTextField: {
            defaultProps: {
                variant: "outlined",
            },
        },
        MuiOutlinedInput: {
            styleOverrides: {
                root: ({ theme }) => ({
                    color: theme.palette.text.primary,

                    "& .MuiOutlinedInput-notchedOutline": {
                        borderColor: theme.palette.text.primary,
                    },

                    "&:hover .MuiOutlinedInput-notchedOutline": {
                        borderColor: theme.palette.text.primary,
                    },

                    "&.Mui-focused .MuiOutlinedInput-notchedOutline": {
                        borderColor: theme.palette.text.primary,
                    },
                }),
            },
        },
        MuiInputLabel: {
            styleOverrides: {
                root: ({ theme }) => ({
                    color: theme.palette.text.primary,
                    "&.Mui-focused": {
                        color: theme.palette.text.primary,
                    },
                }),
            },
        },
    }
})