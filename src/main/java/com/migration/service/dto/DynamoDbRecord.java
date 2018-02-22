package com.migration.service.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "Cobrancas")
public class DynamoDbRecord {
    @DynamoDBHashKey(attributeName = "cobrancaId")
    private int cobrancaId;

    @DynamoDBAttribute
    private Double valor;

    @DynamoDBAttribute
    private Double desconto;

    @DynamoDBAttribute
    private Double total;

    public DynamoDbRecord() {
    }

    public DynamoDbRecord(int cobrancaId, Double valor, Double desconto, Double total) {
        this.cobrancaId = cobrancaId;
        this.valor = valor;
        this.desconto = desconto;
        this.total = total;
    }

    public DynamoDbRecord(MigrationRequest migrationRequest) {
        this(migrationRequest.getCobrancaId(), migrationRequest.getValor(), migrationRequest.getDesconto(), migrationRequest.getTotal());
    }

    public int getCobrancaId() {
        return cobrancaId;
    }

    public void setCobrancaId(int cobrancaId) {
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
        DynamoDbRecord that = (DynamoDbRecord) o;
        return Objects.equals(cobrancaId, that.cobrancaId) && Objects.equals(valor, that.valor) && Objects.equals(desconto, that.desconto) && Objects.equals(total, that.total);
    }

    @Override
    public int hashCode() {

        return Objects.hash(cobrancaId, valor, desconto, total);
    }

    @Override
    public String toString() {
        return "DynamoDbRecord{" + "cobrancaId='" + cobrancaId + '\'' + ", valor=" + valor + ", desconto=" + desconto + ", total=" + total + '}';
    }
}
