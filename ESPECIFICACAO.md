Módulo de Reservas de Áreas Comuns
1. Visão Geral:

   Este documento detalha a implementação do recurso de "Reserva de Áreas Comuns" no sistema legado gerenciador-chamados. O objetivo principal foi adicionar mais uma funcionalidade à plataforma para permitir que moradores solicitem reservas de áreas de uso do condomínio, enquanto administradores realizam a gestão (aprovação ou recusa fundamentada).


2. Decisões Arquiteturais e Padrões Adotados:

   Para garantir a coesão e integridade do projeto, a nova funcionalidade foi desenvolvida seguindo estritamente os padrões estabelecidos na base do código original:


- Versionamento de Banco de Dados: A criação das tabelas foi feita através do Flyway (V19), utilizando UUID para chaves primárias e mapeando devidamente as FKs.


- ORM e Entidades: O carregamento dos relacionamentos foi configurado como FetchType.LAZY, reduzindo o carregamento desnecessário de dados relacionados. A estratégia também evita carregamentos automáticos de grandes grafos de entidades, devendo as consultas específicas ser planejadas conforme a necessidade.


- Padrões de Consulta (Repositories): Adoção de Text Blocks do Java (""") para queries complexas em JPQL, mantendo a legibilidade da base de código. Buscas textuais utilizam funções lower() no banco para garantir comportamentos case-insensitive.


- Padrão MVC com JSP: As novas telas foram integradas à arquitetura baseada em Server-Side Rendering (JSP), reaproveitando fragmentos (taglibs.jspf, sidebar.jspf) para manter o layout unificado do sistema.

3. Modelagem de Dados:

   Foram introduzidas três novas entidades principais para suportar o módulo de reservas:

- AreaComum: Catálogo de espaços disponíveis no condomínio (ex: Churrasqueira, Salão de Festas) com controle de status (ativo/inativo).


- StatusReserva: Catálogo de estados possíveis (SOLICITADO, APROVADO, NEGADO, CANCELADO).


- Reserva: Entidade transacional principal, ligando o Morador, a Área Comum e o Status aos dados de data, horário e motivo de negação (quando aplicável).

4. Regras de Negócio e Estratégias:

- Conflito de Horários: Para centralizar a validação e evitar o carregamento de reservas em memória, foi desenvolvida uma query JPQL dedicada no ReservaRepository, delegando ao banco a identificação de possíveis sobreposições.


- Validação de Datas Passadas e Áreas Inativas: O sistema bloqueia tentativas de agendamento em datas pretéritas e impede a seleção de áreas que foram desativadas pela administração.


- Recusa Fundamentada (RN-01-08): Cumprindo os requisitos de negócio, a ação de negar uma reserva obriga a persistência de um motivo (motivoNegacao), validado na camada de serviço para impedir valores vazios.

5. Interpretações, Inconsistências (Legado) e Decisões Técnicas:
Durante o desenvolvimento, algumas decisões foram tomadas ao interagir com o código legado:

- Inconsistência de Rotas POST/PATCH (Erro 404): O sistema original utilizava, em alguns formulários, um input hidden _method="patch". Isso causou conflitos de roteamento no Spring (HiddenHttpMethodFilter), resultando em erros 404. Decisão: Padronizar as ações de alteração de estado (Aprovar, Negar, Cancelar) utilizando formulários HTML nativos com mapeamento limpo via @PostMapping nos controladores.


- Inconsistência de Encoding no JSP: As telas originais apresentavam quebra de caracteres especiais (acentuação) em novos arquivos JSP. Decisão: Inclusão explícita da diretiva <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> no topo de cada nova view.


- Conflito de CSS Global: A tabela do painel do morador quebrava visualmente ao inserir os botões de ação. Decisão: Utilizar inline styles pontuais nos botões de formulário para resetar margens e fundos, evitando alterar o arquivo CSS global e correr o risco de quebrar outras partes do sistema.


6. Cobertura de Testes Unitários:

   Para cumprir o critério de qualidade exigido (> 40%), foi criada a classe ReservaServiceTest utilizando JUnit 5 e Mockito.

- Escopo Medido: A camada de serviço (ReservaService), responsável por centralizar as regras críticas (validação de horário, áreas inativas, sobreposição e rejeição sem motivo).


- Execução: Os testes foram validados com sucesso através do comando mvn test na pasta raiz.

7. Documentação do Uso de IA:

   O desenvolvimento desta funcionalidade contou com o apoio de ferramentas de Inteligência Artificial (Gemini) atuando estritamente como assistente de raciocínio, sem delegação de decisões arquiteturais críticas.

- Ferramentas utilizadas e atividades:

Gemini: Utilizado para auxiliar na estruturação do planejamento, análise de stack traces de erros (como o 404 de mapeamento e problemas de formatação CSS/JSP), e formatação desta documentação.

- Decisões NÃO delegadas à IA:

  - A modelagem do banco de dados (relacionamentos e cardinalidade).

  - A decisão de como garantir o isolamento das roles de segurança (uso do AuthenticatedUserValidator herdado do legado).

  - A lógica core de verificação de conflitos de horário (decidida por mim a execução via banco ao invés de memória).


-  Sugestões aceitas, modificadas ou rejeitadas:

   - Rejeitada: A IA sugeriu manter o uso de @PatchMapping no controlador e injetar filtros extras no Spring para suportar formulários HTML. A sugestão foi rejeitada em prol da simplicidade estrutural, alterando a rota para @PostMapping padrão.

   - Modificada: A IA gerou o esqueleto da tabela JSP. A estrutura foi aceita, mas as classes CSS foram completamente reescritas por mim para corresponderem ao arquivo global do projeto legado (ex: uso de .data-table e .status-pill).


-  Como o conteúdo foi validado:

Nenhum código gerado foi colado diretamente sem revisão. Toda lógica sugerida foi validada via execução prática no ambiente local (Tomcat), cruzada com testes na interface web e validada pela suíte de testes unitários construída.

- Interações relevantes:

Contexto: Erro de rota ao submeter formulário.

Uso: A IA ajudou a identificar a discrepância entre o action do formulário (/rejeitar) e o mapeamento no Controller (/negar), além do conflito com o input hidden _method="patch".

Contexto: Encoding quebrado no front-end.

Uso: A IA sugeriu corretamente a adição da diretiva pageEncoding="UTF-8" no topo dos arquivos .jsp, resolvendo o problema herdado da configuração padrão do Tomcat.

Contexto: Bug visual no CSS.

Uso: Utilizei a IA para inspecionar que a classe legada .inline-form estava injetando borders e paddings. Aceitei a sugestão de aplicar um style="border: none; background: transparent;" no botão para sobrescrever o legado.

Contexto: Metodologia de Testes.


Uso: A IA apoiou a tática de focar os testes isoladamente na classe ReservaService utilizando Mockito para simular os repositórios, focando nas validações das regras de negócio (lançamento de exceções) para subir a cobertura de ramos (branches).
   