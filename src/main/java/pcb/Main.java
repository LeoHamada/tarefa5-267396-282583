package pcb;

import java.util.List;
import java.util.Scanner;
import pcb.modelo.Produto;
import pcb.servico.GerenciadorProducao;
import pcb.ui.LeitorEntrada;
import pcb.ui.MenuPrincipal;

/**
 * Ponto de entrada da aplicação da fábrica de PCBs. Monta a planta padrão, registra algumas
 * demandas de exemplo e inicia o menu interativo.
 *
 * @author 267396-282583
 */
public final class Main {

    private Main() {
    }

    /**
     * Inicia o sistema.
     *
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        GerenciadorProducao gerenciador = GerenciadorProducao.criarPlantaPadrao();
        List<Produto> produtos = gerenciador.getProdutos();
        gerenciador.criarDemanda(produtos.get(0), 100, 10);
        gerenciador.criarDemanda(produtos.get(1), 50, 5);
        gerenciador.criarDemanda(produtos.get(2), 30, 15);

        try (Scanner scanner = new Scanner(System.in)) {
            new MenuPrincipal(gerenciador, new LeitorEntrada(scanner)).executar();
        }
    }
}
