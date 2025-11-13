import { Button, Container, Typography, Box } from "@mui/material";
import React, { useEffect, useState } from "react";
import {NavBar} from "../components/NavBar.jsx";
import ProductGrid from "../components/ProductGrid.jsx";

import zaraPlaceholder from "../assets/zara_placeholder.jpg";
import bershkaPlaceholder from "../assets/bershka_placeholder.jpg";

export default function MainPage() {
    {/*name, price, size, img, isFavourite, brandName*/}
    const [products, setProducts] = useState([]);

    useEffect(() => {
        const url_to_fetch = new URL('http://localhost:8080/api/clothes/bershka/filter');
        url_to_fetch.searchParams.append('category', 'jeans_w');
        url_to_fetch.searchParams.append('sizeD', '36');
        url_to_fetch.searchParams.append('sizeS', 'M');

        fetch(url_to_fetch, {
            method: 'GET',
        }).then(res => res.json())
            .then(setProducts)
            .catch(err => console.log(err))
    }, []);

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
