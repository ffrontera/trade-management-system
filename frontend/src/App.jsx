import { Button, Container, Typography, Box } from '@mui/material';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';

function App() {
  return (
    <Container maxWidth="sm">
      <Box sx={{ my: 4, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Typography variant="h4" component="h1" gutterBottom>
          2 Generales App
        </Typography>
        <Typography variant="body1" color="text.secondary" align="center">
          Frontend inicializado con éxito.
        </Typography>
        <Button 
          variant="contained" 
          color="primary" 
          startIcon={<ShoppingCartIcon />}
          sx={{ mt: 2 }}
        >
          Iniciar Ventas
        </Button>
      </Box>
    </Container>
  );
}

export default App;