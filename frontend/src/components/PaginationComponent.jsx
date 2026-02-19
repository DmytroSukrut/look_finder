import {Box, Pagination, useTheme} from "@mui/material";


export default function PaginationComponent({current_page, max_pages, setPage}) {
    const theme = useTheme();

    const handlePaginationChange = (event, page) => {
        setPage(page);
    }

    return (
        <Box sx={{
            display: "flex",
            justifyContent: "center",
            width: '100%',
            pb: 4
        }}>
            <Pagination
                count={max_pages}
                page={current_page}
                boundaryCount={1}
                showFirstButton={true}
                showLastButton={true}
                variant="outlined"
                color="pink"
                size="large"
                onChange={handlePaginationChange}
                sx={{
                    "& .MuiPaginationItem-root.Mui-selected": {
                        bgcolor: "custom.themePink",
                        color: "black",
                    },
                    "& .MuiPaginationItem-root.Mui-selected:hover": {
                        bgcolor: "custom.themePinkLighter",
                    },
                }}
            />
        </Box>
    )
}