import {Grid, Box, Divider, Typography, useTheme} from "@mui/material";
import ProductCard from "./ProductCard";

export default function ProductGrid({ products = [], title = "Special for you"}) {
    const theme = useTheme();
    const key_for_bershka1 = "p1";
    const key_for_bershka2 = "a4o";
    console.log(products);
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
                {products && products.length > 0 ? (
                    products.map((product) => {
                        let found = product.photos?.find(p => p[key_for_bershka1]);
                        if (!found || !Object.values(found)[0]) {
                            found = product.photos?.find(p => p[key_for_bershka2]);
                        }
                        const imgUrl = found ? Object.values(found)[0] : "";
                        console.log("imgUrl: " + imgUrl);
                        return (
                            <Grid item xs={12} sm={4} md={3} key={product.id || product.name_en}>
                                <ProductCard
                                    img={imgUrl}
                                    name={product.name}
                                    price={product.price}
                                    size={product.size}
                                    isFavourite={true}
                                    brandName="bershka"
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

