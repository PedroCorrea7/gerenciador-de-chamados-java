<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/fragments/taglibs.jspf" %>
<!DOCTYPE html>
<html lang="pt-BR">
<%@ include file="/WEB-INF/jsp/fragments/head.jspf" %>
<body data-page="morador-reservas">
<div class="app-shell">
    <%@ include file="/WEB-INF/jsp/fragments/sidebar.jspf" %>
    <div class="app-main">
        <%@ include file="/WEB-INF/jsp/fragments/topbar.jspf" %>
        <main class="page-content">
            <%@ include file="/WEB-INF/jsp/fragments/alerts.jspf" %>

            <section class="card">
                <div class="section-header">
                    <div>
                        <p class="eyebrow">Histórico</p>
                        <h2>Minhas reservas</h2>
                    </div>
                    <a href="${ctx}/morador/reservas/nova" class="btn btn-primary">Nova reserva</a>
                </div>

                <c:choose>
                    <c:when test="${empty reservas}">
                        <div class="empty-state">
                            <h3>Nenhuma reserva encontrada</h3>
                            <p>Você ainda não realizou solicitações de reservas.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-wrap">
                            <table class="data-table">
                                <thead>
                                <tr>
                                    <th>Área Comum</th>
                                    <th>Data</th>
                                    <th>Início</th>
                                    <th>Fim</th>
                                    <th>Status</th>
                                    <th>Ações</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${reservas}" var="reserva">
                                    <tr>
                                        <!-- 1. Área Comum -->
                                        <td>${reserva.areaComum.nome}</td>

                                        <!-- 2. Data -->
                                        <td>${reserva.dataReserva}</td>

                                        <!-- 3. Início -->
                                        <td>${reserva.horaInicio}</td>

                                        <!-- 4. Fim -->
                                        <td>${reserva.horaFim}</td>

                                        <!-- 5. Status -->
                                        <td><span class="status-pill">${reserva.statusReserva.nome}</span></td>

                                        <!-- 6. Ações -->
                                        <td class="cell-actions" style="vertical-align: middle;">
                                            <c:choose>
                                                <c:when test="${reserva.statusReserva.nome eq 'SOLICITADO' or reserva.statusReserva.nome eq 'APROVADO'}">
                                                    <form method="post" action="${ctx}/morador/reservas/${reserva.id}/cancelar" style="margin: 0; padding: 0;" data-confirm="Deseja cancelar esta reserva?">
                                                        <%@ include file="/WEB-INF/jsp/fragments/csrf.jspf" %>
                                                        <button type="submit" style="color: #dc3545; padding: 0; text-decoration: none; border: none; background: transparent; cursor: pointer; font-size: 0.9rem; font-weight: 500;">Cancelar</button>
                                                    </form>
                                                </c:when>
                                                <c:when test="${reserva.statusReserva.nome eq 'NEGADO'}">
                                                    <span style="font-size: 0.85rem; color: #495057; display: block; line-height: 1.4;"><strong>Motivo:</strong> ${reserva.motivoNegacao}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span style="color: #6c757d; font-size: 0.85rem; font-weight: 500;">-</span>
                                                </c:otherwise>
                                            </c:choose>
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