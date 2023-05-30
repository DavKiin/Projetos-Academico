package common;

import java.io.*;

public class ArchiveUtils{
    public String diretório;

        //Leitor do arquivo
    public static String ReadArchive (String directory){
        String content = ""; //conteudo declarado null

        try {
            FileReader arch = new FileReader(directory); //leitor do diretório
            BufferedReader read = new BufferedReader(arch); //buffer de leitura do arquivo
            String line = ""; //linha declarada null;
            
            try {
                line = read.readLine();
                    //Enquanto ouver linha ele acrescentará novas linhas sob o conteúdo.
                while (line != null) {
                    content += line + "\n";
                    line = read.readLine();
                } 
                arch.close();

            } catch (IOException ex) {
                    //Leitura de arquivo.
                System.err.println("[ERROR:commom.ArchiveUtils.readArchive]: Não foi possível ler o arquivo");
                return "";
            }
        } catch (FileNotFoundException ex) {
                //Disponibilidade de arquivo.
            System.err.println("[ERROR:commom.ArchiveUtils.readArchive]: Arquivo não encontrado no diretório" + directory);
            return "";
        } 
            //Caso conteúdo expresso tenha ERRO retorno esperado. 
        if(content.contains("Erro")){
            return "";
        } else {
            return content.toString();
        }
    }

        //Leitura de arquivo
    public static boolean WriteArchive(String directory, String text) {
        
        try {
            FileWriter arch = new FileWriter(directory); // leitor do diretorio 
            PrintWriter printArch = new PrintWriter(arch); // metodo de escrita
                //Print dos textos em um arquivo
            printArch.println(text); // Escrita no arquivo.
            printArch.close();

            return true;
        } catch (IOException ex) {
                //Em casos de problemas com leitura.
            System.err.println("[ERROR:commom.ArchiveUtils.WriteArchive]:" + ex.getMessage());
            return false;
        }
    }
   
}
    
    