package br.com.leandrosnazarerth;

import java.math.BigDecimal;
import java.sql.Timestamp;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;

public class BotaoAcao implements AcaoRotinaJava {

	// mensagem exibir na tela
//	@Override
//	public void doAction(ContextoAcao arg0) throws Exception {
//		arg0.setMensagemRetorno("Teste botão de ação ok!");
//
//	}

//	ADD REGISTRO NO BANCO
	@Override
	public void doAction(ContextoAcao contextoAcao) throws Exception {
		Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
		Registro cabecalho = contextoAcao.novaLinha("TGFCAB");
		 cabecalho.setCampo("NUNOTA", new BigDecimal("0"));
		cabecalho.setCampo("CODEMP", new BigDecimal("99"));
		cabecalho.setCampo("CODPARC", new BigDecimal("270022"));
		cabecalho.setCampo("CODTIPOPER", new BigDecimal("1188"));
		cabecalho.setCampo("CODTIPVENDA", new BigDecimal("40"));
		cabecalho.setCampo("DTNEG", "");
		// SALVAR NOTA
		cabecalho.save();

		// RETORNAR O NUMERO DA NOTA SALVA
		String numeroUnico = cabecalho.getCampo("NUNOTA").toString();

		contextoAcao.setMensagemRetorno("O número da nota é " + numeroUnico);
	}
}