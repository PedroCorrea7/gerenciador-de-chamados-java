<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/fragments/taglibs.jspf" %>
<!DOCTYPE html>
<html lang="pt-BR">
<%@ include file="/WEB-INF/jsp/fragments/head.jspf" %>
<body data-page="admin-areas">
<div class="app-shell">
    <%@ include file="/WEB-INF/jsp/fragments/sidebar.jspf" %>
    <div class="app-main">
        <%@ include file="/WEB-INF/jsp/fragments/topbar.jspf" %>
        <main class="page-content">
            <%@ include file="/WEB-INF/jsp/fragments/alerts.jspf" %>

            <section class="card">
                <div class="section-header">
                    <div>
                        <p class="eyebrow">Gestão</p>
                        <h2>Áreas Comuns</h2>
                    </div>
                    <a href="${ctx}/admin/areas/nova" class="btn btn-primary">Nova Área</a>
                </div>

                <c:choose>
                    <c:when test="${empty areas}">
                        <div class="empty-state">
                            <h3>Nenhuma área cadastrada</h3>
                            <p>Utilize o botão acima para cadastrar a primeira área comum do condomínio.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-wrap">
                            <table class="data-table">
                                <thead>
                                <tr>
                                    <th>Nome</th>
                                    <th>Descrição</th>
                                    <th>Status</th>
                                    <th>Ações</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${areas}" var="area">
                                    <tr>
                                        <td><strong>${area.nome}</strong></td>
                                        <td>${area.descricao}</td>
                                        <td>
                                            <span class="status-pill" style="background-color: ${area.ativa ? '#d4edda' : '#f8d7da'}; color: ${area.ativa ? '#155724' : '#721c24'};">
                                                ${area.ativa ? 'Ativa' : 'Inativa'}
                                            </span>
                                        </td>
                                        <td class="cell-actions">
                                            <form method="post" action="${ctx}/admin/areas/${area.id}/status" class="inline-form" data-confirm="Deseja alterar o status desta área?">
                                                <%@ include file="/WEB-INF/jsp/fragments/csrf.jspf" %>
                                                <button type="submit" class="btn btn-secondary">
                                                    ${area.ativa ? 'Desativar' : 'Ativar'}
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </section>
        </main>
    </div>
</div>
<%@ include file="/WEB-INF/jsp/fragments/scripts.jspf" %>
</body>
</html>