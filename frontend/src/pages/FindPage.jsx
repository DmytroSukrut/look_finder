import {Box, Typography} from "@mui/material"
import {NavBar} from "../components/NavBar.jsx";
import React, { useEffect, useState } from "react";

export default function FindPage(){
    return (
      <Box sx={{
          bgcolor: "background.default",
          minHeight: "100vh",
          display: "flex",
          flexDirection: "column"
      }}>
          <NavBar />
          <Typography sx={{
              pt: 10,
              fontSize: 12,
              alignSelf: 'center'
          }} >
              Finding page here
          </Typography>
      </Box>
    );
}