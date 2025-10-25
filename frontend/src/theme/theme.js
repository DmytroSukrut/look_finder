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
            themeBlue: '#97dfff',
        },
        text: {
            primary: '#000000',
            secondary: '#ffffff',
        },
        background: {
            default: '#fffdf8',
        }
    },
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
            themeBlue: '#01017c',
        },
        text: {
            primary: '#ffffff',
            secondary: '#000000',
        },
        background: {
            default: '#232323',
        }
    }
})