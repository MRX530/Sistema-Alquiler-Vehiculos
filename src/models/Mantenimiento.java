package models;

import java.sql.Date;

public class Mantenimiento {
    private int id;
    private int vehiculoId;
    private String tipo;
    private int km;
    private int proximoKm;
    private Date fecha;
    private Date proximaFecha;
    private double costo;
    private String notas;

    public Mantenimiento() {}

    public Mantenimiento(int vehiculoId, String tipo, int km, int proximoKm, Date fecha,
                         Date proximaFecha, double costo, String notas) {
        this.vehiculoId = vehiculoId;
        this.tipo = tipo;
        this.km = km;
        this.proximoKm = proximoKm;
        this.fecha = fecha;
        this.proximaFecha = proximaFecha;
        this.costo = costo;
        this.notas = notas;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getVehiculoId() { return vehiculoId; }
    public void setVehiculoId(int vehiculoId) { this.vehiculoId = vehiculoId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getKm() { return km; }
    public void setKm(int km) { this.km = km; }

    public int getProximoKm() { return proximoKm; }
    public void setProximoKm(int proximoKm) { this.proximoKm = proximoKm; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Date getProximaFecha() { return proximaFecha; }
    public void setProximaFecha(Date proximaFecha) { this.proximaFecha = proximaFecha; }

    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    @Override
    public String toString() {
        return "Mantenimiento{" + "id=" + id + ", vehiculoId=" + vehiculoId +
                ", tipo='" + tipo + '\'' + ", km=" + km + ", fecha=" + fecha + '}';
    }
}