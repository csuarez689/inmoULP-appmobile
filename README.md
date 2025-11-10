# 🏠 ULP Inmobiliaria

Aplicación móvil Android para gestión inmobiliaria desarrollada para la Universidad de La Punta (ULP).

## 📱 Características

- **Gestión de Inmuebles**
  - Listado de propiedades
  - Alta de nuevos inmuebles con fotos
  - Control de disponibilidad

- **Gestión de Contratos**
  - Visualización de contratos activos
  - Información de inquilinos
  - Detalles de pagos

- **Perfil de Usuario**
  - Datos del propietario
  - Cambio de contraseña
  - Gestión de sesión

## 🛠️ Requisitos

- Android Studio
- SDK mínimo: Android 6.0 (API 23)
- SDK objetivo: Android 13 (API 33)
- Gradle 8.0+
- JDK 17

## ⚙️ Configuración Local

Para ejecutar el proyecto, necesitas crear un archivo `local.properties` en la raíz del proyecto con las siguientes claves:

```properties
# Ruta del SDK de Android (generada automáticamente por Android Studio)
sdk.dir=C\:\Users\USERNAME\AppData\Local\Android\Sdk

# Clave de API de Google Maps (requerida para mapas)
MAPS_API_KEY=your_google_maps_api_key_here
```

> **Nota**: Reemplaza `USERNAME` con tu nombre de usuario de Windows y agrega tu propia clave de API de Google Maps.

## ⚙️ Instalación

1. Clonar el repositorio:
```bash
git clone https://github.com/csuarez689/inmoULP-appmobile.git
```

2. Abrir el proyecto en Android Studio

3. Sincronizar el proyecto con Gradle

4. Ejecutar en:
   - Emulador Android
   - Dispositivo físico (modo desarrollador activado)

## 📱 Uso

1. Iniciar sesión con credenciales proporcionadas
2. Navegar usando el menú lateral
3. Gestionar inmuebles y contratos
4. Funciones especiales:
   - Agitar el dispositivo para llamada rápida a inmobiliaria
   - Tomar fotos de inmuebles
   - Ver ubicación de inmobiliaria

## 🔑 Características Técnicas

- **Arquitectura MVVM**
  - ViewModels para lógica de negocio
  - LiveData para actualizaciones de UI
  - ViewBinding para acceso a vistas

- **APIs y Librerías**
  - Retrofit para llamadas HTTP
  - Google Maps para ubicaciones
  - Material Design 3 para UI
  - Glide para carga de imágenes

- **Seguridad**
  - Autenticación mediante token JWT
  - Validación de formularios
  - Manejo de permisos Android

## 🤝 Desarrollado por

**Claudio Suarez**
- Email: csuarez689@gmail.com
- GitHub: [csuarez689](https://github.com/csuarez689)
- LinkedIn: [claudio-suarez](https://www.linkedin.com/in/claudio-suarez)

Proyecto académico para la Universidad de La Punta (ULP)
Materia: Laboratorio de Programación III

## 📄 Licencia

Este proyecto es de uso educativo para la Universidad de La Punta.
