import {Box, Typography} from "@mui/material";
import React, {useEffect, useState} from "react";
import {NavBar} from "../components/NavBar.jsx";
import ProductGrid from "../components/ProductGrid.jsx";
import {useNavigate, useSearchParams} from "react-router";
import PaginationComponent from "../components/PaginationComponent.jsx";

// const JSON_LIFETIME = 24 * 60 * 60; //Lifetime is one day
const JSON_LIFETIME = 1; //Check value

export default function MainPage() {

    const [products, setProducts] = useState([]);

    const [params] = useSearchParams();
    const [page, setPage] = useState(parseInt(params.get("page")));
    const [maxPage, setMaxPage] = useState(1);

    const navigate = useNavigate();

    useEffect(() => {

        const url_params = new URLSearchParams();

        async function load() {

            const user_data = JSON.parse(localStorage.getItem("user"));
            console.log(user_data);

            window.scrollTo({ top: 0, behavior: "smooth" });

            const data = await fetch_similar(user_data.id);
            if (data.error){
                console.log(data.error)
                return
            }

            const CurrentPage = page;

            url_params.set("page", String(CurrentPage));
            navigate(`/main?${url_params.toString()}`);

            setCurrentPageData(data, CurrentPage);
        }

        load();

    }, [page]);

    function setCurrentPageData(data, CurrentPage){
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

            const saved_user_data = JSON.parse(localStorage.getItem("user"));

            let key = `${saved_user_data.id}_`

            if (saved_user_data.favourite1_text_id != null) key += `${saved_user_data.favourite1_text_id}_`;
            if (saved_user_data.favourite2_text_id != null) key += `${saved_user_data.favourite2_text_id}_`;
            if (saved_user_data.favourite3_text_id != null) key += `${saved_user_data.favourite3_text_id}_`;
            if (saved_user_data.favourite4_text_id != null) key += `${saved_user_data.favourite4_text_id}_`;
            if (saved_user_data.favourite5_text_id != null) key += `${saved_user_data.favourite5_text_id}_`;

            console.log(key);

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
            <PaginationComponent
                current_page={page}
                max_pages={maxPage}
                setPage={setPage}
            />
        </Box>
    );
}
