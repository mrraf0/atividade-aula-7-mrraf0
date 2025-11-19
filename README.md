🧭 GUIA DO ALUNO — ATIVIDADE PRÁTICA (AULA 07)

Tema: Segurança de Software • SAST • DAST • DevSecOps
Objetivo: Detectar vulnerabilidades reais usando ferramentas automáticas.

🎯 1. Objetivo da Atividade

Nesta atividade você vai:

Criar um repositório no GitHub

Subir um código intencionalmente vulnerável

Ativar CodeQL (SAST) no GitHub

Executar a pipeline

Analisar os alertas de segurança gerados

O objetivo é entender como as ferramentas encontram vulnerabilidades automaticamente e como isso se aplica ao dia a dia DevSecOps.

🧩 2. Pré-requisitos

Conta no GitHub ativa

Permissão para criar repositórios

GitHub Actions habilitado

Editor de código (VSCode recomendado)

📁 3. Criar o repositório no GitHub

Acesse: https://github.com/new

Nomeie o repositório como:

atividade-sast-devsecops


Escolha: Public (recomendado)

Marque: Add a README

Clique Create Repository

📄 4. Criar o arquivo com código vulnerável

Você vai criar o arquivo:

VulnerableCode.java


No GitHub, clique em Add file > Create new file

Nome do arquivo: VulnerableCode.java

Cole o código disponibilizado pelo professor (já vulnerável e comentado)

Clique em Commit changes

🧪 5. Habilitar o CodeQL (SAST) no repositório

Vá até:
GitHub → Actions

Procure por:
CodeQL — Analyze ou Security → Code scanning → Set up CodeQL

Clique em:
Configure

O GitHub vai abrir um arquivo .yml de workflow pronto

Clique em Commit changes

📌 Isso ativa o CodeQL para rodar automaticamente.

🚀 6. Executar o workflow

Vá até Actions

Clique em CodeQL

Você verá a pipeline rodando

Aguarde até finalizar (leva 1–3 minutos)

Se aparecer um ✔ verde = workflow executado
Se aparecer alertas = ótimo! É isso que queremos analisar.

🔍 7. Verificar vulnerabilidades encontradas

Após a conclusão:

Vá até Security → Code scanning alerts

Você verá uma lista de vulnerabilidades detectadas

Exemplos esperados:

SQL Injection

XSS

Credenciais em código (Secrets)

Uso de MD5 (algoritmo fraco)

Tratamento genérico de exceção

Uso de Statement sem parametrização

Possível vazamento de informação em logs

💡 O aluno deve ler cada alerta e entender o motivo.

✍️ 8. Entrega da Atividade

Você deve entregar:

Link para o repositório

Print da tela de Code Scanning Alerts

Print da pipeline executada (Actions)

Um pequeno comentário respondendo:

Quais vulnerabilidades foram detectadas?

Qual delas é mais crítica e por quê?

Como você corrigiria pelo menos uma delas?

💡 9. Dicas úteis

Se o CodeQL não mostrar nada, verifique se o workflow rodou.

Alterar um pouco o código e fazer novo commit força nova análise.

O objetivo não é corrigir tudo — é identificar, como em um processo real DevSecOps.
