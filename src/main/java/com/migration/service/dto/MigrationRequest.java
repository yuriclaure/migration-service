package com.migration.service.dto;

import java.util.Objects;

public class MigrationRequest {
    String messageId;
    String cobrancaId;
    Double valor;
    Double desconto;
    Double total;

    public MigrationRequest() {
    }

    public MigrationRequest(String messageId, String cobrancaId, Double valor, Double desconto, Double total) {
        this.messageId = messageId;
        this.cobrancaId = cobrancaId;
        this.valor = valor;
        this.desconto = desconto;
        this.total = total;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getCobrancaId() {
        return cobrancaId;
    }

    public void setCobrancaId(String cobrancaId) {
        this.cobrancaId = cobrancaId;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MigrationRequest that = (MigrationRequest) o;
        return Objects.equals(messageId, that.messageId) && Objects.equals(cobrancaId, that.cobrancaId) && Objects.equals(valor, that.valor) && Objects.equals(desconto,
                                                                                                                                                               that.desconto) && Objects.equals(total,
                                                                                                                                                                                                that.total);
    }

    @Override
    public int hashCode() {

        return Objects.hash(messageId, cobrancaId, valor, desconto, total);
    }

    @Override
    public String toString() {
        return "MigrationRequest{" + "messageId='" + messageId + '\'' + ", cobrancaId='" + cobrancaId + '\'' + ", valor=" + valor + ", desconto=" + desconto + ", total=" + total + '}';
    }
}
