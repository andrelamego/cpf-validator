# cpf-validator

Starter Spring Boot para validação de CPF com annotation customizada usando Bean Validation (`jakarta.validation`).

Esta biblioteca permite validar CPFs de forma simples em qualquer projeto Spring Boot por meio da annotation `@ValidCpf`, sem precisar implementar a lógica manualmente em cada aplicação.

## O que o projeto fornece

- Annotation customizada `@ValidCpf`
- Validação automática com Bean Validation
- Auto Configuration com Spring Boot
- Serviço reutilizável para validação de CPF
- Formatação de CPF
- Geração de CPF válido para testes
- Integração pronta para uso como dependência via GitHub Packages

## Tecnologias

- Java 21
- Spring Boot
- Maven
- Jakarta Validation
- Hibernate Validator

## Estrutura do projeto

```text
cpf-validator
|-- annotation
|   `-- ValidCpf.java
|-- config
|   `-- CpfValidatorAutoConfiguration.java
|-- exception
|   `-- InvalidCpfException.java
|-- service
|   `-- CpfValidationService.java
|-- validator
|   `-- CpfValidator.java
`-- META-INF
    `-- spring
        `-- org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

## Funcionalidades

### 1) Annotation customizada

Permite validar CPF diretamente em DTOs:

```java
@ValidCpf
private String cpf;
```

### 2) Campo obrigatório ou opcional

```java
@ValidCpf(required = false)
private String cpf;
```

### 3) Aceitar ou não CPF formatado

Aceita:

- `123.456.789-09`
- `12345678909`

Exemplo:

```java
@ValidCpf(formatted = false)
private String cpf;
```

### 4) Mensagem personalizada

```java
@ValidCpf(message = "CPF informado é inválido")
private String cpf;
```

### 5) Serviço de validação

Também pode ser usado diretamente:

```java
CpfValidationService service = new CpfValidationService();
boolean valido = service.isValid("529.982.247-25");
```

### 6) Formatação automática

```java
String cpfFormatado = service.formatar("52998224725");
```

Resultado:

```text
529.982.247-25
```

### 7) Geração de CPF válido

Útil para testes automatizados:

```java
String cpf = service.gerarCpfValido();
```

## Instalação

### Repositório

Adicione no `pom.xml` do seu projeto:

```xml
<dependencies>
    <dependency>
        <groupId>com.lamego</groupId>
        <artifactId>cpf-validator</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

## Como usar

### Exemplo com DTO

```java
package com.example.demo.dto;

import com.lamego.cpfvalidator.annotation.ValidCpf;

public class ClienteRequest {

    @ValidCpf(
        message = "CPF inválido",
        formatted = true,
        required = true
    )
    private String cpf;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
```

### Exemplo com Controller

```java
package com.example.demo.controller;

import com.example.demo.dto.ClienteRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @PostMapping
    public String criar(@RequestBody @Valid ClienteRequest request) {
        return "Cliente cadastrado com sucesso";
    }
}
```

### Exemplo de request

```json
{
  "cpf": "529.982.247-25"
}
```

### Exemplo de response (válido)

```json
{
  "message": "Cliente cadastrado com sucesso"
}
```

### Exemplo de response (inválido)

```json
{
  "cpf": "CPF inválido"
}
```

## Annotation `@ValidCpf`

### Parâmetros disponíveis

| Parâmetro | Tipo | Default | Descrição |
|---|---|---|---|
| `message` | `String` | `CPF inválido` | Mensagem de erro |
| `formatted` | `boolean` | `true` | Aceita CPF com máscara |
| `required` | `boolean` | `true` | Define se o campo é obrigatório |
| `groups` | `Class<?>[]` | - | Bean Validation |
| `payload` | `Payload[]` | - | Bean Validation |

## Roadmap futuro

Melhorias previstas:

- Validação de CNPJ
- Validação de CEP
- Validação de telefone
- Validação de e-mail avançada
- Starter completo para documentos brasileiros
- Publicação no Maven Central
- Testes automatizados mais robustos
- Suporte a múltiplos formatos de documento

## Licença

Este projeto está sob a licença MIT.

Consulte o arquivo [LICENSE](LICENSE).
