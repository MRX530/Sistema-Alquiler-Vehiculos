package models;

import java.sql.Date;

public class Pago {
    private int id;
    private int reservaId;
    private double monto;
    private String metodoPago;
    private Date fecha;
    private String notas;

    public Pago() {}

    public Pago(int reservaId, double monto, String metodoPago, Date fecha, String notas) {
        this.reservaId = reservaId;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.fecha = fecha;
        this.notas = notas;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getReservaId() { return reservaId; }
    public void setReservaId(int reservaId) { this.reservaId = reservaId; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    @Override
    public String toString() {
        return "Pago{" + "id=" + id + ", reservaId=" + reservaId + ", monto=" + monto +
                ", metodoPago='" + metodoPago + '\'' + ", fecha=" + fecha + '}';
    }
}