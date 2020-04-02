package br.com.pabloraimundo.util;

public class ExceptionsMessages {

    public static String ErroAoAtualizarSubStatus(Exception e){
        return MessageLog.Horario() + "Erro ao tentar atualizar valor do Sub-Status. Erro: " + e ;
    }

    public static String ErroAoPostarUmcomentario(Exception e){
        return MessageLog.Horario() + "Erro ao tentar postar um comentário. Erro: " + e;
    }

    public static String ErroAoReceberValorDoCustomField(Exception e){
        return MessageLog.Horario() + "Erro ao receber valor do customField. Erro: " + e;
    }

    public static String ErroAoBuscarArquivo(Exception e){
        return MessageLog.Horario() + "Erro ao buscar arquivo, verifique se o caminho está correto";
    }

    public static String ErroAoConverterArquivoJson(Exception e){
        return MessageLog.Horario() + "Erro ao tentar converter arquivo json";
    }
}
