import {Grid, Box, Divider, Typography, useTheme, Grow, Fade} from "@mui/material";
import {Pagination, Navigation} from 'swiper/modules'
import ProductCard from "./ProductCard";
import * as React from "react";

export default function ProductGrid({ products = [], title = "Special for you"}) {
    const theme = useTheme();

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
                        const photos = product.photos ?? [];

                        const display = photos
                            .filter(p => "display" in p)
                            .map(p => Object.values(p)[0]);

                        const others = photos
                            .filter(p => !("display" in p))
                            .map(p => Object.values(p)[0]);

                        const all_urls = [...display, ...others];
                        return (
                            <Grid size={{ xs: 12, sm: 6, md: 4, lg: 3, xl: 2, xxl: 1}} key={product.id}>
                                <ProductCard
                                    id={product.id}
                                    img_display={display}
                                    img_all={all_urls}
                                    name={product.name_en}
                                    price={product.price}
                                    size={product.size}
                                    isFavourite={true}
                                    brandName={product.origin}
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

