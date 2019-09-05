package com.exercicios_banco_imobiliario.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import com.exercicios_banco_imobiliario.enums.Peao;
import com.exercicios_banco_imobiliario.exceptions.CorDePeaoRepetidaException;
import com.exercicios_banco_imobiliario.exceptions.CorInexistenteExpcetion;
import com.exercicios_banco_imobiliario.exceptions.NumeroJogadoresExcedenteException;
import com.exercicios_banco_imobiliario.exceptions.NumeroJogadoresInsuficienteException;

/**
 * Classe para objeto do tipo jogo, onde est�o contidos, valores e m�todos para
 * o mesmo.
 * 
 * @author Carlos Eduardo, Alefe, Aisllan e Artur
 */

public class Jogo {

	private int numeroDeJogadores;
	private Tabuleiro tabuleiro = new Tabuleiro();
	private List<Jogador> jogadores = new ArrayList<Jogador>();
	private List<Carta> cartas = tabuleiro.getCartas();
	private Stack<Carta> cartasSorteReves = tabuleiro.getCartasSorteReves();
	private List<Peao> coresExistentes = Arrays.asList(Peao.values());
	private List<Peao> coresIndisponiveis = new ArrayList<>();
	private List<Integer> indicesEspeciais = Arrays.asList(0, 10, 18, 20, 24, 30);
	private List<Integer> indicesSorteReves = Arrays.asList(2, 12, 16, 22, 27, 37);
	private List<Integer> titulosVendidos = new ArrayList<>();
	int contadorDeRodadas = 0;
	
	Scanner scan = new Scanner(System.in);

	/**
	 * M�todo encarregado de criar um novo jogo, cadastrando de 2 a 8 jogagdores. E
	 * que la�ar� uma Exception caso o n�mero de jogadores for menor que 2 ou maior
	 * que 8.
	 * 
	 * @param num - N�mero de jogadores
	 * @throws NumeroJogadoresInsuficienteException N�mero de jogadores insuficiente
	 * @throws NumeroJogadoresExcedenteException    N�mero de jogadores excedeu
	 *                                              limite
	 * @author Carlos Eduardo, Alefe, Aisllan e Artur                                             
	 */

	public void criaJogadores(int num) throws NumeroJogadoresInsuficienteException, NumeroJogadoresExcedenteException {

		if (num < 2)
			throw new NumeroJogadoresInsuficienteException(
					"N�mero de jogadores insuficiente. O jogo deve ter de 2 a 8 jogadores!");
		else if (num > 8)
			throw new NumeroJogadoresExcedenteException(
					"N�mero de jogadores excedente. O jogo deve ter de 2 a 8 jogadores!");
		else {
			this.numeroDeJogadores = num;
		}
	}

	public void escolherPeao(Peao cor, String nome) throws CorDePeaoRepetidaException, CorInexistenteExpcetion {
				
		if (coresIndisponiveis.contains(cor)) {
			throw new CorDePeaoRepetidaException("Cor j� escolhida. Escolha outra cor!");
		} else if (!coresExistentes.contains(cor)) {
			throw new CorInexistenteExpcetion("Cor inexistente. Escolha outra cor dentre as op��es!");
		} else {
			coresIndisponiveis.add(cor);
			Jogador jogador = new Jogador(nome, cor);
			this.jogadores.add(jogador);
		}
	}

	public void criaCartasDoJogo() {
		tabuleiro.pilhaDeCartas.criaPilhaDeCartas();
		tabuleiro.pilhaDeCartas.criaPilhaDeCartasSorteReves();
		cartas = tabuleiro.getCartas();
		cartasSorteReves = tabuleiro.getCartasSorteReves();
	}
	
	/**
	 * M�todo utilizado para calcular o avan�o de cada jogador a cada rodada
	 * 
	 * @param posicaoAntiga
	 * @param dado1
	 * @param dado2
	 * @return retorna o posicaoAposJogada de avan�o
	 * @author @author Carlos Eduardo, Alefe, Aisllan e Artur
	 **/

	public int anda(int posicaoAntiga, int dado1, int dado2) {
		int posicaoAposJogada = 0;
		posicaoAposJogada = (posicaoAntiga + dado1 + dado2) % 40;
		return posicaoAposJogada;
	}

	/**
	 * M�todo que realiza a negocia��o de compra de t�tulo de propriedade
	 * 
	 * @param titulo
	 * @param jogadorDaVez
	 * @author @author Carlos Eduardo, Alefe, Aisllan e Artur
	 **/

	public void negociaTituloDePropriedade(Carta titulo, Jogador jogadorDaVez, String compra) {
		
		if (compra.toLowerCase().equals("sim")) {
			jogadorDaVez.adicionaTitulos(titulo);
			this.titulosVendidos.add(jogadorDaVez.getPosicaoAtual());
			jogadorDaVez.setDinheiro(jogadorDaVez.getDinheiro() - titulo.getPreco());
		} else if (compra.toLowerCase().equals("n�o")) {
			System.out.println("O jogo continua..."); 
			} 
	}
	/**
	 * M�todo que verifica se determinada Propriedade ou Companhia possui um propriet�rio
	 * 
	 * @param jogadorDaVez
	 * @return Retorna verdadeiro caso Propriedade ou Companhia tenha um propriet�rio, ou falso se Propriedade ou Companhia n�o tenha um propriet�rio
	 * @author @author Carlos Eduardo, Alefe, Aisllan e Artur
	 */
	public boolean verificaProprietario(Jogador jogadorDaVez ) {
		return (titulosVendidos.contains(jogadorDaVez.getPosicaoAtual()));
	}
	
	/**
	 * M�todo que realiza a negocia��o de compra de Companhias
	 * 
	 * @param titulo
	 * @param jogadorDaVez
	 * @author Carlos Eduardo, Alefe, Aisllan e Artur
	 **/
	public void negociaCompanhia(Carta companhia, Jogador jogadorDaVez, String compra) {
		if (compra.toLowerCase().equals("sim")) {
			jogadorDaVez.adicionaCompanhia(companhia);
			this.titulosVendidos.add(jogadorDaVez.getPosicaoAtual());
			jogadorDaVez.setDinheiro(jogadorDaVez.getDinheiro() - companhia.getPreco());
		} else if (compra.toLowerCase().equals("n�o")) {
			System.out.println("O jogo continua..."); 
			} 
	}
	
	/**
	 * M�todo respons�vel por realizar o pagamento do aluguel da Companhia
	 * 
	 * @param companhia
	 * @param jogadorDaVez
	 * @param d1
	 * @param d2
	 * 
	 * @author @author Carlos Eduardo, Alefe, Aisllan e Artur
	 */
	public void pagaAluguelDaCompanhia(Companhia companhia, Jogador jogadorDaVez, int d1, int d2) {
		
		int aluguel = companhia.getMultiplicador()* (d1+d2);
		jogadorDaVez.setDinheiro(jogadorDaVez.getDinheiro() - aluguel);
		
	}
	
	public List<Carta> embaralhador(){
		Collections.shuffle(cartasSorteReves);
		return cartasSorteReves;

}
	public Carta retornaUmaCarta(){
		Carta card = cartasSorteReves.get(0);
		cartasSorteReves.remove(0);
		if(card.getId()== 5) {
			return card;
		}else {
		cartasSorteReves.add(card);
		return card;
		}
	  
	}

	public Carta retiraCartaDaPilha(int posicao) {
		return cartas.get(posicao);
	}

	public List<Jogador> getJogadores() {
		return jogadores;
	}
	
	public List<Peao> getCoresIndisponiveis(){
		return coresIndisponiveis;
	}

	public void setNumeroDeJogadores(int numeroDeJogadores) {
		this.numeroDeJogadores = numeroDeJogadores;
	}
	
	public int getNumeroDeJogadores() {
		return numeroDeJogadores;
	}

	public List<Carta> getCartas() {
		return cartas;
	}

	public Stack<Carta> getCartasSorteReves() {
		return cartasSorteReves;
	}

	public List<Integer> getIndicesEspeciais() {
		return indicesEspeciais;
	}

	public List<Integer> getIndicesSorteReves() {
		return indicesSorteReves;
	}
}
