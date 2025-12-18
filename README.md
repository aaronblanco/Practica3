# Practica3 - Aplicación de Compras Android

Una aplicación Android moderna para compra de productos con autenticación de usuario, carrito de compras y visualización de ubicación en mapas.

## 📋 Tabla de Contenidos

- [Características](#características)
- [Requisitos Previos](#requisitos-previos)
- [Instalación](#instalación)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Dependencias](#dependencias)
- [Endpoints API](#endpoints-api)
- [Configuración](#configuración)
- [Uso](#uso)

---

## ✨ Características

- ✅ **Autenticación de Usuario**: Login y logout seguro
- ✅ **Catálogo de Productos**: Visualización de todos los productos disponibles
- ✅ **Carrito de Compras**: Agregar/eliminar productos
- ✅ **Checkout**: Proceso de compra integrado
- ✅ **Gestión de Productos**: Agregar nuevos productos (requiere autenticación)
- ✅ **Mapa Interactivo**: Visualización de ubicación con OpenStreetMap
- ✅ **API REST**: Integración con backend remoto
- ✅ **Interfaz Moderna**: Diseño limpio con RecyclerView y Material Design

---

## 📱 Requisitos Previos

### Software Requerido
- **Android Studio**: Versión Flamingo o superior
- **Java**: JVM 11 o superior
- **Android SDK**: API 24 (Android 7.0) mínimo, API 34 objetivo
- **Kotlin**: 1.9.0 o superior
- **Gradle**: 8.13 o superior

### Configuración del Sistema
- Tener Java 11+ configurado en la variable de entorno `JAVA_HOME`
- Mínimo 2GB de RAM disponible
- Conexión a internet

---

## 📥 Instalación

### Paso 1: Clonar el Repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd Practica3
```

### Paso 2: Abrir en Android Studio

1. Abre **Android Studio**
2. Selecciona **File → Open** (Archivo → Abrir)
3. Navega a la carpeta `Practica3` y selecciona **Open** (Abrir)
4. Espera a que Gradle sincronice automáticamente

### Paso 3: Sincronizar Gradle

Si la sincronización no se inicia automáticamente:
1. Ve a **File → Sync Now** (Archivo → Sincronizar Ahora)
2. Espera a que se descarguen todas las dependencias

### Paso 4: Configurar el Emulador o Dispositivo

**Opción A - Usar Emulador:**
1. Abre el **AVD Manager** (Tools → Device Manager)
2. Crea un nuevo dispositivo Android 7.0+ (API 24+)
3. Inicia el emulador

**Opción B - Usar Dispositivo Físico:**
1. Conecta tu dispositivo Android con USB
2. Activa el **Modo de Desarrollador** y **Depuración USB**
3. Asegúrate de que aparezca en `adb devices`

### Paso 5: Compilar y Ejecutar

1. Haz clic en el botón **▶ Run** (Ejecutar) o presiona `Shift + F10`
2. Selecciona el emulador o dispositivo donde instalar la app
3. Espera a que se compile e instale

Alternativamente, desde terminal:
```bash
./gradlew installDebug
```

---

## 📁 Estructura del Proyecto

```
Practica3/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/practica3/
│   │   │   │   ├── Activities/
│   │   │   │   │   ├── MainActivity.kt              # Pantalla principal con lista de productos
│   │   │   │   │   ├── LoginActivity.kt            # Autenticación de usuario
│   │   │   │   │   ├── CartActivity.kt             # Vista del carrito de compras
│   │   │   │   │   ├── CheckoutActivity.kt         # Proceso de checkout
│   │   │   │   │   ├── AddProductActivity.kt       # Agregar nuevos productos
│   │   │   │   │   └── MapActivity.kt              # Visualización de mapa
│   │   │   │   │
│   │   │   │   ├── API/
│   │   │   │   │   ├── ApiClient.kt                # Configuración de Retrofit y OkHttp
│   │   │   │   │   └── ApiService.kt               # Definición de endpoints
│   │   │   │   │
│   │   │   │   ├── Models/
│   │   │   │   │   ├── Product.kt                  # Modelo de producto
│   │   │   │   │   ├── User.kt                     # Modelo de usuario
│   │   │   │   │   ├── Order.kt                    # Modelo de orden
│   │   │   │   │   ├── LoginResponse.kt            # Respuesta de login
│   │   │   │   │   └── LoginErrorResponse.kt       # Respuesta de error
│   │   │   │   │
│   │   │   │   ├── Adapters/
│   │   │   │   │   ├── ProductAdapter.kt           # Adapter para lista de productos
│   │   │   │   │   └── CartAdapter.kt              # Adapter para carrito
│   │   │   │   │
│   │   │   │   ├── ShoppingCart.kt                 # Gestión del carrito (Singleton)
│   │   │   │   └── SessionManager.kt               # Gestión de sesión del usuario
│   │   │   │
│   │   │   ├── res/
│   │   │   │   ├── layout/                         # Archivos XML de layouts
│   │   │   │   ├── drawable/                       # Recursos gráficos (iconos, fondos)
│   │   │   │   ├── values/                         # Strings, colores, temas
│   │   │   │   └── xml/                            # Configuraciones (redes, backup)
│   │   │   │
│   │   │   └── AndroidManifest.xml                # Configuración de la aplicación
│   │   │
│   │   ├── test/                                   # Pruebas unitarias
│   │   └── androidTest/                            # Pruebas instrumentadas
│   │
│   ├── build.gradle.kts                           # Configuración de Gradle (dependencias)
│   └── proguard-rules.pro                         # Reglas de ofuscación
│
├── gradle/
│   └── libs.versions.toml                         # Definición centralizada de versiones
│
├── build.gradle.kts                               # Script de compilación raíz
├── settings.gradle.kts                            # Configuración de Gradle
├── gradle.properties                              # Propiedades de Gradle
├── gradlew                                        # Gradle Wrapper (Linux/Mac)
├── gradlew.bat                                    # Gradle Wrapper (Windows)
└── README.md                                      # Este archivo
```

### Organización Lógica

**Separación por Capas:**
- **Presentation**: Activities y Adapters (interfaz de usuario)
- **Data**: Models y ShoppingCart (gestión de datos)
- **Network**: ApiClient y ApiService (comunicación con servidor)
- **Utilities**: SessionManager (utilidades varias)

---

## 📦 Dependencias

La aplicación utiliza las siguientes dependencias principales:

### Networking
| Librería | Versión | Propósito |
|----------|---------|----------|
| **Retrofit** | 2.9.0 | Cliente HTTP para consumir API REST |
| **OkHttp** | 4.11.0 | Transportador HTTP subyacente |
| **HttpLoggingInterceptor** | 4.11.0 | Logging de peticiones HTTP |
| **Gson** | 2.8.8 | Serialización/deserialización JSON |

### UI Components
| Librería | Versión | Propósito |
|----------|---------|----------|
| **AppCompat** | 1.3.1 | Compatibilidad hacia atrás |
| **RecyclerView** | 1.2.1 | Listas eficientes y scrollables |
| **CardView** | 1.0.0 | Componentes de tarjeta Material |
| **Glide** | 4.12.0 | Carga y caché de imágenes |

### Maps & Location
| Librería | Versión | Propósito |
|----------|---------|----------|
| **OSMDroid** | 6.1.18 | Mapas OpenStreetMap sin Google Maps |

### Android Core
| Librería | Versión | Propósito |
|----------|---------|----------|
| **Android Core KTX** | 1.7.0 | Extensiones Kotlin para Android |
| **Lifecycle Runtime KTX** | - | Gestión del ciclo de vida |
| **AndroidX Compose** | - | Framework de UI declarativa |

### Versiones de Compilación
- **Compilación SDK**: API 34 (Android 14)
- **SDK Mínimo**: API 24 (Android 7.0)
- **SDK Objetivo**: API 34 (Android 14)
- **Kotlin**: 1.9.0+
- **Java**: 11+

---

## 🔌 Endpoints API

La aplicación se conecta a: **`https://dss-app-deezm.ondigitalocean.app/`**

### Endpoints Disponibles

#### 1. **Productos**

##### GET `/api/products`
Obtiene la lista de todos los productos disponibles.

**Método:** GET  
**Respuesta:** `List<Product>`
```json
[
  {
    "productoId": 1,
    "productoNombre": "Laptop",
    "price": 999.99,
    "imageUrl": "https://ejemplo.com/laptop.jpg"
  }
]
```

**Uso en la app:** 
- Se ejecuta en `MainActivity.fetchProducts()`
- Se ejecuta en `onResume()` para actualizar la lista

---

##### POST `/api/products`
Crea un nuevo producto (requiere autenticación).

**Método:** POST  
**Requiere:** Sesión iniciada (`SessionManager.isLoggedIn = true`)  
**Body:** `Product`
```json
{
  "productoId": null,
  "productoNombre": "Tablet",
  "price": 499.99,
  "imageUrl": "/images/default-product.jpg"
}
```

**Respuesta:** `Product` (con ID generado por el servidor)

**Uso en la app:**
- Se ejecuta en `AddProductActivity.onCreate()` al hacer clic en "Enviar"

---

##### DELETE `/api/products/{id}`
Elimina un producto específico.

**Método:** DELETE  
**Parámetro:** `id` (Int) - ID del producto  
**Respuesta:** `Void`

**Uso en la app:**
- No se utiliza actualmente en la aplicación

---

#### 2. **Pedidos**

##### POST `/api/orders`
Crea un nuevo pedido con los productos del carrito.

**Método:** POST  
**Body:** `Order`
```json
{
  "products": [
    {
      "productoId": 1,
      "productoNombre": "Laptop",
      "price": 999.99,
      "imageUrl": "https://ejemplo.com/laptop.jpg"
    }
  ]
}
```

**Respuesta:** `Order` (con ID de pedido generado)

**Uso en la app:**
- Se ejecuta en `CheckoutActivity.onCreate()` al confirmar compra

---

#### 3. **Carrito**

##### POST `/cart/api/checkout`
Vacía el carrito en el servidor después de confirmar la compra.

**Método:** POST  
**Parámetros:** Ninguno (usa cookies de sesión)  
**Respuesta:** `Void`

**Uso en la app:**
- Se ejecuta en `CheckoutActivity.onCreate()` después de crear la orden

---

#### 4. **Autenticación**

##### POST `/api/login`
Autentica un usuario y establece una sesión.

**Método:** POST  
**Body:** `User`
```json
{
  "username": "admin",
  "password": "password123"
}
```

**Respuesta:** `LoginResponse`
```json
{
  "message": "Login successful"
}
```

**Uso en la app:**
- Se ejecuta en `LoginActivity.onCreate()` al hacer clic en "Login"
- Actualiza `SessionManager.isLoggedIn = true`

---

##### POST `/api/logout`
Cierra la sesión del usuario.

**Método:** POST  
**Parámetros:** Ninguno (usa cookies de sesión)  
**Respuesta:** `Void`

**Uso en la app:**
- Se ejecuta en `MainActivity.onCreate()` al hacer clic en "Cerrar Sesión"
- Actualiza `SessionManager.isLoggedIn = false`

---

## ⚙️ Configuración

### URL Base de la API

La URL base se configura en `ApiClient.kt`:

```kotlin
private const val BASE_URL = "https://dss-app-deezm.ondigitalocean.app/"
```

Para cambiar a un servidor local:
```kotlin
private const val BASE_URL = "http://10.0.2.2:8080/"  // Para emulador Android
// o
private const val BASE_URL = "http://192.168.x.x:8080/"  // Para dispositivo físico
```

### Configuración de Red

**Archivo:** `res/xml/network_security_config.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">10.0.2.2</domain>
        <domain includeSubdomains="true">localhost</domain>
    </domain-config>
</network-security-config>
```

### Permisos Requeridos

**Archivo:** `AndroidManifest.xml`

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

---

## 🚀 Uso

### Flujo Principal de la Aplicación

```
Splash → MainActivity (sin login) 
  ↓
  ├─→ Ver productos (disponible)
  ├─→ Carrito (disponible)
  ├─→ Mapa (disponible)
  └─→ Login
       ↓
       ↓ (Sesión iniciada)
       ├─→ Ver productos
       ├─→ Agregar productos
       ├─→ Carrito
       │    ├─→ Ver items
       │    └─→ Checkout
       │         ├─→ Crear orden
       │         └─→ Vaciar carrito
       ├─→ Mapa
       └─→ Logout

```

### Casos de Uso

#### 1. Visualizar Productos
1. Abre la app
2. La lista de productos carga automáticamente
3. Desliza para ver más productos

#### 2. Agregar al Carrito
1. En la lista de productos, haz clic en **"Agregar al Carrito"**
2. Verás una notificación confirmando la acción
3. El producto se añade al carrito interno

#### 3. Ir al Checkout
1. Haz clic en **"Ir al Carrito"** en la MainActivity
2. Verás todos los productos agregados
3. Haz clic en **"Tramitar Pedido"**

#### 4. Confirmar Compra
1. En CheckoutActivity, revisa el resumen del pedido
2. Haz clic en **"Confirmar Compra"**
3. La app envía el pedido al servidor
4. Se limpia el carrito y vuelves a MainActivity

#### 5. Autenticarse
1. Haz clic en **"Iniciar Sesión"**
2. Ingresa credenciales (ej: `admin` / `password`)
3. La sesión se establece
4. Ahora puedes **"Agregar Producto"**

#### 6. Agregar Producto
1. Estando autenticado, haz clic en **"Agregar Producto"**
2. Completa el formulario (nombre y precio)
3. Haz clic en **"Enviar"**
4. El producto aparecerá en la lista

#### 7. Ver Ubicación
1. Haz clic en **"¿Dónde estamos?"**
2. Se abre un mapa centrado en Granada
3. Puedes hacer zoom y desplazarte

---

## 🐛 Troubleshooting

### Error: "Dependency requires at least JVM runtime version 11"
**Solución:** 
- Instala Java 11 o superior
- Configura `JAVA_HOME` en variables de entorno
- Reinicia Android Studio

### Error: "Network request failed"
**Solución:**
- Verifica que el servidor está activo en `https://dss-app-deezm.ondigitalocean.app/`
- Comprueba tu conexión a internet
- Para servidor local, cambia la URL en `ApiClient.kt`

### Error: "Product already in cart"
**Solución:**
- Es normal, no puedes duplicar productos en el carrito
- Primero elimina el producto del carrito

### Error: "Empty cart"
**Solución:**
- Agrega productos antes de hacer checkout
- Asegúrate de que la lista de productos no esté vacía

---

## 📝 Notas Importantes

- ✅ Las cookies de sesión se manejan automáticamente mediante OkHttp
- ✅ Las imágenes se cachean con Glide para mejor rendimiento
- ✅ El carrito es local (en memoria), se sincroniza con el servidor en checkout
- ✅ No requiere Google Maps API, usa OpenStreetMap
- ✅ Compatible con API 24+ (Android 7.0 y superior)

---

## 👨‍💻 Desarrollo

### Compilación en Modo Release
```bash
./gradlew assembleRelease
```

### Ejecutar Pruebas
```bash
./gradlew test
```

### Generar APK
```bash
./gradlew build
# El APK estará en: app/build/outputs/apk/debug/app-debug.apk
```

---

## 📄 Licencia

Este proyecto es parte de una práctica educativa.

---

## 🔗 Referencias

- [Documentación de Retrofit](https://square.github.io/retrofit/)
- [Documentación de Gson](https://github.com/google/gson)
- [Documentación de Android](https://developer.android.com/)
- [OpenStreetMap / OSMDroid](https://osmdroid.github.io/)
- [Glide Image Loader](https://bumptech.github.io/glide/)

---

**Última actualización:** 2025
**Versión:** 1.0

