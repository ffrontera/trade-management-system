# Sistema de Gestión Comercial & Stock (SaaS Familiar)

![Java](https://img.shields.io/badge/Java-17-orange) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.1-green) ![React](https://img.shields.io/badge/React-18-blue) ![MySQL](https://img.shields.io/badge/MySQL-8.0-lightgrey)

## 📖 Contexto del Proyecto

Este proyecto nace de una necesidad real: **La Transformación Digital de un negocio familiar** dedicado a la venta de artículos de caza, pesca y camping.

Actualmente, el negocio enfrenta desafíos operativos críticos:
1.  **Dispersión de Precios:** Múltiples proveedores con listas en formatos variados (Excel, PDF), dificultando la actualización de costos.
2.  **Ineficiencia en Mostrador:** El cálculo manual de precios de venta retrasa la atención al cliente.
3.  **Falta de Control de Stock:** Reposición basada en "ojo" y no en datos.

**Objetivo:** Desarrollar una solución integral que centralice las listas de precios, automatice el cálculo de márgenes y gestione el inventario, escalando futuramente hacia un E-commerce integrado.

## 🏗️ Arquitectura: Monolito Modular

Dado que el desarrollo es realizado por un equipo unipersonal, se optó por una arquitectura de **Monolito Modular**. Esto permite:
* **Velocidad de Desarrollo:** Sin la complejidad de red de los microservicios.
* **Escalabilidad Futura:** Los módulos (`Catalogo`, `Inventario`, `Ventas`) están desacoplados lógicamente (bounded contexts). Si el sistema crece, separar un módulo a un microservicio es trivial.

### Diagrama de Dominios
* **Módulo Catálogo:** Gestión de Productos, Marcas, Categorías y Reglas de Precios.
* **Módulo Proveedores:** Gestión de Listas de Precios y vinculación de SKUs externos.
* **Módulo Inventario:** Control de Stock y Movimientos.

## 🛠️ Tecnologías Utilizadas

### Backend
* **Lenguaje:** Java 17
* **Framework:** Spring Boot 3 (Web, Data JPA, Validation)
* **Base de Datos:** MySQL
* **Herramientas:** Lombok, Maven, Apache POI (Procesamiento de Excel).

### Frontend
* **Framework:** React (Vite)
* **UI Library:** Material UI (MUI)
* **HTTP Client:** Axios
* **State Management:** React Hooks

## 🚀 Instalación y Despliegue Local

### Prerrequisitos
* Java 17 SDK
* Node.js & NPM
* MySQL Server

### Pasos
1.  **Clonar el repositorio**
    ```bash
    git clone [https://github.com/TU_USUARIO/sistema-gestion-comercio.git](https://github.com/TU_USUARIO/sistema-gestion-comercio.git)
    ```

2.  **Backend (Spring Boot)**
    * Crear base de datos `comercio_db` en MySQL.
    * Configurar credenciales en `backend/src/main/resources/application.properties`.
    * Ejecutar:
        ```bash
        cd backend
        ./mvnw spring-boot:run
        ```

3.  **Frontend (React)**
    * Instalar dependencias y correr:
        ```bash
        cd frontend
        npm install
        npm run dev
        ```

## 🗺️ Roadmap de Desarrollo

- [x] **Sprint 0:** Análisis, Diseño de BD y Configuración de Entorno.
- [ ] **Sprint 1:** Gestión de Catálogo (ABM Productos) y Precios.
- [ ] **Sprint 2:** Buscador Inteligente para Mostrador.
- [ ] **Sprint 3:** Control de Stock e Inventario.
- [ ] **Sprint 4:** Módulo de E-commerce.

---
*Desarrollado con ❤️ para la familia y el código abierto.*
