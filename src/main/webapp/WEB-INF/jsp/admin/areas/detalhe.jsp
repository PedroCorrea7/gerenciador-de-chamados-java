<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/fragments/taglibs.jspf" %>
<!DOCTYPE html>
<html lang="pt-BR">
<%@ include file="/WEB-INF/jsp/fragments/head.jspf" %>
<body data-page="admin-areas-nova">
<div class="app-shell">
    <%@ include file="/WEB-INF/jsp/fragments/sidebar.jspf" %>
    <div class="app-main">
        <%@ include file="/WEB-INF/jsp/fragments/topbar.jspf" %>
        <main class="page-content">
            <%@ include file="/WEB-INF/jsp/fragments/alerts.jspf" %>

            <section class="detail-grid">
                <article class="card">
                    <div class="section-header">
                        <div>
                            <p class="eyebrow">Cadastro</p>
                            <h2>Nova Área Comum</h2>
                        </div>
                    </div>

                    <form method="post" action="${ctx}/admin/areas" class="stack-form">
                        <%@ include file="/WEB-INF/jsp/fragments/csrf.jspf" %>

                        <label class="field">
                            <span>Nome da Área</span>
                            <input type="text" name="nome" required placeholder="Ex: Churrasqueira, Salão de Festas...">
                        </label>

                        <label class="field">
                            <span>Descrição (Opcional)</span>
                            <textarea name="descricao" rows="3" placeholder="Detalhes sobre a capacidade, regras específicas..."></textarea>
                        </label>

                        <div class="button-row" style="margin-top: 1.5rem;">
                            <button type="submit" class="btn btn-primary">Cadastrar Área</button>
                            <a href="${ctx}/admin/areas" class="btn btn-secondary">Voltar</a>
                        </div>
                    </form>
                </article>
            </section>
        </main>
    </div>
</div>
<%@ include file="/WEB-INF/jsp/fragments/scripts.jspf" %>
</body>
</html>