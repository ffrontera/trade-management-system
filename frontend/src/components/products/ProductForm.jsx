import React, { useState, useEffect } from 'react';
import { 
    TextField, Button, Box, Grid, MenuItem, Typography, CircularProgress, Modal, IconButton 
} from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import { catalogService, productService } from '../../services/productService';

export const ProductForm = ({ onProductCreated, selectedProduct, onClearSelection }) => {
    const [brands, setBrands] = useState([]);
    const [categories, setCategories] = useState([]);
    const [uploading, setUploading] = useState(false);
    const [selectedImage, setSelectedImage] = useState(null);
    const [selectedImageIndex, setSelectedImageIndex] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    
    const [formData, setFormData] = useState({
        id: null, // ID nulo para nuevos productos
        name: '',
        description: '',
        skuInternal: '',
        salePrice: '',
        costPrice: '',
        stock: '',
        brandId: '',
        categoryId: '',
        images: [] 
    });

    useEffect(() => {
        const loadAuxiliaries = async () => {
            try {
                const [brandsData, catsData] = await Promise.all([
                    catalogService.getBrands(),
                    catalogService.getCategories()
                ]);
                setBrands(Array.isArray(brandsData) ? brandsData : []);
                setCategories(Array.isArray(catsData) ? catsData : []);
            } catch (error) {
                console.error("Error cargando auxiliares", error);
            }
        };
        loadAuxiliaries();
    }, []);

    useEffect(() => {
        if (selectedProduct) {
            setFormData({
                id: selectedProduct.id || null, // Cargar el ID del producto si existe
                name: selectedProduct.name || '',
                description: selectedProduct.description || '',
                skuInternal: selectedProduct.skuInternal || '',
                salePrice: selectedProduct.salePrice || '',
                costPrice: selectedProduct.costPrice || '',
                stock: selectedProduct.stock || '',
                brandId: selectedProduct.brandId || '',
                categoryId: selectedProduct.categoryId || '',
                images: (selectedProduct.images || []).map((img, index) => ({
                    url: img.url, // Usar la URL de la imagen
                    orderIndex: img.orderIndex || index + 1, // Asignar un índice de orden si no existe
                })),
            });
        } else {
            setFormData({
                id: null, // ID nulo para nuevos productos
                name: '',
                description: '',
                skuInternal: '',
                salePrice: '',
                costPrice: '',
                stock: '',
                brandId: '',
                categoryId: '',
                images: [],
            });
        }
    }, [selectedProduct]);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleFileChange = async (e) => {
        const file = e.target.files[0];
        if (!file) return;

        setUploading(true);
        try {
            await handleImageUpload(file);
        } catch (error) {
            alert("Error subiendo imagen");
        } finally {
            setUploading(false);
        }
    };

    const handleImageUpload = async (file) => {
        try {
            if ((formData.images || []).length >= 3) {
                alert('Solo puedes cargar hasta 3 imágenes.');
                return;
            }

            const response = await productService.uploadImage(file);
            const imageUrl = response.url; // Extraer el valor de la llave `url`

            setFormData((prev) => {
                const updatedImages = [
                    ...prev.images,
                    { url: imageUrl, orderIndex: (prev.images.length + 1) },
                ];
                return { ...prev, images: updatedImages };
            });
        } catch (error) {
            console.error('Error al subir la imagen:', error);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const payload = {
            id: formData.id, // Enviar el ID si existe, o null para nuevos productos
            name: formData.name,
            description: formData.description,
            skuInternal: formData.skuInternal,
            salePrice: parseFloat(formData.salePrice),
            costPrice: parseFloat(formData.costPrice),
            stock: parseInt(formData.stock),
            brandId: formData.brandId,
            categoryId: formData.categoryId,
            images: formData.images.length > 0 ? formData.images : [],
        };

        try {
            if (formData.id) {
                await productService.update(payload); // Cambiado a .update
                alert('Producto actualizado exitosamente');
            } else {
                await productService.create(payload);
                alert('Producto creado exitosamente');
            }

            handleClearForm(); // Limpiamos form visualmente
            if (onProductCreated) onProductCreated(); // Recargamos tabla
        } catch (error) {
            console.error(error);
            alert('Error al guardar producto');
        }
    };

    const handleOrderChange = (url, newOrder) => {
        setFormData((prev) => {
            const updatedImages = prev.images.map((img) => {
                if (img.url === url) {
                    return { ...img, orderIndex: newOrder };
                } else if (img.orderIndex === newOrder) {
                    return { ...img, orderIndex: prev.images.find((i) => i.url === url).orderIndex };
                }
                return img;
            });
            return { ...prev, images: updatedImages.sort((a, b) => a.orderIndex - b.orderIndex) };
        });
    };

    const handleRemoveImage = (url) => {
        setFormData((prev) => {
            const updatedImages = prev.images.filter((img) => img.url !== url);

            // Cerrar el modal si la imagen eliminada está seleccionada
            if (selectedImageIndex !== null && prev.images[selectedImageIndex]?.url === url) {
                closeModal();
            }

            // Ajustar el índice seleccionado si es necesario
            if (selectedImageIndex !== null && updatedImages.length > 0) {
                setSelectedImageIndex((prevIndex) => Math.min(prevIndex, updatedImages.length - 1));
            }

            return { ...prev, images: updatedImages };
        });
    };

    const handleImageClick = (index) => {
        setSelectedImageIndex(index);
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
        setSelectedImageIndex(null);
    };

    const navigateImages = (direction) => {
        setSelectedImageIndex((prevIndex) => prevIndex + direction);
    };

    const handleClearForm = () => {
        setFormData({
            id: null,
            name: '', description: '', skuInternal: '', salePrice: '', 
            costPrice: '', stock: '', brandId: '', categoryId: '', images: [],
        });
        // IMPORTANTE: Avisar al padre para quitar la selección de la tabla
        if (onClearSelection) onClearSelection();
    };

    return (
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2, p: 3, bgcolor: 'white', borderRadius: 2, boxShadow: 1 }}>
            <Typography variant="h6" gutterBottom>Producto</Typography>
            
            <Grid container spacing={2}>
                <Grid >
                    <TextField fullWidth label="Nombre" name="name" value={formData.name} onChange={handleChange} required />
                </Grid>
                <Grid >
                    <TextField fullWidth label="SKU" name="skuInternal" value={formData.skuInternal} onChange={handleChange} required />
                </Grid>
                
                {/* Precios y Stock */}
                <Grid >
                    <TextField fullWidth type="number" label="Costo" name="costPrice" value={formData.costPrice} onChange={handleChange} required />
                </Grid>
                <Grid >
                    <TextField fullWidth type="number" label="Precio Venta" name="salePrice" value={formData.salePrice} onChange={handleChange} required />
                </Grid>
                <Grid >
                    <TextField fullWidth type="number" label="Stock Inicial" name="stock" value={formData.stock} onChange={handleChange} required />
                </Grid>

                {/* Selectores */}
                <Grid >
                    <TextField select fullWidth label="Marca" name="brandId" value={formData.brandId} onChange={handleChange} required>
                        {brands.map((b) => <MenuItem key={b.id} value={b.id}>{b.name}</MenuItem>)}
                    </TextField>
                </Grid>
                <Grid >
                    <TextField select fullWidth label="Categoría" name="categoryId" value={formData.categoryId} onChange={handleChange} required>
                        {categories.map((c) => <MenuItem key={c.id} value={c.id}>{c.name}</MenuItem>)}
                    </TextField>
                </Grid>

                {/* Imagen */}
                <Grid >
                    <Button variant="outlined" component="label" fullWidth disabled={uploading}>
                        {uploading ? <CircularProgress size={24} /> : "Subir Imagen"}
                        <input type="file" hidden accept="image/*" onChange={handleFileChange} />
                    </Button>
                    {formData.images.length > 0 && (
                        <Typography variant="caption" display="block" sx={{ mt: 1, color: 'green' }}>
                            {formData.images.length} imagen(es) cargada(s) correctamente
                        </Typography>
                    )}
                </Grid>

                <Grid >
                    <TextField fullWidth multiline rows={2} label="Descripción" name="description" value={formData.description} onChange={handleChange} />
                </Grid>
                
                {/* Renderizado de miniaturas debajo del botón */}
                <Grid item xs={12} sx={{ mt: 2 }}>
                    {formData.images.map((image, index) => (
                        <ImagePreview
                            key={image.url}
                            image={image}
                            onClick={() => handleImageClick(index)}
                        />
                    ))}
                </Grid>

                <Grid >
                    <Button type="submit" variant="contained" color="primary" fullWidth size="large">
                        Guardar Producto
                    </Button>
                </Grid>

                {/* Botón para limpiar el formulario */}
                <Grid item xs={12}>
                    <Button 
                        variant="outlined" 
                        fullWidth 
                        size="large" 
                        onClick={handleClearForm}
                        sx={{ 
                            mt: 2, 
                            color: 'white', 
                            borderColor: '#1976d2', // Mismo color que el botón de guardar
                            bgcolor: '#FFC107', // Fondo amarillo
                            '&:hover': {
                                bgcolor: '#FFB300', // Fondo amarillo más oscuro al pasar el mouse
                            }
                        }}
                    >
                        Limpiar Formulario
                    </Button>
                </Grid>
            </Grid>

            {/* Modal para imagen ampliada con navegación y botón de quitar */}
            {isModalOpen && (
                <ImageModal
                    open={isModalOpen}
                    images={formData.images}
                    currentIndex={selectedImageIndex}
                    onClose={closeModal}
                    onOrderChange={handleOrderChange}
                    onNavigate={navigateImages}
                    onRemove={handleRemoveImage}
                />
            )}
        </Box>
    );
};

const ImagePreview = ({ image, onClick }) => (
    <Box sx={{ display: 'inline-block', margin: '0 4px' }}>
        <img
            src={image.url}
            alt="Preview"
            style={{ width: 50, height: 50, objectFit: 'cover', cursor: 'pointer', borderRadius: 4 }}
            onClick={onClick}
        />
    </Box>
);

const ImageModal = ({ open, images, currentIndex, onClose, onOrderChange, onNavigate, onRemove }) => {
    useEffect(() => {
        const handleKeyDown = (e) => {
            if (e.key === 'ArrowLeft' && currentIndex > 0) {
                onNavigate(-1);
            } else if (e.key === 'ArrowRight' && currentIndex < images.length - 1) {
                onNavigate(1);
            }
        };

        if (open) {
            window.addEventListener('keydown', handleKeyDown);
        } else {
            window.removeEventListener('keydown', handleKeyDown);
        }

        return () => window.removeEventListener('keydown', handleKeyDown);
    }, [open, currentIndex, onNavigate]);

    const handleOrderChange = (newOrder) => {
        if (newOrder < 1 || newOrder > 3) return;

        const targetImage = images.find((img) => img.orderIndex === newOrder);
        if (targetImage) {
            onOrderChange(targetImage.url, images[currentIndex].orderIndex);
        }
        onOrderChange(images[currentIndex].url, newOrder);
    };

    return (
        <Modal open={open} onClose={onClose}>
            <Box sx={{
                position: 'relative',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                height: '100vh',
                bgcolor: 'rgba(0, 0, 0, 0.8)',
                color: 'white',
            }}>
                <IconButton
                    onClick={onClose}
                    sx={{ position: 'absolute', top: 16, right: 16, color: 'white' }}
                >
                    <CloseIcon />
                </IconButton>

                <IconButton
                    onClick={() => onNavigate(-1)}
                    disabled={currentIndex === 0}
                    sx={{ position: 'absolute', left: 16, color: 'white' }}
                >
                    {'<'}
                </IconButton>

                <img
                    src={images[currentIndex].url}
                    alt="Expanded"
                    style={{ maxWidth: '80%', maxHeight: '70%', marginBottom: 16 }}
                />

                <IconButton
                    onClick={() => onNavigate(1)}
                    disabled={currentIndex === images.length - 1}
                    sx={{ position: 'absolute', right: 16, color: 'white' }}
                >
                    {'>'}
                </IconButton>

                <TextField
                    type="number"
                    label="Orden"
                    value={images[currentIndex].orderIndex}
                    onChange={(e) => handleOrderChange(parseInt(e.target.value))}
                    sx={{ position: 'absolute', bottom: 64, bgcolor: 'white', borderRadius: 1 }}
                />

                <Button
                    variant="contained"
                    color="error"
                    onClick={() => onRemove(images[currentIndex].url)}
                    sx={{ position: 'absolute', bottom: 16 }}
                >
                    Quitar
                </Button>
            </Box>
        </Modal>
    );
};