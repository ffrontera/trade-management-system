import React, { useState } from 'react';
import { Container, Grid, Typography } from '@mui/material';
import { ProductList } from '../components/products/ProductList';
import { ProductForm } from '../components/products/ProductForm';
import { useProducts } from '../hooks/useProducts';

export const InventoryPage = () => {
    const { products, loading, refreshProducts } = useProducts();
    const [selectedProduct, setSelectedProduct] = useState(null);

    // Seleccionar para editar
    const handleProductSelect = (product) => {
        setSelectedProduct(product);
        // Opcional: Hacer scroll hacia arriba si estás en móvil
        window.scrollTo({ top: 0, behavior: 'smooth' });
    };

    // Limpiar selección (nuevo)
    const handleClearSelection = () => {
        setSelectedProduct(null);
    };

    return (
        <Container maxWidth="xl" sx={{ mt: 4, mb: 4 }}>
            <Typography variant="h4" component="h1" gutterBottom sx={{ fontWeight: 'bold', color: '#1976d2' }}>
                Gestión de Inventario
            </Typography>
            
            <Grid container spacing={3}>
                <Grid item xs={12} md={5}>
                    <ProductForm 
                        onProductCreated={refreshProducts} 
                        selectedProduct={selectedProduct} 
                        onClearSelection={handleClearSelection} // Conectamos
                    />
                </Grid>
                
                <Grid item xs={12} md={7}>
                    <ProductList 
                        products={products} 
                        loading={loading} 
                        onProductSelect={handleProductSelect}
                        onStatusChange={refreshProducts} // Para recargar si cambiamos estado
                    />
                </Grid>
            </Grid>
        </Container>
    );
};