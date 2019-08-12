package com.exercicios_banco_imobiliario_alternativo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.junit.platform.commons.util.StringUtils;

public class Jogo {

	private int numeroDeJogadores;
	private Tabuleiro tabuleiro = new Tabuleiro();
	private int valoresDuplicados;
	int contadorDeRodadas = 0;
	private List<Jogador> jogadores = new ArrayList<Jogador>();
	private List<TituloDePropriedade> titulosDePropriedade = new ArrayList<>();
	private List<Companhia> companhias = new ArrayList<>();
	private List<Integer> titulosVendidos = new ArrayList<>();

	// CRIAR PILHA DE CARTAS SORTE OU REVES E IMPLEMENTAR NO M�TODO JOGAR

	Scanner scan = new Scanner(System.in);

	public void criaJogo(int num) throws NumeroJogadoresInsuficienteException, NumeroJogadoresExcedenteException {

		if (num < 2)
			throw new NumeroJogadoresInsuficienteException(
					"N�mero de jogadores insuficiente. O jogo deve ter de 2 a 8 jogadores!");
		else if (num > 8)
			throw new NumeroJogadoresExcedenteException(
					"N�mero de jogadores excedente. O jogo deve ter de 2 a 8 jogadores!");
		else {
			this.numeroDeJogadores = num;
			List<Integer> coresEscolhidas = new ArrayList<>();

			for (int i = 0; i < numeroDeJogadores; i++) {

				System.out.print("Digite o nome do jogador " + (i + 1) + ": \n");
				String nome = scan.nextLine();

				System.out.println("Escolha o n�mero correspondente a cor do pe�o de acordo com as cores dispo�veis:");
				Peao.listaCores();

				int cor = Integer.parseInt(scan.nextLine());
				boolean escolhida = false;

				while (!escolhida) {
					if (coresEscolhidas.contains(cor)) {
						System.out.println("Cor j� escolhida. Escolha outra cor!");
						cor = Integer.parseInt(scan.nextLine());
					} else if (cor < 1 || cor > 8) {
						System.out.println("Cor inexistente. Escolha outra cor!");
						cor = Integer.parseInt(scan.nextLine());
					} else {
						coresEscolhidas.add(cor);
						Peao peao = escolhePeao(cor);
						Jogador jogador = new Jogador(nome, peao);
						this.jogadores.add(jogador);
						escolhida = true;
					}
				}

			}

			tabuleiro.criaCartas();
			System.out.println("\n*** O BANCO IMOBILI�RIO VAI COME�AR, APROVEITE! ***\n");
		}
	}

	public void iniciaJogo() {
		
		while (this.numeroDeJogadores > 1) {
			contadorDeRodadas++;
			System.out.println("\n===== RODADA " + contadorDeRodadas + " =====\n");
			
			for (int i = 0; i < this.jogadores.size(); i++) {
				Jogador jogadorDaVez = jogadores.get(i);
				if (contadorDeRodadas == 2 && !jogadorDaVez.getOpcoes().contains("status")) jogadorDaVez.adicionaOpcoes("status");
				System.out.println("A vez � do jogador " + jogadorDaVez.getNome() + "(" + jogadorDaVez.getPeao() + ")");
				System.out.println("Comandos dispon�veis: " + jogadorDaVez.imprimeOpcoes());
				System.out.println("Digite o comando desejado:");
				
				String opcao = scan.nextLine();

				while (!jogadorDaVez.getOpcoes().contains(opcao.toLowerCase())) {
					System.out.println("Op��o inv�lida!");
					opcao = opcao.replaceAll(opcao, scan.nextLine());
				}

				switch (opcao.toLowerCase()) {

				case "jogar":

					Random rand = new Random();

					int dado1 = 1 + rand.nextInt(6);
					int dado2 = 1 + rand.nextInt(6);
					int movimento = rolaDados(jogadorDaVez.getPosicaoAtual(), dado1, dado2);
					jogadorDaVez.setPosicaoAtual(movimento);

					//Objetos para compara��o
					TituloDePropriedade titulo = new TituloDePropriedade();
					Companhia companhia = new Companhia();
					CartaSorteReves sorteReves = new CartaSorteReves();
					
					if (tabuleiro.getCartas().get(movimento).getClass().equals(titulo.getClass())) {
						
						titulo = (TituloDePropriedade)tabuleiro.getCartas().get(jogadorDaVez.getPosicaoAtual());
						
						System.out.println("O jogador " + jogadorDaVez.getNome() + "(" + jogadorDaVez.getPeao()
						+ ") tirou " + dado1 + ", " + dado2 + " e o pe�o avan�ou para a casa " + movimento
						+ " - " + ((TituloDePropriedade) tabuleiro.getCartas().get(movimento)).getNome());
						
						if (!titulosVendidos.contains(jogadorDaVez.getPosicaoAtual())) {
							System.out.println("O t�tulo da propriedade " + titulo.getNome() + " est� dispon�vel por " + titulo.getPreco());
							System.out.println("Voc� possui " + jogadorDaVez.getDinheiro());
							negociaTituloDePropriedade(titulo, jogadorDaVez);
						} else {
							System.out.println("O t�tulo da propriedade " + titulo.getNome() + " n�o est� dispon�vel para compra!");//Alterar posteriormente
						}
												
					} else if (tabuleiro.getCartas().get(movimento).getClass().equals(companhia.getClass())) {
						System.out.println("O jogador " + jogadorDaVez.getNome() + "(" + jogadorDaVez.getPeao()
						+ ") tirou " + dado1 + ", " + dado2 + " e o pe�o avan�ou para a casa " + movimento
						+ " - " + ((Companhia) tabuleiro.getCartas().get(movimento)).getNome());
					} else if (tabuleiro.getCartas().get(movimento).getClass().equals(sorteReves.getClass())) {
						System.out.println("O jogador " + jogadorDaVez.getNome() + "(" + jogadorDaVez.getPeao()
						+ ") tirou " + dado1 + ", " + dado2 + " e o pe�o avan�ou para a casa " + movimento
						+ " - " + ((CartaSorteReves) tabuleiro.getCartas().get(movimento)).getDescricao());
					}
					
					break;
					
				case "sair":
					
					System.out.println("Voc� realmente quer sair do jogo (sim/n�o)?");
					String escolha = scan.nextLine();
					boolean escolhido = false;
					
					do {
						
						if (escolha.toLowerCase().equals("sim")) {
							this.numeroDeJogadores--;
							jogadores.remove(i--);
							escolhido = true;
							
							if (this.numeroDeJogadores < 2) {
								System.out.println("N�mero de jogadores insuficiente para continuar o jogo.\nJogo encerrado!");
							}
							
						} else if (escolha.toLowerCase().equals("n�o")){
							System.out.println("Segue o jogo...");
							escolhido = true;
						} else {
							System.out.println("Op��o inv�lida! Escolha sim ou n�o:");
							escolha = escolha.replaceAll(escolha, scan.nextLine());
						}
						
					} while(!escolhido);
					
					break;
					
				case "status":
					
					System.out.println(jogadorDaVez.status());
					i--;
					
					break;

				}// fim do switch
			} // fim do for
		} // fim do while
	}

	public int rolaDados(int posicaoAntiga, int dado1, int dado2) {
		int movimento = 0;
		movimento = (posicaoAntiga + dado1 + dado2) % 40;
		return movimento;
	}

	public Peao escolhePeao(int cor) {
		Peao peao = null;
		switch (cor) {
		case 1:
			peao = Peao.PRETO;
			break;
		case 2:
			peao = Peao.BRANCO;
			break;
		case 3:
			peao = Peao.VERMELHO;
			break;
		case 4:
			peao = Peao.VERDE;
			break;
		case 5:
			peao = Peao.AZUL;
			break;
		case 6:
			peao = Peao.AMARELO;
			break;
		case 7:
			peao = Peao.LARANJA;
			break;
		case 8:
			peao = Peao.ROSA;
			break;
		}
		return peao;
	}
	
	public void negociaTituloDePropriedade(TituloDePropriedade titulo, Jogador jogadorDaVez) {
		
		if (jogadorDaVez.getDinheiro() >= titulo.getPreco()) {
			System.out.println("Voc� deseja comprar " + titulo.getNome() + " (sim/n�o)?");
			String compra = scan.nextLine();
		
			if (compra.toLowerCase().equals("sim")) {
				jogadorDaVez.adicionaTitulos(jogadorDaVez.getPosicaoAtual());
				this.titulosVendidos.add(jogadorDaVez.getPosicaoAtual());
				jogadorDaVez.setDinheiro(jogadorDaVez.getDinheiro() - titulo.getPreco());
				System.out.println("Parab�ns! Voc� comprou a propriedade: " + titulo.getNome());
			} else if (compra.toLowerCase().equals("n�o")) {
				System.out.println("O jogo continua...");
			}

		}
	}
	
}
