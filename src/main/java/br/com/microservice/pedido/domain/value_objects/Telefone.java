package br.com.microservice.pedido.domain.value_objects;

public record Telefone(
    String numero,
    String ddd
    ){

    public Telefone {
        if(!numeroTelefoneValido(numero)){
            throw new IllegalArgumentException("numero de telofone está invalido");
        }
        if(ddd.length() != 2) {
            throw new IllegalArgumentException("ddd: {"+ddd+"} do telefone invalido");
        }
    }

    public Boolean numeroTelefoneValido(String numeroTelefone) {
        var numberFormt = numeroTelefone.replaceAll("(?<=\\d)-(?=\\d)", "");
        return numberFormt.length() == 9;
    }
}
