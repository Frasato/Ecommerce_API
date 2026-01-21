
# Ecommerce API

API REST de e-commerce focada em trazer todas as funcionalidades que uma loja virtual necessita, como autenticação, produtos, pedidos, etc. Uma api focada em lojas que querem ter uma presença e venda virtual.

## Visão Geral

Essa API foi desenvolvida para atender as necessidades de um cliente no gerenciamento de uma loja online, incluíndo:

- Autenticação de Usuários
- Gestão de produtos
- Criação de pedidos
- Calculo de delivery
- Pagamento via PIX e cartão
- Relatório de vendas, clicks e visitas

## Objetivos

O objetivo principal é construir uma api que seja:

- Rápida e segura
- Atendesse as necessidades da loja
- Permite integração com front-end web e mobile
- Escalável
- De fácil manutenção

## Funcionalidades Principais

#### ✅ Autenticação de Usuários
- Registro e Login com JWT
- Rotas privadas e protegidas

#### 🛒 Produtos
- Listar, criar, atualizar e remover produtos
- Filtros por categoria, preço e disponibilidade
- Adição e remoção de promoções e descontos

#### 📦 Pedidos
- Criação de pedidos pelo cliente
- Atualização de status do pedido
- Consulta de histórico

#### 📊 Análise de Produtos
- Número de compras
- Número de visitas
- Número de vezes adicionado ao carrinho

---

# Tecnologia Usada
- ☕Java 21
- 🍃 Spring Boot
- 🧮 JPA
- 🐘 PostgreSQL
- ⚙️ Maven
- 📚Swagger
- 🛡️ JWT
- 👀 Prometheus e Grafana

# Decisões técnicas

- Java: Robusto e seguro, além de de ser rápido e aguentar muitas requisições ao mesmo tempo
- Spring Boot: Framework maduro e com grande suporte empresarial
- JWT: Para uma autenticação profissional
- PostgreSQL: Banco relacional para integridade de dados e consultas eficientes
- Swagger: Para documentar e melhorar eficiência no consumo dos endpoints

# Melhorias Futuras

Por mais que o projeto funcione e seja usado, ainda necessita de melhorias em algumas partes, que pretendo fazer, como:

- Refatorar para Arquitetura Limpa
- Testes Unitários e E2E
- Logs com Loki
- Pagamentos passarem por RabbitMQ
- Transformar em um container Docker para automatizar CI/CD

# Sobre o projeto

Este projeto foi desenvolvido por Gabriel Frasato como solução backend para e-commerce com foco profissional, exibindo organização de código, comunicação clara e aplicação de boas práticas.

🔗 LinkedIn: https://br.linkedin.com/in/gabriel-frasato
🔗 GitHub: https://github.com/Frasato
