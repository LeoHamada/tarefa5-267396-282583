# Fábrica de PCBs — MC322, Tarefa 5 267396 282583

Planta industrial de placas de circuito impresso (PCBs) com corte a laser, posicionamento de
componentes (pick-and-place), soldagem por refusão (reflow) e por onda, inspeção óptica (AOI) e teste
funcional. Gerencia demandas, estoque de matéria-prima, orçamento, máquinas, cenários de operação e
auditoria.

## Comandos
| Comando | Efeito |
|---|---|
| `./gradlew build` | compila e constrói o projeto |
| `./gradlew run` | executa o menu interativo |
| `./gradlew javadoc` | gera a documentação em `build/docs/javadoc/index.html` |


## Estrutura
```
src/main/java/pcb/
  Main.java                  ponto de entrada (monta a planta e demandas de exemplo)
  excecao/                   exceções de domínio (PlantaException e filhas)
  modelo/                    entidades, enums, estoque, orçamento, auditoria
    LinhaDeProducao          AGRUPADOR (agregação de máquinas)
    Maquina                  TODO (composição das partes abaixo)
    CabecoteDeProcesso       PARTE 1:1 (pacote, não exposta)
    SensorTermico            PARTE 1:1 (pacote, não exposta)
    Atuador                  PARTE 1:N (pacote, não exposta)
    Auditavel                diagnóstico em cascata (Linha -> Máquina -> Partes)
  estrategia/                EstrategiaProducao + FIFO, Maior urgência, Maior receita
  servico/GerenciadorProducao regras de negócio (sem interação com o usuário)
  ui/                        MenuPrincipal e LeitorEntrada (captura e exibe erros)
  util/Formatacao.java       formatação de moeda/números em pt-BR
docs/
  diagrama-uml.png|pdf       diagrama de classes (UML) da estrutura da Tarefa 5
  diagrama-uml.puml          fonte editável (PlantUML); diagrama-uml.dot = fonte Graphviz
```

## Tarefa 5: composição, ciclo de vida e acoplamento
- **Todo e Partes (composição estrita).** `Maquina` instancia no próprio construtor 1 `CabecoteDeProcesso`,
  1 `SensorTermico` e N `Atuador`es. O construtor só recebe valores primitivos. As partes são classes de
  pacote (sem `public`), então nenhum código fora de `pcb.modelo` consegue criá-las, guardá-las ou
  compartilhá-las: elas vivem e morrem com a máquina.
- **Agrupador (agregação).** `LinhaDeProducao` agrupa máquinas. Uma máquina pode ser transferida de uma
  linha para outra (menu 12) mantendo suas partes e seu desgaste, por isso a relação é agregação.
- **Cardinalidades.** 1:1 (cabeçote, sensor), 1:N (`List<Atuador>`, mínimo 1) e 1:N linha -> máquinas.
- **Deméter / Tell, Don't Ask.** Ninguém obtém uma parte: a `Maquina` oferece `horasPara`, `registrarUso`,
  `getEficiencia`, `getRendimentoQualidade`, `lerTemperaturaAtual`, `repararComponentes` etc. O menu fala
  com o `GerenciadorProducao`, que fala com linhas e máquinas.
- **Coleções protegidas.** `getMaquinas()` e `getLinhas()` devolvem `Collections.unmodifiableList`/lista
  imutável; as alterações passam por `adicionarMaquina`, `removerMaquina`, `transferirMaquina`, `criarLinha`.
- **Partes afetam o todo.** O desgaste (propagado por `registrarUso`) deixa o cabeçote e os atuadores mais
  lentos (a produção demora mais e custa mais), descalibra o sensor (mais refugo, menos receita) e, quando
  uma parte essencial esgota, a máquina para (`PARADA_POR_FALHA`) até o reparo geral (menu 10 -> 2).
- **Auditoria em cascata.** `Auditavel.gerarRelatorioDiagnostico()`: `GerenciadorProducao` -> `LinhaDeProducao`
  -> `Maquina` -> partes, em texto indentado (menu 13 mostra a planta toda; menu 10 -> 3 uma máquina).
