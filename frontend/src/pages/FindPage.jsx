import {Avatar, Box, Grid, Grow, Paper, Popover, Typography, useTheme} from "@mui/material"
import {NavBar} from "../components/NavBar.jsx";
import {FindAccordion} from "../components/FindAccordion.jsx";
import React, { useEffect, useState } from "react";
import ProductGrid from "../components/ProductGrid.jsx";
import PaginationComponent from "../components/PaginationComponent.jsx";
import { useNavigate, useParams, useSearchParams, useLocation} from "react-router";
import {Swiper, SwiperSlide} from "swiper/react";
import {Navigation, Pagination} from "swiper/modules";

import ErrorOutlineRoundedIcon from '@mui/icons-material/ErrorOutlineRounded';

// const JSON_LIFETIME = 24 * 60 * 60; //Lifetime is one day
const JSON_LIFETIME = 1; //Check value

export default function FindPage(){
    const theme = useTheme();

    const [products, setProducts] = useState([]);

    const navigate = useNavigate();
    const location = useLocation();

    const { cat } = useParams();
    const [params] = useSearchParams();

    const [data, setData] = useState({
        clothesType: cat,
        sex: params.get("sex"),
        bust: params.get("bust"),
        waist: params.get("waist"),
        hip: params.get("hip")
    })

    const [page, setPage] = useState(parseInt(params.get("page")));
    const [max_pages, setMaxPage] = useState(1);

    useEffect(() => {
        console.log("data: " + data);
        console.log("page: " + params.get("page"));
        console.log("page const: " + page);

        const url_params = new URLSearchParams();

        const product_category = data.clothesType
        const sex = data.sex
        const bust = data.bust
        const waist = data.waist
        const hip = data.hip

        const key = `${product_category}_${sex}_${bust}_${waist}_${hip}`

        async function load() {

            window.scrollTo({ top: 0, behavior: "smooth" });

            const ResetPage = data.clothesType !== cat;
            const currentPage = ResetPage ? 1 : page;

            if (ResetPage && page !== 1) setPage(1);

            url_params.set("sex", data.sex);
            url_params.set("bust", data.bust);
            url_params.set("waist", data.waist);
            url_params.set("hip", data.hip);
            url_params.set("page", String(currentPage));

            console.log("currentPage const: " + currentPage);
            navigate(`/find/${encodeURIComponent(data.clothesType)}?${url_params.toString()}`);

            const save = {
                cat: data.clothesType,
                sex: data.sex,
                bust: data.bust,
                waist: data.waist,
                hip: data.hip,
            }

            localStorage.setItem("last_search", JSON.stringify(save));

            const saved_data = JSON.parse(localStorage.getItem(key));

            if ((saved_data && (Math.floor(Date.now() / 1000) - saved_data.timestamp < JSON_LIFETIME)) || (saved_data && currentPage !== 1)) {
                setCurrentPageData(saved_data.data, currentPage);
            } else {
                const data_fetched = await fetch_clothes(product_category, sex, bust, waist, hip);

                if (data_fetched.error && saved_data){
                    setCurrentPageData(saved_data.data, currentPage);
                    console.log("data_fetched.error: " + data_fetched.error)
                    return
                } else if (data_fetched.error && !saved_data){
                    return
                }

                console.log("data_fetched.length: " + data_fetched.length)

                const save = {
                    timestamp: Math.floor(Date.now() / 1000),
                    timestampHuman: getCompactDate(),
                    data: data_fetched
                }
                console.log("page const3333333: " + currentPage);

                setCurrentPageData(data_fetched, currentPage);

                localStorage.setItem(key, JSON.stringify(save))
            }
        }

        load();

    }, [data, page]);

    async function fetch_clothes(category, sex, bust, waist, hip) {
        const url_to_fetch =
            `/api/clothes/filter?category=${encodeURIComponent(category)}&sex=${encodeURIComponent(sex)}&bust=${encodeURIComponent(bust)}&waist=${encodeURIComponent(waist)}&hip=${encodeURIComponent(hip)}`;

        try {
            const controller = new AbortController();
            const timeout = setTimeout(() => controller.abort(), 100000000000);

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

    const [error_data, setError_data] = useState([]);
    const [error_less, setError_less] = useState(false);
    const [error_over, setError_over] = useState(false);

    function setCurrentPageData(data_, currentPage){
        const current_page_data = data_.products[currentPage - 1].positions;
        setMaxPage(data_.products.length)
        setProducts(current_page_data)

        for (const error of data_.errors) {

            if (error.error !== "none" && currentPage === 1) {
                handleOpen();
                setError_data(data_.errors);
                switch (error.error) {
                    case "over_max": setError_over(true); break;
                    case "less_min": setError_less(true); break;
                }

            }

            console.log(error.origin);  // Например, вывести значение поля origin
            console.log(error.error);   // Или вывести значение поля error
        }
    }

    const [anchorEl, setAnchorEl] = React.useState(false);

    const handleClose = () => {
        setAnchorEl(false);
    };

    const handleOpen = () => {
        setAnchorEl(true);
    };

    const open = Boolean(anchorEl);

    return (
      <Box sx={{
          bgcolor: "background.default",
          minHeight: "100vh",
          display: "flex",
          flexDirection: "column"
      }}>
          <NavBar/>
          <Popover
              keepMounted
              open={open}
              onClose={handleClose}
              anchorReference="anchorPosition"
              anchorPosition={{
                  top: window.innerHeight / 2,
                  left: window.innerWidth / 2,
              }}
              anchorOrigin={{vertical: "center", horizontal: "center"}}
              transformOrigin={{vertical: "center", horizontal: "center"}}
              slots={{transition: Grow}}
              transitionDuration={200}
              slotProps={{
                  paper: {
                      sx: {
                          backgroundColor: "transparent",
                          boxShadow: "none",
                          overflow: "visible",
                          maxHeight: "none",
                          maxWidth: "none",
                      },
                  },
              }}
              disableRestoreFocus
              disableAutoFocus
              disableEnforceFocus
          >
              <Paper elevation={8} sx={{
                  p: 2,
                  m: 1,
                  height: window.innerHeight * 0.4,
                  width: window.innerWidth * 0.5,
                  overflowY: "auto",
                  textAlign: "center",
                  display: "flex",
                  flexDirection: "column",
                  zIndex: 200,
                  borderRadius: 2,
                  backgroundColor: theme.palette.primary.main,
              }}>
                  <Box sx={{
                      width: "100%",
                  }}>
                      <ErrorOutlineRoundedIcon
                          fontSize="large"
                          sx={{
                              color: theme.palette.error.main,
                          }}
                      />
                      <Typography variant="h4" align={"center"} sx={{
                          textAlign: "center",
                          mt: 2,
                          mb: 2,
                          fontWeight: 500,
                          fontSize: 30,
                          color: theme.palette.error.main,
                          my: 0
                      }}>
                          No exact match
                      </Typography>
                  </Box>
                  {error_over ?
                      <>
                          <Typography variant="h4" align={"center"} sx={{
                              textAlign: "center",
                              mt: 1,
                              mb: 1,
                              fontWeight: 200,
                              fontSize: 18,
                              color: theme.palette.text.primary,
                          }}>
                              Your measurements are above the supported size range for:
                          </Typography>
                          {error_data
                              .filter(e => e.error === "over_max")
                              .map(e => (
                                  <Typography variant="h4" align={"center"} sx={{
                                      textAlign: "center",
                                      mt: 1,
                                      mb: 1,
                                      fontWeight: 300,
                                      fontSize: 22,
                                      color: theme.palette.error.main,
                                  }}>
                                      {e.origin}
                                  </Typography>
                              ))
                          }
                          <Typography variant="h4" align={"center"} sx={{
                              textAlign: "center",
                              mt: 1,
                              mb: 1,
                              fontWeight: 200,
                              fontSize: 18,
                              color: theme.palette.text.primary,
                          }}>
                              We used the maximum available size for these shops.
                          </Typography>
                      </>
                      : null
                  }
                  {error_less ?
                      <>
                          <Typography variant="h4" align={"center"} sx={{
                              textAlign: "center",
                              mt: 1,
                              mb: 1,
                              fontWeight: 200,
                              fontSize: 18,
                              color: theme.palette.text.primary,
                          }}>
                              Your measurements are below the supported size range for:
                          </Typography>
                          {error_data
                              .filter(e => e.error === "less_min")
                              .map(e => (
                                  <Typography variant="h4" align={"center"} sx={{
                                      textAlign: "center",
                                      mt: 1,
                                      mb: 1,
                                      fontWeight: 300,
                                      fontSize: 22,
                                      color: theme.palette.error.main,
                                  }}>
                                      {e.origin}
                                  </Typography>
                              ))
                          }
                          <Typography variant="h4" align={"center"} sx={{
                              textAlign: "center",
                              mt: 1,
                              mb: 1,
                              fontWeight: 200,
                              fontSize: 18,
                              color: theme.palette.text.primary,
                          }}>
                              We used the minimum available size for these shops.
                          </Typography>
                      </>
                      : null
                  }
              </Paper>
          </Popover>
          <FindAccordion
              data={data}
              setData={setData}
          />
          <ProductGrid
              products={products}
              title={"Special for you"}
          />
          <PaginationComponent
              current_page={page}
              max_pages={max_pages}
              setPage={setPage}
          />
      </Box>
    );
}