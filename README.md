# Sistema RentaCar — Equipo 7

Sistema de alquiler de vehiculos desarrollado en Java con Spring Boot y React.

---

## Integrantes y Modulos

| Modulo | Integrante | Responsabilidad |
|--------|-----------|-----------------|
| MOD01 | **Luis Fernando** | Base de datos, DAOs, estructura del proyecto |
| MOD02 | **Eze** | Gestion de Vehiculos (Flota) |
| MOD03 | **Ernesto** | Clientes y Reservas |
| MOD04 | **Keensey** | Contratos, Checkout y Checkin |
| MOD05 | **Cristofer** | Pagos, Danos y Mantenimiento |

---

## Estructura del Proyecto

```
RentaCar-Equipo7/
├── src/
│   ├── database/
│   │   └── DatabaseConnection.java        ← Luis Fernando
│   ├── models/
│   │   ├── Vehiculo.java                  ← Eze
│   │   ├── EstadoVehiculo.java            ← Eze
│   │   ├── Cliente.java                   ← Ernesto
│   │   ├── Reserva.java                   ← Ernesto
│   │   ├── Contrato.java                  ← Keensey
│   │   ├── EstadoContrato.java            ← Keensey
│   │   ├── Pago.java                      ← Cristofer
│   │   ├── Dano.java                      ← Cristofer
│   │   └── Mantenimiento.java             ← Cristofer
│   ├── dao/
│   │   └── (todos los DAOs)               ← Luis Fernando
│   ├── services/
│   │   ├── VehiculoService.java           ← Eze
│   │   ├── ClienteService.java            ← Ernesto
│   │   ├── ReservaService.java            ← Ernesto
│   │   ├── ContratoService.java           ← Keensey
│   │   ├── PagoService.java               ← Cristofer
│   │   ├── DanoService.java               ← Cristofer
│   │   └── MantenimientoService.java      ← Cristofer
│   └── menu/
│       ├── MainWindow.java                ← Luis Fernando
│       ├── FlotaPanel.java                ← Eze
│       ├── ClientesPanel.java             ← Ernesto
│       ├── ReservasPanel.java             ← Ernesto
│       ├── ContratosPanel.java            ← Keensey
│       ├── PagosPanel.java                ← Cristofer
│       ├── DanosPanel.java                ← Cristofer
│       └── MantenimientoPanel.java        ← Cristofer
├── sql/
│   └── rentacar_schema.sql                ← Luis Fernando
├── springboot/                            ← Backend Spring Boot (API REST)
├── frontend/                              ← Frontend React
└── docs/
    ├── Guia_Eze_MOD02.pdf
    ├── Guia_Ernesto_MOD03.pdf
    ├── Guia_Keensey_MOD04.pdf
    ├── Guia_Cristofer_MOD05.pdf
    └── Documentacion_RentaCar.docx
```

---

##  Como correr el proyecto

### Backend (Spring Boot)
1. Abrir IntelliJ IDEA
2. File → Open → seleccionar la carpeta `springboot/`
3. Editar `src/main/resources/application.properties` con tu usuario y password de MySQL
4. Crear la base de datos: `CREATE DATABASE rentacar;`
5. Click en Run ▶ o `Shift+F10`
6. Verificar que diga: `Started RentaCarApplication` en la consola

### Frontend (React)
```bash
cd frontend
npm install
npm run dev
```
Abrir en el navegador: http://localhost:3000

---

##  Requisitos

- Java JDK 17+
- IntelliJ IDEA Community
- Node.js 18+ LTS
- MySQL 8.x
- Git

---

## Reglas del equipo

- Cada integrante trabaja **solo en sus archivos**
- Antes de hacer push, verificar que el codigo **compila sin errores**
- Usar mensajes de commit descriptivos: `MOD02 - Eze: Vehiculo.java y VehiculoService.java`

---

## Como subir tus cambios

```bash
git add .
git commit -m "MODxx - TuNombre: descripcion de lo que hiciste"
git push
```

---

*Sistema RentaCar — Equipo 7 — Marzo 2026*
