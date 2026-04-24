# Release Notes

## v1.3.1 - Reorganização de Pacotes

### Release Notes

Release **v1.3.1** do `br-validator`, com refatoração interna da estrutura de pacotes para melhorar a organização e manutenção do projeto, mantendo compatibilidade com as versões anteriores.

Nesta versão, o foco é reorganizar o código de validação por domínio (CPF, CNPJ, Email e Senha), sem alterações de comportamento.

## Incluído nesta versão

- Reorganização das classes de validação em pacotes dedicados:
  - `cpf`;
  - `cnpj`;
  - `email`;
  - `password`.
- Atualização de imports em todo o código para refletir a nova estrutura.
- Atualização dos exemplos do `README` para corresponder aos novos pacotes.
- Nenhuma mudança funcional: apenas melhoria estrutural para manutenção.

```xml
<dependency>
  <groupId>io.github.andrelamego</groupId>
  <artifactId>br-validator</artifactId>
  <version>1.3.1</version>
</dependency>
```

---

## v1.3.0 - Validação de Senha

### Release Notes

Release **v1.3.0** do `br-validator`, com expansão do suporte de validação além de CPF, CNPJ e Email, mantendo compatibilidade com as versões anteriores.

Nesta versão, o foco é adicionar suporte completo a **Senha**, com regras configuráveis para diferentes cenários.

## Incluído nesta versão

- Annotation `@ValidPassword` para validação declarativa em DTOs.
- `PasswordValidationService` com validação de senha por padrão.
- `PasswordValidationService` com regras avançadas por configuração:
  - controle de tamanho por `minLength` e `maxLength`;
  - exigência de maiúscula por `requireUppercase`;
  - exigência de minúscula por `requireLowercase`;
  - exigência de número por `requireNumber`;
  - exigência de caractere especial por `requireSpecialChar`;
  - bloqueio de espaços por `blockWhitespace`.
- `PasswordValidator` para integração com Bean Validation.
- `InvalidPasswordException` para cenários de erro relacionados a senha.
- Auto-configuração Spring Boot (`PasswordValidatorAutoConfiguration`).
- Testes unitários e de integração cobrindo os principais fluxos de validação de senha.
- Funcionalidades de CPF, CNPJ e Email preservadas sem breaking changes.

```xml
<dependency>
  <groupId>io.github.andrelamego</groupId>
  <artifactId>br-validator</artifactId>
  <version>1.3.0</version>
</dependency>
```

---

## v1.2.0 - Validação de E-mail

### Release Notes

Release **v1.2.0** do `br-validator`, com expansão do suporte de validação além de CPF e CNPJ, mantendo compatibilidade com as versões anteriores.

Nesta versão, o foco é adicionar suporte completo a **Email** sobre a base já estável de documentos brasileiros.

## Incluído nesta versão

- Annotation `@ValidEmail` para validação declarativa em DTOs.
- `EmailValidationService` com validação de email por padrão.
- `EmailValidationService` com regras avançadas por configuração:
  - controle de alias com `allowPlusAlias`;
  - bloqueio de emails descartáveis com `disposableAllowed`;
  - restrição por `allowedDomains`;
  - bloqueio por `blockedDomains`.
- `EmailValidator` para integração com Bean Validation.
- `InvalidEmailException` para cenários de erro relacionados a email.
- Auto-configuração Spring Boot (`EmailValidatorAutoConfiguration`).
- Testes unitários e de integração cobrindo os principais fluxos de validação de email.
- Funcionalidades de CPF e CNPJ preservadas sem breaking changes.

```xml
<dependency>
  <groupId>io.github.andrelamego</groupId>
  <artifactId>br-validator</artifactId>
  <version>1.2.0</version>
</dependency>
```

---

## v1.1.0 - Validação de CNPJ

### Release Notes

Release **v1.1.0** do `br-validator`, com expansão do suporte de validação além de CPF, mantendo compatibilidade com a versão anterior.

Nesta versão, o foco é adicionar suporte completo a CNPJ sobre a base já estável.

## Incluído nesta versão

- Annotation `@ValidCnpj` para validação declarativa em DTOs.
- `CnpjValidationService` com validação de CNPJ.
- `CnpjValidationService` com formatação de CNPJ.
- `CnpjValidationService` com geração de CNPJ válido para testes.
- `CnpjValidator` para integração com Bean Validation.
- `InvalidCnpjException` para cenários de formatação inválida.
- Auto-configuração Spring Boot (`CnpjAutoConfiguration`).
- Testes unitários e de integração cobrindo os principais fluxos de CNPJ.
- Funcionalidades de CPF preservadas sem breaking changes.

```xml
<dependency>
  <groupId>io.github.andrelamego</groupId>
  <artifactId>br-validator</artifactId>
  <version>1.1.0</version>
</dependency>
```

---

## v1.0.0 - Validação de CPF

### Release Notes

Primeira release estável do `br-validator`, starter Spring Boot para validação de documentos e dados brasileiros.

Nesta versão inicial, o foco é CPF, com base pronta para expansão.

## Incluído nesta versão

- Annotation `@ValidCpf` para validação declarativa em DTOs.
- `CpfValidationService` com:
  - validação de CPF;
  - formatação de CPF;
  - geração de CPF válido para testes.
- Auto-configuração Spring Boot (`CpfValidatorAutoConfiguration`).
- Registro em `AutoConfiguration.imports`.
- Testes unitários e de integração cobrindo os principais fluxos.

```xml
<dependency>
  <groupId>com.lamego</groupId>
  <artifactId>br-validator</artifactId>
  <version>1.0.0</version>
</dependency>
```
