import {Grid, Box, Divider, Typography, useTheme} from "@mui/material";
import ProductCard from "./ProductCard";

export default function ProductGrid({ product = [], title = "Special for you"}) {
    const theme = useTheme();

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
            }}>
                {product && product.length > 0 ? (
                    product.map((item) => (
                        <Grid item xs={12} sm={4} md={3} key={product.id}>
                            <ProductCard
                                img={item.img}
                                name={item.name}
                                price={item.price}
                                size={item.size}
                                isFavourite={item.isFavourite}
                                brandName={item.brandName}
                            />
                        </Grid>
                    ))
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

