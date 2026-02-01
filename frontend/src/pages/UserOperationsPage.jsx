import {Box, Button, Paper, TextField, Typography, useTheme} from "@mui/material"
import {useState} from "react";
import {useNavigate} from "react-router";

// const ACCOUNT_LIFETIME = 24 * 60 * 60;
const ACCONT_LIFETIME = 1;

export default function UserOperationsPage() {
    const theme = useTheme();
    const nav = useNavigate();

    const [email_login, setEmailLogin] = useState("");
    const [password_login, setPasswordLogin] = useState("");

    const [name_register, setNameRegister] = useState("");
    const [surname_register, setSurnameRegister] = useState("");
    const [email_register, setEmailRegister] = useState("");
    const [password_register, setPasswordRegister] = useState("");

    const handleUserOperations = async (action) => {
        let result = null;
        if (action === "reg") {
            result = await fetch_register(
                name_register,
                surname_register,
                email_register,
                password_register
            );
            console.log(result);
        } else if (action === "log") {
            result = await fetch_login(
                email_login,
                password_login,
            );
            console.log(result);
        } else {
            console.error("Unknown action:", action);
        }

        if (result.error) {
            console.log(`operation ${action} error:`, result.error);
        } else {
            console.log(`operation ${action} success:`, result);

            const save = {
                timestamp: Math.floor(Date.now() / 1000),
                timestampHuman: getCompactDate(),
                id: result.id,
                email: result.email,
                name: result.name,
                surname: result.surname,
            }

            localStorage.setItem("user", JSON.stringify(save));

            nav("/find")
        }
    }

    function getCompactDate() {
        return new Date().toISOString().slice(0, 19).replace(/[:T-]/g, "-");
    }

    async function fetch_register(name, surname, email, password) {
        try {
            const response = await fetch("http://localhost:8080/api/user/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    name: name,
                    surname: surname,
                    email: email,
                    password: password,
                })
            })

            if (!response.ok) {
                const err = await response.json();
                return { error: err };
            }

            return await response.json();
        } catch (err){
            console.error(err)
            return { error: err.message };
        }
    }

    async function fetch_login(email, password) {
        try {
            const response = await fetch("http://localhost:8080/api/user/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email: email,
                    password: password,
                })
            })

            if (!response.ok) {
                const err = await response.json();
                return { error: err };
            }

            return await response.json();
        } catch (err){
            console.error(err)
            return { error: err.message };
        }
    }

    return (
        <Box sx={{
            bgcolor: "background.default",
            minHeight: "100vh",
            display: "flex",
            flexDirection: "column",
            alignItems:"center",
            justifyContent: "center",
        }}>
            <Paper elevation={24} sx={{
                width: "60vw",
                height: "70vh",
                borderRadius: 5,
                display: "flex",
                flexDirection: "row",
            }}>
                <Box
                    sx={{
                        display: "flex",
                        flexDirection: "column",
                        width: "50%",
                        height: "100%",
                        border: "1px solid black",
                        borderRadius: 5,
                    }}
                >
                    {/* TOP */}
                    <Typography
                        variant="h5"
                        sx={{
                            mt: 3,
                            fontWeight: "bold",
                            alignSelf: "center"
                    }}>
                        Welcome back!
                    </Typography>

                    {/* CENTER */}
                    <Box
                        sx={{
                            flex: 1,
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center",
                            flexDirection: "column",
                        }}
                    >
                        <Typography variant="h5" sx={{alignSelf: "center" }}>
                            Sign In
                        </Typography>
                        <Box component={"form"} sx={{
                            display: "flex",
                            flexDirection: "column",
                            width: "100%",
                            alignItems: "flex-start",
                            alignSelf: "center",
                            px: 10,
                            mt: 2,
                        }}>
                            <TextField
                                id="email-login"
                                label="Email adress"
                                size="small"
                                value={email_login}
                                onChange={(e) => setEmailLogin(e.currentTarget.value)}
                                sx={{width: "100%"}}
                            />
                            <TextField
                                id="password-login"
                                label="Password"
                                size="small"
                                value={password_login}
                                onChange={(e) => setPasswordLogin(e.currentTarget.value)}
                                sx={{width: "100%", mt:3}}
                            />
                            <Button
                                variant="outlined"
                                onClick={() => handleUserOperations("log")}
                                sx={{
                                width: "100%",
                                mt: 2,
                                backgroundColor: theme.palette.custom.themeBlue,
                                border: "1px solid",
                                borderColor: theme.palette.custom.themeBlueLighter,
                                borderRadius: 2,
                                color: theme.palette.text.primary,
                                transition: "all 0.3s ease-in-out",
                                "&:hover": {
                                    backgroundColor: theme.palette.custom.themeBlueDarker,
                                    borderColor: theme.palette.custom.themeBlue,
                                    boxShadow: 6,
                                }
                            }}>
                                LOGIN
                            </Button>
                        </Box>
                    </Box>
                </Box>
                <Box sx={{
                    display: "flex",
                    flexDirection: "column",
                    width: "50%",
                    height: "100%",
                    border: "1px solid black",
                    borderRadius: 5,
                }}>
                    {/* TOP */}
                    <Typography
                        variant="h5"
                        sx={{
                            mt: 3,
                            fontWeight: "bold",
                            alignSelf: "center"
                        }}>
                        Join us and find your perfect fit!
                    </Typography>

                    {/* CENTER */}
                    <Box
                        sx={{
                            flex: 1,
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center",
                            flexDirection: "column",
                        }}
                    >
                        <Typography variant="h5" sx={{alignSelf: "center" }}>
                            Sign Up
                        </Typography>
                        <Box component={"form"} sx={{
                            display: "flex",
                            flexDirection: "column",
                            width: "100%",
                            alignItems: "flex-start",
                            alignSelf: "center",
                            px: 10,
                            mt: 2,
                        }}>
                            <Box sx={{
                                display: "flex",
                                flexDirection: "row",
                                alignItems: "flex-start",
                                justifyContent: "space-between",
                            }}>
                                <Box sx={{
                                    display: "flex",
                                    flexDirection: "column",
                                    width: "48%",
                                }}>
                                    <TextField
                                        id="name-register"
                                        label="Name"
                                        size="small"
                                        value={name_register}
                                        onChange={(e) => setNameRegister(e.target.value)}
                                        sx={{width: "100%"}}
                                    />
                                </Box>
                                <Box sx={{flexGrow: 1}}/>
                                <Box sx={{
                                    display: "flex",
                                    flexDirection: "column",
                                    width: "48%",
                                }}>
                                    <TextField
                                        id="surname-register"
                                        label="Surname"
                                        size="small"
                                        value={surname_register}
                                        onChange={(e) => setSurnameRegister(e.target.value)}
                                        sx={{width: "100%"}}
                                    />
                                </Box>
                            </Box>
                            <TextField
                                id="email-register"
                                label="Email adress"
                                size="small"
                                value={email_register}
                                onChange={(e) => setEmailRegister(e.target.value)}
                                sx={{width: "100%", mt: 3,}}
                            />
                            <TextField
                                id="password-register"
                                label="Password"
                                size="small"
                                value={password_register}
                                onChange={(e) => setPasswordRegister(e.target.value)}
                                sx={{width: "100%", mt: 3,}}
                            />
                            <Button
                                variant="outlined"
                                onClick={() => handleUserOperations("log")}
                                sx={{
                                width: "100%",
                                mt: 2,
                                backgroundColor: theme.palette.custom.themeBlue,
                                border: "1px solid",
                                borderColor: theme.palette.custom.themeBlueLighter,
                                borderRadius: 2,
                                color: theme.palette.text.primary,
                                transition: "all 0.3s ease-in-out",
                                "&:hover": {
                                    backgroundColor: theme.palette.custom.themeBlueDarker,
                                    borderColor: theme.palette.custom.themeBlue,
                                    boxShadow: 6,
                                }
                            }}>
                                REGISTER
                            </Button>
                        </Box>
                    </Box>
                </Box>
            </Paper>
        </Box>
    );

 }