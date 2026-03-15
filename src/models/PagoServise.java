package models;

import dao.PagoDAO;
import models.Pago;
import java.util.List;

public class PagoService {
    private PagoDAO pagoDAO = new PagoDAO();

    public void registrarPago(Pago p) {
        pagoDAO.insertar(p);
    }

    public List<Pago> listarPagos() {
        return pagoDAO.listarPorReserva(0);
    }

    public void eliminarPago(int id) {
        // El DAO actual no tiene eliminar, pero según guía se llama
        // (puedes agregar el método en PagoDAO si es necesario)
    }

    public List<Pago> buscarPorReserva(int reservaId) {
        return pagoDAO.listarPorReserva(reservaId);
    }
}
