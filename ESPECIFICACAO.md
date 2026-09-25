Módulo de Reservas de Áreas Comuns

1. Visão Geral:


   Este documento detalha a implementação do recurso de "Reserva de Áreas Comuns" no sistema legado gerenciador-chamados. O objetivo principal foi adicionar mais uma funcionalidade a plataforma para permitir que moradores solicitem reservas de áreas de uso do condomínio.


2. Decisões Arquiteturais e Padrões Adotados:


   Para garantir a coesão e integridade do projeto, a nova funcionalidade foi feita seguindo os padrões estabelecidos na base do código original:

Versionamento de Banco de Dados: 

A criação das tabelas foi feita através do Flyway V19), utilizando UUID para chaves primárias e mapeando devidamente as FK's.

ORM e Entidades:

O carregamento de relacionamentos foi configurado como FetchType.LAZY, visando otimização de performance e prevenção de N+1 queries.

Padrões de Consulta (Repositories):

Adoção de Text Blocks do Java (""") para queries complexas, mantendo a legibilidade e o padrão usado na base do código.

Buscas textuais utilizando funções lower() no banco para garantir comportamentos case-insensitive e prevenir bugs de digitação de status.

3. Modelagem de Dados:


   Foram introduzidas três novas entidades principais para suportar a arquitetura baseada em catálogos do sistema original:

AreaComum: Catálogo de espaços disponíveis no condomínio (ex: Churrasqueira, Salão de Festas).

StatusReserva: Catálogo de estados possíveis (SOLICITADO, APROVADO, NEGADO, CANCELADO).

Reserva: Entidade transacional principal, ligando o Morador, a Área Comum e o Status aos dados de data e horário.

4. Regras de Negócio e Estratégias (Em progresso):


Conflito de Horários: Para garantir consistência, escalabilidade e performance, a validação de sobreposição de horários não foi feita em memória no Java. Foi desenvolvida uma query JPQL dedicada no ReservaRepository que delega essa verificação diretamente para o banco de dados.

5. Próximos Passos (Camada Web e Negócios)
   