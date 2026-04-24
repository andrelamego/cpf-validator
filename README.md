# br-validator

Starter Spring Boot para validação de documentos brasileiros, e-mail e senha com Bean Validation (`jakarta.validation`).

Com `br-validator`, você valida CPF, CNPJ, e-mail e senha de forma declarativa em DTOs, sem repetir regra de negócio em cada projeto.

## Sumário

- [Principais funcionalidades](#principais-funcionalidades)
- [Instalação](#instalação)
- [Como usar](#como-usar)
  - [Validação declarativa em DTO](#1-validação-declarativa-em-dto)
  - [Parâmetros das annotations](#2-parâmetros-das-annotations)
  - [Uso direto via service](#3-uso-direto-via-service)
  - [Exemplo com controller](#4-exemplo-com-controller)
  - [Exemplo de request](#5-exemplo-de-request)
- [Matriz de compatibilidade](#matriz-de-compatibilidade)
- [Tratamento de erro](#tratamento-de-erro)
- [FAQ](#faq)
- [Tecnologias](#tecnologias)
- [Roadmap](#roadmap)
- [Licença](#licença)

## Principais funcionalidades

- Annotation `@ValidCpf` para validação declarativa.
- Annotation `@ValidCnpj` para validação declarativa.
- Annotation `@ValidEmail` para validação declarativa.
- Annotation `@ValidPassword` para validação declarativa.
- Validação automática com Bean Validation.
- Serviços reutilizáveis para CPF, CNPJ, e-mail e senha.
- Formatação de CPF e CNPJ.
- Geração de CPF e CNPJ válidos para testes.
- Auto-configuração Spring Boot para uso como dependência.

## Instalação

Adicione no `pom.xml` do seu projeto:

```xml
<dependencies>
    <dependency>
        <groupId>io.github.andrelamego</groupId>
        <artifactId>br-validator</artifactId>
        <version>1.3.0</version>
    </dependency>
</dependencies>
```

## Como usar

### 1) Validação declarativa em DTO

```java
package com.example.demo.dto;

import io.github.andrelamego.brValidator.cpf.ValidCpf;
import io.github.andrelamego.brValidator.cnpj.ValidCnpj;
import io.github.andrelamego.brValidator.email.ValidEmail;
import io.github.andrelamego.brValidator.password.ValidPassword;

public class DocumentoRequest {

  @ValidCpf(message = "CPF inválido", formatted = true, required = true)
  private String cpf;

  @ValidCnpj(message = "CNPJ inválido", formatted = true, required = false)
  private String cnpj;

  @ValidEmail(
          message = "E-mail inválido",
          required = true,
          allowPlusAlias = true,
          disposableAllowed = false,
          allowedDomains = {"empresa.com"},
          blockedDomains = {"bloqueado.com"}
  )
  private String email;

  @ValidPassword(
          message = "Senha inválida",
          minLength = 8,
          maxLength = 32,
          requireUppercase = true,
          requireLowercase = true,
          requireNumber = true,
          requireSpecialChar = true,
          blockWhitespace = true
  )
  private String senha;

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getCnpj() {
    return cnpj;
  }

  public void setCnpj(String cnpj) {
    this.cnpj = cnpj;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }
}
```

### 2) Parâmetros das annotations

#### `@ValidCpf` e `@ValidCnpj`

| Parâmetro | Tipo | Default | Descrição |
|---|---|---|---|
| `message` | `String` | `CPF inválido.` / `CNPJ inválido.` | Mensagem de erro |
| `formatted` | `boolean` | `true` | Aceita documento com máscara |
| `required` | `boolean` | `true` | Define se o campo é obrigatório |
| `groups` | `Class<?>[]` | - | Bean Validation |
| `payload` | `Payload[]` | - | Bean Validation |

#### `@ValidEmail`

| Parâmetro | Tipo | Default | Descrição |
|---|---|---|---|
| `message` | `String` | `Email inválido.` | Mensagem de erro |
| `required` | `boolean` | `true` | Define se o campo é obrigatório |
| `allowPlusAlias` | `boolean` | `true` | Aceita e-mails com alias `+` (ex: `nome+tag@dominio.com`) |
| `disposableAllowed` | `boolean` | `true` | Aceita domínios de e-mail descartável |
| `allowedDomains` | `String[]` | `{}` | Lista de domínios permitidos |
| `blockedDomains` | `String[]` | `{}` | Lista de domínios bloqueados |
| `groups` | `Class<?>[]` | - | Bean Validation |
| `payload` | `Payload[]` | - | Bean Validation |

#### `@ValidPassword`

| Parâmetro | Tipo | Default | Descrição |
|---|---|---|---|
| `message` | `String` | `Senha inválida.` | Mensagem de erro |
| `required` | `boolean` | `true` | Define se o campo é obrigatório |
| `minLength` | `int` | `8` | Tamanho mínimo da senha |
| `maxLength` | `int` | `64` | Tamanho máximo da senha |
| `requireUppercase` | `boolean` | `true` | Exige pelo menos uma letra maiúscula |
| `requireLowercase` | `boolean` | `true` | Exige pelo menos uma letra minúscula |
| `requireNumber` | `boolean` | `true` | Exige pelo menos um número |
| `requireSpecialChar` | `boolean` | `true` | Exige pelo menos um caractere especial |
| `blockWhitespace` | `boolean` | `true` | Bloqueia espaços em branco na senha |
| `groups` | `Class<?>[]` | - | Bean Validation |
| `payload` | `Payload[]` | - | Bean Validation |

### 3) Uso direto via service

```java
import io.github.andrelamego.brValidator.cpf.CpfValidationService;
import io.github.andrelamego.brValidator.cnpj.CnpjValidationService;
import io.github.andrelamego.brValidator.email.EmailValidationService;
import io.github.andrelamego.brValidator.password.PasswordValidationService;
import org.springframework.stereotype.Service;

@Service
public class DocumentoService {

  private final CpfValidationService cpfValidationService;
  private final CnpjValidationService cnpjValidationService;
  private final EmailValidationService emailValidationService;
  private final PasswordValidationService passwordValidationService;

  public DocumentoService(
          CpfValidationService cpfValidationService,
          CnpjValidationService cnpjValidationService,
          EmailValidationService emailValidationService,
          PasswordValidationService passwordValidationService
  ) {
    this.cpfValidationService = cpfValidationService;
    this.cnpjValidationService = cnpjValidationService;
    this.emailValidationService = emailValidationService;
    this.passwordValidationService = passwordValidationService;
  }

  public void validar() {
    boolean cpfValido = cpfValidationService.isValid("529.982.247-25");
    boolean cnpjValido = cnpjValidationService.isValid("04.252.011/0001-10");
    boolean emailValido = emailValidationService.isValid("usuario@empresa.com");
    boolean senhaValida = passwordValidationService.isValid("Senha@123");

    boolean emailRestritoValido = emailValidationService.isValid(
            "usuario+tag@empresa.com",
            true,
            false,
            new String[]{"empresa.com"},
            new String[]{"bloqueado.com"}
    );

    boolean senhaRestritaValida = passwordValidationService.isValid(
            "Senha@123",
            8,
            32,
            true,
            true,
            true,
            true,
            true
    );

    String cpfFormatado = cpfValidationService.formatar("52998224725");
    String cnpjFormatado = cnpjValidationService.formatar("04252011000110");

    String cpfGerado = cpfValidationService.gerarCpfValido();
    String cnpjGerado = cnpjValidationService.gerarCnpjValido();
  }
}
```

### 4) Exemplo com controller

```java
package com.example.demo.controller;

import com.example.demo.dto.DocumentoRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    @PostMapping
    public String criar(@RequestBody @Valid DocumentoRequest request) {
        return "Documento válido";
    }
}
```

### 5) Exemplo de request

```json
{
  "cpf": "529.982.247-25",
  "cnpj": "04.252.011/0001-10",
  "email": "usuario@empresa.com",
  "senha": "Senha@123"
}
```

## Matriz de compatibilidade

| Componente | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.x (testado com 4.0.5) |
| Maven | 3.9+ |
| Jakarta Validation | 3.x |
| Hibernate Validator | 9.x |

## Tratamento de erro

As annotations (`@ValidCpf`, `@ValidCnpj`, `@ValidEmail` e `@ValidPassword`) retornam erro de validação via Bean Validation.
Já os métodos `formatar(...)` de CPF/CNPJ lançam exceção quando o documento é inválido.

```java
package com.example.demo.api;

import io.github.andrelamego.brValidator.cnpj.InvalidCnpjException;
import io.github.andrelamego.brValidator.cpf.InvalidCpfException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler({InvalidCpfException.class, InvalidCnpjException.class})
  public ResponseEntity<Map<String, String>> handleDocumentoInvalido(RuntimeException ex) {
    return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleBeanValidation(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .orElse("Dados inválidos");

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", message));
  }
}
```

## FAQ

#### Aceita CPF/CNPJ com máscara?

Sim. Por padrão, `formatted = true`.

#### Posso obrigar sem máscara?

Sim. Use `formatted = false` na annotation ou `isValid(documento, false)` no service.

#### Campo pode ser opcional?

Sim. Use `required = false` nas annotations.

#### E-mail com alias `+` é aceito?

Sim, por padrão (`allowPlusAlias = true`).

#### Posso bloquear e-mail descartável?

Sim. Use `disposableAllowed = false`.

#### Posso limitar domínios permitidos ou bloqueados?

Sim. Use `allowedDomains` e `blockedDomains`.

#### Documento nulo ou vazio é válido no service?

Não. O service retorna `false`.

#### E-mail nulo ou vazio é válido no service?

Não. O service retorna `false`.

#### Senha nula ou vazia é válida no service?

Não. O service retorna `false`.

#### Quais regras de senha são aplicadas por padrão?

Mínimo de 8 caracteres, máximo de 64, com maiúscula, minúscula, número, caractere especial e sem espaços.

#### Quando ocorre exceção?

Em `formatar(...)`, quando CPF/CNPJ é inválido.

## Tecnologias

- Java 21
- Spring Boot
- Maven
- Jakarta Validation
- Hibernate Validator

## Roadmap

Melhorias previstas:

- Validação de CEP
- Validação de telefone
- Publicação no Maven Central
- Testes automatizados mais robustos
- Suporte a múltiplos formatos de documento

## Licença

Este projeto está sob a licença MIT.

Consulte o arquivo [LICENSE](LICENSE).
