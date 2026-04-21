# VPStore — Interrapidísimo Controller APP

<p align="center">
  <img src="https://raw.githubusercontent.com/Enrique213-VP/VPStore---Interrapidisimo/refs/heads/main/app/src/main/screenshots/icon.png" alt="Interrapidísimo" width="320"/>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-2.2.10-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/Jetpack_Compose-2026.02-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white"/>
  <img src="https://img.shields.io/badge/Hilt-2.59.2-FF6F00?style=for-the-badge&logo=google&logoColor=white"/>
  <img src="https://img.shields.io/badge/Room-2.7.1-4CAF50?style=for-the-badge&logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Retrofit-2.11.0-48B983?style=for-the-badge"/>
</p>

<p align="center">
  Aplicación Android para el equipo de Interrapidísimo Colombia.<br/>
  Gestiona autenticación, control de versiones, esquema de datos y localidades de recogida.
</p>

<p align="center">
  <a href="../../releases/latest">
    <img src="https://img.shields.io/github/v/release/tu-usuario/VPStore?style=for-the-badge&logo=android&logoColor=white&label=Descargar%20APK&color=FF5C00"/>
  </a>
</p>

---

## Descarga

Puedes descargar la última versión del APK directamente desde la sección de [**Releases**](https://github.com/Enrique213-VP/VPStore---Interrapidisimo/releases/tag/1.0.0) sin necesidad de compilar el proyecto.

---

## Pantallas

| Login | Localidades |
|:---:|:---:|
| <img src="https://raw.githubusercontent.com/Enrique213-VP/VPStore---Interrapidisimo/refs/heads/main/app/src/main/screenshots/login.png?token=GHSAT0AAAAAADZUIWPE67SL6E7JZJG4YBPM2PHXAQQ" width="220"/> | <img src="https://raw.githubusercontent.com/Enrique213-VP/VPStore---Interrapidisimo/refs/heads/main/app/src/main/screenshots/localidades.png?token=GHSAT0AAAAAADZUIWPFVXUJS64IDYKE3VO42PHW74Q" width="220"/> |

---

## En acción

### Autenticación — Logcat

Trazas reales de OkHttp mostrando la request y response del login contra el servidor de pruebas.

<img src="https://raw.githubusercontent.com/Enrique213-VP/VPStore---Interrapidisimo/refs/heads/main/app/src/main/screenshots/logcat.png?token=GHSAT0AAAAAADZUIWPELTSIRX44HZTR6ROC2PHXAZA" alt="Logcat login" width="100%"/>

### API — Postman


<img src="https://raw.githubusercontent.com/Enrique213-VP/VPStore---Interrapidisimo/refs/heads/main/app/src/main/screenshots/postman.png?token=GHSAT0AAAAAADZUIWPF2JAPEYELHXF2XP7I2PHXBBA" alt="Postman" width="100%"/>

### Tests

Resultados de los unit tests y los instrumented tests desde Android Studio.

| Unit Tests |
|:---:|
| <img src="https://raw.githubusercontent.com/Enrique213-VP/VPStore---Interrapidisimo/refs/heads/main/app/src/main/screenshots/unitTest.png?token=GHSAT0AAAAAADZUIWPEBSD4WDFHGJ2CMFH62PHXBOQ" width="420"/> |

---

## Arquitectura

```
MVVM + Clean Architecture + Repository Pattern
```

```
app/
├── data/
│   ├── local/
│   │   ├── dao/           UserDao · DataTableDao · LocalityDao
│   │   ├── database/      VPStoreDatabase (Room)
│   │   └── entity/        UserEntity · DataTableEntity · LocalityEntity
│   ├── remote/
│   │   ├── api/           VersionApiService · AuthApiService
│   │   │                  DataTablesApiService · LocalitiesApiService
│   │   └── dto/           DTOs con @SerializedName
│   └── repository/        Implementaciones concretas
│
├── domain/
│   ├── common/            Resource<T> — Loading / Success / Error
│   ├── model/             Modelos de dominio puros
│   ├── repository/        Interfaces (contratos)
│   └── usecase/           CheckVersionUseCase · LoginUseCase
│                          GetTablesUseCase · GetLocalitiesUseCase
│
├── presentation/
│   ├── version/           VersionScreen + VersionViewModel
│   ├── login/             LoginScreen + LoginViewModel
│   ├── home/              HomeScreen + HomeViewModel
│   ├── tables/            TablesScreen + TablesViewModel
│   └── localities/        LocalitiesScreen + LocalitiesViewModel
│
├── di/                    NetworkModule · DatabaseModule · RepositoryModule
├── navigation/            VPStoreNavGraph + Routes
└── ui/theme/              Theme · Typography · InterColors
```

---

## Stack tecnológico

| Tecnología | Versión | Uso |
|---|---|---|
| Kotlin | 2.2.10 | Lenguaje principal |
| Jetpack Compose BOM | 2026.02.01 | UI declarativa |
| Hilt | 2.59.2 | Inyección de dependencias |
| Retrofit | 2.11.0 | Consumo de APIs REST |
| OkHttp | 4.12.0 | Cliente HTTP + logging |
| Room | 2.7.1 | Base de datos SQLite local |
| Navigation Compose | 2.9.0 | Navegación entre pantallas |
| Coroutines | 1.10.2 | Programación asíncrona |
| Material 3 | BOM | Sistema de diseño |

---

## APIs consumidas

### Control de versiones
```http
GET /apicontrollerpruebas/api/ParametrosFramework/
    ConsultarParametrosFramework/VPStoreAppControl
```
Compara la versión local con la del servidor e informa al usuario el estado.

### Autenticación
```http
POST /FtEntregaElectronica/MultiCanales/ApiSeguridadPruebas/
     api/Seguridad/AuthenticaUsuarioApp
```
Autentica al usuario con headers específicos. Guarda `Usuario`, `Identificacion` y `Nombre` en SQLite.

### Esquema de tablas
```http
GET /apicontrollerpruebas/api/SincronizadorDatos/ObtenerEsquema/true
Authorization: Bearer {TokenJWT}
```
Descarga el esquema de tablas del sincronizador y lo persiste en Room.

### Localidades
```http
GET /apicontrollerpruebas/api/ParametrosFramework/ObtenerLocalidadesRecogidas
```
Retorna las ciudades disponibles para recogida. Se cachea en Room para uso offline.

---

## Base de datos local

```
vpstore_db (SQLite — Room)
│
├── tbl_user_session       Usuario · Identificación · Nombre · Token
├── tbl_schema_tables      Nombre · Descripción · Registros · Activo
└── tbl_localities         CityCode · FullName · Department
```

La persistencia permite que la app funcione con datos en caché cuando no hay conexión.

---

## Principios SOLID aplicados

| Principio | Implementación |
|---|---|
| **S** Single Responsibility | Cada `UseCase` tiene una única responsabilidad |
| **O** Open/Closed | `Resource<T>` es extensible sin modificar código existente |
| **L** Liskov Substitution | Los repositorios son intercambiables — demostrado en tests con fakes |
| **I** Interface Segregation | 4 servicios API independientes, sin interfaces monolíticas |
| **D** Dependency Inversion | El dominio depende de interfaces, Hilt inyecta las implementaciones |

---

## Flujo de navegación

```
App abre
    │
    ├── Sesión en Room? ──→ SÍ ──→ HOME
    │
    └── NO ──→ VERSION CHECK ──→ LOGIN ──→ HOME
                                              │
                                    ┌─────────┴─────────┐
                                 TABLAS           LOCALIDADES
```

Al hacer **logout** se limpia Room y se regresa al inicio con el back stack vacío.

---

## Manejo de errores

```kotlin
sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String, val code: Int? = null) : Resource<Nothing>()
}
```

- Todos los repositorios usan `try/catch`
- HTTP ≠ 200 → `Resource.Error` con código y mensaje
- Sin conexión en Localidades → carga desde caché de Room
- Sin acceso en Tablas (401) → mensaje claro al usuario

---

## Tests

### Unit Tests — `ExampleUnitTest`
Sin emulador. Prueban lógica de negocio pura.

```
✓ version local menor que servidor retorna OUTDATED
✓ version local igual al servidor retorna UP_TO_DATE
✓ version local mayor al servidor retorna AHEAD
✓ cuando el servidor devuelve 401 el viewmodel expone error en uiState
✓ cuando TokenJWT es null se envia header Bearer vacio
✓ cuando hay token valido se envia en el header correctamente
✓ cuando el sync es exitoso el mensaje de exito aparece en uiState
```

### Instrumented Tests — `ExampleInstrumentedTest`
Requieren dispositivo o emulador. Prueban la UI con Compose Test.

```
✓ useAppContext
✓ versionScreen_muestraNombreApp
✓ versionScreen_muestraIndicadorDeCarga
✓ loginScreen_muestraBotonIniciarSesion
✓ loginScreen_muestraUsuarioDePrueba
✓ loginScreen_botonIniciarSesionEsClickeable
```

---

## Configuración del proyecto

### Requisitos
- Android Studio Meerkat o superior
- JDK 17
- Android SDK API 25+
- Gradle 9.x

### Pasos para ejecutar

```bash
git clone https://github.com/tu-usuario/VPStore.git
```

Luego abrir con Android Studio → **File → Sync Project with Gradle Files** → ejecutar en dispositivo o emulador.

### Correr los tests

```bash
# Unit tests (sin emulador)
./gradlew test

# Instrumented tests (requiere dispositivo conectado)
./gradlew connectedAndroidTest
```


---

<p align="center">
  Desarrollado como prueba técnica Android · Sergio Vargas · Interrapidísimo Colombia · 2025
</p>
