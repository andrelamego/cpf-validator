# br-validator

Starter Spring Boot para validação de documentos brasileiros, e-mail, senha, CEP, telefone e data de nascimento com Bean Validation (`jakarta.validation`).

Com `br-validator`, você valida CPF, CNPJ, e-mail, senha, CEP, telefone e data de nascimento de forma declarativa em DTOs, sem repetir regra de negócio em cada projeto.

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
- Annotation `@ValidCep` para validação declarativa.
- Annotation `@ValidPhone` para validação declarativa.
- Annotation `@ValidBirthDate` para validação declarativa.
- Validação automática com Bean Validation.
- Serviços reutilizáveis para CPF, CNPJ, e-mail, senha, CEP, telefone e data de nascimento.
- Formatação de CPF, CNPJ e CEP.
- Geração de CPF e CNPJ válidos para testes.
- Auto-configuração Spring Boot para uso como dependência.

## Instalação

Adicione no `pom.xml` do seu projeto:

```xml
<dependencies>
    <dependency>
        <groupId>io.github.andrelamego</groupId>
        <artifactId>br-validator</artifactId>
        <version>1.4.0</version>
    </dependency>
</dependencies>
```

## Como usar

### 1) Validação declarativa em DTO

```java
package com.example.demo.dto;

import io.github.andrelamego.brValidator.cpf.ValidCpf;
import io.github.andrelamego.brValidator.cnpj.ValidCnpj;
import io.github.andrelamego.brValidator.birthdate.ValidBirthDate;
import io.github.andrelamego.brValidator.cep.ValidCep;
import io.github.andrelamego.brValidator.email.ValidEmail;
import io.github.andrelamego.brValidator.password.ValidPassword;
import io.github.andrelamego.brValidator.phone.ValidPhone;

import java.time.LocalDate;

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

  @ValidCep(formatted = true, required = true, rejectRepeatedDigits = true)
  private String cep;

  @ValidPhone(
          formatted = true,
          required = true,
          allowLandline = false,
          allowCountryCode = true,
          rejectRepeatedDigits = true,
          allowedAreaCodes = {"11", "21"},
          blockedAreaCodes = {"31"}
  )
  private String telefone;

  @ValidBirthDate(
          required = true,
          minAge = 18,
          maxAge = 120,
          allowFutureDate = false,
          allowToday = true
  )
  private LocalDate dataNascimento;
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

#### `@ValidCep`

| Parâmetro | Tipo | Default | Descrição |
|---|---|---|---|
| `message` | `String` | `CEP inválido.` | Mensagem de erro |
| `required` | `boolean` | `true` | Define se o campo é obrigatório |
| `formatted` | `boolean` | `true` | Aceita CEP com máscara |
| `rejectRepeatedDigits` | `boolean` | `true` | Rejeita CEP com todos os dígitos iguais |
| `groups` | `Class<?>[]` | - | Bean Validation |
| `payload` | `Payload[]` | - | Bean Validation |

#### `@ValidPhone`

| Parâmetro | Tipo | Default | Descrição |
|---|---|---|---|
| `message` | `String` | `Telefone inválido.` | Mensagem de erro |
| `required` | `boolean` | `true` | Define se o campo é obrigatório |
| `formatted` | `boolean` | `true` | Aceita telefone com máscara |
| `allowLandline` | `boolean` | `false` | Permite telefone fixo |
| `allowCountryCode` | `boolean` | `true` | Permite prefixo de país (`+55`) |
| `rejectRepeatedDigits` | `boolean` | `true` | Rejeita sequências repetidas |
| `allowedAreaCodes` | `String[]` | `{}` | DDDs permitidos |
| `blockedAreaCodes` | `String[]` | `{}` | DDDs bloqueados |
| `groups` | `Class<?>[]` | - | Bean Validation |
| `payload` | `Payload[]` | - | Bean Validation |

#### `@ValidBirthDate`

| Parâmetro | Tipo | Default | Descrição |
|---|---|---|---|
| `message` | `String` | `Data de Nascimento inválida.` | Mensagem de erro |
| `required` | `boolean` | `true` | Define se o campo é obrigatório |
| `minAge` | `int` | `0` | Idade mínima permitida |
| `maxAge` | `int` | `120` | Idade máxima permitida |
| `allowFutureDate` | `boolean` | `false` | Permite datas futuras |
| `allowToday` | `boolean` | `true` | Permite data igual a hoje |
| `groups` | `Class<?>[]` | - | Bean Validation |
| `payload` | `Payload[]` | - | Bean Validation |

### 3) Uso direto via service

```java
import io.github.andrelamego.brValidator.birthdate.BirthDateValidationService;
import io.github.andrelamego.brValidator.cep.CepValidationService;
import io.github.andrelamego.brValidator.cpf.CpfValidationService;
import io.github.andrelamego.brValidator.cnpj.CnpjValidationService;
import io.github.andrelamego.brValidator.email.EmailValidationService;
import io.github.andrelamego.brValidator.password.PasswordValidationService;
import io.github.andrelamego.brValidator.phone.PhoneValidationService;

import java.time.LocalDate;

// Exemplo considerando os services já injetados no seu bean.

// 1) Validação padrão
boolean cpfValido = cpfValidationService.isValid("529.982.247-25");
boolean cnpjValido = cnpjValidationService.isValid("04.252.011/0001-10");
boolean emailValido = emailValidationService.isValid("usuario@empresa.com");
boolean senhaValida = passwordValidationService.isValid("Senha@123");
boolean cepValido = cepValidationService.isValid("01310-100", true, true);

// 2) Validação com regras específicas
boolean dataNascimentoValida = birthDateValidationService.isValid(
        LocalDate.of(2000, 1, 1),
        18,   // minAge
        120,  // maxAge
        false, // allowFutureDate
        true   // allowToday
);

String[] dddsPermitidos = {"11"};
String[] dddsBloqueados = {"31"};
boolean telefoneValido = phoneValidationService.isValid(
        "+55 (11) 98765-4321",
        true,   // formatted
        false,  // allowLandline
        true,   // allowCountryCode
        true,   // rejectRepeatedDigits
        dddsPermitidos,
        dddsBloqueados
);

boolean emailRestritoValido = emailValidationService.isValid(
        "usuario+tag@empresa.com",
        true,
        false,
        new String[]{"empresa.com"},
        new String[]{"bloqueado.com"}
);

boolean senhaRestritaValida = passwordValidationService.isValid(
        "Senha@123",
        8,    // minLength
        32,   // maxLength
        true, // requireUppercase
        true, // requireLowercase
        true, // requireNumber
        true, // requireSpecialChar
        true  // blockWhitespace
);

// 3) Formatação e geração
String cepFormatado = cepValidationService.formatar("01310100");
String cpfFormatado = cpfValidationService.formatar("52998224725");
String cnpjFormatado = cnpjValidationService.formatar("04252011000110");

String cpfGerado = cpfValidationService.gerarCpfValido();
String cnpjGerado = cnpjValidationService.gerarCnpjValido();
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
  "senha": "Senha@123",
  "cep": "01310-100",
  "telefone": "+55 (11) 98765-4321",
  "dataNascimento": "2000-01-01"
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

As annotations (`@ValidCpf`, `@ValidCnpj`, `@ValidEmail`, `@ValidPassword`, `@ValidCep`, `@ValidPhone` e `@ValidBirthDate`) retornam erro de validação via Bean Validation.
Já os métodos `formatar(...)` de CPF/CNPJ/CEP lançam exceção quando o documento é inválido.

```java
package com.example.demo.api;

import io.github.andrelamego.brValidator.cnpj.InvalidCnpjException;
import io.github.andrelamego.brValidator.cpf.InvalidCpfException;
import io.github.andrelamego.brValidator.cep.InvalidCepException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler({InvalidCpfException.class, InvalidCnpjException.class, InvalidCepException.class})
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

#### CEP com máscara é aceito?

Sim. Por padrão, `formatted = true` em `@ValidCep`.

#### Telefone fixo é aceito?

Depende da configuração. Em `@ValidPhone`, o padrão é `allowLandline = false`.

#### Posso bloquear DDDs específicos no telefone?

Sim. Use `allowedAreaCodes` e `blockedAreaCodes` em `@ValidPhone`.

#### Data de nascimento futura é aceita?

Não por padrão. Use `allowFutureDate = true` em `@ValidBirthDate` se precisar permitir.

#### Documento nulo ou vazio é válido no service?

Não. O service retorna `false`.

#### E-mail nulo ou vazio é válido no service?

Não. O service retorna `false`.

#### Senha nula ou vazia é válida no service?

Não. O service retorna `false`.

#### Quais regras de senha são aplicadas por padrão?

Mínimo de 8 caracteres, máximo de 64, com maiúscula, minúscula, número, caractere especial e sem espaços.

#### Quando ocorre exceção?

Em `formatar(...)`, quando CPF/CNPJ/CEP é inválido.

## Tecnologias

- Java 21
- Spring Boot
- Maven
- Jakarta Validation
- Hibernate Validator

## Roadmap

Melhorias previstas:

- Evoluir regras avançadas de validação para CEP e telefone
- Adicionar internacionalização das mensagens de validação (i18n)
- Expandir documentação com exemplos por domínio
- Testes automatizados mais robustos
- Suporte a múltiplos formatos de documento

## Licença

Este projeto está sob a licença MIT.

Consulte o arquivo [LICENSE](LICENSE).
