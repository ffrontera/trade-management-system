import React from 'react';
import { DataGrid } from '@mui/x-data-grid';
import { Box, Typography, Chip, Avatar } from '@mui/material';

const columns = [
    { 
        field: 'image', 
        headerName: 'Img', 
        width: 70,
        renderCell: (params) => {
            // Asumimos que el back devuelve una lista de imágenes, tomamos la primera
            const imgUrl = params.row.images && params.row.images.length > 0 
                ? params.row.images[0].imageUrl 
                : null;
            return <Avatar src={imgUrl} variant="rounded" sx={{ width: 40, height: 40, mt: 1 }} />;
        }
    },
    { field: 'sku', headerName: 'SKU', width: 120 },
    { field: 'name', headerName: 'Nombre', width: 200 },
    { 
        field: 'brand', 
        headerName: 'Marca', 
        width: 130,
        valueGetter: (params) => params.row.brand?.name || '-' 
    },
    { 
        field: 'category', 
        headerName: 'Categoría', 
        width: 130, 
        valueGetter: (params) => params.row.category?.name || '-' 
    },
    { 
        field: 'price', 
        headerName: 'Precio Venta', 
        width: 120,
        valueFormatter: (params) => `$ ${params.value}`
    },
    { field: 'stock', headerName: 'Stock', width: 90 },
    {
        field: 'active',
        headerName: 'Estado',
        width: 100,
        renderCell: (params) => (
            <Chip 
                label={params.value ? "Activo" : "Inactivo"} 
                color={params.value ? "success" : "default"} 
                size="small" 
            />
        )
    }
];

export const ProductList = ({ products, loading }) => {
    return (
        <Box sx={{ height: 500, width: '100%', mt: 2, bgcolor: 'white', p: 2, borderRadius: 2, boxShadow: 1 }}>
            <Typography variant="h6" gutterBottom>Catálogo de Productos</Typography>
            <DataGrid
                rows={products}
                columns={columns}
                loading={loading}
                pageSize={10}
                rowsPerPageOptions={[10, 20]}
                disableSelectionOnClick
                getRowId={(row) => row.id} 
            />
        </Box>
    );
};