graph TD
    A[Inicio: Cliente busca Producto] --> B{¿Existe en Base de Datos?}
    B -- No --> C[Mostrar: Producto no encontrado]
    B -- Sí --> D[Obtener Lista de ItemsProveedor vinculados]
    
    D --> E{¿Tiene costos cargados?}
    E -- No --> F[Alerta: Sin costo base]
    E -- Sí --> G[Seleccionar Costo (Mayor o Prioritario)]
    
    G --> H[Obtener Regla de Precio (Categoría/Rubro)]
    H --> I[Cálculo: Costo * Dólar * (1 + %Ganancia) * (1 + IVA)]
    
    I --> J[Verificar Stock]
    J --> K[Mostrar Tarjeta de Producto: Precio + Stock + Foto]
    
    style I fill:#f9f,stroke:#333,stroke-width:2px
    style K fill:#bbf,stroke:#333,stroke-width:2px


sequenceDiagram
    participant Admin as Usuario
    participant Front as React Frontend
    participant Back as Spring Backend
    participant DB as MySQL Database

    Admin->>Front: Sube archivo Excel (Lista Proveedor)
    Front->>Back: POST /api/precios/upload
    Back->>Back: Parsea Excel (Apache POI)
    
    loop Por cada fila del Excel
        Back->>DB: Busca ItemProveedor por SKU Proveedor
        alt Item existe
            Back->>DB: Actualiza Precio Costo
        else Item no existe
            Back->>DB: Crea Item en "Borrador" (Sin vincular)
        end
    end
    
    Back->>Front: Retorna Resumen (50 actualizados, 10 nuevos)
    Front->>Admin: Muestra reporte y pide confirmación
    Admin->>Front: Confirma cambios
    Front->>Back: POST /api/precios/confirmar
    Back->>DB: Recalcula Precios de Venta Final
    Back-->>Front: Éxito