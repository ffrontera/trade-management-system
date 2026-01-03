import React from 'react';
import { DataGrid } from '@mui/x-data-grid';
import { Box, Typography, Chip, Avatar } from '@mui/material';
import { productService } from '../../services/productService'; // Importar servicio

export const ProductList = ({ products, loading, onProductSelect, onStatusChange }) => {

    const handleToggleStatus = async (id, currentStatus, event) => {
        event.stopPropagation(); // IMPORTANTE: Evita que se abra el formulario de edición

        // Confirmación simple (opcional)
        const action = currentStatus ? "desactivar" : "activar";
        if (!window.confirm(`¿Deseas ${action} este producto?`)) return;

        try {
            await productService.toggleStatus(id);
            if (onStatusChange) onStatusChange(); // Recargar lista
        } catch (error) {
            console.error("Error cambiando estado", error);
            alert("No se pudo cambiar el estado (¿Backend implementado?)");
        }
    };

    const columns = [
        {
            field: 'image',
            headerName: 'Img',
            width: 70,
            renderCell: (params) => {
                const imgUrl = params.row?.images?.find((img) => img.orderIndex === 1)?.url || null;
                return <Avatar src={imgUrl} variant="rounded" sx={{ width: 40, height: 40, mt: 1 }} />;
            }
        },
        { field: 'skuInternal', headerName: 'SKU', width: 120 },
        { field: 'name', headerName: 'Nombre', width: 200 },
        { field: 'brandName', headerName: 'Marca', width: 130 },
        { field: 'categoryName', headerName: 'Categoría', width: 130 },
        {
            field: 'salePrice',
            headerName: 'Precio Venta',
            width: 120,
            valueFormatter: (params) => {
                return `$ ${params.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;

            }
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
                    onClick={(e) => handleToggleStatus(params.row.id, params.value, e)}
                    sx={{ cursor: 'pointer', '&:hover': { opacity: 0.8 } }}
                />
            )
        }
    ];

    const sanitizedProducts = products.map((product) => ({
        ...product,
        brand: product.brand || {},
        category: product.category || {},
        images: product.images || [],
    }));

    return (
        <Box sx={{ height: 500, width: '100%', mt: 2, bgcolor: 'white', p: 2, borderRadius: 2, boxShadow: 1 }}>
            <Typography variant="h6" gutterBottom>Catálogo de Productos</Typography>
            <DataGrid
                rows={sanitizedProducts}
                columns={columns}
                loading={loading}
                pageSize={10}
                rowsPerPageOptions={[10, 20]}
                disableSelectionOnClick
                getRowId={(row) => row.id}
                onRowClick={(params) => onProductSelect && onProductSelect(params.row)}
            />
        </Box>
    );
};