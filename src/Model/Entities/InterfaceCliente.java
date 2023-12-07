package Model.Entities;

public interface InterfaceCliente {
    Pessoa metodoCriarContaCliente(); // Método obrigatorio para classe clientes criarem sua conta
    Pessoa metodoLoginCliente(); // Método obrigatorio para poder clientes logarem

    void pedido(Cliente c); // Método de pedidos que qualquer cliente pode fazer que retorna um pedido
}
