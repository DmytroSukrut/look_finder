import {Grid, Box, Divider, Typography, useTheme, Grow, Fade} from "@mui/material";
import {Pagination, Navigation} from 'swiper/modules'
import ProductCard from "./ProductCard";
import * as React from "react";

export default function ProductGrid({ products = [], title = "Special for you"}) {
    const theme = useTheme();
    const key_for_bershka1 = "p1";
    const key_for_bershka2 = "a4o";
    const another_keys_bershka = ["b1", "a1t", "a2d"]
    let img_urls = []

    const [open_, setOpen_] = React.useState(false);

    return (
        <Box>
            <Typography variant="h4" align={"center"} sx={{
                textAlign: "center",
                mt: 2,
                mb: 2,
                fontWeight: 600,
                color: theme.palette.text.primary,
            }}>
                {title}
            </Typography>

            <Grid container spacing={4} sx={{
                width: "90%",
                maxWidth: 1500,
                margin: "0 auto",
                mb: 5,
            }}>
                {products && products.length > 0 ? (
                    products.map((product) => {
                        img_urls = [];
                        let main_url = product.photos?.find(p => p[key_for_bershka1]);
                        if (!main_url || !Object.values(main_url)[0]) {
                            main_url = product.photos?.find(p => p[key_for_bershka2]);
                            if (main_url) img_urls.push(Object.values(main_url)[0]);
                            for (let i = 0; i < 3; i++){
                                let url = product.photos?.find(p => p[another_keys_bershka[i]]);
                                if (url) img_urls.push(Object.values(url)[0]);
                            }
                        } else {
                            let url = product.photos?.find(p => p[key_for_bershka2]);
                            if (main_url) img_urls.push(Object.values(main_url)[0]);
                            if (url) img_urls.push(Object.values(url)[0]);
                            for (let i = 0; i < 3; i++){
                                url = product.photos?.find(p => p[another_keys_bershka[i]]);
                                if (url) img_urls.push(Object.values(url)[0]);
                            }
                        }
                        const imgUrl = main_url ? Object.values(main_url)[0] : "";
                        return (
                            <Grid size={{ xs: 12, sm: 6, md: 4, lg: 3, xl: 2, xxl: 1}} key={product.id}>
                                <ProductCard
                                    id={product.id}
                                    img_main={imgUrl}
                                    img_another={img_urls}
                                    name={product.name_en}
                                    price={product.price}
                                    size={product.size}
                                    isFavourite={true}
                                    brandName="bershka"
                                    setOpen_={setOpen_}
                                />
                            </Grid>
                        );
                    })
                ) : (
                    <Typography variant="h6" align={"center"} sx={{
                        textAlign: "center",
                        mt: 5,
                        width: "100%",
                        fontWeight: 600,
                        color: theme.palette.text.primary,
                    }}>
                        No products found
                    </Typography>
                )}
            </Grid>
        </Box>
    )
}

