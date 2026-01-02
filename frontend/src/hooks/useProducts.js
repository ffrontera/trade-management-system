import { useState, useEffect, useCallback } from 'react';
import { productService } from '../services/productService';

export const useProducts = () => {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchProducts = useCallback(async () => {
        setLoading(true);
        try {
            const data = await productService.getAll();
            setProducts(data);
            setError(null);
        } catch (err) {
            setError('Error al cargar productos');
            console.error(err);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchProducts();
    }, [fetchProducts]);

    return { products, loading, error, refreshProducts: fetchProducts };
};