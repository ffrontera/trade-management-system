import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const api = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

export const productService = {
    getAll: async () => {
        const response = await api.get('/products');
        return Array.isArray(response.data.content)
            ? response.data.content.map(product => ({
                ...product,
                salePrice: product.salePrice ?? 0,
                brandName: product.brandName ?? 'Sin Marca',
                categoryName: product.categoryName ?? 'Sin Categoría',
                images: product.images?.map((img) => ({
                    url: img.url || img.imageUrl, // Compatibilidad con diferentes formatos de URL
                    orderIndex: img.orderIndex,
                })) || [],
            }))
            : [];
    },

    create: async (productData) => {
        const response = await api.post('/products', productData);
        return response.data;
    },

    update: async (productData) => {
        // Convertir la llave 'url' a 'imageUrl' antes de enviar al backend
        const updatedProductData = {
            ...productData,
            images: productData.images.map((img) => ({
                imageUrl: img.url, // Cambiar 'url' a 'imageUrl'
                orderIndex: img.orderIndex,
            })),
        };

        const response = await api.put(`/products/${updatedProductData.id}`, updatedProductData);
        return response.data;
    },

    toggleStatus: async (id) => {
        // Backend debe tener: PUT /api/products/{id}/status
        const response = await api.put(`/products/${id}/status`);
        return response.data;
    },
    
    uploadImage: async (file) => {
        const formData = new FormData();
        formData.append('file', file);
        const response = await api.post('/media/upload', formData, {
            headers: { 'Content-Type': 'multipart/form-data' },
        });
        return response.data;
    }
};

export const catalogService = {
    getBrands: async () => {
        const response = await api.get('/brands');
        return response.data.content;
    },
    getCategories: async () => {
        const response = await api.get('/categories');
        return response.data.content;
    }
};