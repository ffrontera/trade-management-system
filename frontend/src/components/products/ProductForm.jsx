import React, { useState, useEffect } from 'react';
import { 
    TextField, Button, Box, Grid, MenuItem, Typography, Alert, CircularProgress 
} from '@mui/material';
import { catalogService, productService } from '../../services/productService';

export const ProductForm = ({ onProductCreated }) => {
    const [brands, setBrands] = useState([]);
    const [categories, setCategories] = useState([]);
    const [uploading, setUploading] = useState(false);
    
    const [formData, setFormData] = useState({
        name: '',
        description: '',
        sku: '',
        price: '',
        cost: '',
        stock: '',
        brandId: '',
        categoryId: '',
        imageUrl: '' 
    });

    useEffect(() => {
        const loadAuxiliaries = async () => {
            try {
                const [brandsData, catsData] = await Promise.all([
                    catalogService.getBrands(),
                    catalogService.getCategories()
                ]);
                setBrands(brandsData);
                setCategories(catsData);
            } catch (error) {
                console.error("Error cargando auxiliares", error);
            }
        };
        loadAuxiliaries();
    }, []);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleFileChange = async (e) => {
        const file = e.target.files[0];
        if (!file) return;

        setUploading(true);
        try {
            const url = await productService.uploadImage(file);
            setFormData(prev => ({ ...prev, imageUrl: url }));
        } catch (error) {
            alert("Error subiendo imagen");
        } finally {
            setUploading(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        const payload = {
            name: formData.name,
            description: formData.description,
            sku: formData.sku,
            price: parseFloat(formData.price),
            cost: parseFloat(formData.cost),
            stock: parseInt(formData.stock),
            brandId: formData.brandId,
            categoryId: formData.categoryId,
            images: formData.imageUrl ? [{ imageUrl: formData.imageUrl, orderIndex: 0 }] : []
        };

        try {
            await productService.create(payload);
            // Limpiar form
            setFormData({
                name: '', description: '', sku: '', price: '', cost: '', stock: '', 
                brandId: '', categoryId: '', imageUrl: ''
            });
            if (onProductCreated) onProductCreated();
            alert("Producto creado con éxito");
        } catch (error) {
            console.error(error);
            alert("Error al guardar producto");
        }
    };

    return (
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2, p: 3, bgcolor: 'white', borderRadius: 2, boxShadow: 1 }}>
            <Typography variant="h6" gutterBottom>Nuevo Producto</Typography>
            
            <Grid container spacing={2}>
                <Grid item xs={12} sm={6}>
                    <TextField fullWidth label="Nombre" name="name" value={formData.name} onChange={handleChange} required />
                </Grid>
                <Grid item xs={12} sm={6}>
                    <TextField fullWidth label="SKU" name="sku" value={formData.sku} onChange={handleChange} required />
                </Grid>
                
                {/* Precios y Stock */}
                <Grid item xs={4}>
                    <TextField fullWidth type="number" label="Costo" name="cost" value={formData.cost} onChange={handleChange} required />
                </Grid>
                <Grid item xs={4}>
                    <TextField fullWidth type="number" label="Precio Venta" name="price" value={formData.price} onChange={handleChange} required />
                </Grid>
                <Grid item xs={4}>
                    <TextField fullWidth type="number" label="Stock Inicial" name="stock" value={formData.stock} onChange={handleChange} required />
                </Grid>

                {/* Selectores */}
                <Grid item xs={6}>
                    <TextField select fullWidth label="Marca" name="brandId" value={formData.brandId} onChange={handleChange} required>
                        {brands.map((b) => <MenuItem key={b.id} value={b.id}>{b.name}</MenuItem>)}
                    </TextField>
                </Grid>
                <Grid item xs={6}>
                    <TextField select fullWidth label="Categoría" name="categoryId" value={formData.categoryId} onChange={handleChange} required>
                        {categories.map((c) => <MenuItem key={c.id} value={c.id}>{c.name}</MenuItem>)}
                    </TextField>
                </Grid>

                {/* Imagen */}
                <Grid item xs={12}>
                    <Button variant="outlined" component="label" fullWidth disabled={uploading}>
                        {uploading ? <CircularProgress size={24} /> : "Subir Imagen"}
                        <input type="file" hidden accept="image/*" onChange={handleFileChange} />
                    </Button>
                    {formData.imageUrl && (
                        <Typography variant="caption" display="block" sx={{ mt: 1, color: 'green' }}>
                            Imagen cargada correctamente
                        </Typography>
                    )}
                </Grid>

                <Grid item xs={12}>
                    <TextField fullWidth multiline rows={2} label="Descripción" name="description" value={formData.description} onChange={handleChange} />
                </Grid>
                
                <Grid item xs={12}>
                    <Button type="submit" variant="contained" color="primary" fullWidth size="large">
                        Guardar Producto
                    </Button>
                </Grid>
            </Grid>
        </Box>
    );
};