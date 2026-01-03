import React from 'react';
import { BrowserRouter, Routes, Route, Link as RouterLink } from 'react-router-dom';
import { 
  AppBar, 
  Toolbar, 
  Typography, 
  Button, 
  Container, 
  Box, 
  CssBaseline 
} from '@mui/material';

// Iconos
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import InventoryIcon from '@mui/icons-material/Inventory';
import HomeIcon from '@mui/icons-material/Home';

// Páginas (Asegúrate de haber creado InventoryPage como vimos en el paso anterior)
import { InventoryPage } from './pages/InventoryPage';

// --- Componente Home (Pantalla de bienvenida que ya tenías) ---
const HomePage = () => (
  <Container maxWidth="sm">
    <Box sx={{ my: 8, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
      <Typography variant="h3" component="h1" gutterBottom fontWeight="bold" color="primary">
        2 Generales
      </Typography>
      <Typography variant="h5" color="text.secondary" align="center" paragraph>
        Sistema de Gestión Comercial
      </Typography>
      <Typography variant="body1" color="text.secondary" align="center" sx={{ mb: 4 }}>
        Seleccione una opción del menú o comience una venta rápida.
      </Typography>
      
      <Box sx={{ display: 'flex', gap: 2 }}>
        <Button 
          variant="contained" 
          size="large"
          startIcon={<ShoppingCartIcon />}
        >
          Nueva Venta
        </Button>
        <Button 
          variant="outlined" 
          size="large"
          component={RouterLink} 
          to="/inventario"
          startIcon={<InventoryIcon />}
        >
          Ir al Inventario
        </Button>
      </Box>
    </Box>
  </Container>
);

// --- Componente Principal ---
function App() {
  return (
    <BrowserRouter>
      {/* CssBaseline normaliza estilos para que MUI se vea igual en todos los navegadores */}
      <CssBaseline />
      
      {/* Barra de Navegación Superior */}
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1, fontWeight: 'bold' }}>
            2 Generales App
          </Typography>
          
          {/* Botones del Menú */}
          <Button color="inherit" component={RouterLink} to="/" startIcon={<HomeIcon />}>
            Inicio
          </Button>
          <Button color="inherit" component={RouterLink} to="/inventario" startIcon={<InventoryIcon />}>
            Inventario
          </Button>
        </Toolbar>
      </AppBar>

      {/* Contenido de las Rutas */}
      <Box sx={{ minHeight: '90vh', bgcolor: '#f5f5f5', pb: 4 }}>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/inventario" element={<InventoryPage />} />
          {/* Aquí agregaremos futuras rutas como /ventas, /proveedores, etc. */}
        </Routes>
      </Box>
    </BrowserRouter>
  );
}

export default App;