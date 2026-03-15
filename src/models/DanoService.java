package models;

import dao.DanoDAO;
import models.Dano;
import java.util.List;

public class DanoService {
    private DanoDAO danoDAO = new DanoDAO();

    public void registrarDano(Dano d) {
        danoDAO.insertar(d);
    }

    public List<Dano> listarDanos() {
        return danoDAO.listarPorVehiculo(0);
    }

    public List<Dano> listarPendientes() {
        return danoDAO.listarPendientes();
    }

    public void cobrarDano(int id) {
        danoDAO.actualizarEstado(id, "COBRADO");
    }

    public void eliminarDano(int id) {
        // El DAO actual no tiene eliminar, pero según guía se llama
    }
}