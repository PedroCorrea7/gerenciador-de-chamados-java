<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/fragments/taglibs.jspf" %>
<!DOCTYPE html>
<html lang="pt-BR">
<%@ include file="/WEB-INF/jsp/fragments/head.jspf" %>
<body data-page="morador-reserva-nova">
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
                            <p class="eyebrow">Agendamento</p>
                            <h2>Solicitar Nova Reserva</h2>
                        </div>
                    </div>

                    <form method="post" action="${ctx}/morador/reservas" class="stack-form">
                        <%@ include file="/WEB-INF/jsp/fragments/csrf.jspf" %>

                        <label class="field">
                            <span>Área Comum</span>
                            <select name="areaComumId" required>
                                <option value="">Selecione uma área...</option>
                                <c:forEach items="${areasComuns}" var="area">
                                    <option value="${area.id}">${area.nome}</option>
                                </c:forEach>
                            </select>
                        </label>

                        <label class="field">
                            <span>Data da Reserva</span>
                            <input type="date" name="data" required>
                        </label>

                        <div style="display: flex; gap: 1rem;">
                            <label class="field" style="flex: 1;">
                                <span>Hora de Início</span>
                                <input type="time" name="inicio" required>
                            </label>

                            <label class="field" style="flex: 1;">
                                <span>Hora de Fim</span>
                                <input type="time" name="fim" required>
                            </label>
                        </div>

                        <div class="button-row" style="margin-top: 1.5rem;">
                            <button type="submit" class="btn btn-primary">Solicitar Reserva</button>
                            <a href="${ctx}/morador/reservas" class="btn btn-secondary">Voltar</a>
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