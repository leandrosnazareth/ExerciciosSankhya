package br.com.leandrosnazareth;

import java.io.InputStream;

import org.apache.commons.fileupload.FileItem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.vo.EntityVO;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class Importar implements AcaoRotinaJava {

    @Override
    public void doAction(ContextoAcao contexto) throws Exception {
        // Verificar se o arquivo foi carregado
        FileItem arquivo = (FileItem) contexto.getParam("ARQUIVO_UPLOAD"); // Corrigir o nome do parâmetro
        if (arquivo == null) {
            throw new Exception("Nenhum arquivo foi enviado!");
        }

        // Processar o arquivo Excel
        InputStream inputStream = arquivo.getInputStream();
        importProductsFromExcel(inputStream);

        // Mensagem de sucesso
        contexto.setMensagemRetorno("Importação de produtos concluída com sucesso.");
    }

    // Método para processar o arquivo Excel
    public void importProductsFromExcel(InputStream excelInputStream) throws Exception {
        // Abrir o arquivo Excel
        Workbook workbook = new XSSFWorkbook(excelInputStream);

        // Selecionar a primeira planilha
        Sheet sheet = workbook.getSheetAt(0);

        // Iterar sobre as linhas da planilha
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                // Pular cabeçalhos
                continue;
            }

            String nome = row.getCell(0).getStringCellValue();
            String descricao = row.getCell(1).getStringCellValue();
            double preco = row.getCell(2).getNumericCellValue();
            int quantidade = (int) row.getCell(3).getNumericCellValue();

            // Inserir no banco de dados usando DynamicVO
            inserirProduto(nome, descricao, preco, quantidade);
        }

        // Fechar o workbook
        workbook.close();
        excelInputStream.close();
    }

    // Método para inserir um produto usando DynamicVO
    private void inserirProduto(String nome, String descricao, double preco, int quantidade) throws Exception {
        EntityFacade dwfFacade = EntityFacadeFactory.getDWFFacade();
        DynamicVO produtoVO = (DynamicVO) dwfFacade.getDefaultValueObjectInstance("Produto");

        // Preencher os campos do VO
        produtoVO.setProperty("NOMEPROD", nome);
        produtoVO.setProperty("DESCRPROD", descricao);
        produtoVO.setProperty("PRECO", preco);
        produtoVO.setProperty("QUANTIDADE", quantidade);

        // Salvar no banco
        dwfFacade.createEntity("Produto", (EntityVO) produtoVO);
    }
}