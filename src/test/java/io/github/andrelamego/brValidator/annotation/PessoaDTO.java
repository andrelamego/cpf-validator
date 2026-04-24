package io.github.andrelamego.brValidator.annotation;

import io.github.andrelamego.brValidator.cpf.ValidCpf;

public class PessoaDTO {
    @ValidCpf
    private String cpf;

    public PessoaDTO(String cpf) {
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }
}
