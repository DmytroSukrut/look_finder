import {Box, Typography} from "@mui/material";
import React, {useEffect, useState} from "react";
import {NavBar} from "../components/NavBar.jsx";
import ProductGrid from "../components/ProductGrid.jsx";

export default function MainPage() {

    const [products, setProducts] = useState([]);

    useEffect(() => {

        async function load() {

            const user_data = JSON.parse(localStorage.getItem("user"));
            console.log(user_data);

            const data = await fetch_similar(user_data.id);

            if (data.error){
                console.log(data.error)
                return
            }
            setProducts(data)
        }

        load();

    }, []);


    async function fetch_similar(id) {
        const url_to_fetch =
            `/api/clothes/similar?id=${id}`;

        try {
            const controller = new AbortController();
            const timeout = setTimeout(() => controller.abort(), 5000);

            const response = await fetch(url_to_fetch, {
                signal: controller.signal,
            });

            clearTimeout(timeout);

            if (!response.ok) {
                throw new Error("Error fetching similar: " + response.status);
            }

            return await response.json();
        } catch (error) {
            console.log(error);
            return null;
        }
    }

    return (
        <Box sx={{
            bgcolor: "background.default",
            minHeight: "100vh",
            display: "flex",
            flexDirection: "column"
        }}>
            <NavBar />
            <ProductGrid
                products={products}
                title={"Special for you"}
            />
        </Box>
    );
}
