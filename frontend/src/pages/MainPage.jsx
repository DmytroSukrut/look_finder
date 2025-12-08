import {Box} from "@mui/material";
import React, {useEffect, useState} from "react";
import {NavBar} from "../components/NavBar.jsx";
import ProductGrid from "../components/ProductGrid.jsx";

const JSON_LIFETIME = 24 * 60 * 60; //Lifetime is one day

export default function MainPage() {

    const [products, setProducts] = useState([]);

    useEffect(() => {
        let random_product_category = "jeans"
        const sex = "f"
        const bust = "91"
        const waist = "69"
        const hip = "97"

        const key = `${random_product_category}_${sex}_${bust}_${waist}_${hip}`

        async function load() {
            const is_saved = JSON.parse(localStorage.getItem(key));

            if (is_saved && (Math.floor(Date.now() / 1000) - is_saved.timestamp < JSON_LIFETIME )) {
                setProducts(is_saved.data)
            } else {
                const data = await fetch_clothes(random_product_category, sex, bust, waist , hip);

                if (data.error && is_saved){
                    setProducts(is_saved.data)
                    return
                } else if (data.error && !is_saved){
                    return
                }

                const save = {
                    timestamp: Math.floor(Date.now() / 1000),
                    timestampHuman: getCompactDate(),
                    data: data
                }

                setProducts(data)
                localStorage.setItem(key, JSON.stringify(save))
            }
        }

        load();

    }, []);

    async function fetch_clothes(category, sex, bust, waist, hip) {
        const url_to_fetch = new URL('http://localhost:8080/api/clothes/filter');
        url_to_fetch.searchParams.append('category', category);
        url_to_fetch.searchParams.append('sex', sex);
        url_to_fetch.searchParams.append('bust', bust);
        url_to_fetch.searchParams.append('waist', waist)
        url_to_fetch.searchParams.append('hip', hip)

        try {
            const controller = new AbortController();
            const timeout = setTimeout(() => controller.abort(), 5000);

            const res = await fetch(url_to_fetch, {
                signal: controller.signal,
            });

            clearTimeout(timeout);

            if (!res.ok) {
                throw new Error("HTTP error: " + res.status);
            }

            return await res.json();
        } catch (err) {
            console.log(err);
            return {error: err.message};
        }
    }

    function getCompactDate() {
        return new Date().toISOString().slice(0, 19).replace(/[:T-]/g, "-");
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
