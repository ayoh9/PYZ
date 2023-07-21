package com.example.payazatest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "tblTenant")
public class Tenant {
    @Id
    @Column(name = "tenant_id")
    private String tenantID;

    @Column(name = "tenant_username")
    private String userName;

    @Column(name = "tenant_password")
    private String password;

    @Column(name = "tenant_name")
    private String tenantName;

    @Column(name = "tenant_webhook")
    private String tenantWebhook;

    @Column(name = "tenant_provider")
    private String tenantAssignedProvider;

}
