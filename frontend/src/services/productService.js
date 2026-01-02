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
        return response.data;
    },

    create: async (productData) => {
        const response = await api.post('/products', productData);
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
        return response.data;
    },
    getCategories: async () => {
        const response = await api.get('/categories');
        return response.data;
    }
};