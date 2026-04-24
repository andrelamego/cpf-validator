package com.lamego.brValidator.annotation;

import com.lamego.brValidator.annotation.ValidCpf;

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
