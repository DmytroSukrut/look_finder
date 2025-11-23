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
            themeBlueLighter: '#98e9f4',
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
                    width: 10,
                    borderRadius: 10,
                    backgroundColor: '#ff97ca',
                },
                "&::-webkit-scrollbar-track": {
                    borderRadius: 10,
                },
                "&::-webkit-scrollbar-thumb": {
                    backgroundColor: '#ed0074',
                    borderRadius: 10,
                    border: "2px solid #fbafde",
                },
            }
        }
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
        }
    }
})