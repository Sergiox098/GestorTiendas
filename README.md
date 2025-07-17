# 🏬 Gestor Tiendas – Aplicación Android para la Gestión de Comercios

**Gestor Tiendas** es una aplicación Android en desarrollo que permite a pequeños y medianos comerciantes gestionar de forma eficiente sus tiendas, productos e inventarios desde un solo lugar. Está diseñada para facilitar tareas administrativas y brindar acceso a información clave en tiempo real, ayudando a mejorar la toma de decisiones en los negocios.

---

## 📱 Características

- 📦 Registro, edición y visualización de productos con imágenes.
- 🏪 Administración de múltiples tiendas de forma independiente.
- 📊 Control básico de inventarios por tienda.
- 🔐 Autenticación de usuarios (Login y Registro).
- 🖼️ Carga y visualización de imágenes usando Firebase Storage + Glide.
- 💾 Persistencia local de datos como la tienda seleccionada mediante `SharedPreferences`.

> ⚠️ Actualmente en fase de desarrollo. Funciones como reportes, ventas con facturación y estadísticas aún no están implementadas.

---

## 🎯 Propósito

El propósito principal de esta app es ofrecer a los comerciantes una herramienta accesible y moderna para gestionar tiendas físicas o virtuales, automatizando procesos repetitivos y reduciendo errores asociados al manejo manual de la información.

---

## 🧱 Arquitectura y Tecnologías

- 📱 **Lenguajes:** Java (backend), XML (frontend)
- 🔧 **Arquitectura:** MVVM (Model - View - ViewModel)
- ☁️ **Backend:** Firebase Realtime Database, Firebase Storage
- 🔐 **Autenticación:** Firebase Authentication
- 🖼️ **Carga de imágenes:** Glide
- 🎨 **Diseño UI:** ConstraintLayout + Material Design
- 💡 **Persistencia local:** SharedPreferences
- 🔁 **Listas dinámicas:** RecyclerView

---

## 🚀 Instalación

Puedes descargar el APK directamente desde el siguiente enlace:

📦 [Descargar APK](https://drive.google.com/file/d/1wYs43dShQ0Tygis5JSfvcH946zb_DLL_/view?usp=drive_link)

---

## 📂 Estructura del Proyecto

> GestorTiendas/
> 
>├── activities/ ← Pantallas principales (Inicio, Productos, Registro, etc.)
> 
>├── adapters/ ← Adaptadores para RecyclerView
>
>├── models/ ← Clases modelo (Producto, Tienda, Usuario)
>
>├── repositories/ ← Comunicación con Firebase
>
>├── viewmodels/ ← Lógica de negocio y conexión entre vistas y datos
>
>├── utils/ ← Utilidades generales (helpers, constantes)
>
>└── layout/ ← Archivos XML para cada pantalla
