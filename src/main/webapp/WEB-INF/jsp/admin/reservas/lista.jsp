<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/fragments/taglibs.jspf" %>
<!DOCTYPE html>
<html lang="pt-BR">
<%@ include file="/WEB-INF/jsp/fragments/head.jspf" %>
<body data-page="admin-reservas">
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
                        <h2>Gerenciamento de Reservas</h2>
                    </div>
                </div>

                <c:choose>
                    <c:when test="${empty reservas}">
                        <div class="empty-state">
                            <h3>Nenhuma reserva solicitada</h3>
                            <p>Não há solicitações de reservas pendentes ou registradas no sistema.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-wrap">
                            <table class="data-table">
                                <thead>
                                <tr>
                                    <th>Morador</th>
                                    <th>Área Comum</th>
                                    <th>Data</th>
                                    <th>Horário</th>
                                    <th>Status</th>
                                    <th>Ações</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${reservas}" var="reserva">
                                    <tr>
                                        <!-- 1. Morador -->
                                        <td>${reserva.morador.nome}</td>

                                        <!-- 2. Área Comum -->
                                        <td>${reserva.areaComum.nome}</td>

                                        <!-- 3. Data -->
                                        <td>${reserva.dataReserva}</td>

                                        <!-- 4. Horário -->
                                        <td>${reserva.horaInicio} - ${reserva.horaFim}</td>

                                        <!-- 5. Status -->
                                        <td><span class="status-pill">${reserva.statusReserva.nome}</span></td>

                                        <!-- 6. Ações -->
                                        <td class="cell-actions" style="display: flex; gap: 0.5rem; align-items: center;">
                                            <c:choose>
                                                <c:when test="${reserva.statusReserva.nome eq 'SOLICITADO'}">
                                                    <form method="post" action="${ctx}/admin/reservas/${reserva.id}/aprovar" class="inline-form">
                                                        <%@ include file="/WEB-INF/jsp/fragments/csrf.jspf" %>
                                                        <button type="submit" class="btn btn-secondary" style="color: #28a745;">Aprovar</button>
                                                    </form>
                                                    <form method="post" action="${ctx}/admin/reservas/${reserva.id}/negar" class="inline-form" style="display: flex; gap: 0.25rem;">
                                                        <%@ include file="/WEB-INF/jsp/fragments/csrf.jspf" %>
                                                        <input type="text" name="motivo" placeholder="Motivo obrigatório" required style="padding: 0.25rem; font-size: 0.8rem; border: 1px solid #ccc; border-radius: 4px;"/>
                                                        <button type="submit" class="btn btn-link" style="color: #dc3545;">Negar</button>
                                                    </form>
                                                </c:when>
                                                <c:when test="${reserva.statusReserva.nome eq 'NEGADO'}">
                                                    <span style="color: #dc3545; font-size: 0.85rem;"><strong>Motivo:</strong> ${reserva.motivoNegacao}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span style="color: #6c757d; font-size: 0.85rem; font-weight: 500;">Finalizado</span>
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