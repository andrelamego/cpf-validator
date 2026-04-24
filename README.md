# br-validator

Starter Spring Boot para validação de documentos brasileiros com Bean Validation (`jakarta.validation`).

Com `br-validator`, você valida CPF e CNPJ de forma declarativa em DTOs, sem repetir regra de negócio em cada projeto.

## Principais funcionalidades

- Annotation `@ValidCpf` para validação declarativa.
- Annotation `@ValidCnpj` para validação declarativa.
- Validação automática com Bean Validation.
- Serviços reutilizáveis para CPF e CNPJ.
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
        <version>1.1.0</version>
    </dependency>
</dependencies>
```

## Como usar

### 1) Validação declarativa em DTO

```java
package com.example.demo.dto;

import io.github.andrelamego.brValidator.annotation.ValidCpf;
import io.github.andrelamego.brValidator.annotation.ValidCnpj;

public class DocumentoRequest {

    @ValidCpf(message = "CPF inválido", formatted = true, required = true)
    private String cpf;

    @ValidCnpj(message = "CNPJ inválido", formatted = true, required = false)
    private String cnpj;

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
}
```

### 2) Parâmetros das annotations (`@ValidCpf` e `@ValidCnpj`)

| Parâmetro | Tipo | Default | Descrição |
|---|---|---|---|
| `message` | `String` | `CPF inválido.` / `CNPJ inválido.` | Mensagem de erro |
| `formatted` | `boolean` | `true` | Aceita documento com máscara |
| `required` | `boolean` | `true` | Define se o campo é obrigatório |
| `groups` | `Class<?>[]` | - | Bean Validation |
| `payload` | `Payload[]` | - | Bean Validation |

### 3) Uso direto via service

```java
import io.github.andrelamego.brValidator.service.CpfValidationService;
import io.github.andrelamego.brValidator.service.CnpjValidationService;
import org.springframework.stereotype.Service;

@Service
public class DocumentoService {

    private final CpfValidationService cpfValidationService;
    private final CnpjValidationService cnpjValidationService;

    public DocumentoService(
            CpfValidationService cpfValidationService,
            CnpjValidationService cnpjValidationService
    ) {
        this.cpfValidationService = cpfValidationService;
        this.cnpjValidationService = cnpjValidationService;
    }

    public void validar() {
        boolean cpfValido = cpfValidationService.isValid("529.982.247-25");
        boolean cnpjValido = cnpjValidationService.isValid("04.252.011/0001-10");

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
  "cnpj": "04.252.011/0001-10"
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

As annotations (`@ValidCpf` e `@ValidCnpj`) retornam erro de validação via Bean Validation.
Já os métodos `formatar(...)` lançam exceção quando o documento é inválido.

```java
package com.example.demo.api;

import io.github.andrelamego.brValidator.exception.InvalidCnpjException;
import io.github.andrelamego.brValidator.exception.InvalidCpfException;
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

- **Aceita CPF/CNPJ com máscara?** Sim. Por padrão, `formatted = true`.
- **Posso obrigar sem máscara?** Sim. Use `formatted = false` na annotation ou `isValid(documento, false)` no service.
- **Campo pode ser opcional?** Sim. Use `required = false` na annotation.
- **Documento nulo ou vazio é válido no service?** Não. O service retorna `false`.
- **Quando ocorre exceção?** Em `formatar(...)`, quando CPF/CNPJ é inválido.

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
- Validação de e-mail avançada
- Publicação no Maven Central
- Testes automatizados mais robustos
- Suporte a múltiplos formatos de documento

## Licença

Este projeto está sob a licença MIT.

Consulte o arquivo [LICENSE](LICENSE).
