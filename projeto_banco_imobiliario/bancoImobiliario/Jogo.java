package bancoImobiliario;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Jogo {

	Scanner scan = new Scanner(System.in);
	
	private int nJogadores;
	private List<String> peoes = Arrays.asList("preto", "branco", "vermelho", "verde", "azul", "amarelo", "laranja", "rosa");
	private List<String> peoesEscolhidos = new ArrayList<>();
	private List<Jogador> jogadores = new ArrayList<>();
	
	public void criarJogo() throws NumeroJogadoresException {
		
		System.out.println("##################################################");
		System.out.println("BANCO IMOBILIÁRIO");
		System.out.println("Welcome to the best game on board!");
		System.out.println("##################################################\n");
		System.out.println("START:\n");
		System.out.print("Digite a quantidade de jogadores: ");
		
		nJogadores = Integer.parseInt(scan.nextLine());
		peoesEscolhidos.clear();
		jogadores.clear();
		
		if (nJogadores >= 2 && nJogadores <= 8) {
			
			String nome = "";
			String peao = "";

			for (int i = 0; i < nJogadores; i++) {
				
				//Nome do jogador
				System.out.println("Digite o nome do jogador " + (i+1) + ": ");
				nome = scan.nextLine();
				
				//Escolhe a cor do peão do jogador
				System.out.println("Escolha a cor do peão do jogador " + (i+1) + ": ");
				System.out.println("Cores disponíveis:");
				System.out.println(listarPeoes());
				
				peao = escolherPeao();
				
				Jogador j = new Jogador(nome, peao);
				jogadores.add(j);
				//j.setPeao("[" + peao + "]");
				
				
			}
			
			System.out.println("O jogo vai comeaçar. Aproveitem!");
			
		} else {
			throw new NumeroJogadoresException("Número de jogadores inválido. Escolha entre 2 e 8 jogadores!");
		}
	}
	
	public void iniciarJogo() {
		
		
		while (nJogadores>=2) {
			
			for (int i = 0; i < nJogadores; i++) {
				System.out.println("A vez é do jogador " + jogadores.get(i).getNome() + "(" + jogadores.get(i).getPeao() + ").");
				System.out.println("Escolha uma opção: " + jogadores.get(i).getOpcoes());

				String opcao = scan.nextLine().toLowerCase();
				
				switch (opcao) {
				
				case "jogar":
					int dado1, dado2;
					
					dado1 = rolarDado();
					dado2 = rolarDado();
					if(dado1+dado2+jogadores.get(i).getPosicaoAtual()>= 40) {
						jogadores.get(i).setPosicaoAtual(dado1+dado2+jogadores.get(i).getPosicaoAtual()-40);
					}
					System.out.println("Jogador: " + jogadores.get(i).getNome() + "\nPeão: " + jogadores.get(i).getPeao() + "\n"
							+ "Dado 1: " + dado1 + "\nDado 2: " + dado2 + "\nAvançou para a casa: " + 
							(dado1+dado2+jogadores.get(i).getPosicaoAtual()));
					break;
				case "sair":
					System.out.print("Deseja realmente sair do jogo? ");
					String decisao = scan.nextLine().toLowerCase();
					if (decisao.equals("sim")) { 
						jogadores.remove(i);
						nJogadores-= 1;
					}else {
						--i;
					}
					
						
					break;
					
				case "status":
					jogadores.get(i).obterStatus();
					break;
					
				default:
					System.out.println("Opção inválida!");
				}
		}	
		
			}
		System.out.println("Fim de jogo!");
	}
	
	private String escolherPeao() {
		String corPeao = scan.nextLine().toLowerCase();	
		while (!peoes.contains(corPeao)) {
			System.out.println("Essa cor não existe. Escolha uma cor dentre as disponíveis!");
			corPeao = corPeao.replaceAll(corPeao, scan.nextLine().toLowerCase());
		}
		while (peoesEscolhidos.contains(corPeao)) {
			System.out.println("Essa cor já foi escolhida. Escolha outra!");
			corPeao = corPeao.replaceAll(corPeao, scan.nextLine().toLowerCase());
		}
		peoesEscolhidos.add(corPeao);
		return corPeao;
	}

	public String listarPeoes() {
		String p = "";
		for (String peao : peoes) {
			if (!peoesEscolhidos.contains(peao)) p += "[" + peao + "] ";
		}
		
		return p.trim();
	}
	
	private int rolarDado() {
		Random rand = new Random();
		int d = 1 + rand.nextInt(6);
		return d;
	}
}

