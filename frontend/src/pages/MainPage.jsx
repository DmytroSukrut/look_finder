import { Button, Container, Typography, Box } from "@mui/material";
import {NavBar} from "../components/NavBar.jsx";
import ProductGrid from "../components/ProductGrid.jsx";

import zaraPlaceholder from "../assets/zara_placeholder.jpg";
import bershkaPlaceholder from "../assets/bershka_placeholder.jpg";

export default function MainPage() {
    {/*name, price, size, img, isFavourite, brandName*/}
    const products = [
        {
            id: 1,
            name: "Product 1",
            price: 100,
            size: "S",
            img: zaraPlaceholder,
            isFavourite: false,
            brandName: "zara",
        },
        {
            id: 2,
            name: "Product 2",
            price: "200 EUR",
            size: "M",
            img: bershkaPlaceholder,
            isFavourite: false,
            brandName: "bershka",
        },
        {
            id: 3,
            name: "Product 3",
            price: 150,
            size: "L",
            img: zaraPlaceholder,
            isFavourite: false,
            brandName: "zara",
        },
        {
            id: 4,
            name: "Product 4",
            price: 80,
            size: "M",
            img: bershkaPlaceholder,
            isFavourite: false,
            brandName: "bershka",
        },
        {
            id: 5,
            name: "Product 5",
            price: 120,
            size: "XL",
            img: zaraPlaceholder,
            isFavourite: false,
            brandName: "zara",
        },
        {
            id: 6,
            name: "Product 6",
            price: "90 EUR",
            size: "L",
            img: bershkaPlaceholder,
            isFavourite: false,
            brandName: "bershka",
        },
    ];

    return (
        <Box sx={{
            bgcolor: "background.default",
            minHeight: "100vh",
            display: "flex",
            flexDirection: "column"
        }}>
            <NavBar />
            <ProductGrid
                product={products}
                title={"Special for you"}/>
        </Box>
    );
}
