package models;

import java.sql.Date;

public class Dano {
    private int id;
    private int vehiculoId;
    private int clienteId;
    private String descripcion;
    private double costo;
    private Date fecha;
    private String estado;

    public Dano() {}

    public Dano(int vehiculoId, int clienteId, String descripcion, double costo, Date fecha, String estado) {
        this.vehiculoId = vehiculoId;
        this.clienteId = clienteId;
        this.descripcion = descripcion;
        this.costo = costo;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getVehiculoId() { return vehiculoId; }
    public void setVehiculoId(int vehiculoId) { this.vehiculoId = vehiculoId; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Dano{" + "id=" + id + ", vehiculoId=" + vehiculoId +
                ", descripcion='" + descripcion + '\'' + ", estado='" + estado + '\'' + '}';
    }
}