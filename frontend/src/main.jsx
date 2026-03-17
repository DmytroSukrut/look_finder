import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { ThemeProvider, CssBaseline } from '@mui/material'
import {BrowserRouter, Routes, Route, Navigate} from "react-router";
import './index.css'
import MainPage from './pages/MainPage.jsx'
import FindPage from './pages/FindPage.jsx'
import FavoritePage from './pages/FavoritePage.jsx'
import UserOperationsPage from './pages/UserOperationsPage.jsx'
import {darkTheme, lightTheme} from './theme/theme.js'

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <ThemeProvider theme={lightTheme}>
            <CssBaseline />
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Navigate to={"/main?page=1"} replace/>}/>
                    <Route path="/main" element={<MainPage />} />
                    <Route path="/find/:cat" element={<FindPage />} />
                    <Route path="/favorites" element={<FavoritePage />} />
                    <Route path="/user_operations" element={<UserOperationsPage />} />
                </Routes>
            </BrowserRouter>
        </ThemeProvider>
    </StrictMode>
)