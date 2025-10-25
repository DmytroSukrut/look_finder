import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { ThemeProvider, CssBaseline } from '@mui/material'
import './index.css'
import MainPage from './pages/MainPage.jsx'
import {darkTheme, lightTheme} from './theme/theme.js'

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <ThemeProvider theme={darkTheme}>
            <CssBaseline />
            <MainPage />
        </ThemeProvider>
    </StrictMode>
)