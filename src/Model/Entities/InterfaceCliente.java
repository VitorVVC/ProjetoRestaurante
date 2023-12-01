package Model.Entities;

public interface InterfaceCliente {
    Cliente metodoCriarContaCliente(); // Método obrigatorio para classe clientes criarem sua conta
    void metodoLoginCliente(); // Método obrigatorio para poder clientes logarem

    Pedidos pedido(); // Método de pedidos que qualquer cliente pode fazer que retorna um pedido
}
