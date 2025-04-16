# CiberS7 - Pipeline de Segurança CI/CD

## Objetivo
Este projeto demonstra a configuração de um pipeline de CI/CD para realizar análises de segurança SAST e DAST em uma aplicação Node.js.

## Ferramentas Utilizadas
- **GitHub Actions**: Configuração do pipeline CI/CD.
- **SYHUNT Community**: Análise de segurança estática (SAST).
- **OWASP ZAP**: Análise de segurança dinâmica (DAST).

## Estrutura do Projeto
```plaintext
CiberS7/
├── src/                   # Código-fonte da aplicação
├── .github/
│   └── workflows/
│       └── security.yml   # Arquivo do pipeline CI/CD
├── README.md              # Documentação do projeto
```

## Pipeline CI/CD
O pipeline realiza as seguintes etapas:
1. **SAST Analysis**:
   - Utiliza o SYHUNT para análise de código.
   - Gera um relatório `sast-report.html`.
2. **DAST Analysis**:
   - Utiliza o OWASP ZAP para análise dinâmica na aplicação em execução.
   - Gera um relatório `dast-report.html`.

## Como Executar Localmente
1. Clone este repositório:
   ```bash
   git clone https://github.com/Teberges/CiberS7.git
   cd CiberS7
   ```

2. Instale as dependências:
   ```bash
   npm install
   ```

3. Execute a aplicação:
   ```bash
   node src/app.js
   ```

4. Acesse a aplicação em: `http://localhost:8080`.

5. Realize a análise SAST utilizando o SYHUNT Community e a análise DAST com OWASP ZAP.

## Resultados Esperados
Após rodar o pipeline, os relatórios das análises estarão disponíveis como artefatos no GitHub Actions.