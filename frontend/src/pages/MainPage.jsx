import {Box, Typography} from "@mui/material";
import React, {useEffect, useState} from "react";
import {NavBar} from "../components/NavBar.jsx";
import ProductGrid from "../components/ProductGrid.jsx";
import {useNavigate, useSearchParams} from "react-router";
import PaginationComponent from "../components/PaginationComponent.jsx";

const JSON_LIFETIME = 24 * 60 * 60; //Lifetime is one day
// const JSON_LIFETIME = 1; //Check value

export default function MainPage() {

    const [products, setProducts] = useState([]);

    const [params] = useSearchParams();
    const [page, setPage] = useState(parseInt(params.get("page")));
    const [maxPage, setMaxPage] = useState(1);

    const navigate = useNavigate();

    useEffect(() => {

        const url_params = new URLSearchParams();

        async function load() {

            const saved_user_data = JSON.parse(localStorage.getItem("user"));
            console.log(saved_user_data);

            window.scrollTo({ top: 0, behavior: "smooth" });

            let key = `${saved_user_data.id}_`

            if (saved_user_data.favourite1_text_id != null) key += `${saved_user_data.favourite1_text_id}_`;
            if (saved_user_data.favourite2_text_id != null) key += `${saved_user_data.favourite2_text_id}_`;
            if (saved_user_data.favourite3_text_id != null) key += `${saved_user_data.favourite3_text_id}_`;
            if (saved_user_data.favourite4_text_id != null) key += `${saved_user_data.favourite4_text_id}_`;
            if (saved_user_data.favourite5_text_id != null) key += `${saved_user_data.favourite5_text_id}_`;

            console.log(key);

            const saved_data_main = JSON.parse(localStorage.getItem(key));

            if(saved_data_main && (Math.floor(Date.now() / 1000) - saved_data_main.timestamp < JSON_LIFETIME) || (saved_data_main && page !== 1)){
                setCurrentPageData(saved_data_main.data, page);
            } else {

                const data_fetched = await fetch_similar(saved_user_data.id);
                if (data_fetched.error && saved_data_main){
                    console.log(data_fetched.error);
                    setCurrentPageData(saved_data_main.data, page);
                } else {
                    setCurrentPageData(data_fetched, page);

                    const save = {
                        timestamp: Math.floor(Date.now() / 1000),
                        timestampHuman: getCompactDate(),
                        data: data_fetched
                    }

                    localStorage.setItem(key, JSON.stringify(save));
                }
            }
            url_params.set("page", String(page));
            navigate(`/main?${url_params.toString()}`);
        }

        load();

    }, [page]);

    function setCurrentPageData(data, CurrentPage){
        if (isNaN(CurrentPage)) CurrentPage = 1;
        const current_page_data = data.products[CurrentPage - 1].positions;
        setMaxPage(data.products.length)
        setProducts(current_page_data)
    }

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
            <PaginationComponent
                current_page={page}
                max_pages={maxPage}
                setPage={setPage}
            />
        </Box>
    );
}
